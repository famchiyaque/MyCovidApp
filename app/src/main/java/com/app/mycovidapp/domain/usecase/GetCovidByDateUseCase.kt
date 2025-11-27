package com.app.mycovidapp.domain.usecase

import com.app.mycovidapp.domain.common.Result
import com.app.mycovidapp.domain.repository.CovidRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetCovidByDateUseCase
    @Inject
    constructor(
        private val repository: CovidRepository
    ){
    operator fun invoke(
        appointmentName: String,
        date: String,
    ): Flow<Result<List<String>>> =
        flow {
            try {
                emit(Result.Loading)
                val data = repository.getDateAvailability(appointmentName, date)
                emit(Result.Success(data))
            } catch (e: Exception) {
                emit(Result.Error(e))
            }
        }
}