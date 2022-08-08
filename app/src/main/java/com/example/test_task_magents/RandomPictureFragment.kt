package com.example.test_task_magents

import android.graphics.Color
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
import com.example.test_task_magents.adapter.RandomPictureAdapter
import com.example.test_task_magents.databinding.RandomPictureFragmentBinding
import com.example.test_task_magents.model.GetPictureData
import com.example.test_task_magents.model.PictureData
import com.example.test_task_magents.retrofit.RetrofitInstance
import com.example.test_task_magents.retrofit.ServiceApi
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.random.Random


class RandomPictureFragment : Fragment() {
    private var _binding: RandomPictureFragmentBinding? = null

    private val binding get() = _binding!!

    private lateinit var recv : RecyclerView
    private var pictureList : ArrayList<PictureData> = ArrayList()
    private lateinit var randomPictureAdapter: RandomPictureAdapter
    private val pages : MutableList<Int> = mutableListOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
    private lateinit var snackbar: Snackbar


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = RandomPictureFragmentBinding.inflate(inflater, container, false)

        initComponents()

        downloadAndDisplay()
        recv.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (!recyclerView.canScrollVertically(1)) {
                    downloadAndDisplay()
                }
            }
        })
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        CoroutineScope(Dispatchers.IO).launch {
            getAllFavoritePicture()
        }
    }

    private fun initComponents() {
        recv = binding.randomPictureRecyclerView
        randomPictureAdapter = RandomPictureAdapter(this, pictureList)

        recv.layoutManager = LinearLayoutManager(activity)
        recv.adapter = randomPictureAdapter

        snackbar = Snackbar.make(recv, "Картинки закончились(((", Snackbar.LENGTH_LONG)
            .setAction("Любимые картинки") {
                Toast.makeText(context, "Свайпни вправо))", Toast.LENGTH_SHORT).show()
            }
            .setTextColor(Color.parseColor("#fdfffe"))
            .setBackgroundTint(Color.parseColor("#555555"))
            .setActionTextColor(Color.parseColor("#fdfffe"))
    }

    private fun downloadAndDisplay() {
        if(pages.size == 0) {
            if(!snackbar.isShown) snackbar.show()
            return
        }

        binding.progressDownloadPicture.visibility = View.VISIBLE

        val serviceApi = RetrofitInstance.getRetrofit()?.create(ServiceApi::class.java)

        val page = pages[(System.currentTimeMillis() % pages.size).toInt()] // Random не подходите, т.к генит одну и ту же последовательность
        pages.remove(page)

        val call : Call<List<GetPictureData>> = serviceApi!!.getPicture(page, 100)

        call.enqueue(object : Callback<List<GetPictureData>> {
            override fun onResponse(
                call: Call<List<GetPictureData>>,
                response: Response<List<GetPictureData>>) {

                Handler(Looper.getMainLooper()).postDelayed({
                    binding.progressDownloadPicture.visibility = View.GONE
                }, 250)

                CoroutineScope(Dispatchers.Unconfined).launch {
                    for (item in response.body()!!) {
                        addFieldRandomPicture(item.id, item.author, item.download_url)
                    }
                }
            }

            override fun onFailure(call: Call<List<GetPictureData>>, t: Throwable) {

            }
        })

    }
    private suspend fun addFieldRandomPicture(id : String, author : String, url : String) {
        return coroutineScope {
            pictureList.add(PictureData(id, author, url))
            randomPictureAdapter.notifyDataSetChanged()
        }
    }

}