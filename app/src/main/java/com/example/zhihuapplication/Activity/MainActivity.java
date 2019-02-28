package com.example.zhihuapplication.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.zhihuapplication.JsonTool;
import com.example.zhihuapplication.LoadData;
import com.example.zhihuapplication.R;
import com.example.zhihuapplication.ShortNewsData;
import com.example.zhihuapplication.adapter.myRecyclerAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.TreeMap;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class MainActivity extends AppCompatActivity {
    DrawerLayout drawerLayout = null;
    Toolbar toolbar = null;
    public boolean isNightTheme ;
    Calendar cal ;
    TreeMap map = new TreeMap();
    ArrayList<String> dateList = new ArrayList<>();
    String date;
    String lastDate;
    static final int LATEST_SHORT_NEWS = 0;
    static final int SHORT_NEWS = 1;
    static final int DETAIL_NEWS = 2;
    static final int COMMENT = 3;
    static final String LATEST_NEWS_URL = "https://news-at.zhihu.com/api/4/news/latest";
    String latestDate ;
    String mMessage;
    String TAG = "myTag";
    RecyclerView recyclerView;
    myRecyclerAdapter adapter;
    SwipeRefreshLayout  refreshLayout;
    LoadData loadData;
    JsonTool jsonTool;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loadData = new LoadData(MainActivity.this);
        jsonTool = new JsonTool();
        drawerLayout = findViewById(R.id.dl_main);
        getDayNightTheme();
        Log.d("checkTheme", "onCreate: "+isNightTheme);
        if(isNightTheme == true){
            getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }
        initToolbar();

//        try {
//            sendRequest(new URL(LATEST_NEWS_URL),LATEST_SHORT_NEWS);
//
//        } catch (MalformedURLException e) {
//            e.printStackTrace();
//
//        }
        String message = loadData.loadData(LATEST_NEWS_URL);
        map = jsonTool.analyzeLatestShortNews(message);
        initRecyclerView();
        initSwipeRefreshLayout();

    }
    public void initRecyclerView(){
        recyclerView = findViewById(R.id.res_main);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);
        adapter = new myRecyclerAdapter(map,this);
        recyclerView.setAdapter(adapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        adapter.setOnItemClickListener(new myRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position, String args) {
//                try {
//                    sendRequest(new URL("https://news-at.zhihu.com/api/4/news/"+args),DETAIL_NEWS);
//                } catch (MalformedURLException e) {
//
//                }
                String message = loadData.loadData("https://news-at.zhihu.com/api/4/news/"+args);
                Intent intent = new Intent(MainActivity.this, SecondActivity.class);
                intent.putExtra("data",message);

                startActivity(intent);
            }


        });
    }



    public void closeSwipeRefreshLayout() {
        refreshLayout.setRefreshing(false);
        recyclerView.scrollToPosition(0);
    }
    public void initSwipeRefreshLayout(){
        refreshLayout = findViewById(R.id.refresh_layout);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
//                try {
//                    sendRequest(new URL(LATEST_NEWS_URL),LATEST_SHORT_NEWS);
//
//                } catch (MalformedURLException e) {
//                    e.printStackTrace();
//                }

                Log.d(TAG, "onRefresh: ");
            }
        });
    }
    public void loadMore(String date){

//        try {
//            sendRequest( new URL(" https://news-at.zhihu.com/api/4/news/before/"+date),SHORT_NEWS);
//
//        } catch (MalformedURLException e) {
//            e.printStackTrace();
//        }
        String data = loadData.loadData(" https://news-at.zhihu.com/api/4/news/before/"+date);
        map = jsonTool.analyzeShortNews(data);

            adapter.upDate(map);



    }

    public void setLatestDate(String latestDate){
        this.latestDate = latestDate;
    }
    
