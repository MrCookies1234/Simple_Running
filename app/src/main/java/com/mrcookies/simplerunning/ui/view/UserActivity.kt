package com.mrcookies.simplerunning.ui.view

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.mrcookies.simplerunning.data.model.User
import com.mrcookies.simplerunning.databinding.ActivityUserBinding
import com.mrcookies.simplerunning.ui.viewmodel.UserViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UserActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUserBinding
    private var sex: String = "0"
    private val userViewModel : UserViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserBinding.inflate(layoutInflater)

        setUpButton()
        setUprdButton()

        setContentView(binding.root)
    }

    private fun setUprdButton(){
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
            userViewModel.insertUser(User(
                "1",
            binding.EdtName.text.toString(),
            binding.EdtAge.text.toString().toInt(),
            binding.EdtHeight.text.toString().toInt(),
            binding.EdtWeight.text.toString().toInt(),
            sex))

        }

    }
}