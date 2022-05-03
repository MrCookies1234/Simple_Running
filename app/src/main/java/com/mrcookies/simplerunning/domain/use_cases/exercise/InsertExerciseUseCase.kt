package com.mrcookies.simplerunning.domain.use_cases.exercise

import com.mrcookies.simplerunning.data.model.Exercise
import com.mrcookies.simplerunning.domain.repository.ExerciseRepository
import javax.inject.Inject

class InsertExerciseUseCase @Inject constructor(private val exerciseRepository : ExerciseRepository) {

    suspend operator fun invoke(exercise : Exercise){
        exerciseRepository.insertExercise(exercise)
    }
}