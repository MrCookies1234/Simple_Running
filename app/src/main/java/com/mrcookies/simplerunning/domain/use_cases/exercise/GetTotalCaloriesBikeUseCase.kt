package com.mrcookies.simplerunning.domain.use_cases.exercise

import com.mrcookies.simplerunning.domain.repository.ExerciseRepository
import javax.inject.Inject

class GetTotalCaloriesBikeUseCase @Inject constructor(private val exerRepo: ExerciseRepository) {

    suspend operator fun invoke () : Int{
        return exerRepo.getTotalCaloriesBike()
    }
}