package com.example.wheretoeat

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory
import java.util.function.Consumer

class fragment_restaurants : Fragment() {

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_restaurants, container, false)
        val rViewRestaurants = view.findViewById<RecyclerView>(R.id.rViewRestaurants)
        val etxtSearch = view.findViewById<EditText>(R.id.etxtSearch)
        val btnFilter = view.findViewById<Button>(R.id.btnFilters)

        val l = doIt();
        //Log.d("asd", l[0].address)
        rViewRestaurants.adapter = RecyclerViewAdapter(l)
        rViewRestaurants.layoutManager = LinearLayoutManager(this.context)
        rViewRestaurants.setHasFixedSize(true)



        return view
    }

    fun doIt(): List<ExampleItem>{
        val rList = ArrayList<ExampleItem>()


        val retrofit = Retrofit.Builder()
                .baseUrl("https://opentable.herokuapp.com/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        val openTableAPI:OpenTableAPI = retrofit.create(OpenTableAPI::class.java)
        val myCall: Call<ResponseDS> = openTableAPI.getRestaurants()
        myCall.enqueue(object: Callback<ResponseDS>{
            override fun onResponse(call: Call<ResponseDS>, response: Response<ResponseDS>) {
                val restaurants : ResponseDS = response.body()!!
                for (r in restaurants.restaurants){
                    val item = ExampleItem(r.image_url, r.name, r.address, r.price, false);
                    Log.d("uri", r.image_url);
                    Log.d("asd", item.address)
                    rList+=item;
                    Log.d("aa", rList.size.toString())
                }
            }
            override fun onFailure(call: Call<ResponseDS>, t: Throwable) {
                Log.e("asd", t.message.toString());
            }
        })

        Log.d("wtfffff", rList.size.toString())
        return rList

    }

}