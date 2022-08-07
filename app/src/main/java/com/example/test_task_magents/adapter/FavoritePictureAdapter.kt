package com.example.test_task_magents.adapter


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DecodeFormat
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.test_task_magents.R
import com.example.test_task_magents.db.model.FavoritePicture
import com.example.test_task_magents.downloadImage
import com.example.test_task_magents.setFavorite
import com.example.test_task_magents.util.ConvertClass
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FavoritePictureAdapter(val context: Fragment, private val pictureList:ArrayList<FavoritePicture> )
    : RecyclerView.Adapter<FavoritePictureAdapter.PictureViewHolder>()  {

    inner class PictureViewHolder(v: View):RecyclerView.ViewHolder(v) {
        val imagePicture = v.findViewById<ImageView>(R.id.id_image_picture)
        val author = v.findViewById<TextView>(R.id.textview_author)
        val idPicture = v.findViewById<TextView>(R.id.textview_id_picture)
        val favoriteIcon = v.findViewById<ImageView>(R.id.icon_favorite)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoritePictureAdapter.PictureViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val v = inflater.inflate(R.layout.field_of_picture, parent, false)

        return PictureViewHolder(v)
    }

    override fun onBindViewHolder(holder: FavoritePictureAdapter.PictureViewHolder, position: Int) {
        val newList = pictureList[position]
        holder.author.text = newList.author
        holder.idPicture.text = newList.id.toString()
        Glide.with(context)
            .load(ConvertClass.convertStringToBitmap(newList.picture))
            .format(DecodeFormat.PREFER_RGB_565)
            .apply(RequestOptions.bitmapTransform(RoundedCorners(16)))
            .into(holder.imagePicture)

        ViewCompat.animate(holder.imagePicture)
            .withStartAction { holder.imagePicture.visibility = View.VISIBLE }
            .alpha(1f)
            .setInterpolator(AccelerateDecelerateInterpolator())
            .setDuration(100)
            .start()

        setFavorite(context, true, holder.favoriteIcon)
        holder.imagePicture.setOnClickListener {
            pictureList.removeAt(position)
            notifyDataSetChanged()
            CoroutineScope(Dispatchers.Unconfined).launch {
                downloadImage(true, holder.idPicture.text.toString(), holder.author.text.toString(), holder.imagePicture)
            }
        }
    }

    override fun getItemCount(): Int {
        return pictureList.size
    }
}