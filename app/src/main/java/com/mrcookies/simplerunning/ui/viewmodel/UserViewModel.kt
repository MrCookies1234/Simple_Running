package com.mrcookies.simplerunning.ui.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mrcookies.simplerunning.data.model.User
import com.mrcookies.simplerunning.domain.use_cases.user.UserUseCases
import dagger.hilt.android.lifecycle.HiltViewModel

import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(private val userUseCases: UserUseCases) : ViewModel() {

    val userModel = MutableLiveData<User>()

    fun insertUser(user : User){
        viewModelScope.launch{
            userUseCases.insertUser(user)
            Log.i("Inserted :", user.name)
        }
    }

    fun getUser (){
        viewModelScope.launch {
            userModel.postValue(userUseCases.getUser())
        }
    }


}