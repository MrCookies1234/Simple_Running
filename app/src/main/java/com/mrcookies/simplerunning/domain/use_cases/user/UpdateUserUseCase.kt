package com.mrcookies.simplerunning.domain.use_cases.user

import com.mrcookies.simplerunning.data.model.User
import com.mrcookies.simplerunning.domain.repository.UserRepository
import javax.inject.Inject

class UpdateUserUseCase @Inject constructor(private val userRepo : UserRepository) {

    suspend operator fun invoke (user : User){
        userRepo.updateUser(user)
    }
}