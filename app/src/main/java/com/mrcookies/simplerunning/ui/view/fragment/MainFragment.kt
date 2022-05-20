package com.mrcookies.simplerunning.ui.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.mrcookies.simplerunning.adapter.ExerciseAdapter
import com.mrcookies.simplerunning.databinding.FragmentMainBinding
import com.mrcookies.simplerunning.ui.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainFragment: Fragment() {

    private var _binding : FragmentMainBinding? = null
    private val binding get() = _binding!!

    private lateinit var exerciseAdapter: ExerciseAdapter

    private val mainViewModel: MainViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentMainBinding.inflate(layoutInflater)
        setUpButton()
        setUpRecyclerView()

        mainViewModel.listOfExercises.observe(viewLifecycleOwner, Observer {
            exerciseAdapter.submitList(it)
        })

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
    }

    private fun setUpRecyclerView(){
        binding.rcvActivities.apply {
            exerciseAdapter = ExerciseAdapter()
            adapter = exerciseAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }
}