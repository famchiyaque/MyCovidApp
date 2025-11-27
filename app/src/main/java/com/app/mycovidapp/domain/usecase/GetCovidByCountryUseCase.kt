package com.app.mycovidapp.domain.usecase

import com.app.mycovidapp.domain.common.Result
import com.app.mycovidapp.domain.model.CovidByCountry
import com.app.mycovidapp.domain.repository.CovidRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetCovidByCountryUseCase
    @Inject
    constructor(
        private val repository: CovidRepository
    ){
    operator fun invoke(
        country: String,
    ): Flow<Result<CovidByCountry>> =
        flow {
            try {
                emit(Result.Loading)
                val data = repository.getCovidByCountry(country)
                emit(Result.Success(data))
            } catch (e: Exception) {
                emit(Result.Error(e))
            }
        }
}