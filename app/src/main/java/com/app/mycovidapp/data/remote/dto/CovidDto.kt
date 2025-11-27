package com.app.mycovidapp.data.remote.dto

import com.app.mycovidapp.domain.model.Case
import com.app.mycovidapp.domain.model.CovidByCountry
import com.app.mycovidapp.domain.model.CovidByDate
import com.app.mycovidapp.domain.model.CovidDateEntry
import com.app.mycovidapp.domain.model.CountryCaseEntry
import com.google.gson.annotations.SerializedName


data class CovidByDateDto(
    @SerializedName("country")
    val country: String,
    @SerializedName("region")
    val region: String,
    @SerializedName("cases")
    val cases: CaseDto
) {
    fun toDomain(): CovidDateEntry {
        return CovidDateEntry(
            country = country,
            region = region,
            cases = cases.toDomain()
        )
    }
}


data class CovidByCountryDto(
    @SerializedName("country")
    val country: String,
    @SerializedName("cases")
    val cases: Map<String, CaseDto>
) {
    fun toDomain(): CovidByCountry {
        val casesList = cases.map { (date, caseData) ->
            CountryCaseEntry(
                date = date,
                entry = caseData.toDomain()
            )
        }.sortedBy { it.date } // Sort by date
        
        return CovidByCountry(
            country = country,
            cases = casesList
        )
    }
}

data class CaseDto(
    @SerializedName("total")
    val total: Int,
    @SerializedName("new")
    val new: Int
) {
    fun toDomain(): Case {
        return Case(
            total = total,
            new = new
        )
    }
}