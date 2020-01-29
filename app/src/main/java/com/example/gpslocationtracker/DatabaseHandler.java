package com.example.gpslocationtracker;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "Location";
    private static final String LOCATIONS = "locations";
    private static final String KEY_ID = "id";
    private static final String KEY_LATITUDE = "latitude";
    private static final String KEY_LONGITUDE = "longitude";
    private static final String KEY_AREA = "area";


    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_LOCATION_TABLE = "CREATE TABLE " + LOCATIONS + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_LATITUDE + " TEXT,"
                + KEY_LONGITUDE+ " TEXT," + KEY_AREA + " TEXT" + ")";
        db.execSQL(CREATE_LOCATION_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + LOCATIONS);
        // Create tables again
        onCreate(db);
    }

    void addLocation(Whereabouts whereabouts) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_LATITUDE, whereabouts.getLatitude()); // Contact Name
        values.put(KEY_LONGITUDE, whereabouts.getLongitude());
        values.put(KEY_AREA, whereabouts.getArea());// Contact Phone

        // Inserting Row
        db.insert(LOCATIONS, null, values);
        Log.d("inserted location : " , "new location added");
        db.close(); // Closing database connection
    }

    public List<Whereabouts> getAllLocations() {
        List<Whereabouts> locationList = new ArrayList<Whereabouts>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + LOCATIONS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Whereabouts whereabouts = new Whereabouts();
                whereabouts.setId(Integer.parseInt(cursor.getString(0)));
                whereabouts.setLatitude(cursor.getString(1));
                whereabouts.setLongitude(cursor.getString(2));
                whereabouts.setArea(cursor.getString(3));
                // Adding contact to list
                locationList.add(whereabouts);
            } while (cursor.moveToNext());
        }

        // return contact list
        return locationList;
    }
}
