package com.app.mycovidapp.domain.repository

import com.app.mycovidapp.domain.model.CovidByCountry
import com.app.mycovidapp.domain.model.CovidByDate

interface CovidRepository {
    suspend fun getCovidByDate(
        date: String
    ): CovidByDate

    suspend fun getCovidByCountry(
        country: String
    ): CovidByCountry
}