package com.mrcookies.simplerunning.domain.use_cases.exercise

import com.mrcookies.simplerunning.domain.repository.ExerciseRepository
import javax.inject.Inject

class GetAvgSpeedBikeUseCase @Inject constructor(private val exerRepo: ExerciseRepository) {

    suspend operator fun invoke () : Float{
        return exerRepo.getAvgSpeedBike()
    }
}