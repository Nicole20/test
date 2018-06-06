package com.example.nicole.test;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.nicole.test.Data.Contract;
import com.example.nicole.test.Data.dbHelper;

public class Editor extends AppCompatActivity {
    /** EditText field to enter the name */
    private EditText mNameEditText;

    /** EditText field to enter the city */
    private EditText mCityEditText;

    /** EditText field to enter the age */
    private EditText mAgeEditText;

    /** EditText field to enter the gender */
    private Spinner mGenderSpinner;

    /**
     * The possible values are:
     * 0 for male, 1 for female.
     */
    private int mGender = 0;

    private dbHelper dbStuff;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        Intent i = getIntent();
        Uri currUri = i.getData();

        if(currUri == null)
        {
            setTitle(R.string.add_new);
        }
        else
        {
            setTitle(R.string.edit_this);
        }

        // Find all relevant views that we will need to read user input from
        mNameEditText = (EditText) findViewById(R.id.edit_name);
        mCityEditText = (EditText) findViewById(R.id.edit_city);
        mAgeEditText = (EditText) findViewById(R.id.edit_age);
        mGenderSpinner = (Spinner) findViewById(R.id.spinner_gender);

        setupSpinner();

        dbStuff = new dbHelper(this);
    }

    private void setupSpinner() {
        // Create adapter for spinner. The list options are from the String array it will use
        // the spinner will use the default layout
        ArrayAdapter genderSpinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.array_gender_options, android.R.layout.simple_spinner_item);

        // Specify dropdown layout style - simple list view with 1 item per line
        genderSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);

        // Apply the adapter to the spinner
        mGenderSpinner.setAdapter(genderSpinnerAdapter);

        // Set the integer mSelected to the constant values
        mGenderSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selection = (String) parent.getItemAtPosition(position);
                if (!TextUtils.isEmpty(selection)) {
                    if (selection.equals(getString(R.string.gender_male))) {
                        mGender = Contract.MALE; // Male
                    } else if (selection.equals(getString(R.string.gender_female))) {
                        mGender = Contract.FEMALE; // Female
                    } else {
                        mGender = Contract.UNKNOWN;
                    }
                }
            }

            // Because AdapterView is an abstract class, onNothingSelected must be defined
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                mGender = 0; // Unknown
            }
        });
    }

    private void insertStuff() {
        String name = mNameEditText.getText().toString().trim();
        String city = mCityEditText.getText().toString().trim();
        Integer age = Integer.parseInt(mAgeEditText.getText().toString().trim());

        SQLiteDatabase db = dbStuff.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Contract.COLUMN_NAME, name);
        values.put(Contract.COLUMN_AGE, age);
        values.put(Contract.COLUMN_CITY, city);
        values.put(Contract.COLUMN_GENDER, mGender);

        Uri uri = getContentResolver().insert(Contract.CONTENT_URI, values);

        // Show a toast message depending on whether or not the insertion was successful
        if (uri == null) {
            // If the new content URI is null, then there was an error with insertion.
            Toast.makeText(this, getString(R.string.editor_insert_pet_failed),
                    Toast.LENGTH_SHORT).show();
        } else {
            // Otherwise, the insertion was successful and we can display a toast.
            Toast.makeText(this, getString(R.string.editor_insert_pet_successful),
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_editor.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.menu_editor, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Save" menu option
            case R.id.action_save:
                insertStuff();
                // Exit activity
                finish();
                return true;
            // Respond to a click on the "Delete" menu option
            case R.id.action_delete:
                // Do nothing for now
                return true;
            // Respond to a click on the "Up" arrow button in the app bar
            case android.R.id.home:
                // Navigate back to parent activity (CatalogActivity)
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}