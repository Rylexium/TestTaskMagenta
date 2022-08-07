package com.example.test_task_magents.adapter

import android.graphics.Color
import android.graphics.Picture
import android.graphics.PorterDuff
import android.graphics.drawable.BitmapDrawable
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DecodeFormat
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.test_task_magents.R
import com.example.test_task_magents.db.FavoritePictureDatabase
import com.example.test_task_magents.db.model.FavoritePicture
import com.example.test_task_magents.db.repository.FavoritePictureRealization
import com.example.test_task_magents.favoritePictureRepository
import com.example.test_task_magents.model.PictureData
import com.example.test_task_magents.util.ConvertClass
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine


class PictureAdapter(val context: Fragment, val pictureList:ArrayList<PictureData> ) : RecyclerView.Adapter<PictureAdapter.PictureViewHolder>() {
    inner class PictureViewHolder(v: View):RecyclerView.ViewHolder(v){
        val imagePicture = v.findViewById<ImageView>(R.id.id_image_picture)
        val author = v.findViewById<TextView>(R.id.textview_author)
        val idPicture = v.findViewById<TextView>(R.id.textview_id_picture)
        val favoriteIcon = v.findViewById<ImageView>(R.id.icon_favorite)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PictureViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val v = inflater.inflate(R.layout.field_of_picture, parent, false)

        return PictureViewHolder(v)
    }


    override fun onBindViewHolder(holder: PictureViewHolder, position: Int) {
        val newList = pictureList[position]
        holder.author.text = newList.author
        holder.idPicture.text = newList.id
        Glide.with(context)
            .load(newList.url)
            .format(DecodeFormat.PREFER_RGB_565)
            .apply(RequestOptions.bitmapTransform(RoundedCorners(16)))
            .into(holder.imagePicture)

        ViewCompat.animate(holder.imagePicture)
            .withStartAction { holder.imagePicture.visibility = View.VISIBLE }
            .alpha(1f)
            .setInterpolator(AccelerateDecelerateInterpolator())
            .setDuration(100)
            .start()

        var isFavorite = false
        CoroutineScope(Dispatchers.IO).launch {
            if(checkFavoritePicture(holder.idPicture.text.toString().toInt())) {
                isFavorite = true
                for(item in repo!!) {
                    println(item.id)
                }
                Handler(Looper.getMainLooper()).post {
                    setFavorite(isFavorite, holder.favoriteIcon)
                }
            }
        }

        holder.imagePicture.setOnClickListener {
            isFavorite = !isFavorite
            setFavorite(isFavorite, holder.favoriteIcon)
            CoroutineScope(Dispatchers.Unconfined).launch {
                downloadImage(isFavorite, holder.idPicture.text.toString(), holder.author.text.toString(), holder.imagePicture)
            }
        }

    }

    private var repo: List<FavoritePicture>? = null
    private suspend fun checkFavoritePicture(id: Int) : Boolean {
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


    override fun getItemCount(): Int {
        return pictureList.size
    }
    private fun setFavorite(
        isFavorite: Boolean,
        favoriteIcon: ImageView) {
        favoriteIcon.setImageDrawable(ContextCompat.getDrawable(context.requireContext(), R.drawable.ic_baseline_favorite_border_24))
        favoriteIcon.setColorFilter(Color.DKGRAY, PorterDuff.Mode.SRC_ATOP)
        if(isFavorite) {
            favoriteIcon.setImageDrawable(ContextCompat.getDrawable(context.requireContext(), R.drawable.ic_baseline_favorite_24))
            favoriteIcon.setColorFilter(Color.RED, PorterDuff.Mode.SRC_ATOP)
        }
        else {
            favoriteIcon.setImageDrawable(ContextCompat.getDrawable(context.requireContext(), R.drawable.ic_baseline_favorite_border_24))
            favoriteIcon.setColorFilter(Color.DKGRAY, PorterDuff.Mode.SRC_ATOP)
        }
    }



    private suspend fun downloadImage(
        isFavorite: Boolean,
        idPicture: String,
        author: String,
        image: ImageView) {
        return coroutineScope {
            if(isFavorite) {
                favoritePictureRepository.insertFavoritePicture(
                    FavoritePicture(idPicture.toInt(), author,
                        ConvertClass.convertBitmapToString((image.drawable as BitmapDrawable).bitmap)))
            }
            else {
                favoritePictureRepository.deleteFavoritePicture(idPicture.toInt())
            }
            for(item in repo!!) {
                println(item.id)
            }
        }
    }

}