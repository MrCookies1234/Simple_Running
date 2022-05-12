package com.mrcookies.simplerunning.ui.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.mrcookies.simplerunning.databinding.FragmentMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainFragment: Fragment() {

    private var _binding : FragmentMainBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMainBinding.inflate(layoutInflater)
        setUpButton()
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun setUpButton(){
        binding.fabNewExercise.setOnClickListener {
            findNavController().navigate(MainFragmentDirections.actionMainFragmentToNewExerciseFragment())
        }
        binding.fabNewExercise2.setOnClickListener {
            findNavController().navigate(MainFragmentDirections.actionMainFragmentToUserConfigFragment())
        }
    }
}