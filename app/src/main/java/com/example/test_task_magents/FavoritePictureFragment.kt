package com.example.test_task_magents

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.test_task_magents.adapter.FavoritePictureAdapter
import com.example.test_task_magents.adapter.RandomPictureAdapter
import com.example.test_task_magents.databinding.FavoritePictureFragmentBinding
import com.example.test_task_magents.db.model.FavoritePicture
import com.example.test_task_magents.util.ConvertClass
import kotlinx.coroutines.*
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class FavoritePictureFragment : Fragment() {
    private var _binding: FavoritePictureFragmentBinding? = null

    private val binding get() = _binding!!
    private lateinit var recv : RecyclerView
    private lateinit var favoritePictureAdapter: FavoritePictureAdapter
    private var pictureList : ArrayList<FavoritePicture> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FavoritePictureFragmentBinding.inflate(inflater, container, false)
        Toast.makeText(context, "Favorite", Toast.LENGTH_SHORT).show()

        initComponents()

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        showFavoritePictures()
    }

    private fun initComponents() {
        recv = binding.favoritePictureRecyclerView
        favoritePictureAdapter = FavoritePictureAdapter(this, pictureList)

        recv.layoutManager = LinearLayoutManager(activity)
        recv.adapter = favoritePictureAdapter
    }

    private fun clearFavoritePicture() {
        pictureList.clear()
        Handler(Looper.getMainLooper()).post {
            favoritePictureAdapter.notifyDataSetChanged()
        }
    }

    private fun showFavoritePictures() {
        CoroutineScope(Dispatchers.IO).launch {
            Handler(Looper.getMainLooper()).post {
                binding.progressDownloadPicture.visibility = View.VISIBLE
            }
            val data = getAllFavoritePicture()

            if(pictureList.hashCode() == data.hashCode()) { // hash равны если не было изменений
                Handler(Looper.getMainLooper()).postDelayed({
                    binding.progressDownloadPicture.visibility = View.GONE
                }, 250)
                return@launch
            }

            clearFavoritePicture()


            for (picture in data)
                addFieldFavoritePicture(picture.id.toString(), picture.author, picture.picture)

            Handler(Looper.getMainLooper()).postDelayed({
                binding.progressDownloadPicture.visibility = View.GONE
            }, 250)
        }
    }


    private suspend fun addFieldFavoritePicture(id : String, author : String, picture : String) {
        return coroutineScope {
            pictureList.add(FavoritePicture(id.toInt(), author, picture))
            Handler(Looper.getMainLooper()).post {
                favoritePictureAdapter.notifyDataSetChanged()
            }
        }
    }

}