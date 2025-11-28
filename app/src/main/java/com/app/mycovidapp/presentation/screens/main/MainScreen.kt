package com.app.mycovidapp.presentation.screens.main

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.app.mycovidapp.presentation.components.CountrySnapshotCard
import com.app.mycovidapp.presentation.components.CovidDatePickerDialog
import com.app.mycovidapp.presentation.components.SearchBar
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@Suppress("ktlint:standard:function-naming")
@Composable
fun MainScreen(
    navController: NavHostController,
    viewModel: MainViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 16.dp)
    ) {
        // Header
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            Text(
                text = "COVID-19 Statistics",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )
        }

        // Search bar and Date Picker in same row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            SearchBar(
                query = uiState.searchQuery,
                onQueryChange = { viewModel.onSearchQueryChanged(it) },
                modifier = Modifier.weight(1f)
            )

            IconButton(onClick = { viewModel.onDatePickerVisibilityChanged(true) }) {
                Icon(
                    imageVector = Icons.Default.DateRange,
                    contentDescription = "Select Date"
                )
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            Text(
                text = "Showing results for date: ${uiState.selectedDate}",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(top = 4.dp)
            )
        }

        // Content
        Box(modifier = Modifier.fillMaxSize()) {
            when {
                uiState.isLoading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                uiState.error != null -> {
                    Column(
                        modifier = Modifier.align(Alignment.Center),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Error: ${uiState.error}",
                            color = MaterialTheme.colorScheme.error
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(onClick = { viewModel.loadCovidData() }) {
                            Text("Retry")
                        }
                    }
                }
                uiState.filteredCountries.isEmpty() -> {
                    Text(
                        text = if (uiState.searchQuery.isNotBlank()) {
                            "No countries found matching \"${uiState.searchQuery}\""
                        } else {
                            "No data available"
                        },
                        modifier = Modifier.align(Alignment.Center),
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
                else -> {
                    LazyColumn {
                        items(uiState.filteredCountries) { entry ->
                            CountrySnapshotCard(
                                entry = entry,
                                onClick = {
                                    val encodedCountryName = URLEncoder.encode(
                                        entry.country,
                                        StandardCharsets.UTF_8.toString()
                                    )
                                    navController.navigate("country/$encodedCountryName")
                                }
                            )
                        }
                    }
                }
            }
        }
    }

    // Date Picker Dialog
    if (uiState.showDatePicker) {
        CovidDatePickerDialog(
            onDateSelected = { viewModel.onDateSelected(it) },
            onDismiss = { viewModel.onDatePickerVisibilityChanged(false) }
        )
    }
}