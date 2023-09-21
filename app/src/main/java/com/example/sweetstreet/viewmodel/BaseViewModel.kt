package com.example.sweetstreet.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sweetstreet.SweetEvent
import com.example.sweetstreet.data.Vendor
import com.example.sweetstreet.data.remote.VendorService
import com.example.sweetstreet.data.remote.dto.VendorResponse
import com.example.sweetstreet.ui.UiState
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.CameraPositionState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDateTime

class BaseViewModel: ViewModel() {

    private val _uiState = MutableStateFlow(UiState())
    val uiState = _uiState

    init {
        getVendors(VendorService.create())
    }

    private fun isVendorOpen(vendor: VendorResponse): Boolean {

        var open = false

        val dt = LocalDateTime.now()
        val openHour = vendor.openingTimes[0].substring(0, 2).toInt()
        val closeHour = vendor.openingTimes[1].substring(0, 2).toInt()

        if (dt.hour in openHour..closeHour) {
            open = true
        }

        return open
    }

    private fun getVendors(service: VendorService) {
        viewModelScope.launch {

            val apiResults = service.getVendors()

            val allVendors = emptyList<Vendor>().toMutableList()
            val cities = emptyList<String>().toMutableList()
            val items = emptyList<String>().toMutableList()

            apiResults.forEach { vendor ->

                val newVendor = Vendor(
                    id = vendor.id,
                    address = vendor.address,
                    city = vendor.city,
                    description = vendor.description,
                    img = vendor.img,
                    isOpen = isVendorOpen(vendor),
                    items = vendor.items,
                    instagram = vendor.instagram,
                    latLong = LatLng(vendor.latitude.toDouble(), vendor.longitude.toDouble()),
                    name = vendor.name,
                    postcode = vendor.postcode,
                    starRating = vendor.starRating
                )

                allVendors.add(newVendor)

                if (newVendor.city !in cities) {
                    cities.add(newVendor.city)
                }
                for (item in newVendor.items) {
                    if (item !in items) {
                        items.add(item)
                    }
                }

            }

            delay(2000) // to demonstrate loading screen

            _uiState.update {
                it.copy(
                    vendors = allVendors,
                    cities = cities,
                    items = items
                )
            }
        }
    }

    fun onEvent(event: SweetEvent) {

        when (event) {
            is SweetEvent.SelectTab -> {
                val index = event.index

                if (index == 1) {
                    _uiState.update {
                        it.copy(
                            tabIndex = 0,
                            onListView = true,
                            selectedVendor = null,
                            showMapBottomSheet = false,
                            cameraPositionState = CameraPositionState(
                                position = CameraPosition.fromLatLngZoom(
                                    LatLng(53.449967, -2.597613), 8.5f
                                )
                            )
                        )
                    }
                } else {
                    _uiState.update {
                        it.copy(
                            tabIndex = 1,
                            onListView = false
                        )
                    }
                }
            }

            is SweetEvent.SelectVendor -> {
                val vendor = event.vendor

                val cameraPositionState = CameraPositionState(
                    position = CameraPosition.fromLatLngZoom(
                        LatLng(vendor.latLong.latitude - 0.02, vendor.latLong.longitude), 13f
                    )
                )

                _uiState.update {
                    it.copy(
                        showMapBottomSheet = true,
                        selectedVendor = vendor,
                        cameraPositionState = cameraPositionState
                    )
                }
            }

            is SweetEvent.ClearMapVendor -> {
                _uiState.update {
                    it.copy(
                        showMapBottomSheet = false,
//                    selectedVendor = null
                    )
                }
            }

            is SweetEvent.Filter -> {
                val filterType = event.filterType
                val selected = event.selected
                val allVendors = _uiState.value.vendors
                val filteredVendors = _uiState.value.filteredVendors.toMutableList()
                var filterApplied = uiState.value.filterApplied
                val option = event.option

                when (filterType) {
                    "availability" -> {
                        if (selected) {
                            _uiState.update {
                                it.copy(
                                    showOpenOnly = true
                                )
                            }
                        } else {
                            _uiState.update {
                                it.copy(
                                    showOpenOnly = false
                                )
                            }
                        }
                    }

                    "location" -> {
                        if (selected) {
                            if (filterApplied) {
                                filteredVendors.removeAll(filteredVendors.filter { it.city != option })
                                if (filteredVendors.isEmpty()) {
                                    filterApplied = false
                                }
                            } else {
                                filteredVendors.addAll(allVendors.filter { it.city == option })
                                filterApplied = true
                            }
                        } else {
                            filteredVendors.removeAll(filteredVendors.filter { it.city == option })
                            if (filteredVendors.isEmpty()) {
                                filterApplied = false
                            }
                        }
                        _uiState.update {
                            it.copy(
                                filteredVendors = filteredVendors.distinct(),
                                filterApplied = filterApplied
                            )
                        }
                    }
                }
            }
        }
    }
}
