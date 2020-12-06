package com.example.wheretoeat.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.wheretoeat.OpenTableAPI
import com.example.wheretoeat.R
import com.example.wheretoeat.models.Restaurant
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class FragmentDetail() : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view:View = inflater.inflate(R.layout.fragment_detail, container, false)

        val txtRestaurantName = view.findViewById<TextView>(R.id.txtRestaurantName)
        val txtDetails = view.findViewById<TextView>(R.id.txtDetails)
        val imgRestaurant = view.findViewById<ImageView>(R.id.imgRestaurant)
        val callButton = view.findViewById<Button>(R.id.callButton)
        val googleMapsButton = view.findViewById<Button>(R.id.googleMapsButton)

        val id: Int? = arguments?.getInt("id")
        var phoneNumber:String = ""

        val retrofit = Retrofit.Builder()
            .baseUrl("https://opentable.herokuapp.com/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val openTableAPI: OpenTableAPI = retrofit.create(OpenTableAPI::class.java)
        val myCall: Call<Restaurant> = openTableAPI.getRestaurantByID(id)
        myCall.enqueue(object : Callback<Restaurant> {
            override fun onResponse(call: Call<Restaurant>, response: Response<Restaurant>) {
                val restaurant = response.body()!!
                txtRestaurantName.text = restaurant.name
                txtDetails.text = restaurant.toString();
                imgRestaurant.setImageResource(R.mipmap.profileicon);

                phoneNumber = restaurant.phone
                callButton.setOnClickListener {
                    val intent = Intent(Intent.ACTION_CALL, Uri.parse("tel:$phoneNumber"))
                    startActivity(intent)
                }

                val lat = restaurant.lat
                val lng = restaurant.lng
                val name = restaurant.name
                googleMapsButton.setOnClickListener {
                    val gmmIntentUri = Uri.parse("geo:$lat,$lng?z=10&q=$name")
                    val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
                    mapIntent.setPackage("com.google.android.apps.maps")
                    startActivity(mapIntent)
                }

            }

            override fun onFailure(call: Call<Restaurant>, t: Throwable) {
                Log.e("asd", t.message.toString());
            }
        })


        Thread.sleep(1000)


        return view;
    }

}