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

suspend fun getAllFavoritePicture() : List<FavoritePicture>{
    return suspendCoroutine {
        repo = favoritePictureRepository.allFavoritePicture
        it.resume(repo!!)
    }
}

private var repo: List<FavoritePicture>? = null
suspend fun checkFavoritePicture(id: Int) : Boolean {
    return suspendCoroutine {
        if(repo == null)
            repo = favoritePictureRepository.allFavoritePicture

        for(picture in repo!!) {
            if(id == picture.id){
                it.resume(true)
                return@suspendCoroutine
            }
        }
        it.resume(false)
    }
}