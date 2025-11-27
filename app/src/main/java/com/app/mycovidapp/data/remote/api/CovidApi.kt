package com.app.mycovidapp.data.remote.api

import com.app.mycovidapp.data.remote.dto.CovidByCountryDto
import com.app.mycovidapp.data.remote.dto.CovidByDateDto
import retrofit2.http.GET
import retrofit2.http.Query

interface CovidApi {
    @GET("covid19")
    suspend fun getCovidByDate(
        @Query("date") date: String
    ): List<CovidByDateDto>

    @GET("covid19")
    suspend fun getCovidByCountry(
        @Query("country") country: String
    ): List<CovidByCountryDto>
}