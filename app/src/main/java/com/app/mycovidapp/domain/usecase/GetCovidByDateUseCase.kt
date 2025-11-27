package com.app.mycovidapp.domain.usecase

import com.app.mycovidapp.domain.common.Result
import com.app.mycovidapp.domain.model.CovidByDate
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
        date: String,
    ): Flow<Result<CovidByDate>> =
        flow {
            try {
                emit(Result.Loading)
                val data = repository.getCovidByDate(date)
                emit(Result.Success(data))
            } catch (e: Exception) {
                emit(Result.Error(e))
            }
        }
}