package com.example.sweetstreet.data.remote

import com.example.sweetstreet.data.remote.dto.VendorResponse
import io.ktor.client.HttpClient
import io.ktor.client.features.ClientRequestException
import io.ktor.client.features.RedirectResponseException
import io.ktor.client.features.ServerResponseException
import io.ktor.client.request.get
import io.ktor.client.request.url

class VendorServiceImplementation(
    private val client: HttpClient
) : VendorService {

    // return list of vendor response objects through http route or display exception type if unsuccessful
    override suspend fun getVendors(): List<VendorResponse> {
        return try {
            client.get { url(HttpRoutes.VENDORS) }
        } catch(e: RedirectResponseException) {
            println("Error: ${e.response.status.description}")
            return arrayListOf()
        } catch (e: ClientRequestException) {
            println("Error: ${e.response.status.description}")
            return arrayListOf()
        } catch (e: ServerResponseException) {
            println("Error: ${e.response.status.description}")
            return arrayListOf()
        }
    }
}