package com.example.zhihuapplication;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;


import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;


import okhttp3.Response;



public class LoadData {
    private Context context;
    private String SHARED_PREFERENCES = "localData"; 
    private String DATA_NO_FOUND = "no found";
    Response response = null;
    String TAG = "myTag";
    public  String data;

    public LoadData(Activity context){
        this.context = context;
    }
    
    public String loadData(String url){
        if(hasLocalData(url) & !url.endsWith("latest")){
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

    private String getNetData(String url) {


        Runnable callable = new Runnable(url);
        FutureTask<String> ft = new FutureTask<>(callable);
        new Thread(ft).start();
        String data = null;
        try {
            data = ft.get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        saveData(url,data);

        return data;
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
