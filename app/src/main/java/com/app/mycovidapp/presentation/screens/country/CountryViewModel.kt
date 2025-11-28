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
        val countryName = savedStateHandle.get<String>("countryName") ?: ""
        _uiState.update { it.copy(countryName = countryName) }
        loadCountryData(countryName)
    }

    private fun loadCountryData(countryName: String) {
        viewModelScope.launch {
            getCovidByCountryUseCase(countryName).collect { result ->
                when (result) {
                    is Result.Loading -> {
                        _uiState.update { it.copy(isLoading = true, error = null) }
                    }
                    is Result.Success -> {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                cases = result.data.cases,
                                error = null
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