package com.example.wheretoeat

import retrofit2.http.GET
import retrofit2.Call

interface OpenTableAPI {

    @GET("restaurants?city=Chicago")
    fun getRestaurants():Call<ResponseDS>

    @GET("cities")
    fun getCities():Call<Cities>
}