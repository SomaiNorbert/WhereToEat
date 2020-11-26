package com.example.wheretoeat

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory

class FragmentRestaurants : Fragment(), RecyclerViewAdapter.OnItemClickedListener {

    var rList = ArrayList<ExampleItem>()

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_restaurants, container, false)
        val rViewRestaurants = view.findViewById<RecyclerView>(R.id.rViewRestaurants)
        val etxtSearch = view.findViewById<EditText>(R.id.etxtSearch)
        val btnFilter = view.findViewById<Button>(R.id.btnFilters)

        rList = ArrayList();
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
                    val item = ExampleItem(r.id, r.image_url, r.name, r.address, r.price, false);
                    rList.plusAssign(item);
                }
            }
            override fun onFailure(call: Call<ResponseDS>, t: Throwable) {
                Log.e("asd", t.message.toString());
            }
        })
        rViewRestaurants.adapter = RecyclerViewAdapter(rList, this)
        rViewRestaurants.layoutManager = LinearLayoutManager(this.context)
        rViewRestaurants.setHasFixedSize(true)

        return view
    }


    override fun onItemClick(position: Int) {
        val fragmentDetail = FragmentDetail()
        val bundle:Bundle = Bundle();
        bundle.putInt("id", rList[position].id);
        fragmentDetail.arguments = bundle;
        (activity as MainActivity).supportFragmentManager.beginTransaction().apply {
            replace(R.id.fragment, fragmentDetail);
            commit()
        }
    }

}