package com.example.test_task_magents

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.test_task_magents.adapter.PictureAdapter
import com.example.test_task_magents.databinding.RandomPictureFragmentBinding
import com.example.test_task_magents.db.model.FavoritePicture
import com.example.test_task_magents.model.GetPictureData
import com.example.test_task_magents.model.PictureData
import com.example.test_task_magents.retrofit.RetrofitIntenace
import com.example.test_task_magents.retrofit.ServiceApi
import kotlinx.coroutines.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine
import kotlin.random.Random


class RandomPictureFragment : Fragment() {
    private var _binding: RandomPictureFragmentBinding? = null

    private val binding get() = _binding!!

    private lateinit var recv : RecyclerView
    private var pictureList : ArrayList<PictureData> = ArrayList()
    private lateinit var pictureAdapter: PictureAdapter
    private val pages : MutableList<Int> = mutableListOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = RandomPictureFragmentBinding.inflate(inflater, container, false)
        Toast.makeText(context, "Random", Toast.LENGTH_SHORT).show()


        recv = binding.randomPictureRecyclerView
        pictureAdapter = PictureAdapter(this, pictureList)

        recv.layoutManager = LinearLayoutManager(activity)
        recv.adapter = pictureAdapter
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


    private fun downloadAndDisplay() {
        if(pages.size == 0) return
        val serviceApi = RetrofitIntenace.getRetrofit().create(ServiceApi::class.java)

        val page = pages[Random.nextInt(0, pages.size)]
        pages.remove(page)

        val call : Call<List<GetPictureData>> = serviceApi.getPicture(page, 100)

        call.enqueue(object : Callback<List<GetPictureData>> {
            override fun onResponse(
                call: Call<List<GetPictureData>>,
                response: Response<List<GetPictureData>>) {
                CoroutineScope(Dispatchers.Unconfined).launch {
                    for (item in response.body()!!) {
                        onAddField(item.id, item.author, item.download_url)
                    }
                }
            }

            override fun onFailure(call: Call<List<GetPictureData>>, t: Throwable) {

            }
        })

    }
    private suspend fun onAddField(id : String, author : String, url : String) {
        return coroutineScope {
            pictureList.add(PictureData(id, author, url, false))
            pictureAdapter.notifyDataSetChanged()
        }
    }

}