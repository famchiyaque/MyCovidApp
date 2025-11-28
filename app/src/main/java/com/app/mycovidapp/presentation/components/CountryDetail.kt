package com.app.mycovidapp.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.graphics.Color
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.app.mycovidapp.domain.model.CountryCaseEntry
import java.text.NumberFormat
import java.util.Locale

@Composable
fun CountryDetail(
    caseEntry: CountryCaseEntry?,
    yearOptions: List<String>,
    monthOptions: List<String>,
    dayOptions: List<String>,
    selectedYear: String,
    selectedMonth: String,
    selectedDay: String,
    onYearSelected: (String) -> Unit,
    onMonthSelected: (String) -> Unit,
    onDaySelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(
            text = "Data for:",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            FilterDropdown(
                options = yearOptions,
                selected = selectedYear,
                onSelectionChange = onYearSelected,
                label = "Year",
                modifier = Modifier.weight(1.3f)
            )

            FilterDropdown(
                options = monthOptions,
                selected = selectedMonth,
                onSelectionChange = onMonthSelected,
                label = "Month",
                modifier = Modifier.weight(1f)
            )

            FilterDropdown(
                options = dayOptions,
                selected = selectedDay,
                onSelectionChange = onDaySelected,
                label = "Day",
                modifier = Modifier.weight(1f)
            )
        }

        if (caseEntry != null) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                    colors = CardDefaults.cardColors(
            containerColor = Color.Transparent
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 0.dp
        )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text(
                        text = "Date: ${caseEntry.date}",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.primary
                    )

//                    Row(
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .padding(top = 12.dp),
//                        horizontalArrangement = Arrangement.SpaceBetween
//                    ) {
                        Column {
                            Text(
                                text = "Total Cases",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = NumberFormat.getNumberInstance(Locale.US).format(caseEntry.entry.total),
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Column(horizontalAlignment = Alignment.Start) {
                            Text(
                                text = "New Cases",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = "+${NumberFormat.getNumberInstance(Locale.US).format(caseEntry.entry.new)}",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                color = if (caseEntry.entry.new > 0) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary
                            )
                        }
//                    }
                }
            }
        }
    }
}