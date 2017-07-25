package com.example.android.habittracker;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.android.habittracker.data.HabitContract.HabitEntry;
import com.example.android.habittracker.data.HabitDbHelper;

import static com.example.android.habittracker.R.menu.menu_catalog;

/**
 * Displays a list of habits that were entered and stored in the app.
 * The UI is not a required part of the Habit Tracker app project, but has been included as a way to
 * be able to easily see the database at work.
 */
public class CatalogActivity extends AppCompatActivity {

    /**
     * Database helper that will provide access to the database.
     */
    private HabitDbHelper dbHelper;
    private SQLiteDatabase db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalog);

        // Setup FAB to open EditorActivity
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CatalogActivity.this, EditorActivity.class);
                startActivity(intent);
            }
        });

        /*
         * To access the database, the subclass of SQLiteOpenHelper is instantiated and passed the
         * context, which is the current activity.
         */
        dbHelper = new HabitDbHelper(this);
    }

    @Override
    protected void onStart() {

        super.onStart();
        displayDatabaseInfo();

    }

    /**
     * Temporary helper method to disply information in the onscreen TextView about the state of the
     * database.
     */
    private void displayDatabaseInfo() {

        // Create and/or open a database to read from it
        db = dbHelper.getReadableDatabase();

        /*
         * Define a projection that specifies which columns from the database will actually be used
         * after this query.
         */
        String[] projection = new String[]{
                HabitEntry._ID,
                HabitEntry.COLUMN_HABIT,
                HabitEntry.COLUMN_EXTRA_INFO,
                HabitEntry.COLUMN_FREQUENCY,
                HabitEntry.COLUMN_TIME};

        Cursor cursor = readDataBase(projection);

        TextView displayView = (TextView) findViewById(R.id.text_view_habit);

        try {

            /*
             * Create a header in the Text View that looks like this:
             *
             * There are <cursor.getCount()> habits logged.
             * _id - habit - extra - frequency - time
             *
             * The while loop below iterates through the rows of the cursor and displays the
             * information form each column in this order
             */
            displayView.setText(getString(R.string.there_are)
                    + " "
                    + cursor.getCount()
                    + " "
                    + getString(R.string.habits_logged)
                    + "\n\n");

            displayView.append(HabitEntry._ID + " - "
                    + HabitEntry.COLUMN_HABIT + " - "
                    + HabitEntry.COLUMN_EXTRA_INFO + " - "
                    + HabitEntry.COLUMN_FREQUENCY + " - "
                    + HabitEntry.COLUMN_TIME + "\n");

            // Figure out the index of each column
            int idColumnIndex = cursor.getColumnIndex(HabitEntry._ID);
            int habitColumnIndex = cursor.getColumnIndex(HabitEntry.COLUMN_HABIT);
            int extraInfoColumnIndex = cursor.getColumnIndex(HabitEntry.COLUMN_EXTRA_INFO);
            int frequencyColumnIndex = cursor.getColumnIndex(HabitEntry.COLUMN_FREQUENCY);
            int timeColumnIndex = cursor.getColumnIndex(HabitEntry.COLUMN_TIME);

            // Iterate through all the returned rows in the cursor
            while (cursor.moveToNext()) {

                /*
                 * Use the index to extract the String or int value of the word at the current row
                 * the cursor is on.
                 */
                int currentID = cursor.getInt(idColumnIndex);
                String currentHabit = cursor.getString(habitColumnIndex);
                String currentInfo = cursor.getString(extraInfoColumnIndex);
                int currentFrequency = cursor.getInt(frequencyColumnIndex);
                int currentTime = cursor.getInt(timeColumnIndex);

                /*
                 * Display the values from each column of the current row in the cursor in the
                 * TextView
                 */
                displayView.append(("\n"
                        + currentID + " - "
                        + currentHabit + " - "
                        + currentInfo + " - "
                        + currentFrequency + " - "
                        + currentTime));
            }

        } finally {

            /*
             * Close the cursor to release all of its resources and to make it invalid.
             */
            cursor.close();

        }

    }

    private Cursor readDataBase(String[] projection) {

        // query for cursor
        // Perform a query on the habits table

        return db.query(
                HabitEntry.TABLE_NAME,    // The table to query
                projection,               // The columns to return
                null,                     // The columns for the WHERE clause
                null,                     // The values for the WHERE clause
                null,                     // Don't group the rows
                null,                     // Don't filter by row groups
                null);
    }

    // Helper method to insert hardcoded habit data into the database. For debugging purposes only.
    private void insertHabit() {

        // Gets the database in write mode
        db = dbHelper.getWritableDatabase();

        /*
         * Create a ContentValues object where column names are the keys, and studying habit's
         * attributes are the values.
         */
        ContentValues values = new ContentValues();
        values.put(HabitEntry.COLUMN_HABIT, getString(R.string.study_udacity));
        values.put(HabitEntry.COLUMN_EXTRA_INFO, getString(R.string.work_on_abnd_course));
        values.put(HabitEntry.COLUMN_FREQUENCY, HabitEntry.DAILY);
        values.put(HabitEntry.COLUMN_TIME, 120);

        /*
         * Insert a new row for the habit in the database, returning the ID of that new row.
         * The first argument for db.insert() is the habits table name.
         * The second argument provides the name of a column in which the framework can insert NULL
         * in the event that the ContentValues is empty (if this is set to "null", then the
         * framework will not insert a row when there are no values.
         * The third argument is the ContentValues object containing the info for the habit.
         */
        long newRowId = db.insert(HabitEntry.TABLE_NAME, null, values);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        /*
         * Inflate the menu options from the res/menu/menu_catalog.xml file to add the menu to
         * the app bar.
         */
        getMenuInflater().inflate(menu_catalog, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Triggered when the user clicks on a menu option in the app bar overflow menu
        switch (item.getItemId()) {

            // Respond to a click on the "Insert Dummy data" menu option
            case R.id.action_insert_dummy_data:
                insertHabit();
                displayDatabaseInfo();
                return true;

        }

        return super.onOptionsItemSelected(item);
    }

}
