package com.mrcookies.simplerunning.domain.repository

import com.mrcookies.simplerunning.data.model.Exercise

interface ExerciseRepository {

    suspend fun insertExercise(exercise: Exercise)
    suspend fun deleteExercise(exercise: Exercise)
    suspend fun getAllExerciseByDate(): List<Exercise>
    suspend fun getAllExerciseBySpeed(): List<Exercise>
    suspend fun getAllExerciseByDistance(): List<Exercise>
    suspend fun getAllExerciseByTime(): List<Exercise>
    suspend fun getExerciseById(id : Int): Exercise
    suspend fun getTotalTimeRun(): Long
    suspend fun getTotalTimeBike(): Long
    suspend fun getTotalDistanceRun(): Float
    suspend fun getTotalDistanceBike(): Float
    suspend fun getTotalCaloriesBike(): Int
    suspend fun getTotalCaloriesRun(): Int
    suspend fun getAvgSpeedBike(): Float
    suspend fun getAvgSpeedRun(): Float
}