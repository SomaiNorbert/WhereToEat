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
import com.example.wheretoeat.MainActivity
import com.example.wheretoeat.MySqliteHandler
import com.example.wheretoeat.R


class FragmentProfile : Fragment() {

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
            //imgProfile.setImageURI(Uri.parse(profile.image)) //TODO
        }

        btnModify.setOnClickListener {
            val fragmentModifyProfile = FragmentModifyProfile()
            (activity as MainActivity).supportFragmentManager.beginTransaction().apply {
                replace(R.id.fragment, fragmentModifyProfile);
                commit()
            }
        }

        return view;
    }

}