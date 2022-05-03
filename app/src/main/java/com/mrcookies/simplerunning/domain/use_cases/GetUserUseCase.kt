package com.mrcookies.simplerunning.domain.use_cases

import com.mrcookies.simplerunning.data.model.User
import com.mrcookies.simplerunning.domain.repository.UserRepository
import javax.inject.Inject

class GetUserUseCase @Inject constructor(private val userRepo: UserRepository){

    suspend operator fun invoke() : User {
        return userRepo.getUser()
    }
}