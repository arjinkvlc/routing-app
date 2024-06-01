package com.example.routingproject.data.api

import com.example.routingproject.data.model.AddLocationRequest
import com.example.routingproject.data.model.AddTripRequest
import com.example.routingproject.data.model.ChangeLocationRequest
import com.example.routingproject.data.model.ChangePasswordRequest
import com.example.routingproject.data.model.ChangePasswordResponse
import com.example.routingproject.data.model.ChangeTripRequest
import com.example.routingproject.data.model.ConfirmationRequest
import com.example.routingproject.data.model.ConfirmationResponse
import com.example.routingproject.data.model.DeleteTripByIdResponse
import com.example.routingproject.data.model.LocationGetAllResponse
import com.example.routingproject.data.model.LocationGetByIdResponse
import com.example.routingproject.data.model.LocationGetByNameResponse
import com.example.routingproject.data.model.LoginRequest
import com.example.routingproject.data.model.LoginResponse
import com.example.routingproject.data.model.RegisterRequest
import com.example.routingproject.data.model.RegisterResponse
import com.example.routingproject.data.model.SearchGetByNameResponse
import com.example.routingproject.data.model.SearchHistoryGetAllResponse
import com.example.routingproject.data.model.SearchHistoryGetByIdResponse
import com.example.routingproject.data.model.SearchRequest
import com.example.routingproject.data.model.StopGetAllResponse
import com.example.routingproject.data.model.StopGetByIdResponse
import com.example.routingproject.data.model.StopGetByNameResponse
import com.example.routingproject.data.model.StopRequest
import com.example.routingproject.data.model.TripGetAllResponse
import com.example.routingproject.data.model.TripGetByIdResponse
import com.example.routingproject.data.model.TripGetByNameResponse
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    //Account endpoints
    @POST("/api/Account/register")
    suspend fun register(@Body registerRequest: RegisterRequest): RegisterResponse

    @POST("/api/Account/confirm-email")
    suspend fun confirmEmail(@Body confirmationRequest: ConfirmationRequest): ConfirmationResponse

    @POST("/api/Account/authenticate")
    suspend fun authenticate(@Body loginRequest: LoginRequest): LoginResponse

    @POST("/api/Account/change-password")
    suspend fun changePassword(
        @Body changePasswordRequest: ChangePasswordRequest,
        @Header("Authorization") token: String
    ): ChangePasswordResponse

    //Location Endpoints
    @GET("api/v1/Location")
    suspend fun getLocations(
        @Query("PageNumber") pageNumber: Int,
        @Query("PageSize") pageSize: Int,
        @Header("Authorization") token: String
    ): LocationGetAllResponse

    @POST("/api/v1/Location")
    suspend fun addLocation(
        @Body addLocationRequest: AddLocationRequest,
        @Header("Authorization") token: String
    ): Int

    @GET("api/v1/Location/{id}")
    suspend fun getLocationById(
        @Path("id") id: Int,
        @Header("Authorization") token: String
    ): LocationGetByIdResponse

    @PUT("/api/v1/Location/{id}")
    suspend fun changeLocationById(
        @Body changeLocationRequest: ChangeLocationRequest,
        @Path("id") id: Int,
        @Header("Authorization") token: String
    ): Int

    @DELETE("/api/v1/Location/{id}")
    suspend fun deleteLocationById(
        @Path("id") id: Int,
        @Header("Authorization") token: String
    )

    @GET("GetLocationByName")
    suspend fun getLocationByName(
        @Query("LocationName") locationName: String,
        @Query("PageNumber") pageNumber: Int,
        @Query("PageSize") pageSize: Int,
        @Header("Authorization") token: String
    ):LocationGetByNameResponse

    //Search History endpoints
    @GET("/api/v1/SearchHistory")
    suspend fun getSearchHistory(
        @Query("PageNumber") pageNumber: Int,
        @Query("PageSize") pageSize: Int,
        @Header("Authorization") token: String
    ): SearchHistoryGetAllResponse

    @POST("/api/v{version}/SearchHistory")
    suspend fun postSearch(
        @Path("version") version: String,
        @Query("SearchString") searchString: String,
        @Header("Authorization") token: String
    ) : Int

    @GET("/api/v1/SearchHistory/{id}")
    suspend fun getSearchHistoryById(
        @Path("id") id: Int,
        @Header("Authorization") token: String
    ): SearchHistoryGetByIdResponse

    @DELETE("/api/v1/SearchHistory/{id}")
    suspend fun deleteSearchById(
        @Path("id") id: Int,
        @Header("Authorization") token: String
    )
    @GET("api/v1/Stop/GetSearchHistoryByName")
    suspend fun getSearchHistoryByName(
        @Query("SearchString") searchString: String,
        @Query("PageNumber") pageNumber: Int,
        @Query("PageSize") pageSize: Int,
        @Header("Authorization") token: String
    ): SearchGetByNameResponse

    //Stop endpoints
    @GET("/api/v1/Stop")
    suspend fun getAllStops(
        @Query("PageNumber") pageNumber: Int,
        @Query("PageSize") pageSize: Int,
        @Header("Authorization") token: String
    ) : StopGetAllResponse

    @POST("/api/v1/Stop")
    suspend fun postStop(
        @Body stopRequest: StopRequest,
        @Header("Authorization") token: String
    ): Int

    @GET("/api/v1/Stop/{id}")
    suspend fun getStopExistsById(
        @Path("id") id: Int,
        @Header("Authorization") token: String
    ): StopGetByIdResponse

    @DELETE("/api/v1/Stop/{id}")
    suspend fun deleteStopById(
        @Path("id") id: Int,
        @Header("Authorization") token: String
    )

    @GET("api/v1/Stop/GetStopByName")
    suspend fun getStopByName(
        @Query("StopName") stopName: String,
        @Query("PageNumber") pageNumber: Int,
        @Query("PageSize") pageSize: Int,
        @Header("Authorization") token: String
    ):StopGetByNameResponse

    //Trip endpoints
    @GET("api/v1/Trip")
    suspend fun getTrips(
        @Query("PageNumber") pageNumber: Int,
        @Query("PageSize") pageSize: Int,
        @Header("Authorization") token: String
    ): TripGetAllResponse

    @POST("/api/v1/Trip")
    suspend fun addTrip(
        @Body addTripRequest: AddTripRequest,
        @Header("Authorization") token: String
    ): Int

    @GET("api/v1/Trip/{id}")
    suspend fun getTripById(
        @Path("id") id: Int,
        @Header("Authorization") token: String
    ): TripGetByIdResponse

    @PUT("/api/v1/Trip/{id}")
    suspend fun changeTripById(
        @Body changeTripRequest: ChangeTripRequest,
        @Path("id") id: Int,
        @Header("Authorization") token: String
    ): Int

    @DELETE("/api/v1/Trip/{id}")
    suspend fun deleteTripById(
        @Path("id") id: Int,
        @Header("Authorization") token: String
    ) : DeleteTripByIdResponse

    @GET("api/v1/Trip/GetTripByName")
    suspend fun getTripByName(
        @Query("TripName") stopName: String,
        @Query("PageNumber") pageNumber: Int,
        @Query("PageSize") pageSize: Int,
        @Header("Authorization") token: String
    ):TripGetByNameResponse

    @PUT("api/v1/Trip/changeFavorite")
    suspend fun changeFavoriteFlagForTrip(
        @Query("TripId") tripId: Int,
        @Query("isFavorite") isFavorite: Boolean,
        @Header("Authorization") token: String
    ): Boolean
}