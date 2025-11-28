package com.app.mycovidapp.presentation.screens.country

import com.app.mycovidapp.domain.model.CountryCaseEntry

data class CountryUIState(
    val isLoading: Boolean = false,
    val countryName: String = "",
    val cases: List<CountryCaseEntry> = emptyList(),
    val error: String? = null
)