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
import android.widget.TextView;
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

        dbStuff = new dbHelper(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        displayDatabase();
    }

    private void displayDatabase()
    {

        Cursor c = getContentResolver().query(Contract.CONTENT_URI, null, null, null, null);

        TextView text = (TextView) findViewById(R.id.text_view_stuff);
        text.setText("");

        int columnIndexId = c.getColumnIndex(Contract.ID_COLUMN);
        int columnIndexName = c.getColumnIndex(Contract.COLUMN_NAME);
        int columnIndexAge = c.getColumnIndex(Contract.COLUMN_AGE);
        int columnIndexCity = c.getColumnIndex(Contract.COLUMN_CITY);
        int columnIndexGender = c.getColumnIndex(Contract.COLUMN_GENDER);

        try {
            while (c.moveToNext()) {
                int currId = c.getInt(columnIndexId);
                String currName = c.getString(columnIndexName);
                int currAge = c.getInt(columnIndexAge);
                String currCity = c.getString(columnIndexCity);
                int currGender = c.getInt(columnIndexGender);

                String gender;

                if (currGender == 0)
                {
                    gender = "Unknown";
                }
                else if(currGender == 1)
                {
                    gender = "Male";
                }
                else
                {
                    gender = "Female";
                }

                text.append(currId + " - "
                        + currName + " - "
                        + currAge + " - "
                        + currCity + " - "
                        + gender + "\n");
            }
        }
        finally {
            c.close();
        }
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