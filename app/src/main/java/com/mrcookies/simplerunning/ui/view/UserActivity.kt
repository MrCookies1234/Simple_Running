package com.mrcookies.simplerunning.ui.view

import android.os.Bundle
import android.widget.Toast
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
        val user = User(
            "1",
            binding.EdtName.text.toString(),
            binding.EdtAge.text.toString().toInt(),
            binding.EdtHeight.text.toString().toInt(),
            binding.EdtWeight.text.toString().toFloat(),
            sex)

        button.setOnClickListener {
            if (!isDataCompleted()){
                Toast.makeText(this,"PLEASE COMPLETE ALL THE DATA",Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if(!userViewModel.checkHeight(user.height)){
                Toast.makeText(this,"Please Insert valid height",Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if(!userViewModel.checkWeight(user.weight)){
                Toast.makeText(this,"Please Insert valid weight",Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if(!userViewModel.checkAge(user.age)){
                Toast.makeText(this,"Please Insert valid age",Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            userViewModel.insertUser(user)
            finish()
        }

    }

    private fun isDataCompleted() : Boolean{
        if(binding.EdtAge.text.isEmpty() || binding.EdtName.text.isEmpty() || binding.EdtHeight.text.isEmpty() || binding.EdtWeight.text.isEmpty()){
            return false
        }
        return true
    }

}