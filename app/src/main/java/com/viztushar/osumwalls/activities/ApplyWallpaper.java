package com.viztushar.osumwalls.activities;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.WallpaperManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.viztushar.osumwalls.R;
import com.viztushar.osumwalls.dialogs.ISDialogs;
import com.viztushar.osumwalls.items.WallpaperItem;
import com.viztushar.osumwalls.others.PermissionUtils;
import com.viztushar.osumwalls.others.Preferences;
import com.viztushar.osumwalls.others.Utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by Tushar on 15-05-2016.
 */
public class ApplyWallpaper extends AppCompatActivity implements View.OnClickListener {

    ImageView imageView;
    ProgressBar mProgress;
    private FloatingActionMenu menufab;
    private FloatingActionButton fab1;
    private FloatingActionButton fab2;
    private FloatingActionButton fab3;
    public String walls, saveWallLocation,wallname;
    Activity context;
    Preferences mPrefs;


    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.apply_wallpaper);
        context =this;
       mPrefs = new Preferences(this);
        saveWallLocation = Environment.getExternalStorageDirectory().getAbsolutePath()
                + context.getResources().getString(R.string.walls_save_location);
        imageView = (ImageView) findViewById(R.id.walls2);
        mProgress = (ProgressBar) findViewById(R.id.progress);
        mProgress.setVisibility(View.VISIBLE);
        menufab = (FloatingActionMenu) findViewById(R.id.menu_labels_right);
        fab1 = (FloatingActionButton) findViewById(R.id.quick_spply);
        fab1.setBackgroundColor(getResources().getColor(R.color.colorAccent));
        fab2 = (FloatingActionButton) findViewById(R.id.fav);
        fab2.setBackgroundColor(getResources().getColor(R.color.colorAccent));
        fab3 = (FloatingActionButton) findViewById(R.id.save);
        fab3.setBackgroundColor(getResources().getColor(R.color.colorAccent));
        fab1.setOnClickListener(this);
        fab2.setOnClickListener(this);
        fab3.setOnClickListener(this);

        Window w = getWindow();
        w.setFlags(
                WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION,
                WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        w.setFlags(
                WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        walls = getIntent().getStringExtra("walls");
        wallname = getIntent().getStringExtra("wallname");
        Log.d("tag", "onCreate() returned: " + walls);
        if (walls != null) {
            Glide.with(this)
                    .load(walls)
                    .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.ALL).centerCrop(this))
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            mProgress.setVisibility(View.GONE);
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            mProgress.setVisibility(View.GONE);
                            return false;
                        }
                    })
                    .thumbnail(0.5f)
                    .into(imageView);
        }


    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.quick_spply:
                final WallpaperManager manager = WallpaperManager.getInstance(this);
                Glide.with(this)
                        .asBitmap()
                        .load(walls)
                        .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.ALL))
                        .into(new SimpleTarget<Bitmap>() {
                            @Override
                            public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                                try {
                                    manager.setBitmap(resource);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                Snackbar.make(findViewById(R.id.menu_labels_right), "Wallpaper Set!!", Snackbar.LENGTH_SHORT).show();
                break;



            case R.id.save:
                if (!PermissionUtils.canAccessStorage(context)) {
                    PermissionUtils.setViewerActivityAction("save");
                    PermissionUtils.requestStoragePermission(context);
                } else {
                    showDialogs("save");
                }
                break;

            case R.id.fav:

                break;

        }

    }


    private void saveWallpaper(final Activity context, final String wallName,
                               final MaterialDialog downloadDialog, final Bitmap result) {
        downloadDialog.setContent(context.getString(R.string.walls_downloading));
        new Thread(new Runnable() {
            @Override
            public void run() {

                final File destFile = new File(saveWallLocation, wallName + ".png");
                destFile.getParentFile().mkdirs();
                destFile.delete();
                Log.i("location", "run: "+destFile);
                String snackbarText;
                if (!destFile.exists()) {
                    try {
                        result.compress(Bitmap.CompressFormat.PNG, 100,
                                new FileOutputStream(destFile));
                        snackbarText = context.getString(R.string.wallpaper_downloaded,
                                destFile.getAbsolutePath());
                    } catch (final Exception e) {
                        snackbarText = context.getString(R.string.error);
                    }
                } else {
                    snackbarText = context.getString(R.string.wallpaper_downloaded,
                            destFile.getAbsolutePath());
                }
                 final String finalSnackbarText = snackbarText;
                context.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        downloadDialog.dismiss();
                        Snackbar.make(findViewById(R.id.menu_labels_right), finalSnackbarText, Snackbar.LENGTH_SHORT).show();

                    }
                });

            }
        }).start();
    }

    private void saveWallpaperAction(final String name, String url) {
        final MaterialDialog downloadDialog = ISDialogs.showDownloadDialog(this);
        downloadDialog.show();
        Log.i("savewall", "saveWallpaperAction: "+ url);
        Glide.with(this)
                .asBitmap()
                .load(url)
                .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.ALL))
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                        if (resource != null) {
                            saveWallpaper(context, name, downloadDialog, resource);
                        }
                    }
                });

    }

    private void showDialogs(String action) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
                ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) !=
                        PackageManager.PERMISSION_GRANTED) {
            new MaterialDialog.Builder(context)
                    .title(R.string.md_error_label)
                    .content(context.getResources().getString(R.string.md_storage_perm_error,
                            context.getResources().getString(R.string.app_name)))
                    .positiveText(android.R.string.ok)
                    .show();
        } else {
            if (Utils.hasNetwork(context)) {
                switch (action) {
                    case "save":
                        saveWallpaperAction(wallname, walls);
                        break;


                }
            } else {
                Snackbar.make(findViewById(R.id.menu_labels_right), R.string.no_conn_title, Snackbar.LENGTH_SHORT).show();
            }
        }

    }



}
