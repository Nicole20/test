package com.example.nicole.test;

import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.example.nicole.test.Data.Contract;

public class Stuff extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    public static final int LOADER = 0;

    StuffAdapter cursorAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stuff);

        // Setup FAB to open EditorActivity
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Stuff.this, Editor.class);
                startActivity(intent);
            }
        });

        // Find the ListView which will be populated with the pet data
        ListView listView = (ListView) findViewById(R.id.text_view_stuff);

        // Find and set empty view on the ListView, so that it only shows when the list has 0 items.
        View emptyView = findViewById(R.id.empty_view);
        listView.setEmptyView(emptyView);

        cursorAdapter = new StuffAdapter(this, null);
        listView.setAdapter(cursorAdapter);

        getLoaderManager().initLoader(LOADER, null, this);
    }

    private void insertStuff() {

        ContentValues values = new ContentValues();

        values.put(Contract.COLUMN_NAME, "Tom");
        values.put(Contract.COLUMN_AGE, 24);
        values.put(Contract.COLUMN_CITY, "New York");
        values.put(Contract.COLUMN_GENDER, Contract.MALE);

        Uri uri = getContentResolver().insert(Contract.CONTENT_URI, values);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_catalog.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.menu_catalog, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Insert dummy data" menu option
            case R.id.action_insert_dummy_data:
                insertStuff();
                Toast toast = Toast.makeText(this, "Saved", Toast.LENGTH_SHORT);
                toast.show();
                return true;
            // Respond to a click on the "Delete all entries" menu option
            case R.id.action_delete_all_entries:
                // Do nothing for now
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        String[] projection = {Contract.ID_COLUMN, Contract.COLUMN_NAME, Contract.COLUMN_CITY};

        switch (id) {
            case LOADER:
                return new CursorLoader(
                        this,
                        Contract.CONTENT_URI,
                        projection,
                        null,
                        null,
                        null);
            default:
                return null;
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        cursorAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        cursorAdapter.swapCursor(null);
    }
}