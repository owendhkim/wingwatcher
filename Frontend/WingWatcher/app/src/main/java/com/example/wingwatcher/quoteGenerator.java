package com.example.wingwatcher;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import java.util.Random;

public class quoteGenerator extends AppCompatActivity {

    private EditText quoteBox;
    private Button quoteButton;
    private Button backButton;
    private String[] quotesArray = {
            "Life without dreams is like a bird with a broken wing - it can't fly.\n Dan Pena",
            "The early bird gets the worm. The early worm... gets eaten.\n Norman Ralph Augustine",
            "Everyone likes birds. What wild creature is more accessible to our eyes and ears," +
                    "as close to us and everyone in the world, as universal as a bird?\n David Attenborough",
            "Each day has a story to - deserves to be told, because we are made of stories. " +
                    "I mean, scientists say that human beings are made of atoms, " +
                    "but a little bird told me that we are also made of stories. \n Eduardo Galeano",
            "I don't ask for the meaning of the song of a bird or the rising of the sun on a misty morning. " +
                    "There they are, and they are beautiful. \n Pete Hamill",
            "It's best to have failure happen early in life." +
                    " It wakes up the Phoenix bird in you so you rise from the ashes \n Anne Baxter",
            "Just as the bird sings or the butterfly soars, because it is his natural characteristic," +
                    " so the artist works.\n Alma Gluck",
            "It's impossible to explain creativity. It's like asking a bird, 'How do you fly?' " +
                    "You just do.\n Eric Jerome Dickey",
            "Be like the bird who, pausing in her flight awhile on boughs too slight, feels them give way beneath her," +
                    " and yet sings, knowing she hath wings. \n Victor Hugo",
            "I'd rather learn from one bird how to sing than teach ten thousand stars how not to dance.\n e. e. cummings"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quote_generator);

        quoteBox = findViewById(R.id.quoteBox);
        quoteButton = findViewById(R.id.quoteButton);
        backButton = findViewById(R.id.backButton);

        quoteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Random random = new Random();
                int index = random.nextInt(quotesArray.length);
                quoteBox.setText(quotesArray[index]);
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(quoteGenerator.this, dashboard.class);
                startActivity(intent);
            }
        });
    }
    public String[] getQuotesArray() {
        return quotesArray;
    }
}
