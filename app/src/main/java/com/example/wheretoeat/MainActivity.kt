package com.example.wheretoeat

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val fragmentProfile = fragment_profile()
        val fragmentRestaurants = fragment_restaurants()
        val btnRestaurants = findViewById<Button>(R.id.btnRestaurants)
        val btnProfile = findViewById<Button>(R.id.btnProfile)

        supportFragmentManager.beginTransaction().apply{
            replace(R.id.fragment, fragmentRestaurants)
            commit()
        }


        btnRestaurants.setOnClickListener{
            supportFragmentManager.beginTransaction().apply{
                replace(R.id.fragment, fragmentRestaurants)
                commit()
            }
        }

        btnProfile.setOnClickListener{
            supportFragmentManager.beginTransaction().apply{
                replace(R.id.fragment, fragmentProfile)
                commit()
            }
        }
    }
}