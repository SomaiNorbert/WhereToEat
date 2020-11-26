package com.example.wheretoeat

import retrofit2.http.GET
import retrofit2.Call
import retrofit2.http.Path

interface OpenTableAPI {

    @GET("restaurants?city=Chicago")//TODO
    fun getRestaurants():Call<ResponseDS>

    @GET("cities")
    fun getCities():Call<Cities>

    @GET("restaurants/{id}")
    fun getRestaurantByID(@Path("id") id:Int?):Call<Restaurant>
}