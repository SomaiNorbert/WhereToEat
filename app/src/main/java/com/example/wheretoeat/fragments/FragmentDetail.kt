package com.example.wheretoeat.fragments

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.wheretoeat.MainActivity
import com.example.wheretoeat.MySqliteHandler
import com.example.wheretoeat.OpenTableAPI
import com.example.wheretoeat.R
import com.example.wheretoeat.models.Favorite
import com.example.wheretoeat.models.Restaurant
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class FragmentDetail() : Fragment() {

    private lateinit var imgFavorite:ImageView
    lateinit var imgRestaurant:ImageView
    var id:Int? = null
    val context = this

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view:View = inflater.inflate(R.layout.fragment_detail, container, false)

        val txtRestaurantName = view.findViewById<TextView>(R.id.txtRestaurantName)
        val txtDetails = view.findViewById<TextView>(R.id.txtDetails)
        imgRestaurant = view.findViewById<ImageView>(R.id.imgRestaurant)
        val callButton = view.findViewById<Button>(R.id.callButton)
        val googleMapsButton = view.findViewById<Button>(R.id.googleMapsButton)
        imgFavorite = view.findViewById<ImageView>(R.id.imgFavorite)
        val changeImageButton = view.findViewById<Button>(R.id.changeImgButton)

        id = arguments?.getInt("id")
        var phoneNumber:String = ""

        changeImageButton.setOnClickListener {
            val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            startActivityForResult(gallery, 100)
        }

        imgFavorite.setOnClickListener {
            val databaseHandler = MySqliteHandler((activity as MainActivity))

            if(databaseHandler.getFavorites().contains(databaseHandler.getFavoriteByID(id!!))){
                if(!databaseHandler.isFavorite(id!!)){
                    databaseHandler.favoritizeID(id!!)
                }
                else{
                    databaseHandler.unFavoritizeID(id!!)
                }
            }else{
                databaseHandler.addFavorite(Favorite(id!!, true, ""))
            }
            updateImg(id!!)
        }

        val retrofit = Retrofit.Builder()
            .baseUrl("https://ratpark-api.imok.space/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val openTableAPI: OpenTableAPI = retrofit.create(OpenTableAPI::class.java)
        val myCall: Call<Restaurant> = openTableAPI.getRestaurantByID(id)

        val restaurant = myCall.execute().body()!!
        txtRestaurantName.text = restaurant.name
        txtDetails.text = restaurant.toString()
        val databaseHandler = MySqliteHandler(activity as MainActivity)
        val data = databaseHandler.getFavoriteByID(id!!)
        if(data?.img == ""){
            Log.d("img", restaurant.image_url)
            Glide.with(context).load(restaurant.image_url).into(imgRestaurant)
        }
        else{
            Log.d("img2", data?.img.toString())
            Glide.with(context).load(data?.img).into(imgRestaurant)
        }

        updateImg(restaurant.id)

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
        Thread.sleep(1000)
        return view;
    }

    private fun updateImg(id:Int){
        val databaseHandler = MySqliteHandler((activity as MainActivity))
        if(databaseHandler.isFavorite(id)){
            imgFavorite.setImageResource(R.drawable.favorite)
        }
        else {
            imgFavorite.setImageResource(R.drawable.star)
        }
    }

    @Override
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == 100) {
            Glide.with(this).load(data?.data).into(imgRestaurant)
            val databaseHandler = MySqliteHandler(activity as MainActivity)
            if(databaseHandler.getFavorites().contains(databaseHandler.getFavoriteByID(id!!))){
                databaseHandler.setImgToID(id!!, data?.data.toString())
            }
            else{
                databaseHandler.addFavorite(Favorite(id!!, false, data?.data.toString()))
            }
        }
    }

}