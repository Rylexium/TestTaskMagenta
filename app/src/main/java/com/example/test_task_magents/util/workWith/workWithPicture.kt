package com.example.test_task_magents.util.workWith

import android.graphics.drawable.BitmapDrawable
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.test_task_magents.R
import com.example.test_task_magents.db.model.FavoritePicture
import com.example.test_task_magents.util.ConvertClass
import kotlinx.coroutines.coroutineScope

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
                          image: ImageView
) {
    return coroutineScope {
        if(image.drawable == null) return@coroutineScope

        if(isFavorite) {
            favoritePictureRepository.insertFavoritePicture(
                FavoritePicture(idPicture.toInt(), author,
                    ConvertClass.convertBitmapToString((image.drawable as BitmapDrawable).bitmap))
            )
        }
        else {
            favoritePictureRepository.deleteFavoritePicture(idPicture.toInt())
        }
    }
}
