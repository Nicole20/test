package com.example.nicole.test;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Nicole on 12/8/2017.
 */

public class WordAdapter extends ArrayAdapter<Word> {

    private int colourID;

    public WordAdapter(@NonNull Context context, @NonNull List<Word> objects, int colourID) {
        super(context, 0, objects);
        this.colourID = colourID;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        // Get the {@link Word} object located at this position in the list
        Word currentWord = getItem(position);

        // Check if the existing view is being reused, otherwise inflate the view
        View listItemView = convertView;
        if (listItemView == null)
        {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        }

        TextView english = (TextView) listItemView.findViewById(R.id.tv_english);
        english.setText(currentWord.getEnglish());

        TextView french = (TextView) listItemView.findViewById(R.id.tv_french);
        french.setText(currentWord.getFrench());

        ImageView image = (ImageView) listItemView.findViewById(R.id.im_number);

        View textLayout = listItemView.findViewById(R.id.textLayout);

        int colour = ContextCompat.getColor(getContext(), colourID);

        textLayout.setBackgroundColor(colour);

        if(currentWord.isImage())
        {
            image.setImageResource(currentWord.getImageID());
            image.setVisibility(View.VISIBLE);
        }
        else
        {
            image.setVisibility(View.GONE);
        }

        return listItemView;
    }
}
