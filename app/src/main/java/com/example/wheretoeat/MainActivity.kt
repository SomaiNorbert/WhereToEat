package com.example.wheretoeat

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.fragment.app.Fragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val fragmentProfile = FragmentProfile()
        val fragmentRestaurants = FragmentRestaurants()
        val btnRestaurants = findViewById<Button>(R.id.btnRestaurants)
        val btnProfile = findViewById<Button>(R.id.btnProfile)

        replaceFragments(fragmentRestaurants);

        btnRestaurants.setOnClickListener{
            replaceFragments(fragmentRestaurants);
        }

        btnProfile.setOnClickListener{
            replaceFragments(fragmentProfile);
        }
    }

    private fun replaceFragments(fragment: Fragment) {
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fragment, fragment)
            commit()

        }
    }
}