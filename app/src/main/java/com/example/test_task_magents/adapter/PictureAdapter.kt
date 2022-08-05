package com.example.test_task_magents.adapter

import android.graphics.Color
import android.graphics.PorterDuff
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
import com.example.test_task_magents.model.PictureData
import com.example.test_task_magents.R
import kotlinx.coroutines.coroutineScope
import org.w3c.dom.Text

class PictureAdapter(val context: Fragment, val pictureList:ArrayList<PictureData> ) : RecyclerView.Adapter<PictureAdapter.PictureViewHolder>() {
    inner class PictureViewHolder(v: View):RecyclerView.ViewHolder(v){
        val imagePicture = v.findViewById<ImageView>(R.id.id_image_picture)
        val author = v.findViewById<TextView>(R.id.textview_author)
        val idPicture = v.findViewById<TextView>(R.id.textview_id_picture)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PictureViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val v = inflater.inflate(R.layout.field_of_picture, parent, false)

        var isFavorite = false

        val idPicture = v.findViewById<TextView>(R.id.textview_id_picture).text
        val author = v.findViewById<TextView>(R.id.textview_author).text
        val imageBitmap = v.findViewById<ImageView>(R.id.id_image_picture)
        val favoriteIcon = v.findViewById<ImageView>(R.id.icon_favorite)

        setFavorite(isFavorite, idPicture as String, author as String, imageBitmap, favoriteIcon)

        v.findViewById<ImageView>(R.id.id_image_picture).setOnClickListener {
            isFavorite = !isFavorite
            setFavorite(isFavorite, idPicture, author, imageBitmap, favoriteIcon)
        }
        return PictureViewHolder(v)
    }

    private fun setFavorite(
                isFavorite: Boolean,
                idPicture: String,
                author: String,
                imageBitmap: ImageView,
                favoriteIcon: ImageView) {
        favoriteIcon.setImageDrawable(ContextCompat.getDrawable(context.requireContext(), R.drawable.ic_baseline_favorite_border_24))
        favoriteIcon.setColorFilter(Color.DKGRAY, PorterDuff.Mode.SRC_ATOP)
        if(isFavorite) {
            favoriteIcon.setImageDrawable(ContextCompat.getDrawable(context.requireContext(), R.drawable.ic_baseline_favorite_24))
            favoriteIcon.setColorFilter(Color.RED, PorterDuff.Mode.SRC_ATOP)

            Toast.makeText(context.activity, "Тебе нравится?!!?!?! "
                    + idPicture + " "
                    + author, Toast.LENGTH_SHORT).show()
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
                                    imageBitmap: ImageView) {
        return coroutineScope {
            if(isFavorite) {
                // Скачиваем
            }
            else {
                // Удаляю из кеша
            }
        }
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
            .setDuration(250)
            .start()
    }

    override fun getItemCount(): Int {
        return pictureList.size
    }
}