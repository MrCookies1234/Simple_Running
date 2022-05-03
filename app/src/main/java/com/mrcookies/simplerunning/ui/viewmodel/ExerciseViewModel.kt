package com.mrcookies.simplerunning.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.mrcookies.simplerunning.domain.use_cases.exercise.ExerciseUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ExerciseViewModel @Inject constructor(private val exerciseUseCases : ExerciseUseCases) : ViewModel() {

}