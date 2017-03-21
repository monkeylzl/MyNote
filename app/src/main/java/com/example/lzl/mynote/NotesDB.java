package com.example.lzl.mynote;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by lzl on 2017/3/21.
 */

class NotesDB extends SQLiteOpenHelper {

    static final String TABLE_NAME = "notes";
    static final String CONTENT = "content";
    static final String PATH = "path";
    static final String VIDEO = "video";
    static final String ID = "_id";
    static final String TIME = "time";

    NotesDB(Context context) {
        super(context, "notes", null, 1);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME + "(" + ID
                + " INTEGER  PRIMARY KEY AUTOINCREMENT," + CONTENT
                + " TEXT NOT NULL," + PATH + " TEXT NOT NULL," + VIDEO
                + " TEXT NOT NULL," + TIME + " TEXT NOT NULL)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub

    }

}

