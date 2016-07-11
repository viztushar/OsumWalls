package com.viztushar.osumwalls.activities;


import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.SwitchPreference;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.ViewGroup;

import com.mikepenz.aboutlibraries.Libs;
import com.mikepenz.aboutlibraries.LibsBuilder;
import com.viztushar.osumwalls.MainActivity;
import com.viztushar.osumwalls.R;
import com.viztushar.osumwalls.others.Utils;

public class SettingsActivity extends AppCompatPreferenceActivity {

    MainActivity mainActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings);
        getLayoutInflater().inflate(R.layout.activity_settings, (ViewGroup) findViewById(android.R.id.content));
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mainActivity = new MainActivity();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));

        Preference joincommunity = (Preference) findPreference("join_community");
        joincommunity.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                Intent joinweb = new Intent(Intent.ACTION_VIEW, Uri.parse(getResources().getString(R.string.join_com_link)));
                startActivity(joinweb);
                return false;
            }
        });

        Preference Wall = (Preference) findPreference("developer_one");
        Wall.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                Intent Wall = new Intent(Intent.ACTION_VIEW, Uri.parse(getResources().getString(R.string.dev_one_link)));
                startActivity(Wall);
                return false;
            }
        });

        Preference Wall1 = (Preference) findPreference("developer_two");
        Wall1.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                Intent Wall1 = new Intent(Intent.ACTION_VIEW, Uri.parse(getResources().getString(R.string.dev_two_link)));
                startActivity(Wall1);
                return false;
            }
        });

        Preference licenses = (Preference) findPreference("licenses_libs");
        licenses.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                lib();
                return false;
            }
        });


        final CheckBoxPreference mCheckBoxPreference = (CheckBoxPreference) findPreference("dark_theme");
            mCheckBoxPreference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
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
                return false;
            }
        });

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
