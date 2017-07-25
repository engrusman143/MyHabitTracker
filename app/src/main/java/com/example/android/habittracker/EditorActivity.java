package com.example.android.habittracker;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.android.habittracker.data.HabitContract.HabitEntry;
import com.example.android.habittracker.data.HabitDbHelper;

/** Created by Usman on 24/07/2017
 * EditorActivity allows the user to log a new habit or edit an existing one
 */

public class EditorActivity extends AppCompatActivity {

    // EditText field to enter the habit
    private EditText habitEditText;

    // EditText field to enter any extra info the user might want to store about the habit
    private EditText infoEditText;

    // EditText field to enter the habit's frequency
    private Spinner frequencySpinner;

    /**
     * Frequency of the habit. The possible valid values are in the HabitContract.java file@
     * {@link HabitEntry#DAILY}, {@link HabitEntry#EVERY_OTHER_DAY}, {@link HabitEntry#WEEKLY},
     * {@link HabitEntry#TWICE_MONTHLY}, or {@link HabitEntry#MONTHLY}
     */
    private int frequency = HabitEntry.DAILY;

    // EditText field to enter the length of time it takes to complete the habit
    private EditText timeEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        // Find all relevant views that we will need to read user input from
        habitEditText = (EditText) findViewById(R.id.edit_habit);
        infoEditText = (EditText) findViewById(R.id.edit_info);
        frequencySpinner = (Spinner) findViewById(R.id.spinner_frequency);
        timeEditText = (EditText) findViewById(R.id.edit_time);

        setupSpinner();

    }

    // Setup the dropdown spinner that allows the user to select the frequency of the habit
    private void setupSpinner() {

        /*
         * Create adapter for the spinner. The list options are from the String array it will use.
         * The spinner will use the default layout.
         */
        ArrayAdapter frequencySpinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.array_frequency_options, android.R.layout.simple_spinner_item);

        // Specify dropdown layout style - simple list view with 1 item per line
        frequencySpinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);

        // apply the adapter to the spinner
        frequencySpinner.setAdapter(frequencySpinnerAdapter);

        // Set the integer "selected" to the constant values
        frequencySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                String selection = (String) parent.getItemAtPosition(position);
                if (!TextUtils.isEmpty(selection)) {
                    if (selection.equals(getString(R.string.every_other_day))) {
                        frequency = HabitEntry.EVERY_OTHER_DAY;
                    } else if (selection.equals(getString(R.string.weekly))) {
                        frequency = HabitEntry.WEEKLY;
                    } else if (selection.equals(getString(R.string.twice_monthly))) {
                        frequency = HabitEntry.TWICE_MONTHLY;
                    } else if (selection.equals(getString(R.string.monthly))) {
                        frequency = HabitEntry.MONTHLY;
                    } else if (selection.equals(getString(R.string.daily))) {
                        frequency = HabitEntry.DAILY;
                    }
                }
            }

            // Because AdapterView is an abstract class, onNothingSelected must be defined
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

                frequency = HabitEntry.DAILY;

            }

        });

    }

    // Get user input from editor and save new habit into database.
    private void insertHabit() {

        /*
         * Read from input fields, using trim to eliminate leading or trailing white space
         */
        String habitString = habitEditText.getText().toString().trim();
        String infoString = infoEditText.getText().toString().trim();
        String timeString = timeEditText.getText().toString().trim();
        int time = Integer.parseInt(timeString);

        // Create database helper
        HabitDbHelper dbHelper = new HabitDbHelper(this);

        // Get the database in write mode
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        /*
         * Create a ContentValues object where column names are the keys, and habit attributes from
         * the editor are the values.
         */
        ContentValues values = new ContentValues();
        values.put(HabitEntry.COLUMN_HABIT, habitString);
        values.put(HabitEntry.COLUMN_EXTRA_INFO, infoString);
        values.put(HabitEntry.COLUMN_FREQUENCY, frequency);
        values.put(HabitEntry.COLUMN_TIME, time);

        // Insert a new row for the habit in the database, returning the ID of that new row.
        long newRowId = db.insert(HabitEntry.TABLE_NAME, null, values);

        // Show a toast message depending on whether or not the insertion was successful.
        if (newRowId == -1) {

            // If the row ID is -1, then there was an error with the insertion.
            Toast.makeText(this, R.string.saving_error, Toast.LENGTH_SHORT).show();

        } else {

            // Otherwise, the insertion was successful. Display a toast with the row ID.
            Toast.makeText(this, getString(R.string.save_successful)
                    + newRowId, Toast.LENGTH_SHORT).show();

        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        /*
         * Inflate the menu options from the res/menu/menu_editor.xml file to add menu items to the
         * app bar.
         */
        getMenuInflater().inflate(R.menu.menu_editor, menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {

            // Respond to a click on the "Save" menu option
            case R.id.action_save:

                // Save habit to database
                insertHabit();

                // Exit activity
                finish();
                return true;

            // Respond to a click on the "Up" arrow button in the app bar.
            case android.R.id.home:

                // Navigate back to parent activity (CatalogActivity)
                NavUtils.navigateUpFromSameTask(this);
                return true;

        }

        return super.onOptionsItemSelected(item);
    }
}
