package com.example.zhihuapplication;

import android.util.Log;

import java.util.concurrent.Callable;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Runnable implements Callable<String> {
    String url;

    public Runnable(String url){
        this.url = url;
    }

    @Override
    public String call() throws Exception {
        OkHttpClient client = new OkHttpClient();
        Request.Builder builder = new Request.Builder();
        Request request = builder.get().url(url).build();
        Call call = client.newCall(request);

        Response response = call.execute();
        String data = response.body().string();
        Log.d("aaa", "call: "+data);
        return data;
    }
}