//    public void jsonTool(String message , int mod){
//        switch (mod){
//
//
//            case LATEST_SHORT_NEWS:
//                try {
//
//                    JSONObject object = new JSONObject(message);
//
//                    latestDate = object.get("date").toString();
//
//                    JSONArray array = null;
//
//                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
//
//                        array = new JSONArray(object.get("stories").toString());
//
//                    }
//                    ArrayList<ShortNewsData> shortNewsDataArrayList = new ArrayList<>(array.length());
//
//                    for(int num = 0;num<array.length();num++){
//
//                        JSONObject object1 = array.getJSONObject(num);
//                        String title = object1.get("title").toString();
//                        String id = object1.get("id").toString();
//                        String images =  object1.get("images").toString();
//                        ShortNewsData newsData = new ShortNewsData(title,id,images,latestDate);
//                        shortNewsDataArrayList.add(newsData);
//                    }
//
//                    map.put("latestDate",shortNewsDataArrayList);
//                    Log.d("lzx", "jsonTool: "+shortNewsDataArrayList);
//                    Log.d(TAG, "jsonTool: "+shortNewsDataArrayList.get(0).printMessage());
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//
//                break;
//
//
//            case SHORT_NEWS:
//                try {
//                    JSONObject object = new JSONObject(message);
//                    if(date == null){
//                        lastDate = latestDate;
//                    }
//                    else {
//                        lastDate = date;
//                    }
//                    date = object.get("date").toString();
//                    JSONArray array = null;
//                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
//                        array = new JSONArray(object.get("stories").toString());
//                    }
//                    ArrayList<ShortNewsData> shortNewsDataArrayList = new ArrayList<>(array.length());
//                    for(int num = 0;num<array.length();num++){
//
//                        JSONObject object1 = array.getJSONObject(num);
//                        String title = object1.get("title").toString();
//                        String id = object1.get("id").toString();
//                        String images =  object1.get("images").toString();
//                        ShortNewsData newsData = new ShortNewsData(title,id,images,date);
//                        shortNewsDataArrayList.add(newsData);
//                    }
//
//                    map.put(lastDate,shortNewsDataArrayList);
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//
//                break;
//
//
//            case DETAIL_NEWS:
//
//
//                break;
//
//
//            case COMMENT:
//
//
//                break;
//            default:
//                    break;
//        }
//    }

    public void initToolbar(){
        ImageView imageView = findViewById(R.id.img_user);
        Glide.with(MainActivity.this).load(R.mipmap.ic_user).into(imageView);
        toolbar = findViewById(R.id.tb_main);
        toolbar.setTitle("首页");
        toolbar.inflateMenu(R.menu.menu);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP & isNightTheme == true) {
            toolbar.setNavigationIcon(R.mipmap.ic_menu_dark);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP & isNightTheme == false){
            toolbar.setNavigationIcon(R.mipmap.ic_menu_light);
        }

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(3);
            }
        });
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.tb_log_in:
                        break;

                    case R.id.tb_day_night_mod:
                        isNightTheme = !isNightTheme;
                        setTheme();
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                            recreate();
                        }
                        break;
                    case R.id.tb_setting:
                        break;
                    default :
                        break;

                }


                return false;
            }
        });
    }

    public void getDayNightTheme(){
        SharedPreferences sharedPreferences = getSharedPreferences("data",MODE_PRIVATE);
        isNightTheme = sharedPreferences.getBoolean("isNightTheme",false);
    }

    public void setTheme(){

        //将夜间模式设置存入sharedpreferences
        SharedPreferences sharedPreferences = getSharedPreferences("data",MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("isNightTheme",isNightTheme);
        editor.commit();

        //切换模式
        if(isNightTheme){
            getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            toolbar.setNavigationIcon(R.mipmap.ic_menu_dark);
        }
        else {
            getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            toolbar.setNavigationIcon(R.mipmap.ic_menu_light);
        }
    }

    public void mapAdd(String key , ArrayList<ShortNewsData> list){
        map.put(key , list);
    }

//    public void sendRequest (final URL url, final int mod){
//
//        Thread thread = new Thread(new Runnable() {
//            StringBuffer message = null;
//            String flag;
//            BufferedReader reader = null;
//            FileInputStream inputStream = null;
//            InputStreamReader inputStreamReader = null;
//            @Override
//            public void run() {
//                try {
//                    OkHttpClient client = new OkHttpClient();
//                    Request.Builder builder = new Request.Builder();
//                    Request request = builder.get().url(url).build();
//                    Call call = client.newCall(request);
//                    call.enqueue(new Callback() {
//                        @Override
//                        public void onFailure(Call call, IOException e) {
//                            e.printStackTrace();
//                        }
//
//                        @Override
//                        public void onResponse(Call call, Response response) throws IOException {
//                            mMessage = response.body().string();
//                            Log.d(TAG, "onResponse: "+mMessage);
//                            if(mod != DETAIL_NEWS){
//                                jsonTool(mMessage,mod);
//                                }else {
//                                    Intent intent = new Intent(MainActivity.this, SecondActivity.class);
//                                    intent.putExtra("data",mMessage);
//                                Log.d("lxx", "onResponse: "+mMessage);
//                                    startActivity(intent);
//                                }
//                            if(mod == LATEST_SHORT_NEWS){
//                                runOnUiThread(new Runnable() {
//                                @Override
//                                public void run() {
//                                    if(recyclerView == null){
//                                        initRecyclerView();
//                                    }
//                                    else {
//                                        adapter.refreshDate(map);
//                                    }
//                                }
//                            });
//                            }
//                            if (mod == SHORT_NEWS){
//                                runOnUiThread(new Runnable() {
//                                    @Override
//                                    public void run() {
//                                        adapter.upDate(map);
//                                    }
//                                });
//                            }
//                        }
//                    });
//
//
//
//
//
//
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//
//
//                }
//
//        });
//        thread.start();
//
//    }
}
