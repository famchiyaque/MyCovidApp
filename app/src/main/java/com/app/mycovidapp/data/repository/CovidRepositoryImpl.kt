package com.app.mycovidapp.data.repository


import com.app.mycovidapp.domain.model.CovidByDate
import com.app.mycovidapp.domain.model.CovidByCountry
import com.app.mycovidapp.domain.repository.CovidRepository
import com.app.mycovidapp.data.remote.api.CovidApi
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CovidRepositoryImpl
    @Inject
    constructor(
        private val api: CovidApi
    ) : CovidRepository{
    override suspend fun getCovidByDate(date: String): CovidByDate {
        val response = api.getCovidByDate(date)
        val entries = response.map { it.toDomain() }
        return CovidByDate(data = entries)
    }

    override suspend fun getCovidByCountry(country: String): CovidByCountry {
        val response = api.getCovidByCountry(country)
        return if (response.isNotEmpty()) {
            response.first().toDomain()
        } else {
            CovidByCountry(country = country, cases = emptyList())
        }
    }
}