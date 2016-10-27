package com.viztushar.osumwalls;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.viztushar.osumwalls.activities.FavoritesActivity;
import com.viztushar.osumwalls.activities.SettingsActivity;
import com.viztushar.osumwalls.database.DBController;
import com.viztushar.osumwalls.fragments.HomeFragment;
import com.viztushar.osumwalls.others.Preferences;
import com.viztushar.osumwalls.others.Utils;

import static com.viztushar.osumwalls.others.Preferences.KEY_NAVBAR;
import static com.viztushar.osumwalls.others.Utils.PREF_COLORED_NAV;

public class MainActivity extends AppCompatActivity {

    public static final String PREF_DARK_THEME = "dark_theme";
    public boolean isTheme, isColorednav;
    SharedPreferences mPref;
    Preferences mpref;
    private NavigationView navigationView;
    private DrawerLayout mDrawerLayout;
    private Context context;
    boolean doubleBackToExitPressedOnce = false;
    DBController controller = new DBController(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mPref = PreferenceManager.getDefaultSharedPreferences(this);
        isTheme = mPref.getBoolean(PREF_DARK_THEME, false);
        Utils.openToWalls = mPref.getBoolean("open_to_walls", false);
        Utils.mTheme = isTheme;
        if (isTheme) {
            setTheme(R.style.AppTheme_Dark_NoActionBar);
        } else {
            setTheme(R.style.AppTheme);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;
        if (!BuildConfig.DEBUG)
            Log.i("License:", "Not Debug");
        // checkLicense();

        SharedPreferences preferences = context.getSharedPreferences("APP_PREFERENCES", 0);
        Utils.liveRun = preferences.getBoolean("live_run", false);
        Utils.useOffileMode = preferences.getBoolean("offline_mode", false);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.main_drawerlayout);
        navigationView = (NavigationView) findViewById(R.id.main_navigationview);
        isColorednav = mPref.getBoolean(PREF_COLORED_NAV, false);
        Utils.coloredNavBar = isColorednav;
        if (Utils.openToWalls) {
            navigationView.setCheckedItem(R.id.navigation_all);
            setWallsFrag();
        } else {
            setHomeFrag();
        }

        mSetNav();
    }

    public void mSetNav() {
        if (isColorednav) {
            if (Build.VERSION.SDK_INT >= 21) {
                getWindow().setNavigationBarColor(ContextCompat.getColor(context, R.color.primary_dark));
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
                    case R.id.navigation_fav:
                        item.setChecked(true);
                        startFavSection();
                        mDrawerLayout.closeDrawers();
                        break;
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

    public void startFavSection() {
        Intent intent = new Intent(this, FavoritesActivity.class);
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
            case R.id.action_sendemail:
                sendEmail();
                break;
            case R.id.action_changelog:

                AlertDialog.Builder alertDialogBuilder =
                        new AlertDialog.Builder(this)
                                .setTitle(R.string.app_name)
                                .setMessage(R.string.changelog_desc)
                                .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();

                                    }
                                });
                alertDialogBuilder.show();
                break;


        }
        return super.onOptionsItemSelected(item);
    }




    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }
        this.doubleBackToExitPressedOnce = true;
        Snackbar.make(findViewById(R.id.fragment_holder),getResources().getString(R.string.back),Snackbar.LENGTH_LONG).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }


    protected void sendEmail() {
        Log.i("Send email", "");

        String[] TO = {getResources().getString(R.string.email)};
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setData(Uri.parse(getResources().getString(R.string.mailto)));
        emailIntent.setType("text/plain");


        emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, getResources().getString(R.string.app_name));
        emailIntent.putExtra(Intent.EXTRA_TEXT, "Type the message you would like to send to the devs");

        try {
            startActivity(Intent.createChooser(emailIntent, "Send mail..."));
            //finish();
            //Log.i("Finished sending email", "");
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(MainActivity.this,
                    "There is no email client installed.", Toast.LENGTH_SHORT).show();
        }
    }

    private void checkLicense() {
        String installer = getPackageManager().getInstallerPackageName(getPackageName());
        Log.d("Installer", "Installer: " + installer);
        if (installer == null || !installer.equals("com.android.vending")) {
            AlertDialog.Builder alertDialogBuilder =
                    new AlertDialog.Builder(this)
                            .setTitle(R.string.license)
                            .setMessage(R.string.license_unsuccessful)
                            .setPositiveButton(R.string.download, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + getPackageName()));
                                    startActivity(browserIntent);
                                    dialog.dismiss();
                                    finish();
                                }
                            })
                            .setNegativeButton(R.string.exit, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    finish();
                                }
                            })
                            .setOnCancelListener(new DialogInterface.OnCancelListener() {
                                @Override
                                public void onCancel(DialogInterface dialog) {
                                    finish();
                                }
                            });
            alertDialogBuilder.show();
        }
    }

    private void setHomeFrag() {
        HomeFragment fragment0 = new HomeFragment("walls");
        switchFragment(fragment0, false);
        navigationView.setCheckedItem(R.id.navigation_all);
    }

    private void setWallsFrag() {
        HomeFragment fragment0 = new HomeFragment("new");
        switchFragment(fragment0, false);
        navigationView.setCheckedItem(R.id.navigation_all);
    }

    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
    }

}
