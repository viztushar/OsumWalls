package com.viztushar.osumwalls.activities;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.preference.SwitchPreference;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.ViewGroup;
import com.mikepenz.aboutlibraries.Libs;
import com.mikepenz.aboutlibraries.LibsBuilder;
import com.viztushar.osumwalls.MainActivity;
import com.viztushar.osumwalls.R;
import com.viztushar.osumwalls.others.Utils;


public class SettingsActivity extends AppCompatPreferenceActivity {

    private static final String PREFS_NAME = "APP_PREFERENCES";
    private static final String PREF_DARK_THEME = "dark_theme";
    private static final String PREF_OPEN_MODE = "open_to_walls";
    SharedPreferences mPref;
    public boolean isTheme,isColorednav;
    MainActivity mainActivity;
    private PendingIntent pendingIntent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mPref = PreferenceManager.getDefaultSharedPreferences(this);
        isTheme = mPref.getBoolean(PREF_DARK_THEME,false);
        Utils.mTheme = isTheme;
        if(isTheme) {setTheme(R.style.AppTheme_Dark_NoActionBar);}
        else {setTheme(R.style.AppTheme);}
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings);
        getLayoutInflater().inflate(R.layout.activity_settings, (ViewGroup) findViewById(android.R.id.content));
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mainActivity = new MainActivity();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));

        Preference rateapp = findPreference("rate_app");
        rateapp.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + "com.viztushar.osumwalls")));
                } catch (android.content.ActivityNotFoundException anfe) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + "com.viztushar.osumwalls")));
                }
                return false;
            }
        });

        Preference donateapp = findPreference("donate_app");
        donateapp.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://plus.google.com/collection/wk1jNB")));
                } catch (android.content.ActivityNotFoundException anfe) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://plus.google.com/collection/wk1jNB")));
                }
                return false;
            }
        });

        Preference pub = findPreference("pub_one");
        pub.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                Intent Wall = new Intent(Intent.ACTION_VIEW, Uri.parse(getResources().getString(R.string.pub_link)));
                startActivity(Wall);
                return false;
            }
        });

        Preference Wall = findPreference("developer_one");
        Wall.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                Intent Wall = new Intent(Intent.ACTION_VIEW, Uri.parse(getResources().getString(R.string.dev_one_link)));
                startActivity(Wall);
                return false;
            }
        });

        Preference Wall1 = findPreference("developer_two");
        Wall1.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                Intent Wall1 = new Intent(Intent.ACTION_VIEW, Uri.parse(getResources().getString(R.string.dev_two_link)));
                startActivity(Wall1);
                return false;
            }
        });

       /* Preference licenses = (Preference) findPreference("licenses_libs");
        licenses.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                lib();
                return false;
            }
        });*/
        Preference version = findPreference("version");
        version.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                startAboutSection();
                return false;
            }
        });

        final CheckBoxPreference mCheckBoxPreference = (CheckBoxPreference) findPreference("dark_theme");
            mCheckBoxPreference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    if(isTheme)
                    {
                        toggleTheme(false);
                    }
                    else
                    {
                        toggleTheme(true);
                    }

                    return false;
                }
            });

        SwitchPreference switchPreference = (SwitchPreference)findPreference("colored_nav");
        switchPreference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                if (mainActivity.isColorednav)
                {
                    if (Build.VERSION.SDK_INT >= 21){
                        getWindow().setNavigationBarColor(ContextCompat.getColor(getApplicationContext(),R.color.primary_dark));
                    }
                }
                restartApp();
                return false;
            }
        });

        SwitchPreference switchPreference2 = (SwitchPreference)findPreference("open_to_walls");
        switchPreference2.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                if(Utils.openToWalls)
                {
                    SharedPreferences.Editor editor = getSharedPreferences(PREFS_NAME, MODE_PRIVATE).edit();
                    editor.putBoolean(PREF_OPEN_MODE, false);
                    editor.apply();
                }
                else
                {
                    SharedPreferences.Editor editor = getSharedPreferences(PREFS_NAME, MODE_PRIVATE).edit();
                    editor.putBoolean(PREF_OPEN_MODE, false);
                    editor.apply();
                }

                return false;
            }
        });
       /* final CheckBoxPreference mCheckBoxPreference2 = (CheckBoxPreference) findPreference("offline_mode");
        mCheckBoxPreference2.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                if(Utils.useOffileMode)
                {
                    android.content.SharedPreferences.Editor editor = getSharedPreferences(PREFS_NAME, MODE_PRIVATE).edit();
                    editor.putBoolean("offline_mode", false);
                    editor.apply();
                    //Check all the walls and see if they need downloading
                }
                else
                {
                    android.content.SharedPreferences.Editor editor = getSharedPreferences(PREFS_NAME, MODE_PRIVATE).edit();
                    editor.putBoolean("offline_mode", true);
                    editor.apply();
                    //Check all the walls and see if they need downloading
                }

                return false;
            }
        });
        SwitchPreference switchPreference3 = (SwitchPreference)findPreference("live_run");
        switchPreference3.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                if(Utils.liveRun)
                {
                    android.content.SharedPreferences.Editor editor = getSharedPreferences(PREFS_NAME, MODE_PRIVATE).edit();
                    editor.putBoolean("live_run", false);
                    editor.apply();
                    Utils.liveRun = false;
                    mainActivity.cancelAlarm();
                }
                else
                {
                    android.content.SharedPreferences.Editor editor = getSharedPreferences(PREFS_NAME, MODE_PRIVATE).edit();
                    editor.putBoolean("live_run", true);
                    editor.apply();
                    Utils.liveRun = true;
                    mainActivity.startAlarm();
                }

                return false;
            }
        });*/


    }

    public void startAboutSection(){
        Intent intent = new Intent(this, AboutAppActivity.class);
        startActivity(intent);
    }
    private void toggleTheme(boolean darkTheme) {
        SharedPreferences.Editor editor = getSharedPreferences(PREFS_NAME, MODE_PRIVATE).edit();
        editor.putBoolean(PREF_DARK_THEME, darkTheme);
        editor.apply();
        restartApp();
    }
    private void restartApp()
    {
        Intent i = getBaseContext().getPackageManager()
                .getLaunchIntentForPackage( getBaseContext().getPackageName() );
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
    }

    public void lib() {

        new LibsBuilder()
                .withAutoDetect(true)
                .withLicenseShown(true)
                .withActivityTitle("Libraries")
                .withAboutIconShown(true)
                .withAboutVersionShown(true)
                .withActivityStyle(Libs.ActivityStyle.LIGHT_DARK_TOOLBAR)
                .start(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
        }
        return super.onOptionsItemSelected(item);
    }

}
