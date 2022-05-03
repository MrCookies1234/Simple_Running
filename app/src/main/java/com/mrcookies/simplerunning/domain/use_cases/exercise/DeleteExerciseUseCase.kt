package com.mrcookies.simplerunning.domain.use_cases.exercise

import com.mrcookies.simplerunning.data.model.Exercise
import com.mrcookies.simplerunning.domain.repository.ExerciseRepository
import javax.inject.Inject

class DeleteExerciseUseCase @Inject constructor(private val exerRepo : ExerciseRepository) {

    suspend operator fun invoke(exercise : Exercise){
        exerRepo.deleteExercise(exercise)
    }
}