package com.viztushar.osumwalls.others;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class Utils {

    public static final String PREF_COLORED_NAV = "colored_nav";
    public static boolean mTheme = false;
    public static boolean coloredNavBar = false;
    public static boolean darkTheme = false;
    public static boolean noConnection = false;
    public static boolean newWallsShown = false;
    public static boolean openToWalls = false;
    public static boolean liveRun = false;
    public static int gridNo = 2;
    public static String currentWallFrag;
    public static boolean useOffileMode = false;
    public static boolean hasNetwork(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }


}
