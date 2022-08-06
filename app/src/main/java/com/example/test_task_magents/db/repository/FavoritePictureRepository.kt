package com.example.test_task_magents.db.repository

import com.example.test_task_magents.db.model.FavoritePicture


interface FavoritePictureRepository {
    val allFavoritePicture : List<FavoritePicture>
    suspend fun insertFavoritePicture(favoritePicture: FavoritePicture)
    suspend fun deleteFavoritePicture(favoritePicture: FavoritePicture)
}