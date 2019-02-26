package com.example.zhihuapplication;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class LoadData {
    private Context context;
    private String SHARED_PREFERENCES = "localData"; 
    private  String DATA_NO_FOUND = "no found";
    public LoadData(Activity context){
        this.context = context;
    }
    
    public String loadData(String url){
        if(hasLocalData(url)){
            return getLocalData(url);
        }
        else {
            return getNetData(url);
        }
    }
    private void saveData(String url,String data){
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCES,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(url,data);
        editor.commit();
    }
    private String getNetData(String url){
       
        OkHttpClient client = new OkHttpClient();
        Request.Builder builder = new Request.Builder();
        Request request = builder.get().url(url).build();
        Call call = client.newCall(request);
        Response response = null;
        String data = null;
        try {
            response = call.execute();
            data = response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(response.isSuccessful()){
            saveData(url,data);
            return data;
        }
        else {
            return null;
        }
    }
    private String getLocalData(String url){
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCES,Context.MODE_PRIVATE);
        String data = sharedPreferences.getString(url,DATA_NO_FOUND);
        return data;
    }
    private boolean hasLocalData(String url){
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCES,Context.MODE_PRIVATE);
        String data = sharedPreferences.getString(url,DATA_NO_FOUND);
        if(data == DATA_NO_FOUND){
            return false;
        }
        else {
            return true;
        }
    }
}
