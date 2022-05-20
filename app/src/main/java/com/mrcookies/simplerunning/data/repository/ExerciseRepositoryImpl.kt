package com.mrcookies.simplerunning.data.repository

import com.mrcookies.simplerunning.data.data_source.exercise.ExerciseDAO
import com.mrcookies.simplerunning.data.model.Exercise
import com.mrcookies.simplerunning.domain.repository.ExerciseRepository
import javax.inject.Inject

class ExerciseRepositoryImpl @Inject constructor(private val exerciseDAO: ExerciseDAO) : ExerciseRepository {
    override suspend fun insertExercise(exercise: Exercise) {
        exerciseDAO.insertExercise(exercise)
    }

    override suspend fun deleteExercise(exercise: Exercise) {
        exerciseDAO.deleteRun(exercise)
    }

    override suspend fun getAllExerciseByDate(): List<Exercise> {
        return exerciseDAO.getAllExerciseByDate()
    }

    override suspend fun getAllExerciseBySpeed(): List<Exercise> {
        return exerciseDAO.getAllExerciseBySpeed()
    }

    override suspend fun getAllExerciseByDistance(): List<Exercise> {
        return exerciseDAO.getAllExerciseByDistance()
    }

    override suspend fun getAllExerciseByTime(): List<Exercise> {
        return exerciseDAO.getAllExerciseByTime()
    }

    override suspend fun getExerciseById(id: Int): Exercise {
       return exerciseDAO.getExerciseById(id)
    }

    override suspend fun getTotalTimeRun(): Long {
        return exerciseDAO.getTotalTimeRun()
    }

    override suspend fun getTotalTimeBike(): Long {
       return exerciseDAO.getTotalTimeBike()
    }

    override suspend fun getTotalDistanceRun(): Float {
        return exerciseDAO.getTotalDistanceRun()
    }

    override suspend fun getTotalDistanceBike(): Float {
        return exerciseDAO.getTotalDistanceBike()
    }

    override suspend fun getTotalCaloriesBike(): Int {
        return exerciseDAO.getTotalCaloriesBike()
    }

    override suspend fun getTotalCaloriesRun(): Int {
        return exerciseDAO.getTotalCaloriesRun()
    }

    override suspend fun getAvgSpeedBike(): Float {
        return exerciseDAO.getAvgSpeedBike()
    }

    override suspend fun getAvgSpeedRun(): Float {
        return exerciseDAO.getAvgSpeedRun()
    }

    override suspend fun getTotalNumber(): Int{
        return exerciseDAO.getTotalNumber()
    }

}