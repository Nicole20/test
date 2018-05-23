package com.example.nicole.test;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import java.text.NumberFormat;
import android.content.Intent;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    public int numberOfCoffee = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView numbers = (TextView) findViewById(R.id.numbers);
        TextView colours = (TextView) findViewById(R.id.colours);
        TextView music   = (TextView) findViewById(R.id.music);
        TextView stuff   = (TextView) findViewById(R.id.stuff);

        numbers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, Numbers.class);
                startActivity(i);
            }
        });

        colours.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, Colours.class);
                startActivity(i);
            }
        });

        music.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, Music.class);
                startActivity(i);
            }
        });

        stuff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, Stuff.class);
                startActivity(i);
            }
        });
    }

    /**
    * This method is called when the Order button is clicked
    */
    protected void submitOrder(View view)
    {
        display(numberOfCoffee);
        displayPrice(numberOfCoffee * 5);
    }

    protected void increment(View view)
    {
        display(++numberOfCoffee);
    }

    protected void decrement(View view)
    {
        if(numberOfCoffee > 0) {
            display(--numberOfCoffee);
        }
    }

    /**
     * This method displays the given quantity value on the screen.
     */
    private void display(int number) {
        TextView quantityTextView = (TextView) findViewById(R.id.quantity_text_view);
        quantityTextView.setText("" + number);
    }

    private void displayPrice(int number) {
        TextView priceTextView = (TextView) findViewById(R.id.price_text_view);
        priceTextView.setText(NumberFormat.getCurrencyInstance().format(number));
    }

}
