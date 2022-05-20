package com.mrcookies.simplerunning.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mrcookies.simplerunning.data.model.Exercise
import com.mrcookies.simplerunning.domain.use_cases.exercise.ExerciseUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val exerciseUseCases: ExerciseUseCases) : ViewModel(){

    private val _listOfExercises = MutableLiveData<List<Exercise>>()
    val listOfExercises get() = _listOfExercises

    val totalNumber = MutableLiveData<Int>()

    init{
        viewModelScope.launch {
            _listOfExercises.postValue(exerciseUseCases.getAllByDateUseCase.invoke())
            totalNumber.postValue(exerciseUseCases.getTotalNumberUseCase.invoke())
        }
    }

}