package com.mrcookies.simplerunning.ui.view.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.mrcookies.simplerunning.data.model.User
import com.mrcookies.simplerunning.databinding.FragmentUserConfigBinding
import com.mrcookies.simplerunning.ui.viewmodel.UserViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UserConfigFragment : Fragment() {

    private var _binding: FragmentUserConfigBinding? = null
    private val binding get() = _binding!!

    private var sex: String = "0"

    private val userViewModel : UserViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentUserConfigBinding.inflate(layoutInflater)
        setUpButton()
        setUpRdButton()
        return binding.root
    }

    private fun setUpRdButton(){
        binding.rdbMale.setOnClickListener {
            sex = "0"
        }
        binding.rdbFemale.setOnClickListener {
            sex = "1"
        }
    }

    private fun setUpButton() {
        val button = binding.btnStart

        button.setOnClickListener {
            if (!isDataCompleted()){
                Toast.makeText(requireContext(),"PLEASE COMPLETE ALL THE DATA", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val user = User(
                "1",
                binding.EdtName.text.toString(),
                binding.EdtAge.text.toString().toInt(),
                binding.EdtHeight.text.toString().toInt(),
                binding.EdtWeight.text.toString().toFloat(),
                sex)

            if(!userViewModel.checkHeight(user.height)){
                Toast.makeText(requireContext(),"Please Insert valid height", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if(!userViewModel.checkWeight(user.weight)){
                Toast.makeText(requireContext(),"Please Insert valid weight", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if(!userViewModel.checkAge(user.age)){
                Toast.makeText(requireContext(),"Please Insert valid age", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            userViewModel.insertUser(user)
            findNavController().navigate(UserConfigFragmentDirections.actionUserConfigFragmentToMainFragment())

        }

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun isDataCompleted() : Boolean{
        if(binding.EdtAge.text.isEmpty() || binding.EdtName.text.isEmpty() || binding.EdtHeight.text.isEmpty() || binding.EdtWeight.text.isEmpty()){
            return false
        }
        return true
    }
}