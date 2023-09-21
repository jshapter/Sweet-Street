package com.example.sweetstreet.ui.views

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import com.example.sweetstreet.R
import com.example.sweetstreet.SweetEvent
import com.example.sweetstreet.ui.UiState
import com.example.sweetstreet.ui.components.MapBottomSheet
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberMarkerState
import kotlinx.coroutines.flow.MutableStateFlow

@SuppressLint("StateFlowValueCalledInComposition")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapView(
    uiState: MutableStateFlow<UiState>,
    onEvent: (SweetEvent) -> Unit
) {
    val context = LocalContext.current

    val collectedUiState: State<UiState> = uiState.collectAsState()
    val vendors = collectedUiState.value.vendors

    val selectedVendor = uiState.value.selectedVendor

    val cameraPositionState = collectedUiState.value.cameraPositionState
    Log.d(ContentValues.TAG, cameraPositionState.position.toString())

    val sheetState = rememberModalBottomSheetState()

    var showBottomSheet by remember { mutableStateOf(uiState.value.showMapBottomSheet) }

    GoogleMap(
        cameraPositionState = cameraPositionState
    ) {
        vendors.forEach { vendor ->
            val markerState = rememberMarkerState(
                position = vendor.latLong
            )
            var icon = R.drawable.standard_marker
            if (vendor == selectedVendor) {
                icon = R.drawable.selected_marker
            }
            Marker(
                state = markerState,
                icon = bitmapDescriptor(context, icon),
                onClick = {
                    onEvent(SweetEvent.SelectVendor(vendor))
                    showBottomSheet = true
                    true
                }
            )
        }
    }
    if (showBottomSheet) {

        ModalBottomSheet(
            modifier = Modifier
                .fillMaxHeight(0.5f),
            onDismissRequest = {
                onEvent(SweetEvent.ClearMapVendor)
                showBottomSheet = false
            },
            sheetState = sheetState
        ) {
            if (selectedVendor != null) {
                MapBottomSheet(vendor = selectedVendor)
            }
        }
    }
}

fun bitmapDescriptor(
    context: Context,
    vectorResId: Int
): BitmapDescriptor? {

    val drawable = ContextCompat.getDrawable(context, vectorResId) ?: return null

    drawable.setBounds(0, 0, drawable.intrinsicWidth, drawable.intrinsicHeight)

    val bm = Bitmap.createBitmap(
        drawable.intrinsicWidth,
        drawable.intrinsicHeight,
        Bitmap.Config.ARGB_8888
    )

    val canvas = android.graphics.Canvas(bm)
    drawable.draw(canvas)

    return BitmapDescriptorFactory.fromBitmap(bm)
}