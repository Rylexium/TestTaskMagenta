package com.example.test_task_magents.retrofit;

import com.example.test_task_magents.model.GetPictureData;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ServiceApi {
    @GET("list?")
    Call<List<GetPictureData>> getPicture(@Query("page") int id,
                                          @Query("limit") int limit);
}
