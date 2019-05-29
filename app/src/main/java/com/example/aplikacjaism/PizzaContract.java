package com.example.aplikacjaism;

import android.provider.BaseColumns;

public final class PizzaContract {
    private PizzaContract() {}

    public static class PizzaEntry implements BaseColumns {
        public static final String TABLE_NAME = "entry";
        public static final String COLUMN_NAME_TITLE = "title";
        public static final String COLUMN_NAME_SUBTITLE = "subtitle";

    }

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + PizzaEntry.TABLE_NAME + " (" +
                    PizzaEntry._ID + " INTEGER PRIMARY KEY," +
                    PizzaEntry.COLUMN_NAME_TITLE + " TEXT," +
                    PizzaEntry.COLUMN_NAME_SUBTITLE + " TEXT)";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + PizzaEntry.TABLE_NAME;
}
