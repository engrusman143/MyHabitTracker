package com.example.android.habittracker.data;

import android.provider.BaseColumns;

/**
 * HabitTracker Created by Muir on 21/06/2017.
 * API Contract for the app.
 */

public final class HabitContract {

    // An empty constructor prevents accidental instantiation of the contract class.
    private HabitContract() {
    }

    /**
     * Inner class that defines constant values for the database table.
     * Each entry represents a single habit
     */
    public static final class HabitEntry implements BaseColumns {

        // Name of the the database table
        public final static String TABLE_NAME = "habits";

        /**
         * Unique ID number for the habit (only for use in the database table).
         * Type: INTEGER
         */
        public final static String _ID = BaseColumns._ID;

        /**
         * The habit being recorded
         * Type: TEXT
         */
        public final static String COLUMN_HABIT = "habit";

        /**
         * Frequency of habit.
         * The only possible values are {@link #DAILY}, {@link #EVERY_OTHER_DAY},
         * {@link #WEEKLY}, {@link #TWICE_MONTHLY}, or {@link #MONTHLY}
         * Type: INTEGER
         */
        public final static String COLUMN_FREQUENCY = "frequency";

        /**
         * Time (in minutes) that habit took to complete.
         * Type: INTEGER
         */
        public final static String COLUMN_TIME = "time";

        /**
         * Any extra information the user wishes to log
         * Type: TEXT
         */
        public final static String COLUMN_EXTRA_INFO = "extra";

        /**
         * Possible values for the frequency of the habit.
         */
        public static final int DAILY = 0;
        public static final int EVERY_OTHER_DAY = 1;
        public static final int WEEKLY = 2;
        public static final int TWICE_MONTHLY = 3;
        public static final int MONTHLY = 4;
    }

}
