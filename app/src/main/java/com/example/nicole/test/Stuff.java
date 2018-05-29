package com.example.nicole.test;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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
import com.example.nicole.test.Data.dbHelper;

public class Stuff extends AppCompatActivity {

    private dbHelper dbStuff;

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
        ListView petListView = (ListView) findViewById(R.id.text_view_stuff);

        // Find and set empty view on the ListView, so that it only shows when the list has 0 items.
        View emptyView = findViewById(R.id.empty_view);
        petListView.setEmptyView(emptyView);

        dbStuff = new dbHelper(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        displayDatabase();
    }

    private void displayDatabase()
    {
        String[] projection = {Contract.ID_COLUMN, Contract.COLUMN_NAME, Contract.COLUMN_AGE,
                Contract.COLUMN_CITY, Contract.COLUMN_GENDER};

        Cursor c = getContentResolver().query(Contract.CONTENT_URI, null, null, null, null);

        ListView listView = (ListView)findViewById(R.id.text_view_stuff);

        StuffAdapter adapter = new StuffAdapter(this, c);

        listView.setAdapter(adapter);

        c.close();
    }

    private void insertStuff() {
        SQLiteDatabase db = dbStuff.getWritableDatabase();

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
                displayDatabase();
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
}