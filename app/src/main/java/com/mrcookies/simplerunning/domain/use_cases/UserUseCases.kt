package com.mrcookies.simplerunning.domain.use_cases

data class UserUseCases(
    val getUser : GetUserUseCase,
    val insertUser : InsertUserUseCase,
    val updateUser : UpdateUserUseCase
)