package com.example.nicole.test;

import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.content.Intent;
import android.widget.Toast;

import java.util.ArrayList;

public class Numbers extends AppCompatActivity {

    private  final String LOG_TAG = "Number_Activity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_numbers);

        final ArrayList<Word> words = new ArrayList<>();
        final ArrayList<Word> phrases = new ArrayList<>();

        words.add(new Word("one", "un", MediaPlayer.create(Numbers.this, R.raw.giants1), R.drawable.number_one));
        words.add(new Word("two", "deux", MediaPlayer.create(Numbers.this, R.raw.giants2), R.drawable.number_two));
        words.add(new Word("three", "trois", MediaPlayer.create(Numbers.this, R.raw.giants3), R.drawable.number_three));
        words.add(new Word("four", "quatre", MediaPlayer.create(Numbers.this, R.raw.giants1), R.drawable.number_four));
        words.add(new Word("five", "cinq", MediaPlayer.create(Numbers.this, R.raw.giants2), R.drawable.number_five));
        words.add(new Word("six", "six", MediaPlayer.create(Numbers.this, R.raw.giants3), R.drawable.number_six));
        words.add(new Word("seven", "sept", MediaPlayer.create(Numbers.this, R.raw.giants1), R.drawable.number_seven));
        words.add(new Word("eight", "huit", MediaPlayer.create(Numbers.this, R.raw.giants2), R.drawable.number_eight));
        words.add(new Word("nine", "neuf", MediaPlayer.create(Numbers.this, R.raw.giants3), R.drawable.number_nine));
        words.add(new Word("ten", "dix", MediaPlayer.create(Numbers.this, R.raw.giants1), R.drawable.number_ten));

        phrases.add(new Word("Hello", "Bonjour", MediaPlayer.create(Numbers.this, R.raw.giants2)));
        phrases.add(new Word("How are you?", "Comment ca va?", MediaPlayer.create(Numbers.this, R.raw.giants3)));
        phrases.add(new Word("My name is ...", "Je m'appelle ...", MediaPlayer.create(Numbers.this, R.raw.giants1)));

        WordAdapter itemAdapter1 = new WordAdapter(Numbers.this, words, R.color.darkOrange);
        WordAdapter itemAdapter2 = new WordAdapter(Numbers.this, phrases, R.color.darkGreen);

        ListView listView1 = (ListView) findViewById(R.id.list1);

        listView1.setAdapter(itemAdapter1);

        ListView listView2 = (ListView) findViewById(R.id.list2);

        listView2.setAdapter(itemAdapter2);

        listView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final MediaPlayer mediaPlayer = words.get(position).getMediaPlayer();
                mediaPlayer.start();
            }
        });

        Log.i(LOG_TAG,"onCreate");
    }

}
