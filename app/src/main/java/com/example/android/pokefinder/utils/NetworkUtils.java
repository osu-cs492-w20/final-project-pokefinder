package com.example.android.pokefinder.utils;

import android.util.Log;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class NetworkUtils {

    private static final OkHttpClient mHTTPClient = new OkHttpClient();
    private static final String TAG = NetworkUtils.class.getSimpleName();

    public static String doHTTPGet(String url) throws IOException {
        Log.d(TAG, "Making request to url: " + url);
        Request request = new Request.Builder()
                .url(url)
                .build();
        Response response = mHTTPClient.newCall(request).execute();

        try {
            return response.body().string();
        } finally {
            response.close();
        }
    }
}