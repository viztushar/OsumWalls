package com.viztushar.osumwalls.activities;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
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
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.bumptech.glide.request.target.SimpleTarget;
import com.mikepenz.materialize.MaterializeBuilder;
import com.viztushar.osumwalls.R;
import com.viztushar.osumwalls.database.DBController;
import com.viztushar.osumwalls.dialogs.ISDialogs;
import com.viztushar.osumwalls.items.WallpaperItem;
import com.viztushar.osumwalls.others.Preferences;
import com.viztushar.osumwalls.others.Utils;
import com.viztushar.osumwalls.utils.StaticUtils;

import java.io.File;
import java.io.FileOutputStream;

/**
 * Created by Tushar on 15-05-2016.
 */
public class ApplyWallpaper extends AppCompatActivity {

    private static final String TAG = ApplyWallpaper.class.getSimpleName();
    public static final String EXTRA_WALLPAPER = "wallpaperItem";
    public String FAV_TAG = "false";
    public String saveWallLocation, favSaveLocation;
    public Window w = getWindow();
    ImageView imageView, favicon;
    ProgressBar mProgress;
    Activity context;
    Preferences mPrefs;
    TextView mTextWall;
    LinearLayout wallbg;
    LinearLayout btnSave;
    LinearLayout btnApply;
    LinearLayout btnfav;
    final String[] PERMISSIONS = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
    boolean permission;
    private FloatingActionButton fab1;
    boolean restoredText = false;
    boolean fav;
    Preferences prefs;
    public boolean isTheme;
    SharedPreferences mPref;
    private File output, current_saved;
    WallpaperItem wallpaperItem;
    private static final String PREF_DARK_THEME = "dark_theme";
    DBController controller = new DBController(this);


    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
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
        setContentView(R.layout.activity_wallpaper);
        context = this;
        prefs = new Preferences(getApplicationContext());
        mPrefs = new Preferences(this);
        saveWallLocation = Environment.getExternalStorageDirectory().getAbsolutePath()
                + context.getResources().getString(R.string.walls_save_location);
        imageView = (ImageView) findViewById(R.id.walls2);
        mProgress = (ProgressBar) findViewById(R.id.progress);
        mProgress.setVisibility(View.VISIBLE);
        mTextWall = (TextView) findViewById(R.id.wallname);
        wallbg = (LinearLayout) findViewById(R.id.wallbg);
        fab1 = (FloatingActionButton) findViewById(R.id.fav_fab);
        favicon = (ImageView) findViewById(R.id.favicon);

        wallpaperItem = getIntent().getParcelableExtra(EXTRA_WALLPAPER);

        Log.d("tag", "onCreate() returned: " + wallpaperItem.getUrl());
        mTextWall.setText(wallpaperItem.getName());
        getPermissions();

