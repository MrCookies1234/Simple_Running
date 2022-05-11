package com.mrcookies.simplerunning.core

import android.Manifest.permission.*
import android.content.Context
import android.os.Build
import com.vmadalin.easypermissions.EasyPermissions

object PermissionsUtility{

    fun hasLocationPermissions(context : Context) =
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            EasyPermissions.hasPermissions(
                context,
                ACCESS_FINE_LOCATION,
                ACCESS_COARSE_LOCATION)
        }else{
            EasyPermissions.hasPermissions(
                context,
                ACCESS_FINE_LOCATION,
                ACCESS_COARSE_LOCATION,
                ACCESS_BACKGROUND_LOCATION)
        }


}