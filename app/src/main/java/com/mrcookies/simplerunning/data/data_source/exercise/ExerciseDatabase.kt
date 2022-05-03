package com.mrcookies.simplerunning.data.data_source.exercise

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.mrcookies.simplerunning.core.Converter
import com.mrcookies.simplerunning.data.model.Exercise

@Database(
    entities = [Exercise::class],
    version = 1
)
@TypeConverters(Converter::class)
abstract class ExerciseDatabase : RoomDatabase(){
    abstract val exerciseDao : ExerciseDAO
}