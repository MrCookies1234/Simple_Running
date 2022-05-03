package com.mrcookies.simplerunning.domain.repository

import com.mrcookies.simplerunning.data.model.User

interface UserRepository {

    suspend fun getUser() : User
    suspend fun insertUser(user : User)
    suspend fun updateUser(user : User)
}