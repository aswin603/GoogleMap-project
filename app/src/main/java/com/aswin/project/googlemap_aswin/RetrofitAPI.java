package com.aswin.project.googlemap_aswin;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface RetrofitAPI {


    @POST("api.php")
    Call<DataModal> createPost(@Body DataModal dataModal);
}
