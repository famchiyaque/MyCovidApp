package com.app.mycovidapp.presentation.screens.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.mycovidapp.domain.common.Result
import com.app.mycovidapp.domain.usecase.GetCovidByDateUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel
    @Inject
    constructor(
        private val getCovidByDateUseCase: GetCovidByDateUseCase
    ) : ViewModel() {

    private val _uiState = MutableStateFlow(MainUIState())
    val uiState: StateFlow<MainUIState> = _uiState.asStateFlow()

    init {
        loadCovidData()
    }

    fun onDateSelected(date: String) {
        _uiState.update { it.copy(selectedDate = date, showDatePicker = false) }
        loadCovidData()
    }

    fun onSearchQueryChanged(query: String) {
        _uiState.update { it.copy(searchQuery = query) }
    }

    fun onDatePickerVisibilityChanged(show: Boolean) {
        _uiState.update { it.copy(showDatePicker = show) }
    }

    fun loadCovidData() {
        viewModelScope.launch {
            getCovidByDateUseCase(uiState.value.selectedDate).collect { result ->
                when (result) {
                    is Result.Loading -> {
                        _uiState.update { it.copy(isLoading = true, error = null) }
                    }
                    is Result.Success -> {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                countries = result.data.data,
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