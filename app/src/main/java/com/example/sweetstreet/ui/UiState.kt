package com.example.sweetstreet.ui

import com.example.sweetstreet.data.Vendor
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.CameraPositionState

data class UiState(
    val tabIndex: Int = 0,
    val onListView: Boolean = true,
    val showOpenOnly: Boolean = false,

    val showMapBottomSheet: Boolean = false,

    val filterApplied: Boolean = false,

    val vendors: List<Vendor> = emptyList<Vendor>().toMutableList(),
    val filteredVendors: List<Vendor> = vendors.toMutableList(),
    val filteredByCity: List<Vendor> = vendors.toMutableList(),
    val filteredByOpen: List<Vendor> = vendors.toMutableList(),
    val selectedVendor: Vendor? = null,

    val cities: MutableList<String> = emptyList<String>().toMutableList(),
    val items: MutableList<String> = emptyList<String>().toMutableList(),

    val cameraPositionState: CameraPositionState = CameraPositionState(position = CameraPosition.fromLatLngZoom(
        LatLng(53.449967, -2.597613),8.5f)
    )
)
