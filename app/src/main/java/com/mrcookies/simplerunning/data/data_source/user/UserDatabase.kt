package com.mrcookies.simplerunning.data.data_source.user

import androidx.room.Database
import androidx.room.RoomDatabase
import com.mrcookies.simplerunning.data.model.User

@Database(
    entities = [User::class],
    version = 1
)
abstract class UserDatabase : RoomDatabase() {
    abstract val userDao : UserDAO
}