package com.app.mycovidapp.data.remote.api

import com.app.mycovidapp.domain.model.CovidByDate
import com.app.mycovidapp.domain.model.CovidByCountry
import retrofit2.http.GET
import retrofit2.http.Query

interface CovidApi {
    @GET("/covid19?date={date}")
    suspend fun getCovidByDate(
        @Query("date") date: String
    ): CovidByDate

    @GET("/covid19?country={country}")
    suspend fun getCovidByCountry(
        @Query("country") country: String
    ): CovidByCountry
}