package data.api

import data.model.TruckResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers

interface TrucksApi {
    @Headers("x-api-key: PG6HsbuPFpA5que1IWC6WeS91NMyzt8vZnlJINj0")
    @GET("/dev/vehicles")
    suspend fun getAvailableTrucks(): Response<TruckResponse>
}
