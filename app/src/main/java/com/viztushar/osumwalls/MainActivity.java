package com.viztushar.osumwalls;

import android.annotation.TargetApi;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.viztushar.osumwalls.adapter.WallAdapter;
import com.viztushar.osumwalls.items.WallpaperItem;
import com.viztushar.osumwalls.tasks.GetWallpapers;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements GetWallpapers.Callbacks {

    Toolbar toolbar;
    private RecyclerView recyclerView;
    private ArrayList<WallpaperItem> items;
    boolean scroll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        items = new ArrayList<>();
        new GetWallpapers(this,this).execute();

    }


    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onListLoaded(String jsonResult) {
        try {
            if (jsonResult != null) {
                try {
                    JSONObject jsonResponse = new JSONObject(jsonResult);
                    JSONArray jsonMainNode = jsonResponse.optJSONArray("walls");
                    for (int i = 0; i < jsonMainNode.length(); i++) {
                        JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
                        items.add(new WallpaperItem(jsonChildNode.optString("name"),
                                jsonChildNode.optString("author"),
                                jsonChildNode.optString("url"),
                                jsonChildNode.optString("thumb")));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getApplicationContext(), 2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(new WallAdapter(this,items));
    }
}
