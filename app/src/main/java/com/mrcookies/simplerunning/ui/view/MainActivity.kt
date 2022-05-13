package com.mrcookies.simplerunning.ui.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.NavHost
import androidx.navigation.findNavController
import com.mrcookies.simplerunning.NavGraphDirections
import com.mrcookies.simplerunning.R
import com.mrcookies.simplerunning.core.Constants
import com.mrcookies.simplerunning.databinding.ActivityMainBinding
import com.mrcookies.simplerunning.ui.viewmodel.UserViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        navigateToTrackingFragment(intent)
    }

    private fun navigateToTrackingFragment(intent : Intent?){
        if (intent?.action == Constants.ACTION_SHOW_TRACKING_ACTIVITY){
            findNavController(R.id.nav_host_fragment).navigate(NavGraphDirections.actionGlobalToNewExerciseFragment())
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        navigateToTrackingFragment(intent)
    }

}