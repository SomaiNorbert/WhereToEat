package com.example.wheretoeat.fragments

import android.os.Bundle
import android.os.StrictMode
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.wheretoeat.*
import com.example.wheretoeat.models.Cities
import com.example.wheretoeat.models.ExampleItem
import com.example.wheretoeat.models.ResponseDS
import com.example.wheretoeat.models.Restaurant
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class FragmentRestaurants : Fragment(), RecyclerViewAdapter.OnItemClickedListener {

    var currentList = ArrayList<ExampleItem>()
    var rList = ArrayList<ExampleItem>()
    private lateinit var rViewRestaurants : RecyclerView
    var onlyFavorites = false
    lateinit var onlyFavoritesCheckBox:CheckBox
    lateinit var noRestaurants:TextView

    override fun onResume(){
        super.onResume()
        onlyFavoritesCheckBox.isChecked = false
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)

        val view = inflater.inflate(R.layout.fragment_restaurants, container, false)
        rViewRestaurants = view.findViewById(R.id.rViewRestaurants)
        val etxtSearch = view.findViewById<EditText>(R.id.etxtSearch)
        val citiesSpinner = view.findViewById<Spinner>(R.id.citiesSpinner)
        val priceSpinner = view.findViewById<Spinner>(R.id.priceSpinner)
        onlyFavoritesCheckBox = view.findViewById(R.id.onlyFavoritesCheckBox)
        noRestaurants = view.findViewById(R.id.txtNoRestaurants)
        noRestaurants.visibility = View.INVISIBLE

        rList = ArrayList();
        var executed = false

        val retrofit = Retrofit.Builder()
            .baseUrl("https://ratpark-api.imok.space/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val openTableAPI: OpenTableAPI = retrofit.create(OpenTableAPI::class.java)
        val callCities:Call<Cities> = openTableAPI.getCities()

        val responseCities = callCities.execute()
        val cities = responseCities.body()!!

        val citiesSpinnerAdapter = activity?.let {
            ArrayAdapter(it, android.R.layout.simple_spinner_item, cities.cities)
        }
        citiesSpinner.adapter = citiesSpinnerAdapter
        citiesSpinner.setSelection(0)

        val priceSpinnerAdapter = activity?.let{
            ArrayAdapter(it, android.R.layout.simple_spinner_item, listOf(1,2,3,4))
        }
        priceSpinner.adapter = priceSpinnerAdapter
        priceSpinner.setSelection(0)
        citiesSpinner.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                rList.clear()
                Log.d("pos", position.toString())
                val callRestaurants: Call<ResponseDS> = openTableAPI.getRestaurants(cities.cities[position])
                val response = callRestaurants.execute()
                val restaurants : ResponseDS = response.body()!!
                for (r in restaurants.restaurants){
                    var favorite = false
                    val databaseHandler = MySqliteHandler((activity as MainActivity))
                    if(databaseHandler.isFavorite(r.id)){
                        favorite = true;
                    }
                    val item:ExampleItem
                    val fav = databaseHandler.getFavoriteByID(r.id)
                    if(databaseHandler.getFavorites().contains(fav)){
                        if(fav?.img != ""){
                            item = ExampleItem(r.id, fav?.img!!, r.name, r.address, r.price, favorite);
                        }else{
                            item = ExampleItem(r.id, r.image_url, r.name, r.address, r.price, favorite);
                        }
                    }else{
                        item = ExampleItem(r.id, r.image_url, r.name, r.address, r.price, favorite);
                    }
                    rList.plusAssign(item);
                    Log.d("listSize", rList.size.toString())
                }
                val newList = filterByPrice(priceSpinner.selectedItemPosition + 1)
                showRestaurants(newList)
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }
        priceSpinner.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val newList = filterByPrice(position+1)
                showRestaurants(newList)
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }

        onlyFavoritesCheckBox.setOnClickListener {
            onlyFavorites = onlyFavoritesCheckBox.isChecked
            if(onlyFavorites){
                val newList = ArrayList<ExampleItem>()
                val databaseHandler = MySqliteHandler(activity as MainActivity)
                val favoritesIDList = databaseHandler.getFavorites()
                for(fav in favoritesIDList){
                    val callRestaurant:Call<Restaurant> = openTableAPI.getRestaurantByID(fav.ID)
                    val responseRestaurant = callRestaurant.execute()
                    val restaurant = responseRestaurant.body()!!
                    val img:String
                    val fav = databaseHandler.getFavoriteByID(restaurant.id)
                    if(databaseHandler.getFavorites().contains(fav)){
                        if(fav?.img != ""){
                            img = fav?.img!!
                        }else{
                            img = restaurant.image_url
                        }
                    }else{
                        img = restaurant.image_url
                    }
                    if(databaseHandler.isFavorite(restaurant.id)){
                        newList.add(ExampleItem(restaurant.id, img,restaurant.name, restaurant.address, restaurant.price, true))
                    }
                }
                showRestaurants(newList)
            }
            else{
                showRestaurants(rList)
            }

        }


        etxtSearch.addTextChangedListener {
            val txt = etxtSearch.text.toString()
            val newList = ArrayList<ExampleItem>()
            for(exampleItem in rList){
                if(exampleItem.title.toUpperCase().contains(txt.toUpperCase())){
                    newList.add(exampleItem)
                }
            }
            showRestaurants(newList)
        }

        return view
    }

    fun showRestaurants(list:ArrayList<ExampleItem>){
        currentList = list
        if(currentList.isEmpty()){
            noRestaurants.visibility = View.VISIBLE
        }
        else{
            noRestaurants.visibility = View.INVISIBLE
        }
        rViewRestaurants.adapter = RecyclerViewAdapter(list, this, this)
        rViewRestaurants.layoutManager = LinearLayoutManager(requireContext())
        rViewRestaurants.setHasFixedSize(true)

    }

    fun filterByPrice(price:Int):ArrayList<ExampleItem>{
        val newList = ArrayList<ExampleItem>()
        for(e in rList){
            if(e.price == price){
                newList.add(e)
            }
        }
        return newList
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