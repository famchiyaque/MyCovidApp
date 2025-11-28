package com.app.mycovidapp.presentation.screens.main

import com.app.mycovidapp.domain.model.CovidDateEntry

data class MainUIState(
    val isLoading: Boolean = false,
    val countries: List<CovidDateEntry> = emptyList(),
    val selectedDate: String = "2021-04-20",
    val searchQuery: String = "",
    val error: String? = null,
    val showDatePicker: Boolean = false,
    val listIndex: Int = 0,
    val pageSize: Int = 20
) {
//    fun paginateCountries() {
//
//    }: List<CovidDateEntry>
//        get() {
//            val start = listIndex * pageSize
//            val end = start + pageSize
//            if (start >= countries.size) return emptyList()
//            return countries.subList(start, minOf(end, countries.size))
//        }
    val baseFilteredCountries: List<CovidDateEntry>
        get() = if (searchQuery.isBlank()) {
            countries
        } else {
            countries.filter {
                it.country.contains(searchQuery, ignoreCase = true)
            }
        }

    val filteredCountries: List<CovidDateEntry>
        get() {
            val base = baseFilteredCountries

            // paginate the filtered list
            val start = listIndex * pageSize
            val end = start + pageSize

            if (start >= base.size) return emptyList()
            return base.subList(start, minOf(end, base.size))
        }

    val currentPageStartIndex: Int
        get() = (listIndex * pageSize) + 1

    val currentPageEndIndex: Int
        get() = minOf((listIndex + 1) * pageSize, baseFilteredCountries.size)

    val totalFilteredCount: Int
        get() = baseFilteredCountries.size

    val currentPageDisplayText: String
        get() {
            if (totalFilteredCount == 0) return "Showing 0 results"
            return "Showing ${currentPageStartIndex}-${currentPageEndIndex} of $totalFilteredCount results"
        }
}