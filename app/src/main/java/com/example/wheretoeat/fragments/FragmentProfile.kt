package com.example.wheretoeat.fragments

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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.wheretoeat.*
import com.example.wheretoeat.models.ExampleItem
import com.example.wheretoeat.models.Restaurant
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class FragmentProfile : Fragment(), RecyclerViewAdapter.OnItemClickedListener  {

    lateinit var txtNoFavorites:TextView
    lateinit var rViewFavorites:RecyclerView
    lateinit var currentList:ArrayList<ExampleItem>

    override fun onResume(){
        super.onResume()
        showFavorites()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        val txtName = view.findViewById<TextView>(R.id.txtName)
        val txtAddress = view.findViewById<TextView>(R.id.txtAddress)
        val txtPhoneNumber = view.findViewById<TextView>(R.id.txtPhoneNumber)
        val txtEmail = view.findViewById<TextView>(R.id.txtEmail)
        val imgProfile = view.findViewById<ImageView>(R.id.imgProfil);
        val btnModify = view.findViewById<Button>(R.id.btnModify)
        rViewFavorites = view.findViewById(R.id.rViewFavorites)
        txtNoFavorites = view.findViewById(R.id.txtNoFavorites)
        txtNoFavorites.visibility = View.INVISIBLE

        val databaseHandler = MySqliteHandler((activity as MainActivity).applicationContext)
        val profile = databaseHandler.getProfile()

        if(profile!=null){
            var txt = "Name: ${profile.name}"
            txtName.text = txt
            txt = "Address: ${profile.address}"
            txtAddress.text = txt
            txt = "Phone Number: ${profile.phoneNumber}"
            txtPhoneNumber.text = txt
            txt = "Email: ${profile.email}"
            txtEmail.text = txt
            //Log.d("bug", profile.image);
            //imgProfile.setImageURI(Uri.parse(profile.image)) //TODO set the images
        }

        btnModify.setOnClickListener {
            val fragmentModifyProfile = FragmentModifyProfile()
            (activity as MainActivity).supportFragmentManager.beginTransaction().apply {
                replace(R.id.fragment, fragmentModifyProfile);
                commit()
            }
        }
        showFavorites()

        return view;
    }

    private fun showFavorites(){
        val retrofit = Retrofit.Builder()
            .baseUrl("https://opentable.herokuapp.com/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val openTableAPI: OpenTableAPI = retrofit.create(OpenTableAPI::class.java)

        val newList = ArrayList<ExampleItem>()
        val databaseHandler = MySqliteHandler(activity as MainActivity)
        val favoritesIDList = databaseHandler.getFavorites()
        for(id in favoritesIDList){
            val callRestaurant: Call<Restaurant> = openTableAPI.getRestaurantByID(id.toIntOrNull())
            val responseRestaurant = callRestaurant.execute()
            val restaurant = responseRestaurant.body()!!
            var favorite = false
            if(databaseHandler.isFavorite(restaurant.id)){
                favorite = true;
            }
            newList.add(ExampleItem(restaurant.id, restaurant.image_url,restaurant.name, restaurant.address, restaurant.price, favorite))
        }

        if(newList.isEmpty()){
            txtNoFavorites.visibility = View.VISIBLE
        }
        else{
            txtNoFavorites.visibility = View.INVISIBLE
        }
        currentList = newList
        rViewFavorites.adapter = RecyclerViewAdapter(newList, this)
        rViewFavorites.layoutManager = LinearLayoutManager(requireContext())
        rViewFavorites.setHasFixedSize(true)

    }

    override fun onItemClick(position: Int) {
        val fragmentDetail = FragmentDetail()
        val bundle:Bundle = Bundle();
        bundle.putInt("id", currentList[position].id);
        fragmentDetail.arguments = bundle;
        (activity as MainActivity).supportFragmentManager.beginTransaction().apply {
            replace(R.id.fragment, fragmentDetail);
            commit()
        }
    }

}