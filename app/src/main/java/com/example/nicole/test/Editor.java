package com.example.nicole.test;

import android.app.AlertDialog;
import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.nicole.test.Data.Contract;
import com.example.nicole.test.Data.dbHelper;

public class Editor extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
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

    private Uri mCurrentUri = null;

    private boolean mStuffHasChanged = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        Intent i = getIntent();
        mCurrentUri = i.getData();

        if(mCurrentUri == null)
        {
            setTitle(R.string.add_new);

            // Invalidate the options menu, so the "Delete" menu option can be hidden.
            // (It doesn't make sense to delete something that hasn't been created yet.)
            invalidateOptionsMenu();
        }
        else
        {
            setTitle(R.string.edit_this);

            // Initialize a loader to read the data from the database
            // and display the current values in the editor
            getLoaderManager().initLoader(Stuff.LOADER, null, this);
        }

        // Find all relevant views that we will need to read user input from
        mNameEditText = (EditText) findViewById(R.id.edit_name);
        mCityEditText = (EditText) findViewById(R.id.edit_city);
        mAgeEditText = (EditText) findViewById(R.id.edit_age);
        mGenderSpinner = (Spinner) findViewById(R.id.spinner_gender);

        mNameEditText.setOnTouchListener(mTouchListener);
        mCityEditText.setOnTouchListener(mTouchListener);
        mAgeEditText.setOnTouchListener(mTouchListener);
        mGenderSpinner.setOnTouchListener(mTouchListener);

        setupSpinner();
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

    private void saveStuff() {
        Uri uri;
        String name = mNameEditText.getText().toString().trim();
        String city = mCityEditText.getText().toString().trim();
        Integer age = Integer.parseInt(mAgeEditText.getText().toString().trim());

        ContentValues values = new ContentValues();
        values.put(Contract.COLUMN_NAME, name);
        values.put(Contract.COLUMN_AGE, age);
        values.put(Contract.COLUMN_CITY, city);
        values.put(Contract.COLUMN_GENDER, mGender);

        if(mCurrentUri == null)
        {
            uri = getContentResolver().insert(Contract.CONTENT_URI, values);

            // Show a toast message depending on whether or not the insertion was successful
            if (uri == null) {
                // If the new content URI is null, then there was an error with insertion.
                Toast.makeText(this, getString(R.string.editor_save_failed),
                        Toast.LENGTH_SHORT).show();
            } else {
                // Otherwise, the insertion was successful and we can display a toast.
                Toast.makeText(this, getString(R.string.editor_save_successful),
                        Toast.LENGTH_SHORT).show();
            }
        }
        else
        {
            int rowsAffected = getContentResolver().update(mCurrentUri, values, null, null);

            // Show a toast message depending on whether or not the update was successful.
            if (rowsAffected == 0) {
                // If no rows were affected, then there was an error with the update.
                Toast.makeText(this, getString(R.string.editor_save_failed),
                        Toast.LENGTH_SHORT).show();
            } else {
                // Otherwise, the update was successful and we can display a toast.
                Toast.makeText(this, getString(R.string.editor_save_successful),
                        Toast.LENGTH_SHORT).show();
            }
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
                saveStuff();
                // Exit activity
                finish();
                return true;
            // Respond to a click on the "Delete" menu option
            case R.id.action_delete:
                getContentResolver().delete(mCurrentUri, null, null);
                finish();
                return true;
            // Respond to a click on the "Up" arrow button in the app bar
            case android.R.id.home:
                // If the stuff hasn't changed, continue with navigating up to parent activity
                // which is the {@link Stuff}.
                if (!mStuffHasChanged) {
                    NavUtils.navigateUpFromSameTask(Editor.this);
                    return true;
                }

                // Otherwise if there are unsaved changes, setup a dialog to warn the user.
                // Create a click listener to handle the user confirming that
                // changes should be discarded.
                DialogInterface.OnClickListener discardButtonClickListener =
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                // User clicked "Discard" button, navigate to parent activity.
                                NavUtils.navigateUpFromSameTask(Editor.this);
                            }
                        };

                // Show a dialog that notifies the user they have unsaved changes
                showUnsavedChangesDialog(discardButtonClickListener);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        // Since the editor shows all info, define a projection that contains
        // all columns from the table
        String[] projection = {Contract.ID_COLUMN, Contract.COLUMN_NAME, Contract.COLUMN_CITY, Contract.COLUMN_AGE, Contract.COLUMN_GENDER};

        return new CursorLoader(
                this,
                mCurrentUri,
                projection,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        // Bail early if the cursor is null or there is less than 1 row in the cursor
        if (data == null || data.getCount() < 1) {
            return;
        }

        if(data.moveToFirst())
        {
            // Find the columns that we're interested in
            int nameColumnIndex = data.getColumnIndex(Contract.COLUMN_NAME);
            int cityColumnIndex = data.getColumnIndex(Contract.COLUMN_CITY);
            int genderColumnIndex = data.getColumnIndex(Contract.COLUMN_GENDER);
            int ageColumnIndex = data.getColumnIndex(Contract.COLUMN_AGE);

            // Extract out the value from the Cursor for the given column index
            String name = data.getString(nameColumnIndex);
            String city = data.getString(cityColumnIndex);
            int gender = data.getInt(genderColumnIndex);
            int age = data.getInt(ageColumnIndex);

            // Update the views on the screen with the values from the database
            mNameEditText.setText(name);
            mCityEditText.setText(city);
            mAgeEditText.setText(Integer.toString(age));
            mGenderSpinner.setSelection(gender);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        // If the loader is invalidated, clear out all the data from the input fields.
        mNameEditText.setText("");
        mCityEditText.setText("");
        mAgeEditText.setText("");
        mGenderSpinner.setSelection(0); // Select "Unknown" gender
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        // If this new, hide the "Delete" menu item.
        if (mCurrentUri == null) {
            MenuItem menuItem = menu.findItem(R.id.action_delete);
            menuItem.setVisible(false);
        }
        return true;
    }

    // OnTouchListener that listens for any user touches on a View, implying that they are modifying
    // the view, and we change the mStuffHasChanged boolean to true.
    private View.OnTouchListener mTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            mStuffHasChanged = true;
            return false;
        }
    };

    @Override
    public void onBackPressed() {
        // If the stuff hasn't changed, continue with handling back button press
        if (!mStuffHasChanged) {
            super.onBackPressed();
            return;
        }

        // Otherwise if there are unsaved changes, setup a dialog to warn the user.
        // Create a click listener to handle the user confirming that changes should be discarded.
        DialogInterface.OnClickListener discardButtonClickListener =
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // User clicked "Discard" button, close the current activity.
                        finish();
                    }
                };

        // Show dialog that there are unsaved changes
        showUnsavedChangesDialog(discardButtonClickListener);
    }

    private void showUnsavedChangesDialog(DialogInterface.OnClickListener discardButtonClickListener) {
        // Create an AlertDialog.Builder and set the message, and click listeners
        // for the positive and negative buttons on the dialog.
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.unsaved_changes_dialog_msg);
        builder.setPositiveButton(R.string.discard, discardButtonClickListener);
        builder.setNegativeButton(R.string.keep_editing, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Keep editing" button, so dismiss the dialog
                // and continue editing the pet.
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        // Create and show the AlertDialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}