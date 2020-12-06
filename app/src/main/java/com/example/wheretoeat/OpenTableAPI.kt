package com.example.wheretoeat

import com.example.wheretoeat.models.Cities
import com.example.wheretoeat.models.ResponseDS
import com.example.wheretoeat.models.Restaurant
import retrofit2.http.GET
import retrofit2.Call
import retrofit2.http.Path
import retrofit2.http.Query

interface OpenTableAPI {

    @GET("restaurants?")//TODO
    fun getRestaurants(@Query("city") city:String):Call<ResponseDS>

    @GET("cities")
    fun getCities():Call<Cities>

    @GET("restaurants/{id}")
    fun getRestaurantByID(@Path("id") id:Int?):Call<Restaurant>
}