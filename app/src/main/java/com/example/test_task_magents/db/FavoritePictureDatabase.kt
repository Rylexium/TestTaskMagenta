package com.example.test_task_magents.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.test_task_magents.db.dao.FavoritePictureDao
import com.example.test_task_magents.db.model.FavoritePicture

@Database(entities = [FavoritePicture::class], version = 1)
abstract class FavoritePictureDatabase : RoomDatabase() {
    abstract fun getFavoritePictureDao() : FavoritePictureDao

    companion object {
        private var database : FavoritePictureDatabase? = null

        @Synchronized
        fun getInstance(context: Context) : FavoritePictureDatabase {
            return if(database == null) {
                database = Room
                    .databaseBuilder(context, FavoritePictureDatabase::class.java, "db")
                    .build()
                database as FavoritePictureDatabase
            }
            else {
                database as FavoritePictureDatabase
            }
        }
    }
}
