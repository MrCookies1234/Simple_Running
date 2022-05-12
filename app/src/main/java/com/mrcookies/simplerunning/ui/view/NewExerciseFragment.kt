package com.mrcookies.simplerunning.ui.view

import android.Manifest
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.mrcookies.simplerunning.core.Constants
import com.mrcookies.simplerunning.core.PermissionsUtility
import com.mrcookies.simplerunning.databinding.FragmentNewExerciseBinding
import com.mrcookies.simplerunning.services.TrackingService
import com.vmadalin.easypermissions.EasyPermissions
import com.vmadalin.easypermissions.dialogs.SettingsDialog
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NewExerciseFragment : Fragment(), EasyPermissions.PermissionCallbacks {

    private var _binding: FragmentNewExerciseBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentNewExerciseBinding.inflate(inflater, container, false)
        setupButton()
        return binding.root
    }

    private fun setupButton(){
        binding.btnControlExercise.setOnClickListener {
            sendCommandToService(Constants.ACTION_START_RESUME_SERVICE)
        }
    }

    override fun onStart() {
        requestPermissions()
        super.onStart()
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
        _binding= null
    }
}