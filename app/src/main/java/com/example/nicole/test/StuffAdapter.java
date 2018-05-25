package com.example.nicole.test;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.example.nicole.test.Data.Contract;

import java.util.List;

public class StuffAdapter extends CursorAdapter{

    public StuffAdapter(Context context, Cursor c) {
        super(context, c, 0 /* flags */);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.other_list, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        TextView nameText = (TextView) view.findViewById(R.id.name);
        TextView cityText = (TextView) view.findViewById(R.id.city);

        int nameColumn = cursor.getColumnIndex(Contract.COLUMN_NAME);
        int cityColumn = cursor.getColumnIndex(Contract.COLUMN_CITY);

        String sName = cursor.getString(nameColumn);
        String sCity = cursor.getString(cityColumn);

        nameText.setText(sName);
        cityText.setText(sCity);
    }
}
