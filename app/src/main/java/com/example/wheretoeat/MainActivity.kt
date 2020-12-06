package com.example.wheretoeat

import android.Manifest
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.example.wheretoeat.fragments.FragmentModifyProfile
import com.example.wheretoeat.fragments.FragmentProfile
import com.example.wheretoeat.fragments.FragmentRestaurants

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        requestPermissions()

        val fragmentProfile = FragmentProfile()
        val fragmentModifyProfile = FragmentModifyProfile()
        val fragmentRestaurants = FragmentRestaurants()
        val btnRestaurants = findViewById<Button>(R.id.btnRestaurants)
        val btnProfile = findViewById<Button>(R.id.btnProfile)

        replaceFragments(fragmentRestaurants);

        btnRestaurants.setOnClickListener{
            replaceFragments(fragmentRestaurants)
        }

        btnProfile.setOnClickListener{
            val databaseHandler = MySqliteHandler(this)
            if(databaseHandler.getProfile() == null) {
                replaceFragments(fragmentModifyProfile)
            }else{
                replaceFragments(fragmentProfile)
            }
        }
    }

    private fun replaceFragments(fragment: Fragment) {
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fragment, fragment)
            commit()

        }
    }

    private fun hasCallPhonePermission() =
        ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED

    private fun hasReadExternalStoragePermission() =
        ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED

    private fun requestPermissions(){
        var permissionsToRequest = mutableListOf<String>()
        if(!hasCallPhonePermission()){
            permissionsToRequest.add(Manifest.permission.CALL_PHONE)
        }
        if(!hasReadExternalStoragePermission()){
            permissionsToRequest.add(Manifest.permission.READ_EXTERNAL_STORAGE)
        }

        if(permissionsToRequest.isNotEmpty()){
            ActivityCompat.requestPermissions(this, permissionsToRequest.toTypedArray(), 0)
        }
        else{
            Log.d("permisson", "nothing to request")

        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode == 0 && grantResults.isNotEmpty()){
            for(i in grantResults.indices){
                if(grantResults[i] == PackageManager.PERMISSION_GRANTED){
                    Log.d("PermissionRequest", "${permissions[i]} granted")
                }
            }
        }
    }

}