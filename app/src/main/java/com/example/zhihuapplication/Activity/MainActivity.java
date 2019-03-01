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

import com.example.zhihuapplication.adapter.myRecyclerAdapter;


import java.util.TreeMap;




public class MainActivity extends AppCompatActivity {
    DrawerLayout drawerLayout = null;
    Toolbar toolbar = null;
    public boolean isNightTheme ;
    TreeMap map = new TreeMap();
    static final String LATEST_NEWS_URL = "https://news-at.zhihu.com/api/4/news/latest";
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

                String data = loadData.loadData(LATEST_NEWS_URL);
                map = jsonTool.analyzeLatestShortNews(data);
                adapter.refreshDate(map);
                Log.d(TAG, "onRefresh: ");
            }
        });
    }
    public void loadMore(String date){

        String data = loadData.loadData(" https://news-at.zhihu.com/api/4/news/before/"+date);
        map = jsonTool.analyzeShortNews(data);

            adapter.upDate(map);
    }

    public void initToolbar(){
        ImageView imageView = findViewById(R.id.img_user);
        Glide.with(MainActivity.this).load(R.mipmap.ic_main).into(imageView);
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




}
