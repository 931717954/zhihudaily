package com.example.zhihuapplication.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import android.webkit.WebView;


import com.example.zhihuapplication.R;


import org.json.JSONObject;



public class SecondActivity extends AppCompatActivity {
    String HIDE_HEADER_STYLE = "<style>div.headline{display:none;}</style>";
    String NEEDED_FORMAT_CSS_TAG = "<link rel=\"stylesheet\" type=\"text/css\" href=\"%s\"/>";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        WebView webView = findViewById(R.id.web_view);
        Intent intent = getIntent();
        String data = intent.getStringExtra("data");
        Log.d("zzz", "onCreate: "+data);
        JSONObject object = null;
        StringBuffer htmlData = new StringBuffer();
        htmlData.append(NEEDED_FORMAT_CSS_TAG);
        try {
            object = new JSONObject(data);
            htmlData.append((object.get("body")));
            htmlData.append((object.get("HIDE_HEADER_STYLE ")));
            htmlData.append((object.get("css")));

        } catch (Exception e) {
            e.printStackTrace();
        }

        webView.loadData(htmlData.toString(),"text/html; charset=utf-8","utf-8");
        Log.d("lll", "onCreate: "+htmlData.toString());
    }
}