        fab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }


        });

        try {
            output = new File(Environment.getExternalStorageDirectory() + context.getResources().getString(R.string.walls_save_location));
        } catch (Exception e) {
            output = new File(getFilesDir().toString() + context.getResources().getString(R.string.walls_save_location));
        }

        current_saved = new File(output.toString() + "/" + wallpaperItem.getName() + ".png");

        btnSave = (LinearLayout) findViewById(R.id.download);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogs("save");
            }
        });

        btnApply = (LinearLayout) findViewById(R.id.apply);
        btnApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogs("apply");
            }
        });

        btnfav = (LinearLayout) findViewById(R.id.fav);
        btnfav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkFav();
                wallpaperItem.setFavorite(ApplyWallpaper.this, !wallpaperItem.isFavorite());
            }
        });

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        fav = preferences.getBoolean(wallpaperItem.getUrl(), false);

        if(fav){
            favicon.setImageDrawable(getResources().getDrawable(R.drawable.ic_addtofav));
        } else {
            favicon.setImageDrawable(getResources().getDrawable(R.drawable.ic_removefromfav));
        }

        final Window w = getWindow();
        w.setFlags(
                WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        Glide.with(this)
                .load(wallpaperItem.getUrl())
                .thumbnail(0.2f)
                .into(new GlideDrawableImageViewTarget(imageView) {
                    @Override
                    public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> animation) {
                        super.onResourceReady(resource, animation);
                        mProgress.setVisibility(View.GONE);
                        Palette.from(StaticUtils.drawableToBitmap(resource)).generate(new Palette.PaletteAsyncListener() {
                            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
                            @Override
                            public void onGenerated(Palette palette) {
                                setColors(context, palette);
                                if (Build.VERSION.SDK_INT >= 21) {
                                    w.setNavigationBarColor(palette.getLightVibrantColor(Color.DKGRAY));
                                }
                            }
                        });
                    }
                });

        new MaterializeBuilder().withActivity(this).withFullscreen(false).withTransparentStatusBar(true).withTintedStatusBar(false).build();
    }


    @TargetApi(Build.VERSION_CODES.M)
    public void setColors(Context colors, Palette palette) {
        fab1.setBackgroundTintList(ColorStateList.valueOf(palette.getDarkVibrantColor(Color.DKGRAY)));
        wallbg.setBackgroundColor(palette.getLightVibrantColor(Color.DKGRAY));

    }

    private void checkFav() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        restoredText = preferences.getBoolean(wallpaperItem.getUrl(), false);
        Log.d(TAG, "checkFav: " + restoredText);
        if (restoredText) {
            favicon.setImageDrawable(getResources().getDrawable(R.drawable.ic_removefromfav));
            controller = new DBController(getApplicationContext());
            SQLiteDatabase db = controller.getWritableDatabase();
            db.delete("favourite", "wallname = ?", new String[]{wallpaperItem.getName()});
            Snackbar.make(findViewById(R.id.wallbg), getResources().getString(R.string.removed_favorites), Snackbar.LENGTH_SHORT).show();
        } else {
            favicon.setImageDrawable(getResources().getDrawable(R.drawable.ic_addtofav));
            controller = new DBController(getApplicationContext());
            SQLiteDatabase db = controller.getWritableDatabase();
            ContentValues cv = new ContentValues();
            cv.put("wallname", wallpaperItem.getName());
            cv.put("wallauthor", wallpaperItem.getAuthor());
            cv.put("wallurl", wallpaperItem.getUrl());
            cv.put("wallthumb", wallpaperItem.getThumb());
            db.insert("favourite", null, cv);
            Log.d(TAG, "onClick: " + FAV_TAG + "\n" + wallpaperItem.getThumb() + "\n cyz" + db.insert("favourite", null, cv));
            db.close();
            Snackbar.make(findViewById(R.id.wallbg), getResources().getString(R.string.added_favorites), Snackbar.LENGTH_SHORT).show();
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
                        // Snackbar.make(findViewById(R.id.wallbg), finalSnackbarText, Snackbar.LENGTH_SHORT).show();
                        Toast.makeText(getApplicationContext(), finalSnackbarText, Toast.LENGTH_LONG).show();

                        //Refresh the gallery so the image appears!
                        MediaScannerConnection.scanFile(getApplicationContext(), new String[]{destFile.getPath()}, new String[]{"image/jpeg"}, null);
                    }
                });

            }
        }).start();
    }

    private void saveWallpaperAction() {
        final MaterialDialog downloadDialog = ISDialogs.showDownloadDialog(this);
        downloadDialog.show();
        Log.i("savewall", "saveWallpaperAction: " + wallpaperItem.getUrl());
        Glide.with(this)
                .load(wallpaperItem.getUrl())
                .asBitmap()
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        if (resource != null) {
                            saveWallpaper(context, wallpaperItem.getName(), downloadDialog, resource);
                        }
                    }
                });

    }

    private Uri getImageContentUri(Context context, File imageFile) {
        String filePath = imageFile.getAbsolutePath();
        Cursor cursor = context.getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                new String[]{MediaStore.Images.Media._ID},
                MediaStore.Images.Media.DATA + "=? ",
                new String[]{filePath}, null);
        if (cursor != null && cursor.moveToFirst()) {
            int id = cursor.getInt(cursor.getColumnIndex(MediaStore.MediaColumns._ID));
            return Uri.withAppendedPath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "" + id);
        } else {
            if (imageFile.exists()) {
                ContentValues values = new ContentValues();
                values.put(MediaStore.Images.Media.DATA, filePath);
                return context.getContentResolver().insert(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            } else {
                return Uri.parse("");
            }
        }
    }


    private void applyWallpaper(final Activity context, final String wallName,
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
                        // Snackbar.make(findViewById(R.id.wallbg), finalSnackbarText, Snackbar.LENGTH_SHORT).show();
                        //  Toast.makeText(getApplicationContext(), finalSnackbarText, Toast.LENGTH_LONG).show();

                        //Refresh the gallery so the image appears!
                        MediaScannerConnection.scanFile(getApplicationContext(), new String[]{destFile.getPath()}, new String[]{"image/jpeg"}, null);
                        Intent setWall = new Intent(Intent.ACTION_ATTACH_DATA);
                        setWall.setDataAndType(getImageContentUri(getApplicationContext(), destFile), "image/*");
                        setWall.putExtra("png", "image/*");
                        startActivityForResult(Intent.createChooser(setWall, "Set using..."), 1);

                    }
                });

            }
        }).start();
    }

    private void applyWallpaperAction() {
        final MaterialDialog downloadDialog = ISDialogs.showDownloadDialog(this);
        downloadDialog.show();
        Log.i("savewall", "saveWallpaperAction: " + wallpaperItem.getUrl());
        Glide.with(this)
                .load(wallpaperItem.getUrl())
                .asBitmap()
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        if (resource != null) {
                            applyWallpaper(context, wallpaperItem.getUrl(), downloadDialog, resource);
                        }
                    }
                });

    }


    private void getPermissions() {
        int perm = ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        this.permission = perm == PackageManager.PERMISSION_GRANTED;

        if (perm != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    this,
                    PERMISSIONS,
                    1
            );
        }
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
                        saveWallpaperAction();
                        break;
                    case "apply":
                        applyWallpaperAction();
                        break;

                }
            } else {
                Toast.makeText(getApplicationContext(), R.string.no_conn_title, Toast.LENGTH_LONG).show();
                //Snackbar.make(findViewById(R.id.wallbg), R.string.no_conn_title, Snackbar.LENGTH_SHORT).show();
            }
        }

    }

}


