package com.viztushar.osumwalls.fragments;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.viztushar.osumwalls.MainActivity;
import com.viztushar.osumwalls.R;
import com.viztushar.osumwalls.adapter.WallAdapter;
import com.viztushar.osumwalls.items.WallpaperItem;
import com.viztushar.osumwalls.others.Preferences;
import com.viztushar.osumwalls.others.Utils;
import com.viztushar.osumwalls.tasks.GetWallpapers;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class HomeFragment extends Fragment implements GetWallpapers.Callbacks {

    String wall;
    private View mainView;
    private Context context;
    private RecyclerView recyclerView;
    private ArrayList<WallpaperItem> items;
    private DrawerLayout drawer;
    private ProgressBar mainProgress;
    SharedPreferences mPref;
    private SwipeRefreshLayout swipeContainer;

    public HomeFragment() {

    }

    @SuppressLint("ValidFragment")
    public HomeFragment(String wall) {
        this.wall = wall;
        if (wall != null) {
            Log.i("wallpaper", "onCreateView: " + wall);
            Utils.currentWallFrag = wall;
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mainView = inflater.inflate(R.layout.wall_fragment, container, false);
        context = mainView.getContext();
        mainProgress = (ProgressBar) mainView.findViewById(R.id.mainProgress);
        mainProgress.setVisibility(View.VISIBLE);
        init();
        mPref = PreferenceManager.getDefaultSharedPreferences(context);
        swipeContainer = (SwipeRefreshLayout) mainView.findViewById(R.id.swipeContainer);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to refresh the list here.
                // Make sure you call swipeContainer.setRefreshing(false)
                // once the network request has completed successfully.
                //fetchTimelineAsync(0);
                FragmentTransaction tr = getFragmentManager().beginTransaction();
                tr.replace(R.id.fragment_holder, new HomeFragment(wall));
                tr.commit();
            }
        });

        Preferences prefs = new Preferences(context);
        Utils.gridNo = prefs.getInteger("grid", 2);
        items = new ArrayList<>();
        new GetWallpapers(context, this).execute();
        return mainView;
    }

    private void init() {
        setHasOptionsMenu(true);
        Toolbar toolbar = (Toolbar) mainView.findViewById(R.id.toolbar);
        setActionBar(toolbar);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBarTitle(wall);
        if (Build.VERSION.SDK_INT >= 21) {
            ActivityManager.TaskDescription taskDescription = new
                    ActivityManager.TaskDescription(getResources().getString(R.string.app_name),
                    BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher),
                    ContextCompat.getColor(getContext(), R.color.colorPrimary));
            getActivity().setTaskDescription(taskDescription);
        }

        getHomeActivity().setNav(toolbar);

    }

    private MainActivity getHomeActivity() {
        return ((MainActivity) getActivity());
    }

    private ActionBar getActionBar() {
        return ((MainActivity) getActivity()).getSupportActionBar();
    }

    private void setActionBar(Toolbar toolbar) {
        ((MainActivity) getActivity()).setSupportActionBar(toolbar);
    }

    private void setActionBarTitle(String title) {
        if (title != null) {
            getActionBar().setTitle(title);
        } else {
            getActionBar().setTitle(getResources().getString(R.string.app_name));
        }
    }

    private void getActionBarTitle(String wall) {
        if (wall != null) {
            if (wall == "walls") {
                setActionBarTitle(getResources().getString(R.string.nav_all));
            } else if (wall == "new") {
                setActionBarTitle(getResources().getString(R.string.nav_new));
            } else if (wall == "dope") {
                setActionBarTitle(getResources().getString(R.string.nav_one));
            } else if (wall == "landscape") {
                setActionBarTitle(getResources().getString(R.string.nav_two));
            } else if (wall == "nature") {
                setActionBarTitle(getResources().getString(R.string.nav_three));
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == android.R.id.home) {
            drawer.openDrawer(Gravity.LEFT);
        }

        return super.onOptionsItemSelected(item);
    }

    public void setDrawer(DrawerLayout drawer) {
        this.drawer = drawer;
    }


    @Override
    public void onListLoaded(String jsonResult, boolean newWalls) {
        try {
            if (jsonResult != null) {
                try {
                    JSONObject jsonResponse = new JSONObject(jsonResult);
                    JSONArray jsonMainNode = jsonResponse.optJSONArray(wall);
                    for (int i = 0; i < jsonMainNode.length(); i++) {
                        JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
                        items.add(new WallpaperItem(getActivity(), jsonChildNode.optString("name"),
                                jsonChildNode.optString("author"),
                                jsonChildNode.optString("url"),
                                jsonChildNode.optString("thumb")));
                        Log.i("Respones", "onListLoaded: " + jsonResponse);
                    }
                    Utils.noConnection = false;
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.i("Respones", "onListLoaded: " + jsonResult);

        if (!Utils.noConnection) {
            if (newWalls && !Utils.newWallsShown) {
                Toast.makeText(getActivity(), "New walls available!", Toast.LENGTH_LONG).show();
                Utils.newWallsShown = true;
            }
        } else {
            Toast.makeText(getContext(), "No Connection", Toast.LENGTH_LONG).show();
            ImageView noConnectionImg = (ImageView) mainView.findViewById(R.id.noConnectionImage);
            TextView nocon = (TextView) mainView.findViewById(R.id.nocon);
            TextView nocontext = (TextView) mainView.findViewById(R.id.nocontext);
            if (Utils.darkTheme) {
                nocon.setTextColor(getResources().getColor(R.color.white));
                nocontext.setTextColor(getResources().getColor(R.color.white));
            }
            noConnectionImg.setVisibility(View.VISIBLE);
            nocon.setVisibility(View.VISIBLE);
            nocontext.setVisibility(View.VISIBLE);

        }
        mainProgress.setVisibility(View.GONE);
        recyclerView = (RecyclerView) mainView.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager;
        mLayoutManager = new GridLayoutManager(context, Utils.gridNo);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(new WallAdapter(context, items));
        swipeContainer.setRefreshing(false);

    }
}
