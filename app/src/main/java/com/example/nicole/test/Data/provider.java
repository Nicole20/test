package com.example.nicole.test.Data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

public class provider extends ContentProvider{

    /** Tag for the log messages */
    public static final String LOG_TAG = provider.class.getSimpleName();

    public static final int STUFF = 1;
    public static final int STUFF_ID = 2;
    public static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    dbHelper someHelper;

    static {
        sUriMatcher.addURI(Contract.CONTENT_AUTHORITY, Contract.TABLE_NAME, 1);
        sUriMatcher.addURI(Contract.CONTENT_AUTHORITY, Contract.TABLE_NAME + "/#", 2);
    }

    public provider() {
    }

    @Override
    public boolean onCreate() {
        someHelper = new dbHelper(getContext());
        return false;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        SQLiteDatabase database = someHelper.getReadableDatabase();
        Cursor cursor;

        int match = sUriMatcher.match(uri);

        switch(match)
        {
            case STUFF:
                cursor = database.query(Contract.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case STUFF_ID:
                selection = Contract._ID + "=?";
                selectionArgs = new String[] {String.valueOf(ContentUris.parseId(uri))};
                cursor = database.query(Contract.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Cannot query unknown URI: " + uri);
        }

        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case STUFF:
                return Contract.CONTENT_LIST_TYPE;
            case STUFF_ID:
                return Contract.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalStateException("Unknown URI " + uri + " with match " + match);
        }
    }

    @Nullable
    @Override
    /* Returns a String that describes the type of the data stored at the input Uri */
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        final int match = sUriMatcher.match(uri);

        switch(match) {
            case STUFF:
                return insertStuff(uri, values);
            default:
                throw new IllegalArgumentException("Insertion is not supported for " + uri);
        }
    }

    private Uri insertStuff(Uri uri, ContentValues values) {

        SQLiteDatabase dbStuff = someHelper.getWritableDatabase();

        // Insert the new pet with the given values
        long id = dbStuff.insert(Contract.TABLE_NAME, null, values);

        if (id == -1) {
            Log.e(LOG_TAG, "Failed to insert row for " + uri);
            return null;
        }

        getContext().getContentResolver().notifyChange(uri, null);

        // Once we know the ID of the new row in the table,
        // return the new URI with the ID appended to the end of it
        return ContentUris.withAppendedId(uri, id);
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        final int match = sUriMatcher.match(uri);

        SQLiteDatabase database = someHelper.getWritableDatabase();

        switch (match){
            // Delete whole table
            case STUFF:
                getContext().getContentResolver().notifyChange(uri, null);
                return database.delete(Contract.TABLE_NAME, selection, selectionArgs);
            case STUFF_ID:
                selection = Contract._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                getContext().getContentResolver().notifyChange(uri, null);
                return database.delete(Contract.TABLE_NAME, selection, selectionArgs);
            default:
                throw new IllegalArgumentException("Deletion is not supported for " + uri);
        }
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        final int match = sUriMatcher.match(uri);

        switch(match) {
            case STUFF:
                return updateStuff(uri, values, selection, selectionArgs);
            case STUFF_ID:
                selection = Contract._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                return updateStuff(uri, values, selection, selectionArgs);
            default:
                throw new IllegalArgumentException("Update is not supported for " + uri);
        }
    }

    private int updateStuff(Uri uri, ContentValues values, String selection, String[] selectionArgs) {

        if (values.containsKey(Contract.COLUMN_NAME)) {
            String name = values.getAsString(Contract.COLUMN_NAME);
            if (name == null) {
                throw new IllegalArgumentException("Requires a name");
            }
        }

        if (values.containsKey(Contract.COLUMN_GENDER)) {
            Integer gender = values.getAsInteger(Contract.COLUMN_GENDER);
            if (gender == null || !Contract.isValidGender(gender)) {
                throw new IllegalArgumentException("Requires valid gender");
            }
        }

        if (values.containsKey(Contract.COLUMN_AGE)) {
            // Check that the age is greater than to 0
            Integer weight = values.getAsInteger(Contract.COLUMN_AGE);
            if (weight != null && weight < 0) {
                throw new IllegalArgumentException("Requires valid age");
            }
        }

        // If there are no values to update, then don't try to update the database
        if (values.size() == 0) {
            return 0;
        }

        // Otherwise, get writable database to update the data
        SQLiteDatabase database = someHelper.getWritableDatabase();

        getContext().getContentResolver().notifyChange(uri, null);

        // Returns the number of database rows affected by the update statement
        return database.update(Contract.TABLE_NAME, values, selection, selectionArgs);
    }
}
