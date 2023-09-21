package com.example.sweetstreet.data.remote

import com.example.sweetstreet.data.remote.dto.VendorResponse
import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.KotlinxSerializer
import io.ktor.client.features.logging.Logging

interface VendorService {

    // return list of vendor response objects
    suspend fun getVendors(): List<VendorResponse>

    // create http client, install logging and serialization plugins
    companion object {
        fun create(): VendorService {
            return VendorServiceImplementation(
                client = HttpClient(Android) {
                    install(Logging)
                    install(JsonFeature) {
                        serializer = KotlinxSerializer()
                    }
                }
            )
        }
    }
}