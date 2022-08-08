package com.example.test_task_magents

import android.content.Context
import android.graphics.drawable.BitmapDrawable
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.test_task_magents.db.FavoritePictureDatabase
import com.example.test_task_magents.db.model.FavoritePicture
import com.example.test_task_magents.db.repository.FavoritePictureRealization
import com.example.test_task_magents.util.ConvertClass
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

fun setFavorite(fragment: Fragment,
                        isFavorite: Boolean,
                        favoriteIcon: ImageView
) {
    favoriteIcon.setImageDrawable(ContextCompat.getDrawable(fragment.requireContext(), R.drawable.ic_baseline_favorite_border_24))
    if(isFavorite)
        favoriteIcon.setImageDrawable(ContextCompat.getDrawable(fragment.requireContext(), R.drawable.ic_baseline_favorite_24))
    else
        favoriteIcon.setImageDrawable(ContextCompat.getDrawable(fragment.requireContext(), R.drawable.ic_baseline_favorite_border_24))

}

suspend fun downloadImage(isFavorite: Boolean,
                                  idPicture: String,
                                  author: String,
                                  image: ImageView) {
    return coroutineScope {
        if(image.drawable == null) return@coroutineScope

        if(isFavorite) {
            favoritePictureRepository.insertFavoritePicture(
                FavoritePicture(idPicture.toInt(), author,
                    ConvertClass.convertBitmapToString((image.drawable as BitmapDrawable).bitmap)))
        }
        else {
            favoritePictureRepository.deleteFavoritePicture(idPicture.toInt())
        }
    }
}
