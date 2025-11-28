package com.app.mycovidapp.data.repository

import com.app.mycovidapp.domain.model.Case
import com.app.mycovidapp.domain.model.CovidByDate
import com.app.mycovidapp.domain.model.CovidByCountry
import com.app.mycovidapp.domain.model.CovidDateEntry
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
        
        // Aggregate entries by country, combining cases from different regions
        val aggregatedEntries = entries
            .groupBy { it.country }
            .map { (country, countryEntries) ->
                val aggregatedCases = countryEntries.fold(
                    Case(total = 0, new = 0)
                ) { acc, entry ->
                    Case(
                        total = acc.total + entry.cases.total,
                        new = acc.new + entry.cases.new
                    )
                }
                CovidDateEntry(
                    country = country,
                    region = "", // Empty region for aggregated data
                    cases = aggregatedCases
                )
            }
            .sortedBy { it.country } // Sort alphabetically by country name
        
        return CovidByDate(data = aggregatedEntries)
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