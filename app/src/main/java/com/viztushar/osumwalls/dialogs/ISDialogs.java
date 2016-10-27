package com.viztushar.osumwalls.dialogs;

import android.content.Context;

import com.afollestad.materialdialogs.MaterialDialog;

import java.util.concurrent.Callable;

public final class ISDialogs {

    public static MaterialDialog showDownloadDialog(Context context) {
        return new MaterialDialog.Builder(context)
                .content("downloading_wallpaper")
                .progress(true, 0)
                .cancelable(false)
                .build();
    }

}