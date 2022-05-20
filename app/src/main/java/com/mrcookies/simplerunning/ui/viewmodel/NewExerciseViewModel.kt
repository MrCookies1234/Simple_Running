package com.mrcookies.simplerunning.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mrcookies.simplerunning.data.model.Exercise
import com.mrcookies.simplerunning.data.model.User
import com.mrcookies.simplerunning.domain.use_cases.exercise.ExerciseUseCases
import com.mrcookies.simplerunning.domain.use_cases.user.UserUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewExerciseViewModel @Inject constructor(private val exerciseUseCases : ExerciseUseCases,
                                                private val userUserCases : UserUseCases
) : ViewModel() {

    lateinit var user : User

    private val _userModel = MutableLiveData<User>()
    val userModel : LiveData<User> get() = _userModel

    private fun getUser(){
        viewModelScope.launch {
            user = userUserCases.getUser.invoke()
        }
    }

    fun insertExercise(exercise : Exercise){
        viewModelScope.launch {
            exerciseUseCases.insertExerciseUseCase(exercise)
        }
    }

}