package com.example.test_task_magents.db.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.test_task_magents.db.model.FavoritePicture

@Dao
interface FavoritePictureDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(favoritePicture: FavoritePicture)

    @Query("Delete from favorite_picture where id=:id")
    suspend fun deleteById(id : Int)

    @Query(value = "SELECT * from favorite_picture")
    fun getAll() : List<FavoritePicture>

}