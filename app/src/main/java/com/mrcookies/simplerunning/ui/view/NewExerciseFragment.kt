package com.mrcookies.simplerunning.ui.view

import android.Manifest
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.PolylineOptions
import com.mrcookies.simplerunning.R
import com.mrcookies.simplerunning.core.Constants
import com.mrcookies.simplerunning.core.PermissionsUtility
import com.mrcookies.simplerunning.core.TrackingUtility
import com.mrcookies.simplerunning.databinding.FragmentNewExerciseBinding
import com.mrcookies.simplerunning.services.PolyLine
import com.mrcookies.simplerunning.services.TrackingService
import com.vmadalin.easypermissions.EasyPermissions
import com.vmadalin.easypermissions.dialogs.SettingsDialog
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NewExerciseFragment : Fragment(), EasyPermissions.PermissionCallbacks {

    private var _binding: FragmentNewExerciseBinding? = null
    private val binding get() = _binding!!

    private var map : GoogleMap? = null

    private var isTracking = false
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

    private fun moveCameratoUser(){
        if(pointsInMap.isNotEmpty() && pointsInMap.last().isNotEmpty()){
            map?.animateCamera(
                CameraUpdateFactory.newLatLngZoom(
                    pointsInMap.last().last(),
                    Constants.MAP_ZOOM
                )
            )
        }
    }

    private fun toggleRun(){
        if(isTracking){
            sendCommandToService(Constants.ACTION_PAUSE_SERVICE)
        }else{
            sendCommandToService(Constants.ACTION_START_RESUME_SERVICE)
        }
    }

    private fun subscribe(){
        TrackingService.isTracking.observe(viewLifecycleOwner, Observer {
            updateTracking(it)
        })

        TrackingService.pointsInMap.observe(viewLifecycleOwner, Observer {
            pointsInMap = it
            addLatestPolyline()
            moveCameratoUser()
        })

        TrackingService.timeinMillis.observe(viewLifecycleOwner , Observer {
            curTimeInMillis = it
            val formattedTime = TrackingUtility.getFormattedStopWatchTime(curTimeInMillis, true)
            binding.txvTimer.text = formattedTime
        })
    }

    private fun updateTracking(isTracking : Boolean){
        this.isTracking = isTracking
        if(!isTracking){
            binding.fabStart.visibility = View.VISIBLE
        }else{

        }
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

    private fun setupButton(){
        binding.fabStart.setOnClickListener {
            toggleRun()
        }
    }

    override fun onStart() {
        requestPermissions()
        super.onStart()
        binding.map.onStart()
    }

    private fun sendCommandToService(action : String){
        Intent(requireContext(), TrackingService::class.java).also {
            it.action =action
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