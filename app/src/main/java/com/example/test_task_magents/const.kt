package com.example.test_task_magents

import android.content.Context
import com.example.test_task_magents.db.FavoritePictureDatabase
import com.example.test_task_magents.db.model.FavoritePicture
import com.example.test_task_magents.db.repository.FavoritePictureRealization
import kotlinx.coroutines.coroutineScope

lateinit var favoritePictureRepository : FavoritePictureRealization

suspend fun initDatabase(context : Context) {
    return coroutineScope {
        val daoFavoritePicture = FavoritePictureDatabase
            .getInstance(context)
            .getFavoritePictureDao()
        favoritePictureRepository = FavoritePictureRealization(daoFavoritePicture)
    }
}