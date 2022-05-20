package com.mrcookies.simplerunning.domain.use_cases.exercise

data class ExerciseUseCases (
    val getExerciseDetail : GetExerciseDetailUseCase,
    val deleteExerciseUseCase: DeleteExerciseUseCase,
    val insertExerciseUseCase: InsertExerciseUseCase,
    val getAllByDateUseCase: GetAllByDateUseCase,
    val getAllBySpeedUseCase: GetAllBySpeedUseCase,
    val getAllByDistanceUseCase: GetAllByDistanceUseCase,
    val getAllByTimeUseCase: GetAllByTimeUseCase,
    val getAvgSpeedBikeUseCase: GetAvgSpeedBikeUseCase,
    val getAvgSpeedRunUseCase: GetAvgSpeedRunUseCase,
    val getTotalCaloriesBikeUseCase: GetTotalCaloriesBikeUseCase,
    val getTotalCaloriesRunUseCase: GetTotalCaloriesRunUseCase,
    val getTotalDistanceBikeUseCase: GetTotalDistanceBikeUseCase,
    val getTotalDistanceRunUseCase: GetTotalDistanceRunUseCase,
    val getTotalTimeBikeUseCase: GetTotalTimeBikeUseCase,
    val getTotalTimeRunUseCase: GetTotalTimeRunUseCase,
    val getTotalNumberUseCase: GetTotalNumberUseCase
)