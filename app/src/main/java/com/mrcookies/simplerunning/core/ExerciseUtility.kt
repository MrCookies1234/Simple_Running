package com.mrcookies.simplerunning.core

import kotlin.math.round

object ExerciseUtility {

    fun calculateCaloriesFromExercise(weight: Float, avgSpeed : Float, duration : Long, type : Int ) : Int {
        var met = 0f
        val durationInMinutes = duration / 60 / 60

        if(type == 0){
            met = when(avgSpeed){
                in 0f..8.04f -> 8f
                in 8.041f..9.65f -> 10f
                in 9.651f..11.26f -> 11.5f
                in 11.261f..12.87f -> 13.5f
                in 12.871f..14.48f -> 15f
                else -> 18f
            }
        }
        if (type == 1){
            met = when(avgSpeed){
                in 0f..16f -> 4f
                in 16.01f..19.3f -> 6f
                in 19.31f..22.53f -> 8f
                in 22.531f..25.74f -> 10f
                in 25.741f..30.57f -> 12f
                else -> 16f
            }
        }


        return round(durationInMinutes * (met * 3.5 * weight / 200)).toInt()
    }
}