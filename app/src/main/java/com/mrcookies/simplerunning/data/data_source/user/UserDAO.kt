package com.mrcookies.simplerunning.data.data_source.user

import androidx.room.*
import com.mrcookies.simplerunning.data.model.User

@Dao
interface UserDAO {

    @Query("SELECT * FROM User WHERE id = '1'")
    suspend fun getUser(): User

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user : User)

    @Update
    suspend fun updateUser(user : User)

}