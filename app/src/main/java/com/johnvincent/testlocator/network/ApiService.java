package com.johnvincent.testlocator.network;

import com.johnvincent.testlocator.model.JsonResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ApiService {

    @GET("organizations/organisation/latlon/{latitude}/{longitude}?distance=100")
    Call<List<JsonResponse>> getNearMeList(
        @Path("latitude") double latitude,
        @Path("longitude") double longitude
    );
}
