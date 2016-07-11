package com.viztushar.osumwalls;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import com.viztushar.osumwalls.activities.SettingsActivity;
import com.viztushar.osumwalls.fragments.HomeFragment;
import com.viztushar.osumwalls.others.Utils;

import static com.viztushar.osumwalls.others.Utils.PREF_COLORED_NAV;

public class MainActivity extends AppCompatActivity {

    private static final String PREF_DARK_THEME = "dark_theme";
    public boolean isTheme,isColorednav;
    SharedPreferences mPref;
    private NavigationView navigationView;
    private DrawerLayout mDrawerLayout;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mPref = PreferenceManager.getDefaultSharedPreferences(this);
        isTheme = mPref.getBoolean(PREF_DARK_THEME,false);
        Utils.mTheme = isTheme;
        if(isTheme) {setTheme(R.style.AppTheme_Dark);}
        else {setTheme(R.style.AppTheme);}
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;
        switchFragment(new HomeFragment("walls"), false);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.main_drawerlayout);
        navigationView = (NavigationView) findViewById(R.id.main_navigationview);
        isColorednav = mPref.getBoolean(PREF_COLORED_NAV,false);
        Utils.coloredNavBar = isColorednav;

        mSetNav();
    }

    public void mSetNav(){
        if (isColorednav)
        {
            if (Build.VERSION.SDK_INT >= 21){
                getWindow().setNavigationBarColor(ContextCompat.getColor(context,R.color.primary_dark));
            }
        }
    }

    public void updateToggleButton(Toolbar toolbar) {
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this,
                mDrawerLayout, toolbar, R.string.open, R.string.close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                invalidateOptionsMenu();
                syncState();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                invalidateOptionsMenu();
                syncState();
            }
        };
        mDrawerLayout.setDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
    }

    public void setNav(Toolbar toolbar) {
        updateToggleButton(toolbar);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_all:
                        item.setChecked(true);
                        HomeFragment fragment = new HomeFragment("walls");
                        switchFragment(fragment, true);
                        mDrawerLayout.closeDrawers();
                        break;
                   /* case R.id.navigation_fav:
                        item.setChecked(true);
                        startFavSection();
                        mDrawerLayout.closeDrawers();
                        break;*/
                    case R.id.navigation_new:
                        item.setChecked(true);
                        HomeFragment fragment1 = new HomeFragment("walls2");
                        switchFragment(fragment1, true);
                        mDrawerLayout.closeDrawers();
                        break;
                    case R.id.navigation_material:
                        item.setChecked(true);
                        HomeFragment fragment3 = new HomeFragment("walls2");
                        switchFragment(fragment3, true);
                        mDrawerLayout.closeDrawers();
                        break;
                    case R.id.navigation_landscape:
                        item.setChecked(true);
                        HomeFragment fragment4 = new HomeFragment("walls");
                        switchFragment(fragment4, true);
                        mDrawerLayout.closeDrawers();
                        break;
                    case R.id.navigation_nature:
                        item.setChecked(true);
                        HomeFragment fragment5 = new HomeFragment("walls");
                        switchFragment(fragment5, true);
                        mDrawerLayout.closeDrawers();
                        break;
                    case R.id.navigation_sea:
                        item.setChecked(true);
                        HomeFragment fragment6 = new HomeFragment("walls2");
                        switchFragment(fragment6, true);
                        mDrawerLayout.closeDrawers();
                        break;
                    case R.id.navigation_city:
                        item.setChecked(true);
                        HomeFragment fragment7 = new HomeFragment("walls");
                        switchFragment(fragment7, true);
                        mDrawerLayout.closeDrawers();
                        break;
                    case R.id.navigation_vintage:
                        item.setChecked(true);
                        HomeFragment fragment8 = new HomeFragment("walls2");
                        switchFragment(fragment8, true);
                        mDrawerLayout.closeDrawers();
                        break;
                    case R.id.navigation_about:
                        item.setChecked(true);
                        startSettingSection();
                        mDrawerLayout.closeDrawers();
                        break;

                }
                return true;
            }
        });
    }

    private void startSettingSection() {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }


    public void switchFragment(Fragment fragment, boolean b) {
        if (!b)
            getSupportFragmentManager().beginTransaction()
                    .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                    .replace(R.id.fragment_holder, fragment).commit();
        else
            getSupportFragmentManager().beginTransaction().addToBackStack(null)
                    .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                    .replace(R.id.fragment_holder, fragment).commit();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
        }
        return super.onOptionsItemSelected(item);
    }



    @Override
    protected void onNewIntent(Intent intent)
    {
        super.onNewIntent(intent);
    }

}
