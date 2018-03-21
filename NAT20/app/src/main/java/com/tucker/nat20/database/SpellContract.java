package com.tucker.nat20.database;

import android.provider.BaseColumns;

/**
 * Created by eli on 3/18/2018.
 */

public class SpellContract {
    private SpellContract() {}
    public static class SavedSpells implements BaseColumns {
        public static final String TABLE_NAME = "savedSpells";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_URL = "url";
        public static final String COLUMN_TIMESTAMP = "timestamp";
    }
}
