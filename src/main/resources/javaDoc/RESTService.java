package com.beatte.art.imgsrch.rest;

import retrofit2.GsonConverterFactory;
import retrofit2.Retrofit;

/**
 * @author art.beatte
 * @version 2/1/16.
 */
public class RESTService {

    private static final String sEndpoint = "https://api.gettyimages.com/v3/search/images/";
    private Retrofit mRetrofit;

    private Retrofit getRetrofit() {
        if (mRetrofit == null) {
            mRetrofit = new Retrofit.Builder()
                    .baseUrl(sEndpoint)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return mRetrofit;
    }

    public GettyServiceImpl getImageService() {
        return getRetrofit().create(GettyServiceImpl.class);
    }
}