package com.example.sweetstreet.data

import com.google.android.gms.maps.model.LatLng

// data class object to represent each vendor
data class Vendor(
    val id: String,
    val address: String,
    val city: String,
    val description: String,
    val img: String,
    val isOpen: Boolean,
    val items: List<String>,
    val instagram: String,
    val latLong: LatLng,
    val name: String,
    val postcode: String,
    val starRating: String,
)
