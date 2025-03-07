package com.fantasy.team.prediction;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

 

public class FirstScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_screen);

        findViewById(R.id.btn_fantasy_news).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(FirstScreenActivity.this,MainActivity.class).putExtra("type","news").putExtra("sa","s"));
                finish();
            }
        });

        findViewById(R.id.btn_fantasy_pred).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(FirstScreenActivity.this,MainActivity.class).putExtra("type","pred").putExtra("sa","s"));
                finish();
            }
        });


    }
}