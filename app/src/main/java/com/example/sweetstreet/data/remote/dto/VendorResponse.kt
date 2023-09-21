package com.example.sweetstreet.data.remote.dto

import kotlinx.serialization.Serializable

// API response will be serialized to this data class
@Serializable
data class VendorResponse(
    val id: String,
    val address: String,
    val city: String,
    val description: String,
    val img: String,
    val items: List<String>,
    val instagram: String,
    val latitude: String,
    val longitude: String,
    val name: String,
    val openingTimes: List<String>,
    val postcode: String,
    val starRating: String,
)
