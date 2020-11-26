package com.example.wheretoeat

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import org.w3c.dom.Text
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

        val id: Int? = arguments?.getInt("id")

        val retrofit = Retrofit.Builder()
            .baseUrl("https://opentable.herokuapp.com/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val openTableAPI:OpenTableAPI = retrofit.create(OpenTableAPI::class.java)
        val myCall: Call<Restaurant> = openTableAPI.getRestaurantByID(id)
        myCall.enqueue(object: Callback<Restaurant> {
            override fun onResponse(call: Call<Restaurant>, response: Response<Restaurant>) {
                val restaurant = response.body()!!
                txtRestaurantName.text = restaurant.name
                txtDetails.text = restaurant.toString();
                imgRestaurant.setImageResource(R.mipmap.profileicon);
            }
            override fun onFailure(call: Call<Restaurant>, t: Throwable) {
                Log.e("asd", t.message.toString());
            }
        })

        return view;
    }

}