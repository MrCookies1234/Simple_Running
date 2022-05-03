package com.mrcookies.simplerunning.data.data_source.exercise

import androidx.room.*
import com.mrcookies.simplerunning.data.model.Exercise

@Dao
interface ExerciseDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExercise(exercise : Exercise)

    @Delete
    suspend fun deleteRun(exercise: Exercise)

    @Query("SELECT * FROM Exercise ORDER BY timestamp DESC")
    suspend fun getAllExerciseByDate(): List<Exercise>

    @Query("SELECT * FROM Exercise ORDER BY avgSpeed DESC")
    suspend fun getAllExerciseBySpeed(): List<Exercise>

    @Query("SELECT * FROM Exercise ORDER BY distance DESC")
    suspend fun getAllExerciseByDistance(): List<Exercise>

    @Query("SELECT * FROM Exercise ORDER BY time DESC")
    suspend fun getAllExerciseByTime(): List<Exercise>

    @Query("SELECT * FROM Exercise WHERE id = :id")
    suspend fun getExerciseById(id : Int): Exercise

    @Query("SELECT SUM(time) from Exercise where type = 0")
    suspend fun getTotalTimeRun(): Long

    @Query("SELECT SUM(time) from Exercise where type = 1")
    suspend fun getTotalTimeBike(): Long

    @Query("SELECT SUM(distance) from Exercise where type = 0")
    suspend fun getTotalDistanceRun(): Float

    @Query("SELECT SUM(distance) from Exercise where type = 1")
    suspend fun getTotalDistanceBike(): Float

    @Query("SELECT SUM(calories) from Exercise where type = 1")
    suspend fun getTotalCaloriesBike(): Int

    @Query("SELECT SUM(calories) from Exercise where type = 0")
    suspend fun getTotalCaloriesRun(): Int

    @Query("SELECT AVG(avgSpeed) from Exercise where type = 1")
    suspend fun getAvgSpeedBike(): Float

    @Query("SELECT AVG(avgSpeed) from Exercise where type = 0")
    suspend fun getAvgSpeedRun(): Float

}