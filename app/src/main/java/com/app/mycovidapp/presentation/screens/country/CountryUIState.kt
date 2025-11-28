package com.app.mycovidapp.presentation.screens.country

import com.app.mycovidapp.domain.model.CountryCaseEntry

data class CountryUIState(
    val isLoading: Boolean = false,
    val countryName: String = "",
    val cases: List<CountryCaseEntry> = emptyList(),
    val error: String? = null,
    val selectedFilterYear: String = "",
    val selectedFilterMonth: String = "",
    val selectedFilterDay: String = ""
) {
    val firstDate: String
        get() = cases.firstOrNull()?.date ?: ""

    val lastDate: String
        get() = cases.lastOrNull()?.date ?: ""

    val totalCases: Int
        get() = cases.sumOf { it.entry.total }

    // Extract unique years from valid dates
    val filterYearOptions: List<String>
        get() = cases.map { it.date.split("-")[0] }.distinct().sorted()

    // Extract unique months for selected year
    val filterMonthOptions: List<String>
        get() = if (selectedFilterYear.isBlank()) {
            emptyList()
        } else {
            cases
                .filter { it.date.startsWith("$selectedFilterYear-") }
                .map { it.date.split("-")[1] }
                .distinct()
                .sorted()
        }

    // Extract unique days for selected year and month
    val filterDayOptions: List<String>
        get() = if (selectedFilterYear.isBlank() || selectedFilterMonth.isBlank()) {
            emptyList()
        } else {
            cases
                .filter { it.date.startsWith("$selectedFilterYear-$selectedFilterMonth-") }
                .map { it.date.split("-")[2] }
                .distinct()
                .sorted()
        }

    // Get the selected date's case entry
    val selectedCaseEntry: CountryCaseEntry?
        get() {
            if (selectedFilterYear.isBlank() || selectedFilterMonth.isBlank() || selectedFilterDay.isBlank()) {
                return cases.firstOrNull()
            }
            val selectedDate = "$selectedFilterYear-$selectedFilterMonth-$selectedFilterDay"
            return cases.firstOrNull { it.date == selectedDate }
        }
}