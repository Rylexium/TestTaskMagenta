package com.example.test_task_magents.util.workWith

import android.content.Context
import com.example.test_task_magents.db.FavoritePictureDatabase
import com.example.test_task_magents.db.model.FavoritePicture
import com.example.test_task_magents.db.repository.FavoritePictureRealization
import kotlinx.coroutines.coroutineScope
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

lateinit var favoritePictureRepository : FavoritePictureRealization

suspend fun initDatabase(context : Context) {
    return coroutineScope {
        val daoFavoritePicture = FavoritePictureDatabase
            .getInstance(context)
            .getFavoritePictureDao()
        favoritePictureRepository = FavoritePictureRealization(daoFavoritePicture)
    }
}

suspend fun getAllFavoritePicture() : List<FavoritePicture> {
    return suspendCoroutine {
        favoritePictureList = favoritePictureRepository.allFavoritePicture
        it.resume(favoritePictureList!!)
    }
}

fun deleteFavoritePicture(id : Int) {
    val list = ArrayList(favoritePictureList!!)
    for(picture in list) {
        if(picture.id == id) {
            list.remove(picture)
            break
        }
    }
    favoritePictureList = list
}

private var favoritePictureList: List<FavoritePicture>? = null

suspend fun checkFavoritePicture(id: Int) : Boolean {
    return suspendCoroutine {
        if(favoritePictureList == null)
            favoritePictureList = favoritePictureRepository.allFavoritePicture

        for(picture in favoritePictureList!!) {
            if(id == picture.id){
                it.resume(true)
                return@suspendCoroutine
            }
        }
        it.resume(false)
    }
}