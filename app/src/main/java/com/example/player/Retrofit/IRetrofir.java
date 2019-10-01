package com.example.player.Retrofit;

import com.example.player.Common.Common;
import com.example.player.Model.Result;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface IRetrofir {
    @GET(Common.API_SEARCH)
    Observable<Result> getMusic(@Query("term") String search);
}
