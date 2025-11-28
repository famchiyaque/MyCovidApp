package com.app.mycovidapp.presentation.screens.country

import com.app.mycovidapp.domain.model.CountryCaseEntry

data class CountryUIState(
    val isLoading: Boolean = false,
    val countryName: String = "",
    val cases: List<CountryCaseEntry> = emptyList(),
    val error: String? = null,

//    val firstDate =
//        get() {
//            return cases.firstOrNull()?.date
//        },
//    val lastDate = cases.lastOrNull()?.date,
//    val totalCases = cases.sumOf { it.entry.total },


    val filterYearOptions: List<String>,
    val filterMonthOptions = List<String>,
    val filterDayOptions = List<String>,

    val selectedFilterYear: String,
    val selectedFilterMonth: String,
    val selectedFilterDay: String,
)

//    val
)