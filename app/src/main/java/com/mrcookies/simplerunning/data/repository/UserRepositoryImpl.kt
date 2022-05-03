package com.mrcookies.simplerunning.data.repository

import com.mrcookies.simplerunning.data.data_source.user.UserDAO
import com.mrcookies.simplerunning.data.model.User
import com.mrcookies.simplerunning.domain.repository.UserRepository
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(private val userDAO: UserDAO) : UserRepository{

    override suspend fun getUser(): User {
        return userDAO.getUser()
    }

    override suspend fun insertUser(user: User) {
        userDAO.insertUser(user)
    }

    override suspend fun updateUser(user: User) {
        userDAO.updateUser(user)
    }


}