package com.example.Indexer;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;

public class IndexerProvider extends ContentProvider{
    private SQLiteOpenHelper dbHelper;

    @Override
    public boolean onCreate() {
        dbHelper = new SQLiteOpenHelper(getContext(), "Indexes", null, 1) {
            public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
                // I won't update DB
            }

            @Override
            public void onCreate(SQLiteDatabase sqLiteDatabase) {
               sqLiteDatabase.execSQL("CREATE TABLE if not exists indexes (" +
                       "_id integer PRIMARY KEY autoincrement," +
                       "word TEXT, " +
                       "count INTEGER)");
            }
        };
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] strings, String s, String[] strings2, String s2) {
        SQLiteDatabase database = dbHelper.getReadableDatabase();
        return database.query("indexes", strings, s, strings2, null, null, s2);
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        database.insert("indexes", null, contentValues);
        return null;
    }

    @Override
    public int delete(Uri uri, String s, String[] strings) {
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        return database.delete("indexes", s, strings);
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String s, String[] strings) {
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        return database.update("indexes", contentValues, s, strings);
    }
}
