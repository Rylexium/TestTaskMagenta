package com.example.test_task_magents.retrofit

import com.example.test_task_magents.model.GetPictureData
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ServiceApi {
    @GET("list?")
    fun getPicture(@Query("page") id: Int, @Query("limit") limit: Int): Call<List<GetPictureData>>
}
