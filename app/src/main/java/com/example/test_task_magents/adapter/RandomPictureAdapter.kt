package com.example.test_task_magents.adapter


import android.graphics.drawable.Drawable
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.DecodeFormat
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.example.test_task_magents.*
import com.example.test_task_magents.util.workWith.checkFavoritePicture
import com.example.test_task_magents.util.workWith.downloadImage
import com.example.test_task_magents.util.workWith.setFavorite
import com.example.test_task_magents.model.PictureData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class RandomPictureAdapter(
    val context: Fragment,
    private val pictureList: MutableList<PictureData>
)
    : RecyclerView.Adapter<RandomPictureAdapter.PictureViewHolder>() {

    inner class PictureViewHolder(v: View):RecyclerView.ViewHolder(v) {
        val fieldOfPicture = v.findViewById<CardView>(R.id.field_of_picture)
        val imagePicture = v.findViewById<ImageView>(R.id.id_image_picture)
        val author = v.findViewById<TextView>(R.id.textview_author)
        val idPicture = v.findViewById<TextView>(R.id.textview_id_picture)
        val favoriteIcon = v.findViewById<ImageView>(R.id.icon_favorite)
        val pictureProgressbar = v.findViewById<ProgressBar>(R.id.picture_progressbar)
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

        holder.pictureProgressbar.visibility = View.VISIBLE
        var isDownload = false
        Glide.with(context)
            .load(newList.url)
            .error(R.drawable.ic_baseline_error_outline_24)
            .listener(object : RequestListener<Drawable?> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable?>?,
                    isFirstResource: Boolean
                ): Boolean {
                    Toast.makeText(context.requireContext(), "Error in ${holder.idPicture.text}", Toast.LENGTH_SHORT).show()
                    holder.pictureProgressbar.visibility = View.GONE
                    holder.imagePicture.isEnabled = false
                    isDownload = false
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: Target<Drawable?>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    ViewCompat.animate(holder.imagePicture)
                        .withStartAction { holder.imagePicture.visibility = View.VISIBLE }
                        .alpha(1f)
                        .setInterpolator(AccelerateDecelerateInterpolator())
                        .setDuration(100)
                        .start()
                    isDownload = true
                    return false
                }
            })
            .format(DecodeFormat.PREFER_RGB_565)
            .apply(RequestOptions.bitmapTransform(RoundedCorners(16)))
            .into(holder.imagePicture)

        var isFavorite = false
        CoroutineScope(Dispatchers.IO).launch {
            isFavorite = checkFavoritePicture(holder.idPicture.text.toString().toInt())
            Handler(Looper.getMainLooper()).post {
                setFavorite(context, isFavorite, holder.favoriteIcon)
            }
        }

        holder.fieldOfPicture.setOnClickListener {
            if(!isDownload) {
                Glide.with(context)
                    .load(R.drawable.ic_baseline_error_outline_24)
                    .format(DecodeFormat.PREFER_RGB_565)
                    .apply(RequestOptions.overrideOf(150, 150))
                    .into(holder.imagePicture)
                return@setOnClickListener
            }
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