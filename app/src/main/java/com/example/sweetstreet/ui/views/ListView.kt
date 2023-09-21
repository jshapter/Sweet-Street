package com.example.sweetstreet.ui.views

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.sweetstreet.SweetEvent
import com.example.sweetstreet.ui.UiState
import com.example.sweetstreet.ui.components.VendorCard
import kotlinx.coroutines.flow.MutableStateFlow

@Composable
fun ListView(
    uiState: MutableStateFlow<UiState>,
    onEvent: (SweetEvent) -> Unit
) {

    val collectedUiState: State<UiState> = uiState.collectAsState()

    val showOpenOnly = collectedUiState.value.showOpenOnly

    var vendors = collectedUiState.value.vendors

    if (collectedUiState.value.filteredVendors.isNotEmpty()) {
        vendors = collectedUiState.value.filteredVendors
    }

    LazyColumn(
        contentPadding = PaddingValues(vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier.padding(start = 12.dp, end = 12.dp)
    ) {
        items(vendors) { vendor ->
            if (showOpenOnly) {
                if (vendor.isOpen) {
                    VendorCard(
                        uiState = uiState,
                        vendor = vendor,
                        onEvent = onEvent
                    )
                }
            } else {
                VendorCard(
                    uiState = uiState,
                    vendor = vendor,
                    onEvent = onEvent
                )
            }
        }
    }
}