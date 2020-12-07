package com.example.wheretoeat

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.wheretoeat.models.Favorite
import com.example.wheretoeat.models.Profile


private const val DATABASE_NAME = "WheretoeatDB.db"

class MySqliteHandler(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, 2) {

    private val TABLE_PROFILE = "profile"
    private val TABLE_FAVORITES = "favorites"

    private val COLUMN_RESTAURANT_ID = "restaurantID"
    private val COLUMN_IS_FAVORITE = "isFavorite"
    private val COLUMN_RESTAURANT_IMAGE = "restaurantImage"

    private val COLUMN_NAME = "name"
    private val COLUMN_PHONE_NUMBER = "phoneNumber"
    private val COLUMN_ADDRESS = "address"
    private val COLUMN_EMAIL = "email"
    private val COLUMN_IMAGE = "image"

    private val CREATE_PROFILE_TABLE = "CREATE TABLE " + TABLE_PROFILE + " ( " + COLUMN_NAME + " TEXT , " + COLUMN_PHONE_NUMBER + " TEXT , " +
             COLUMN_ADDRESS + " TEXT , " + COLUMN_EMAIL + " TEXT , " + COLUMN_IMAGE + " TEXT " + " ) "

    private val CREATE_FAVORITES_TABLE = "CREATE TABLE $TABLE_FAVORITES ( $COLUMN_RESTAURANT_ID INTEGER PRIMARY KEY , $COLUMN_IS_FAVORITE BOOLEAN " +
            ", $COLUMN_RESTAURANT_IMAGE TEXT ) "

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(CREATE_FAVORITES_TABLE)
        db?.execSQL(CREATE_PROFILE_TABLE);
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_PROFILE");
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_FAVORITES");
        onCreate(db);
    }

    fun addFavorite(favorite:Favorite){
        val database = this.writableDatabase
        val values = ContentValues()
        values.put(COLUMN_RESTAURANT_ID, favorite.ID)
        values.put(COLUMN_IS_FAVORITE, favorite.isFavorite)
        values.put(COLUMN_RESTAURANT_IMAGE, favorite.img)
        database.insert(TABLE_FAVORITES, null, values)
        database.close()
    }

    fun favoritizeID(id: Int){
        val database = this.writableDatabase
        database.execSQL(
                "UPDATE $TABLE_FAVORITES SET $COLUMN_IS_FAVORITE = 'TRUE' WHERE $COLUMN_RESTAURANT_ID = '$id'"
        )
        database.close()
    }

    fun unFavoritizeID(id: Int){
        val database = this.writableDatabase
        database.execSQL(
                "UPDATE $TABLE_FAVORITES SET $COLUMN_IS_FAVORITE = 'FALSE' WHERE $COLUMN_RESTAURANT_ID = '$id'"
        )
        database.close()
    }

    fun setImgToID(id:Int, img:String){
        val database = this.writableDatabase
        database.execSQL(
                "UPDATE $TABLE_FAVORITES SET $COLUMN_RESTAURANT_IMAGE = '$img' WHERE $COLUMN_RESTAURANT_ID = '$id'"
        )
    }

    fun getFavoriteByID(id:Int):Favorite?{
        val database = this.readableDatabase
        val cursor: Cursor = database.rawQuery("SELECT * FROM $TABLE_FAVORITES WHERE $COLUMN_RESTAURANT_ID = '$id'", null)
        if (cursor.moveToFirst()) {
            return Favorite(cursor.getString(0).toInt(), cursor.getString(1).toBoolean(), cursor.getString(2))
        }
        return null
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
        database.execSQL(
            "UPDATE $TABLE_PROFILE SET $COLUMN_NAME = '" + profile.name + "' , $COLUMN_PHONE_NUMBER = '" + profile.phoneNumber +
                    "' , $COLUMN_ADDRESS = '" + profile.address + "' , $COLUMN_EMAIL = '" + profile.email + "' , $COLUMN_IMAGE = '" + profile.image + "'"
        )
        database.close()
    }

    fun getProfile() : Profile?{
        val database = this.readableDatabase
        val cursor: Cursor = database.rawQuery("SELECT * FROM $TABLE_PROFILE", null)
        if (cursor.moveToFirst()) {
                return Profile(
                    cursor.getString(0), cursor.getString(1), cursor.getString(2),
                    cursor.getString(3), cursor.getString(4)
                )
        }
        return null
    }

    fun getFavorites():ArrayList<Favorite>{
        val list = ArrayList<Favorite>()
        val database = this.readableDatabase
        val cursor = database.rawQuery("SELECT * FROM $TABLE_FAVORITES", null)
        if (cursor.moveToFirst()) {
            do {
                list.add(Favorite(cursor.getInt(0), cursor.getString(1).toBoolean(), cursor.getString(2)))
            } while (cursor.moveToNext())
        }
        return list
    }

    fun isFavorite(id: Int): Boolean{
        val database = this.readableDatabase
        val cursor: Cursor = database.rawQuery("SELECT * FROM $TABLE_FAVORITES WHERE $COLUMN_RESTAURANT_ID = '$id' AND $COLUMN_IS_FAVORITE = 'TRUE' ", null)
        if (cursor.moveToFirst()) {
                return true
        }
        return false
    }





}