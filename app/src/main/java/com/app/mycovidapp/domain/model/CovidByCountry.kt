package com.app.mycovidapp.domain.model

data class CountryCaseEntry(
    val date: String,
    val entry: Case
)

data class CovidByCountry(
    val country: String,
    val cases: List<CountryCaseEntry>
)