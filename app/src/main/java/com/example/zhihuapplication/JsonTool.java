package com.example.zhihuapplication;

import android.util.Log;

import com.example.zhihuapplication.ShortNewsData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.TreeMap;

public class JsonTool {
    static String date;
    static String lastDate;
    static String latestDate;
    static TreeMap map = new TreeMap();
    String TAG = "myTag";
    public TreeMap analyzeLatestShortNews(String message){

        ArrayList<ShortNewsData> shortNewsDataArrayList = null;
        try {
            Log.d(TAG, "analyzeLatestShortNews: "+message);
            JSONObject object = new JSONObject(message);

            latestDate = object.get("date").toString();

            JSONArray array = null;

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {

                array = new JSONArray(object.get("stories").toString());

            }
            shortNewsDataArrayList = new ArrayList<>(array.length());

            for(int num = 0;num<array.length();num++){

                JSONObject object1 = array.getJSONObject(num);
                String title = object1.get("title").toString();
                String id = object1.get("id").toString();
                String images =  object1.get("images").toString();
                ShortNewsData newsData = new ShortNewsData(title,id,images,latestDate);
                shortNewsDataArrayList.add(newsData);
            }



        } catch (JSONException e) {
            e.printStackTrace();
        }
       map.put("latestDate",shortNewsDataArrayList);
        return map;
    }

    public TreeMap analyzeShortNews(String message){
        ArrayList<ShortNewsData> shortNewsDataArrayList = null;
        try {
            JSONObject object = new JSONObject(message);
            if(date == null){
                lastDate = latestDate;
            }
            else {
                lastDate = date;
            }
            date = object.get("date").toString();
            JSONArray array = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                array = new JSONArray(object.get("stories").toString());
            }
            shortNewsDataArrayList = new ArrayList<>(array.length());
            for(int num = 0;num<array.length();num++){

                JSONObject object1 = array.getJSONObject(num);
                String title = object1.get("title").toString();
                String id = object1.get("id").toString();
                String images =  object1.get("images").toString();
                ShortNewsData newsData = new ShortNewsData(title,id,images,date);
                shortNewsDataArrayList.add(newsData);
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
        map.put(lastDate,shortNewsDataArrayList);
        return map;
    }
}
