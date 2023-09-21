package com.example.sweetstreet

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.sweetstreet.ui.ScreenScaffold
import com.example.sweetstreet.ui.UiState
import com.example.sweetstreet.viewmodel.BaseViewModel

@Composable
fun SweetStreetApp(
    viewModel: BaseViewModel
) {
    val collectedUiState: State<UiState> = viewModel.uiState.collectAsState()

    val vendors = collectedUiState.value.vendors

    if (vendors.isEmpty()) {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(modifier = Modifier.align(Alignment.Center)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    LinearProgressIndicator()
                }
                Spacer(modifier = Modifier.height(32.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(text = "Fetching local vendors")
                }
            }
        }
    } else {
        ScreenScaffold(viewModel = viewModel)
    }
}