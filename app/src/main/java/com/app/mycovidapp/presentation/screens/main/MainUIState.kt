package com.app.mycovidapp.presentation.screens.main

import com.app.mycovidapp.domain.model.CovidDateEntry

data class MainUIState(
    val isLoading: Boolean = false,
    val countries: List<CovidDateEntry> = emptyList(),
    val selectedDate: String = "2021-04-20",
    val searchQuery: String = "",
    val error: String? = null,
    val showDatePicker: Boolean = false
) {
    val filteredCountries: List<CovidDateEntry>
        get() = if (searchQuery.isBlank()) {
            countries
        } else {
            countries.filter {
                it.country.contains(searchQuery, ignoreCase = true)
            }
        }
}