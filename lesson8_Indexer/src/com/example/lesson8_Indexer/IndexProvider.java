package com.example.lesson8_Indexer;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

/**
 * Created with IntelliJ IDEA.
 * User: slavian
 * Date: 01.11.13
 * Time: 14:52
 * To change this template use File | Settings | File Templates.
 */
public class IndexProvider extends ContentProvider {

    private static final String TAG = IndexProvider.class.getSimpleName();

    public static final String STRUCTURE_NOTES = "notes";
    public static final String AUTHORITY = "org.nottingham.provider";

    private static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY);
    public static final Uri URI_NOTES = Uri.withAppendedPath(CONTENT_URI, STRUCTURE_NOTES);

    private static final String  DB_NAME = "Inventory";
    private static final int     DB_VERSION = 1;
    private static final String  T_NOTES = "Notes";

    public static final String T_NOTES_ID = "note_id";
    public static final String T_NOTES_TEXT = "text";
    public static final String T_NOTES_UPDATED = "updated";

    private static final String SQL_DB_CREATE =
            "CREATE TABLE if not exists " + T_NOTES + " (" +
                    T_NOTES_ID + " integer PRIMARY KEY autoincrement," +
                    T_NOTES_TEXT + "," +
                    T_NOTES_UPDATED + "," +
                    " UNIQUE (" + T_NOTES_TEXT +"));";
    private static final String SQL_DB_DROP = "DROP TABLE IF EXISTS " + T_NOTES;

    private static final int MATCH_ALLNOTES = 1;
    private static final int MATCH_NOTE = 2;

    // a content URI pattern matches content URIs using wildcard characters:
    // *: Matches a string of any valid characters of any length.
    // #: Matches a string of numeric characters of any length.
    private static final UriMatcher uriMatcher;
    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(AUTHORITY, STRUCTURE_NOTES, MATCH_ALLNOTES);
        uriMatcher.addURI(AUTHORITY, STRUCTURE_NOTES + "/#", MATCH_NOTE);
    }

    //------
    private SQLiteOpenHelper dbHelper;

    @Override
    public boolean onCreate() {
        dbHelper = new SQLiteOpenHelper(getContext(), DB_NAME, null, DB_VERSION) {
            public void onCreate(SQLiteDatabase db) {
                db.execSQL(SQL_DB_CREATE);
            }
            public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
                Log.w(TAG, "onUpgrade() old: " + oldVersion + " new: " + newVersion);
                db.execSQL(SQL_DB_DROP);
                onCreate(db);
            }
        };
        return true;
    }

    //Return the MIME type corresponding to a content URI
    @Override
    public String getType(Uri uri) {
        switch (uriMatcher.match(uri)) {
            case MATCH_ALLNOTES :
                return "vnd.android.cursor.dir/vnd.org.nottingham.contentprovider.notes";
            case MATCH_NOTE :
                return "vnd.android.cursor.item/vnd.org.nottingham.contentprovider.notes";
            default :
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
    }

    // The query() method must return a Cursor object, or if it fails,
    // throw an Exception. If you are using an SQLite database as your data storage,
    // you can simply return the Cursor returned by one of the query() methods of the
    // SQLiteDatabase class. If the query does not match any rows, you should return a
    // Cursor instance whose getCount() method returns 0. You should return null only
    // if an internal error occurred during the query process.
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();

        switch (uriMatcher.match(uri)) {
            case MATCH_ALLNOTES :
                queryBuilder.setTables(T_NOTES);
                break;
            case MATCH_NOTE :
                queryBuilder.setTables(T_NOTES);
                String id = uri.getLastPathSegment();
                queryBuilder.appendWhere(T_NOTES_ID + "=");
                queryBuilder.appendWhereEscapeString(id);
                break;
            default :
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }

        Cursor cursor = queryBuilder.query(db, projection, selection, selectionArgs, null, null, sortOrder);

        // if something is added after query() and notifyChange() is called, cursor will be recalculated
        // getContentResolver().notifyChange(uri, null);
        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;

    }

    // The insert() method adds a new row to the appropriate table, using the values
    // in the ContentValues argument. If a column name is not in the ContentValues argument,
    // you may want to provide a default value for it either in your provider code or in
    // your database schema.
    @Override
    public Uri insert(Uri uri, ContentValues values) {

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        switch (uriMatcher.match(uri)) {
            case MATCH_ALLNOTES :
                //do nothing
                break;
            default :
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
        long id = db.insert(T_NOTES, null, values);
        getContext().getContentResolver().notifyChange(uri, null);
        return Uri.parse(CONTENT_URI + "/" + id);
    }

    // The update method() is same as delete() which updates multiple rows
    // based on the selection or a single row if the row id is provided. The
    // update method returns the number of updated rows.
    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        switch (uriMatcher.match(uri)) {
            case MATCH_ALLNOTES :
                //do nothing
                break;
            case MATCH_NOTE :
                String id = uri.getPathSegments().get(1);
                selection = T_NOTES_ID + "=" + id + (!TextUtils.isEmpty(selection) ? " AND (" + selection + ')' : "");
                break;
            default :
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
        int updateCount = db.update(T_NOTES, values, selection, selectionArgs);
        getContext().getContentResolver().notifyChange(uri, null);
        return updateCount;
    }

    // The delete() method deletes rows based on the seletion or if an id is
    // provided then it deleted a single row. The methods returns the numbers
    // of records delete from the database. If you choose not to delete the data
    // physically then just update a flag here.
    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        switch (uriMatcher.match(uri)) {
            case MATCH_ALLNOTES :
                //do nothing
                break;
            case MATCH_NOTE :
                String id = uri.getPathSegments().get(1);
                selection = T_NOTES_ID + "=" + id + (!TextUtils.isEmpty(selection) ? " AND (" + selection + ')' : "");
                break;
            default :
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
        int deleteCount = db.delete(T_NOTES, selection, selectionArgs);
        getContext().getContentResolver().notifyChange(uri, null);
        return deleteCount;
    }
}
