package com.example.test_task_magents.adapter


import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DecodeFormat
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.test_task_magents.*
import com.example.test_task_magents.model.PictureData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class RandomPictureAdapter(val context: Fragment, val pictureList:ArrayList<PictureData> ) : RecyclerView.Adapter<RandomPictureAdapter.PictureViewHolder>() {
    inner class PictureViewHolder(v: View):RecyclerView.ViewHolder(v) {
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
                Handler(Looper.getMainLooper()).post {
                    println(holder.idPicture.text.toString())
                    setFavorite(context, true, holder.favoriteIcon)
                }
            }
        }

        holder.imagePicture.setOnClickListener {
            isFavorite = !isFavorite
            setFavorite(context, isFavorite, holder.favoriteIcon)
            CoroutineScope(Dispatchers.Unconfined).launch {
                downloadImage(isFavorite, holder.idPicture.text.toString(), holder.author.text.toString(), holder.imagePicture)
            }
        }

    }



    override fun getItemCount(): Int {
        return pictureList.size
    }




}