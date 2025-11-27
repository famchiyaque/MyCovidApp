package com.app.mycovidapp.data.repository


import com.app.mycovidapp.domain.model.CovidByDate
import com.app.mycovidapp.domain.model.CovidByCountry
import com.app.mycovidapp.domain.repository.CovidRepository
import com.app.mycovidapp.remote.api.CovidApi
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class CovidApiImpl
    @Inject
    constructor(
        private val api: CovidApi
    ) : CovidRepository{
    override suspend fun getCovidByDate(date: String): CovidByDate {
        val response = api.getCovidByDate()
        return response.map { it.toDomain() }
    }

    override suspend fun getCovidByCountry(country: String): CovidByCountry {
        val response = api.getCovidByCountry()
        return response.map { it.toDomain() }
    }
}