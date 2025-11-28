package com.app.mycovidapp.presentation.components

import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.material3.Text
import com.app.mycovidapp.domain.model.CountryCaseEntry
import com.app.mycovidapp.presentation.components.FilterDropdown

@Composable
fun CountryDetail(
    caseEntry: CountryCaseEntry
) {
    Column (
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.Start
    ) {
        Text (
            text = "Data for: ${caseEntry.date}",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )

        Row (
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            FilterDropdown(
                options = ,
                selected = "Option 1",
            ) {

            }
        }
    }
}