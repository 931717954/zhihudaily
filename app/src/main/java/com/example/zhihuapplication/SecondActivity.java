package com.example.zhihuapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;

import com.example.zhihuapplication.HtmlUtil.HtmlUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

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
        JSONObject object = null;
        StringBuffer htmlData = new StringBuffer();
        htmlData.append(NEEDED_FORMAT_CSS_TAG);
        try {
            object = new JSONObject(data);
            Log.d("lxx", "onCreate:1 "+object.get("css"));
            htmlData.append((object.get("css")));

            htmlData.append((object.get("body")));
        } catch (Exception e) {
            e.printStackTrace();
        }

        Log.d("lxx", "onCreate: "+htmlData);
        webView.loadData(htmlData.toString(),"text/html; charset=utf-8","utf-8");

    }

}
