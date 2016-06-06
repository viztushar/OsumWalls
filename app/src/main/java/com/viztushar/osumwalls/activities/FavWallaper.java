package com.viztushar.osumwalls.activities;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.viztushar.osumwalls.R;
import com.viztushar.osumwalls.adapter.WallAdapter;
import com.viztushar.osumwalls.items.WallpaperItem;
import com.viztushar.osumwalls.others.Preferences;
import com.viztushar.osumwalls.tasks.GetWallpapers;

import java.util.ArrayList;

/**
 * Created by Tushar on 06-06-2016.
 */

public class FavWallaper extends AppCompatActivity  {

    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private ArrayList<WallpaperItem> items;
    Preferences mPre;
    public WallAdapter mAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fav_wall);
        mPre=new Preferences(this);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if(items != null){

            mAdapter = new WallAdapter(this,items);

            recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
            RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getApplicationContext(), 2);
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setAdapter(new WallAdapter(this,items));

        }
    }

}
