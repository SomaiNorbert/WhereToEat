package com.example.wheretoeat

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.wheretoeat.models.Profile


private const val DATABASE_NAME = "WheretoeatDB.db"

class MySqliteHandler(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, 1) {

    private val TABLE_PROFILE = "profile"
    private val TABLE_FAVORITES = "favorites"

    private val COLUMN_RESTAURANT_ID = "restaurantID"

    private val COLUMN_NAME = "name"
    private val COLUMN_PHONE_NUMBER = "phoneNumber"
    private val COLUMN_ADDRESS = "address"
    private val COLUMN_EMAIL = "email"
    private val COLUMN_IMAGE = "image"

    private val CREATE_PROFILE_TABLE = "CREATE TABLE " + TABLE_PROFILE + " ( " + COLUMN_NAME + " TEXT , " + COLUMN_PHONE_NUMBER + " TEXT , " +
             COLUMN_ADDRESS + " TEXT , " + COLUMN_EMAIL + " TEXT , " + COLUMN_IMAGE + " TEXT " + " ) "

    private val CREATE_FAVORITES_TABLE = "CREATE TABLE $TABLE_FAVORITES ( $COLUMN_RESTAURANT_ID INTEGER PRIMARY KEY  ) "

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(CREATE_FAVORITES_TABLE)
        db?.execSQL(CREATE_PROFILE_TABLE);
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_PROFILE");
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_FAVORITES");
        onCreate(db);
    }

    fun addIDtoFavorites(id: Int){
        val database = this.writableDatabase
        val values = ContentValues()
        values.put(COLUMN_RESTAURANT_ID, id)
        database.insert(TABLE_FAVORITES, null, values)
        database.close()
    }

    fun removeIDfromFavorites(id: Int){
        val database = this.writableDatabase
        database.execSQL("DELETE FROM $TABLE_FAVORITES WHERE $COLUMN_RESTAURANT_ID = $id")
        database.close()
    }

    fun addProfile(profile: Profile){
        val database = this.writableDatabase
        val values = ContentValues()
        values.put(COLUMN_NAME, profile.name)
        values.put(COLUMN_PHONE_NUMBER, profile.phoneNumber)
        values.put(COLUMN_ADDRESS, profile.address)
        values.put(COLUMN_EMAIL, profile.email)
        values.put(COLUMN_IMAGE, profile.image)
        database.insert(TABLE_PROFILE, null, values)
        database.close()
    }

    fun updateProfile(profile: Profile){
        val database = this.writableDatabase
        val values = ContentValues()
        values.put(COLUMN_NAME, profile.name)
        values.put(COLUMN_PHONE_NUMBER, profile.phoneNumber)
        values.put(COLUMN_ADDRESS, profile.address)
        values.put(COLUMN_EMAIL, profile.email)
        values.put(COLUMN_IMAGE, profile.image)
        database.execSQL(
            "UPDATE $TABLE_PROFILE SET $COLUMN_NAME = '" + profile.name + "' , $COLUMN_PHONE_NUMBER = '" + profile.phoneNumber +
                    "' , $COLUMN_ADDRESS = '" + profile.address + "' , $COLUMN_EMAIL = '" + profile.email + "' , $COLUMN_IMAGE = '" + profile.image + "'"
        )//TODO
        database.close()
    }

    fun getProfile() : Profile?{
        val selectAllQuery = "SELECT * FROM $TABLE_PROFILE"
        val database = this.readableDatabase
        val cursor: Cursor = database.rawQuery(selectAllQuery, null)
        if (cursor.moveToFirst()) {
                return Profile(
                    cursor.getString(0), cursor.getString(1), cursor.getString(2),
                    cursor.getString(3), cursor.getString(4)
                )
        }

        return null
    }

    fun getFavorites():ArrayList<String>{
        val list = ArrayList<String>()
        val database = this.readableDatabase
        val cursor = database.rawQuery("SELECT * FROM $TABLE_FAVORITES", null)
        if (cursor.moveToFirst()) {
            do {
                list.add(cursor.getString(0))
            } while (cursor.moveToNext())
        }
        return list
    }

    fun isFavorite(id: Int): Boolean{
        val selectAllQuery = "SELECT * FROM $TABLE_FAVORITES WHERE $COLUMN_RESTAURANT_ID = $id"
        val database = this.readableDatabase
        val cursor: Cursor = database.rawQuery(selectAllQuery, null)
        if (cursor.moveToFirst()) {
            return true
        }
        return false
    }





}