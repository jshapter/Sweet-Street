package com.example.sweetstreet.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sweetstreet.SweetEvent
import com.example.sweetstreet.ui.UiState

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun FilterDrawerSheet(
    uiState: State<UiState>,
    onEvent: (SweetEvent) -> Unit
) {

    val cities = uiState.value.cities
    val items = uiState.value.items

    Column(modifier = Modifier.padding(16.dp)) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ){
            Text(
                text = "Open now",
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium
            )
            var checked by remember { mutableStateOf(false) }
            Switch(
                checked = checked,
                onCheckedChange = {
                    checked = it
                    onEvent(SweetEvent.Filter(
                        option = "open",
                        filterType = "availability",
                        selected = checked
                    ))
                }
            )
        }
        Divider(modifier = Modifier.padding(8.dp))
        Column {
            Text(
                text = "Location",
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium
            )
            FlowRow(
                maxItemsInEachRow = 3
            ) {
                GenerateCityChips(
                    cities = cities,
                    onEvent = onEvent
                )
            }
        }
        Divider(modifier = Modifier.padding(8.dp))
        Column {
            Text(
                text = "Items",
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium
            )
            FlowRow(
                maxItemsInEachRow = 3
            ) {
                GenerateItemChips(items = items)
            }
        }
        Divider(modifier = Modifier.padding(8.dp))
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GenerateCityChips(
    cities: MutableList<String>,
    onEvent: (SweetEvent) -> Unit
) {
    cities.forEach { option: String ->
        var selected by remember { mutableStateOf(false) }
        val selectable = true
        FilterChip(
            enabled = selectable,
            selected = selected,
            onClick = {
                selected = !selected
                onEvent(SweetEvent.Filter(
                    filterType = "location",
                    option = option,
                    selected = selected
                ))
            },
            label = { Text(text = option) },
            modifier = Modifier.padding(4.dp)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GenerateItemChips(items: MutableList<String>) {
    items.forEach { option: String ->
        var selected by remember { mutableStateOf(false) }
        val selectable = false
        FilterChip(
            enabled = selectable,
            selected = selected,
            onClick = {
                selected = !selected
            },
            label = { Text(text = option) },
            modifier = Modifier.padding(4.dp)
        )
    }
}