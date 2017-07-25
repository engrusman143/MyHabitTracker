package com.example.android.habittracker.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.android.habittracker.data.HabitContract.HabitEntry;

/**
 * HabitTracker Created by Muir on 21/06/2017.
 * Database helper for the app. Manages database creation and version management.
 */

public class HabitDbHelper extends SQLiteOpenHelper {

    public static final String LOG_TAG = HabitDbHelper.class.getSimpleName();

    /**
     * Name of the database file
     */
    private static final String DATABASE_NAME = "tracker.db";

    /**
     * Database version. If you change the database schema, you must increment the database version.
     */
    private static final int DATABASE_VERSION = 1;

    /**
     * Constructs a new instance of {@link HabitDbHelper}.
     *
     * @param context of the app
     */
    public HabitDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Create a String that contains the SQL statement to create the habits table
    private static final String SQL_CREATE_HABITS_TABLE = "CREATE TABLE "
            + HabitEntry.TABLE_NAME + " ("
            + HabitEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + HabitEntry.COLUMN_HABIT + " TEXT NOT NULL, "
            + HabitEntry.COLUMN_EXTRA_INFO + " TEXT, "
            + HabitEntry.COLUMN_FREQUENCY + " INTEGER NOT NULL, "
            + HabitEntry.COLUMN_TIME + " INTEGER NOT NULL DEFAULT 1);";

    /**
     * This is called when the database is created for the first time.
     */
    @Override
    public void onCreate(SQLiteDatabase db) {

        // Execute the SQL statement
        db.execSQL(SQL_CREATE_HABITS_TABLE);
    }

    /**
     * onUpgrade() is called when the database needs to be upgraded.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        /*
         * Check the versions and update where applicable. For now, this just returns the current
         * database table, which is better than having just an empty function. In future, this code
         * can be changed to handle any changes to the database that we need to make.
         */

        if (oldVersion < 2 || oldVersion < 3) {
            db.execSQL(SQL_CREATE_HABITS_TABLE);
        }

    }

}
