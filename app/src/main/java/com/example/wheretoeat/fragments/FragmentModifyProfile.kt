package com.example.wheretoeat.fragments

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.wheretoeat.MainActivity
import com.example.wheretoeat.MySqliteHandler
import com.example.wheretoeat.R
import com.example.wheretoeat.models.Profile


class FragmentModifyProfile : Fragment() {

    private var imageUri: Uri? = null
    private lateinit var imgViewProfilePicture:ImageView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_modify_profile, container, false)

        val btnChangeImage = view.findViewById<Button>(R.id.btnChangeImage)
        val btnSave = view.findViewById<Button>(R.id.btnSave)
        val etxtName = view.findViewById<EditText>(R.id.etxtName)
        val etxtAddress = view.findViewById<EditText>(R.id.etxtAddress)
        val etxtEmail = view.findViewById<EditText>(R.id.etxtEmail)
        val etxtPhoneNumber = view.findViewById<EditText>(R.id.etxtPhoneNumber)
        imgViewProfilePicture = view.findViewById<ImageView>(R.id.imgViewProfilPicture)
        imgViewProfilePicture.setImageResource(R.drawable.imgexample)


        btnChangeImage.setOnClickListener {
            val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            startActivityForResult(gallery, 100)
        }

        btnSave.setOnClickListener {
            val databaseHandler = MySqliteHandler((activity as MainActivity).applicationContext)
            val profile = Profile(
                etxtName.text.toString(),
                etxtPhoneNumber.text.toString(),
                etxtAddress.text.toString(),
                etxtEmail.text.toString(),
                imageUri.toString()
            )
            if (databaseHandler.getProfile() == null) {
                databaseHandler.addProfile(profile)
            } else {
                databaseHandler.updateProfile(profile)
            }
            val fragmentProfile = FragmentProfile()
            (activity as MainActivity).supportFragmentManager.beginTransaction().apply {
                replace(R.id.fragment, fragmentProfile);
                commit()
            }
        }
        return view;
    }

    @Override
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == 100) {
            imageUri = data?.data
            Glide.with(this).load(imageUri).into(imgViewProfilePicture)
        }
    }



}