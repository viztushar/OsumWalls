package com.viztushar.osumwalls.activities;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

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
import com.viztushar.osumwalls.R;
import com.viztushar.osumwalls.dialogs.ISDialogs;
import com.viztushar.osumwalls.items.WallpaperItem;
import com.viztushar.osumwalls.others.Preferences;
import com.viztushar.osumwalls.others.Utils;

import java.io.File;
import java.io.FileOutputStream;


/**
 * Created by Tushar on 15-05-2016.
 */
public class ApplyWallpaper extends AppCompatActivity implements View.OnClickListener {

    ImageView imageView;
    ProgressBar mProgress;
    private FloatingActionButton fab1;
    public String walls, saveWallLocation, wallname;
    Activity context;
    Preferences mPrefs;
    TextView mTextWall;
    LinearLayout wallbg;
    WallpaperItem wallpaperItem;


    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallpaper);
        context = this;
        mPrefs = new Preferences(this);
        saveWallLocation = Environment.getExternalStorageDirectory().getAbsolutePath()
                + context.getResources().getString(R.string.walls_save_location);
        imageView = (ImageView) findViewById(R.id.walls2);
        mProgress = (ProgressBar) findViewById(R.id.progress);
        mProgress.setVisibility(View.VISIBLE);
        mTextWall = (TextView) findViewById(R.id.wallname);
        wallbg = (LinearLayout) findViewById(R.id.wallbg);
        fab1 = (FloatingActionButton) findViewById(R.id.fav_fab);
//        if(wallpaperItem.favorite) fab1.setImageResource(R.drawable.ic_favorite_border);
        fab1.setOnClickListener(this);

        Window w = getWindow();
        w.setFlags(
                WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        walls = getIntent().getStringExtra("walls");
        wallname = getIntent().getStringExtra("wallname");
        Log.d("tag", "onCreate() returned: " + walls);
        mTextWall.setText(wallname);

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

        Glide.with(this)
                .asBitmap()
                .load(walls)
                .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.ALL))
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                        if (resource != null) {
                            Palette.from(resource).generate(new Palette.PaletteAsyncListener() {
                                @TargetApi(Build.VERSION_CODES.LOLLIPOP)
                                @Override
                                public void onGenerated(Palette palette) {
                                    setColors(context, palette);
                                }
                            });
                        }
                    }
                });


    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void setColors(Context colors, Palette palette) {
        // getWindow().setStatusBarColor(ContextCompat.getColor(colors, palette.getLightMutedColor(Color.DKGRAY)));
        fab1.setBackgroundTintList(ColorStateList.valueOf(palette.getDarkVibrantColor(Color.DKGRAY)));
        wallbg.setBackgroundColor(palette.getLightVibrantColor(Color.DKGRAY));

    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    public void onClick(View view) {
//        fab1.setImageResource(wallpaperItem.favorite ? R.drawable.ic_favorite : R.drawable.ic_favorite_border);
        wallpaperItem.setFav(ApplyWallpaper.this, !wallpaperItem.favorite);
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
                Log.i("location", "run: " + destFile);
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
        Log.i("savewall", "saveWallpaperAction: " + url);
        Glide.with(this)
                .asBitmap()
                .load(url)
                .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.ALL))
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                        if (resource != null) {
                            Palette.from(resource).generate(new Palette.PaletteAsyncListener() {
                                @TargetApi(Build.VERSION_CODES.LOLLIPOP)
                                @Override
                                public void onGenerated(Palette palette) {
                                    fab1.setBackgroundTintList(ColorStateList.valueOf(palette.getDarkMutedColor(Color.DKGRAY)));
                                    wallbg.setBackgroundColor(palette.getLightMutedColor(Color.DKGRAY));
                                    if (Build.VERSION.SDK_INT >= 21)
                                        context.getWindow().setStatusBarColor(ContextCompat.getColor(context, palette.getLightMutedColor(Color.DKGRAY)));
                                }
                            });
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
