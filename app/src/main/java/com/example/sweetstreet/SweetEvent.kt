package com.example.sweetstreet

import com.example.sweetstreet.data.Vendor

sealed interface SweetEvent {
    data class SelectTab(
        val index: Int
    ): SweetEvent

    data class SelectVendor(
        val vendor: Vendor
    ): SweetEvent

    data class Filter(
        val filterType: String,
        val option: String,
        val selected: Boolean
    ): SweetEvent

    object ClearMapVendor: SweetEvent
}