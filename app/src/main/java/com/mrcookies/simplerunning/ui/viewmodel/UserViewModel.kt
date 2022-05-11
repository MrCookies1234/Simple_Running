package com.mrcookies.simplerunning.ui.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mrcookies.simplerunning.domain.util.user.UserUtility
import com.mrcookies.simplerunning.data.model.User
import com.mrcookies.simplerunning.domain.use_cases.user.UserUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(private val userUseCases: UserUseCases,
                                        private val userUtility: UserUtility
) : ViewModel() {

    private val _userModel = MutableLiveData<User>()
    val userModel : LiveData<User> get() = _userModel

    private val _isLoading = MutableStateFlow(true)
    val isLoading = _isLoading.asStateFlow()

    init {
        getUser()
    }

    fun checkHeight(height: Int) : Boolean{
        return userUtility.isHeightInRange(height)
    }

    fun checkWeight(weight: Float) : Boolean{
        return userUtility.isWeightInRange(weight)
    }

    fun checkAge(age: Int) : Boolean{
        return userUtility.isAgeInRange(age)
    }

    fun insertUser(user : User){
        viewModelScope.launch{
            userUseCases.insertUser(user)
            Log.i("Inserted :", user.name)
        }
    }

    fun getUser(){
        viewModelScope.launch {
            _isLoading.value = true
            _userModel.postValue(userUseCases.getUser())
            _isLoading.value = false
        }
    }

}