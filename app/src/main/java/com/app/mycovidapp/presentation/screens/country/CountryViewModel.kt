package com.app.mycovidapp.presentation.screens.country

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.mycovidapp.domain.common.Result
import com.app.mycovidapp.domain.usecase.GetCovidByCountryUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.net.URLDecoder
import java.nio.charset.StandardCharsets
import javax.inject.Inject

@HiltViewModel
class CountryViewModel
    @Inject
    constructor(
        private val getCovidByCountryUseCase: GetCovidByCountryUseCase,
        savedStateHandle: SavedStateHandle
    ) : ViewModel() {

    private val _uiState = MutableStateFlow(CountryUIState())
    val uiState: StateFlow<CountryUIState> = _uiState.asStateFlow()

    init {
        val encodedCountryName = savedStateHandle.get<String>("countryName") ?: ""
        // Decode the country name in case it was URL encoded (handles both "+" and "%20" formats)
        val countryName = try {
            URLDecoder.decode(encodedCountryName, StandardCharsets.UTF_8.toString())
        } catch (e: Exception) {
            encodedCountryName // Fallback to original if decoding fails
        }
        _uiState.update { it.copy(countryName = countryName) }
        loadCountryData(countryName)
    }

    fun onYearSelected(year: String) {
        _uiState.update { 
            val firstCase = it.cases.firstOrNull()
            val defaultMonth = if (year == firstCase?.date?.split("-")?.get(0)) {
                firstCase.date.split("-")[1]
            } else {
                it.cases
                    .filter { case -> case.date.startsWith("$year-") }
                    .firstOrNull()
                    ?.date
                    ?.split("-")
                    ?.get(1) ?: ""
            }
            val defaultDay = if (defaultMonth.isNotBlank()) {
                it.cases
                    .filter { case -> case.date.startsWith("$year-$defaultMonth-") }
                    .firstOrNull()
                    ?.date
                    ?.split("-")
                    ?.get(2) ?: ""
            } else ""
            
            it.copy(
                selectedFilterYear = year,
                selectedFilterMonth = defaultMonth,
                selectedFilterDay = defaultDay
            )
        }
    }

    fun onMonthSelected(month: String) {
        _uiState.update {
            val defaultDay = it.cases
                .filter { case -> case.date.startsWith("${it.selectedFilterYear}-$month-") }
                .firstOrNull()
                ?.date
                ?.split("-")
                ?.get(2) ?: ""
            
            it.copy(
                selectedFilterMonth = month,
                selectedFilterDay = defaultDay
            )
        }
    }

    fun onDaySelected(day: String) {
        _uiState.update { it.copy(selectedFilterDay = day) }
    }

    private fun loadCountryData(countryName: String) {
        viewModelScope.launch {
            getCovidByCountryUseCase(countryName).collect { result ->
                when (result) {
                    is Result.Loading -> {
                        _uiState.update { it.copy(isLoading = true, error = null) }
                    }
                    is Result.Success -> {
                        val cases = result.data.cases.sortedBy { it.date }
                        val firstCase = cases.firstOrNull()
                        val firstYear = firstCase?.date?.split("-")?.get(0) ?: ""
                        val firstMonth = firstCase?.date?.split("-")?.get(1) ?: ""
                        val firstDay = firstCase?.date?.split("-")?.get(2) ?: ""
                        
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                cases = cases,
                                error = null,
                                selectedFilterYear = firstYear,
                                selectedFilterMonth = firstMonth,
                                selectedFilterDay = firstDay
                            )
                        }
                    }
                    is Result.Error -> {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                error = result.exception.message ?: "Unknown error occurred"
                            )
                        }
                    }
                }
            }
        }
    }
}