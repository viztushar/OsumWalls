package com.viztushar.osumwalls.activities;

import android.app.ActivityManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.NavUtils;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.CursorLoader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.viztushar.osumwalls.MainActivity;
import com.viztushar.osumwalls.R;
import com.viztushar.osumwalls.adapter.WallAdapter;
import com.viztushar.osumwalls.database.DBController;
import com.viztushar.osumwalls.items.WallpaperItem;
import com.viztushar.osumwalls.others.Utils;

import java.util.ArrayList;

/**
 * Created by Tushar on 27-10-2016.
 */

public class FavoritesActivity extends AppCompatActivity {
    private static final String TAG = FavoritesActivity.class.getSimpleName();
    DBController controller = new DBController(this);
    public Context context;
    private RecyclerView recyclerView;
    WallAdapter favadapteer;
    Toolbar mToolbar;
    SharedPreferences mPref;
    public boolean isTheme;
    Cursor c,c2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mPref = PreferenceManager.getDefaultSharedPreferences(this);
        isTheme = mPref.getBoolean(MainActivity.PREF_DARK_THEME, false);
        Utils.openToWalls = mPref.getBoolean("open_to_walls", false);
        Utils.mTheme = isTheme;
        if (isTheme) {
            setTheme(R.style.AppTheme_Dark_NoActionBar);
        } else {
            setTheme(R.style.AppTheme);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);
        mToolbar = (Toolbar) findViewById(R.id.fav_wall_toolbar);
        setSupportActionBar(mToolbar);
        if (Build.VERSION.SDK_INT >= 21)
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        if (Build.VERSION.SDK_INT >= 21) {
            mToolbar.setElevation(6);
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view2);

        if (Build.VERSION.SDK_INT >= 21) {
            ActivityManager.TaskDescription taskDescription = new
                    ActivityManager.TaskDescription(getResources().getString(R.string.fav),
                    BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher),
                    ContextCompat.getColor(this, R.color.colorPrimary));
            setTaskDescription(taskDescription);
        }

        favadapteer = new WallAdapter(this, getDatawall());
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager;
        mLayoutManager = new GridLayoutManager(context, 2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(favadapteer);
        ImageView noConnectionImg = (ImageView)findViewById(R.id.nofavImage);
       /* TextView nocon = (TextView)findViewById(R.id.nofav);
        TextView nocontext = (TextView)findViewById(R.id.nofavtext);
*/
        if (getDatawall().isEmpty()){
            noConnectionImg.setVisibility(View.VISIBLE);
            /*nocon.setVisibility(View.VISIBLE);
            nocontext.setVisibility(View.VISIBLE);*/
        }
    }

    public ArrayList<WallpaperItem> getDatawall() {
        ArrayList<WallpaperItem> images = new ArrayList<>();
        WallpaperItem mainInfo = null;
        c = controller.getData();
        if (c != null) {
            while (c.moveToNext()) {
                int wallnameIndex = c.getColumnIndex(DBController.wallname);
                String wallname = c.getString(wallnameIndex);
                int wallauthorIndex = c.getColumnIndex(DBController.wallauthor);
                String wallauthor = c.getString(wallauthorIndex);

                int wallurlIndex = c.getColumnIndex(DBController.wallurl);
                String wallurl = c.getString(wallurlIndex);

                int wallthumbIndex = c.getColumnIndex(DBController.wallthumb);
                String wallthumb = c.getString(wallthumbIndex);

                mainInfo = new WallpaperItem(this,wallname, wallauthor, wallurl, wallthumb);
                images.add(mainInfo);
            }
        }
        return images;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
               /* finish();*/
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

