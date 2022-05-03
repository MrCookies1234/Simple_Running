package com.mrcookies.simplerunning.di

import android.app.Application
import androidx.room.Room
import com.mrcookies.simplerunning.data.data_source.UserDatabase
import com.mrcookies.simplerunning.data.repository.UserRepositoryImpl
import com.mrcookies.simplerunning.domain.repository.UserRepository
import com.mrcookies.simplerunning.domain.use_cases.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideUserDatabase(app : Application) : UserDatabase{
        return Room.databaseBuilder(
            app,
            UserDatabase::class.java,
            "USER_DATABASE"
        ).build()
    }

    @Provides
    @Singleton
    fun provideUserRepository(db : UserDatabase) : UserRepository{
        return UserRepositoryImpl(db.userDao)
    }

    @Provides
    @Singleton
    fun provideUserUseCases(repository : UserRepository) : UserUseCases{
        return UserUseCases(
            getUser = GetUserUseCase(repository),
            insertUser = InsertUserUseCase(repository),
            updateUser = UpdateUserUseCase(repository)
        )
    }
}