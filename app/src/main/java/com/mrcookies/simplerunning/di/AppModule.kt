package com.mrcookies.simplerunning.di

import android.app.Application
import androidx.room.Room
import com.mrcookies.simplerunning.data.data_source.exercise.ExerciseDatabase
import com.mrcookies.simplerunning.data.data_source.user.UserDatabase
import com.mrcookies.simplerunning.data.repository.ExerciseRepositoryImpl
import com.mrcookies.simplerunning.data.repository.UserRepositoryImpl
import com.mrcookies.simplerunning.domain.repository.ExerciseRepository
import com.mrcookies.simplerunning.domain.repository.UserRepository
import com.mrcookies.simplerunning.domain.use_cases.exercise.*
import com.mrcookies.simplerunning.domain.use_cases.user.GetUserUseCase
import com.mrcookies.simplerunning.domain.use_cases.user.InsertUserUseCase
import com.mrcookies.simplerunning.domain.use_cases.user.UpdateUserUseCase
import com.mrcookies.simplerunning.domain.use_cases.user.UserUseCases
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
    fun provideUserDatabase(app : Application) : UserDatabase {
        return Room.databaseBuilder(
            app,
            UserDatabase::class.java,
            "USER_DATABASE"
        ).build()
    }

    @Provides
    @Singleton
    fun provideExerciseDatabase(app : Application) : ExerciseDatabase {
        return Room.databaseBuilder(
            app,
            ExerciseDatabase::class.java,
            "EXERCISE_DATABASE"
        ).build()
    }

    @Provides
    @Singleton
    fun provideUserRepository(db : UserDatabase) : UserRepository{
        return UserRepositoryImpl(db.userDao)
    }

    @Provides
    @Singleton
    fun provideExerciseRepository(db : ExerciseDatabase) : ExerciseRepository {
        return ExerciseRepositoryImpl(db.exerciseDao)
    }


    @Provides
    @Singleton
    fun provideUserUseCases(repository : UserRepository) : UserUseCases {
        return UserUseCases(
            getUser = GetUserUseCase(repository),
            insertUser = InsertUserUseCase(repository),
            updateUser = UpdateUserUseCase(repository)
        )
    }

    @Provides
    @Singleton
    fun provideExerciseUseCases(repository : ExerciseRepository) : ExerciseUseCases {
        return ExerciseUseCases(
            getExerciseDetail = GetExerciseDetailUseCase(repository),
            insertExerciseUseCase = InsertExerciseUseCase(repository),
            deleteExerciseUseCase = DeleteExerciseUseCase(repository),
            getAllByDistanceUseCase = GetAllByDistanceUseCase(repository),
            getAllByTimeUseCase = GetAllByTimeUseCase(repository),
            getAllBySpeedUseCase = GetAllBySpeedUseCase(repository),
            getTotalCaloriesBikeUseCase = GetTotalCaloriesBikeUseCase(repository),
            getTotalCaloriesRunUseCase = GetTotalCaloriesRunUseCase(repository),
            getTotalDistanceBikeUseCase = GetTotalDistanceBikeUseCase(repository),
            getTotalDistanceRunUseCase = GetTotalDistanceRunUseCase(repository),
            getTotalTimeBikeUseCase = GetTotalTimeBikeUseCase(repository),
            getTotalTimeRunUseCase = GetTotalTimeRunUseCase(repository),
            getAvgSpeedBikeUseCase = GetAvgSpeedBikeUseCase(repository),
            getAvgSpeedRunUseCase = GetAvgSpeedRunUseCase(repository),
            getAllByDateUseCase = GetAllByDateUseCase(repository)
        )
    }
}