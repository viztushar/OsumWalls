package com.viztushar.osumwalls.database;

import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Tushar on 24-10-2016.
 */

public class DBController extends SQLiteOpenHelper {

    private static final String tablename = "favourite";  // tablename
    private static final String fav = "fav";  // column name
    private static final String id = "ID";  // auto generated ID column
    //wall data
    public static final String wallname = "wallname"; // column name
    public static final String wallauthor = "wallauthor"; // column name
    public static final String wallurl = "wallurl"; // column name
    public static final String wallthumb = "wallthumb"; // column name

    private static final String databasename = "addtofav"; // Dtabasename
    private static final int versioncode = 1; //versioncode of the database

    String query;

    public DBController(Context context) {
        super(context, databasename, null, versioncode);
    }


    @Override
    public void onCreate(SQLiteDatabase database) {
        query = "CREATE TABLE IF NOT EXISTS " + tablename + "(" + id + " integer, " + wallname + " text primary key," + wallauthor +
                " text," + wallurl + " text," + wallthumb + " text)";
        database.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int i, int i1) {
        query = "DROP TABLE IF EXISTS " + tablename;
        database.execSQL(query);
        onCreate(database);
    }

    public ArrayList<HashMap<String, String>> getAllPlace() {
        ArrayList<HashMap<String, String>> favwall;
        favwall = new ArrayList<HashMap<String, String>>();
        String selectQuery = "SELECT  * FROM " + tablename;
        SQLiteDatabase database = this.getReadableDatabase();
        Cursor cursor = database.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("id", cursor.getString(0));
                map.put("wallname", cursor.getString(1));
                map.put("wallauthor", cursor.getString(2));
                map.put("wallurl", cursor.getString(3));
                map.put("wallthumb", cursor.getString(3));
                favwall.add(map);

            } while (cursor.moveToNext());

        }

        return favwall;
    }

    public Cursor getData() {
        String selectQuery = "SELECT  * FROM " + tablename;
        SQLiteDatabase database  = getReadableDatabase();
        String[] columns = {id,wallname,wallauthor,wallurl,wallauthor};
        Cursor c = database.rawQuery(selectQuery, null);
        return c;
    }
}
