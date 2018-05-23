package com.example.nicole.test;

import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;

public class Colours extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_colours);

        final ArrayList<Word> colours = new ArrayList<>();

        colours.add(new Word("red", "rouge", MediaPlayer.create(Colours.this, R.raw.giants1), R.drawable.color_red));
        colours.add(new Word("orange", "orange", MediaPlayer.create(Colours.this, R.raw.giants2), R.drawable.color_dusty_yellow));
        colours.add(new Word("yellow", "jaune", MediaPlayer.create(Colours.this, R.raw.giants3), R.drawable.color_mustard_yellow));
        colours.add(new Word("green", "vert", MediaPlayer.create(Colours.this, R.raw.giants1), R.drawable.color_green));
        colours.add(new Word("blue", "bleu", MediaPlayer.create(Colours.this, R.raw.giants2), R.drawable.color_black));
        colours.add(new Word("purple", "violet", MediaPlayer.create(Colours.this, R.raw.giants3), R.drawable.color_brown));

        WordAdapter itemAdapter1 = new WordAdapter(Colours.this, colours, R.color.darkYellow);

        ListView listView1 = (ListView) findViewById(R.id.colourList);

        listView1.setAdapter(itemAdapter1);
    }
}
