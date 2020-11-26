package com.example.wheretoeat

data class Restaurant(
    val id: Int,
    val name: String,
    val address: String,
    val city: String,
    val state: String,
    val area: String,
    val postal_code: String,
    val country: String,
    val phone: String,
    val lat: Double,
    val lng: Double,
    val price: Int,
    val reserve_url: String,
    val mobile_reserve_url: String,
    val image_url: String,
){
    override fun toString(): String {

        return "Id: $id\nAddress: $address\nCity: $city\nState: $state\nArea: $area\nPostal code: $postal_code\nCountry: $country\nPhone: $phone\nLat: $lat\n" +
                "Lng: $lng\nPrice: $price\nReserve url: $reserve_url\nMobile reserve url: $mobile_reserve_url\nImage_url: $image_url\n"

    }
}