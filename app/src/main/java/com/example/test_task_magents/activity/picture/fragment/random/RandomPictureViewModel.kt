package com.example.test_task_magents.activity.picture.fragment.random

import android.os.Parcelable
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.test_task_magents.model.GetPictureData
import com.example.test_task_magents.model.PictureData
import com.example.test_task_magents.retrofit.RetrofitInstance
import com.example.test_task_magents.retrofit.ServiceApi
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine


class RandomPictureViewModel : ViewModel() {
    companion object {
         var liveDataPictureList: MutableLiveData<MutableList<PictureData>> = MutableLiveData() //static для обновления status favorite после удаления
    }
    private val pages : MutableList<Int> = mutableListOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
    private val serviceApi : ServiceApi? = RetrofitInstance.getRetrofit()?.create(ServiceApi::class.java)
    private var state: Parcelable? = null

    fun setScrollState(state: Parcelable?) {
        this.state = state
    }

    fun getScrollState(): Parcelable? {
        return state
    }

    fun getLiveDataPictureList(): MutableLiveData<MutableList<PictureData>> {
        return liveDataPictureList
    }
    suspend fun downloadPictures() : Boolean? {
        return suspendCoroutine {
            if(pages.size == 0) {
                it.resume(null)
                return@suspendCoroutine
            }

            val page = pages[(System.currentTimeMillis() % pages.size).toInt()] // Random не подходите, т.к генит одну и ту же последовательность
            pages.remove(page)

            val call : Call<List<GetPictureData>> = serviceApi!!.getPicture(page, 100)

            call.enqueue(object : Callback<List<GetPictureData>> {
                override fun onResponse(call: Call<List<GetPictureData>>,
                                        response: Response<List<GetPictureData>>) {
                    val downloadList = ArrayList<PictureData>()

                    for(picture in response.body()!!)
                        downloadList.add(PictureData(picture.id, picture.author, picture.download_url))

                    downloadList.shuffle() //мешаем

                    val res : ArrayList<PictureData>?
                    if(liveDataPictureList.value != null) {
                        res = ArrayList(liveDataPictureList.value!!) //сначала кладём что есть
                        res.addAll(downloadList) //добавляем скачанное
                    }
                    else res = downloadList // ещё ничего нет -> в res кладём скачанное

                    liveDataPictureList.value = res!!

                    it.resume(true)
                }

                override fun onFailure(call: Call<List<GetPictureData>>, t: Throwable) {
                    it.resume(false)
                }
            })
        }
    }
}