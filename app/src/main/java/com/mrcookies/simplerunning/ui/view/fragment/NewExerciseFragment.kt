package com.mrcookies.simplerunning.ui.view.fragment

import android.Manifest
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.PolylineOptions
import com.mrcookies.simplerunning.R
import com.mrcookies.simplerunning.core.Constants
import com.mrcookies.simplerunning.core.ExerciseUtility
import com.mrcookies.simplerunning.core.PermissionsUtility
import com.mrcookies.simplerunning.core.TrackingUtility
import com.mrcookies.simplerunning.data.model.Exercise
import com.mrcookies.simplerunning.services.PolyLine
import com.mrcookies.simplerunning.services.TrackingService
import com.vmadalin.easypermissions.EasyPermissions
import com.mrcookies.simplerunning.databinding.FragmentNewExerciseBinding
import com.mrcookies.simplerunning.ui.viewmodel.NewExerciseViewModel
import com.vmadalin.easypermissions.dialogs.SettingsDialog
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import kotlin.math.round

@AndroidEntryPoint
class NewExerciseFragment : Fragment(), EasyPermissions.PermissionCallbacks {

    private var _binding: FragmentNewExerciseBinding? = null
    private val binding get() = _binding!!

    private val newExerciseViewModel : NewExerciseViewModel by viewModels()

    private var map : GoogleMap? = null

    private var pointsInMap = mutableListOf<PolyLine>()

    private var curTimeInMillis = 0L

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentNewExerciseBinding.inflate(inflater, container, false)
        setupButton()
        setupMap(savedInstanceState)
        subscribe()
        return binding.root
    }

    private fun setupMap(savedInstanceState: Bundle?){
        binding.map.onCreate(savedInstanceState)
        binding.map.getMapAsync{
            map = it
            addAllPolylines()
        }
    }

    override fun onResume() {
        super.onResume()
        binding.map.onResume()
    }

    override fun onStop() {
        super.onStop()
        binding.map.onStop()
    }

    override fun onPause() {
        super.onPause()
        binding.map.onPause()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        binding.map.onLowMemory()
    }

    private fun addAllPolylines(){
        for (polyline in pointsInMap){
            val options = PolylineOptions()
                .color(R.color.action)
                .width(Constants.POLYLINE_WIDTH)
                .addAll(polyline)

            map?.addPolyline(options)
        }
    }

    private fun moveCameraToUser(){
        if(pointsInMap.isNotEmpty() && pointsInMap.last().isNotEmpty()){
            map?.animateCamera(
                CameraUpdateFactory.newLatLngZoom(
                    pointsInMap.last().last(),
                    Constants.MAP_ZOOM
                )
            )
        }
    }

    private fun subscribe(){
        TrackingService.pointsInMap.observe(viewLifecycleOwner, Observer {
            pointsInMap = it
            addLatestPolyline()
            moveCameraToUser()
        })

        TrackingService.timeinMillis.observe(viewLifecycleOwner , Observer {
            curTimeInMillis = it
            val formattedTime = TrackingUtility.getFormattedStopWatchTime(curTimeInMillis, true)
            binding.txvTimer.text = formattedTime
        })
    }

    private fun addLatestPolyline(){
        if(pointsInMap.isNotEmpty() && pointsInMap.last().size> 1){
            val preLastLatLng = pointsInMap.last()[pointsInMap.last().size -2]
            val lastLatLng = pointsInMap.last().last()
            val polyLineOptions = PolylineOptions()
                .color(R.color.action)
                .width(Constants.POLYLINE_WIDTH)
                .add(preLastLatLng)
                .add(lastLatLng)
            map?.addPolyline(polyLineOptions)
        }
    }

    private fun zoomToSeeWholeTrack(){
        val bounds = LatLngBounds.Builder()
        for(polyline in pointsInMap){
            for (pos in polyline){
                bounds.include(pos)
            }
        }

        map?.moveCamera(
            CameraUpdateFactory.newLatLngBounds(bounds.build(),
                binding.map.width,
                binding.map.height,
                (binding.map.height * 0.05f).toInt())
        )
    }

    private fun endRun(){
        map?.snapshot {
            var distanceInMeters = 0f
            for (polyline in pointsInMap){
                distanceInMeters += TrackingUtility.calculatePolylineLength(polyline)
            }
            val avgSpeed = round((distanceInMeters / 1000f) / (curTimeInMillis / 1000f / 60 / 60) * 10) /10
            val dateTimeStamp = Calendar.getInstance().timeInMillis
            val caloriesBurned = ExerciseUtility.calculateCaloriesFromExercise(80f,avgSpeed,curTimeInMillis, 0)
            val exercise = Exercise(it,dateTimeStamp,curTimeInMillis,caloriesBurned,0, distanceInMeters, avgSpeed)
            newExerciseViewModel.insertExercise(exercise)
        }
    }

    private fun setupButton(){
        binding.fabStart.setOnClickListener {
            binding.fabStart.visibility = View.GONE
            binding.fabPause.visibility = View.VISIBLE
            binding.fabStop.visibility = View.VISIBLE
            sendCommandToService(Constants.ACTION_START_RESUME_SERVICE)
        }

        binding.fabPause.setOnClickListener {
            binding.fabStart.visibility = View.VISIBLE
            binding.fabPause.visibility = View.GONE
            sendCommandToService(Constants.ACTION_PAUSE_SERVICE)
        }

        binding.fabStop.setOnClickListener {
            zoomToSeeWholeTrack()
            endRun()
            sendCommandToService(Constants.ACTION_STOP_SERVICE)
            findNavController().navigate(NewExerciseFragmentDirections.actionNewExerciseFragmentToMainFragment())
        }
    }

    override fun onStart() {
        requestPermissions()
        super.onStart()
        binding.map.onStart()
    }

    private fun sendCommandToService(action : String){
        Intent(requireContext(), TrackingService::class.java).also {
            it.action = action
            requireContext().startService(it)
        }

    }

    private fun requestPermissions(){
        if(PermissionsUtility.hasLocationPermissions(requireContext())){
            return
        }
        //if android api < 29 we don't need background location
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q){
            EasyPermissions.requestPermissions(this,
                "This app needs to access your location to properly function.",
                Constants.REQUEST_CODE_LOCATION_PERMISSION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
        }
        //if android api > 29 background location needs to be requested separately from other location permissions
        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.Q){
            EasyPermissions.requestPermissions(this,
                "This app needs to access your location to properly function.",
                Constants.REQUEST_CODE_LOCATION_PERMISSION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
            EasyPermissions.requestPermissions(this,
                "This app needs to access your location to properly function.",
                Constants.REQUEST_CODE_LOCATION_PERMISSION,
                Manifest.permission.ACCESS_BACKGROUND_LOCATION)
        }else{
            // if android api = 29 we can ask for background location and others together
            EasyPermissions.requestPermissions(this,
                "This app needs to access your location to properly function.",
                Constants.REQUEST_CODE_LOCATION_PERMISSION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_BACKGROUND_LOCATION
            )
        }
    }

    override fun onPermissionsDenied(requestCode: Int, perms: List<String>) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this,perms)){
            SettingsDialog.Builder(requireContext()).build().show()
        }else{
            requestPermissions()
        }
    }

    override fun onPermissionsGranted(requestCode: Int, perms: List<String>) {}

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode,permissions,grantResults,this)
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.map?.onDestroy()
        _binding= null
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        binding.map.onSaveInstanceState(outState)
    }
}