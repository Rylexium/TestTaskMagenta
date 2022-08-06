package com.example.test_task_magents.db.dao

import androidx.room.*
import com.example.test_task_magents.db.model.FavoritePicture

@Dao
interface FavoritePictureDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(favoritePicture: FavoritePicture)

    @Delete
    suspend fun delete(favoritePicture: FavoritePicture)

    @Query(value = "SELECT * from favorite_picture")
    fun getAll() : List<FavoritePicture>

}