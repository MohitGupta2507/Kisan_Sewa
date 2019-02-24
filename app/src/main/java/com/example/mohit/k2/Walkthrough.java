package com.example.mohit.k2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class Walkthrough extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_walkthrough);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent i=new Intent(Walkthrough.this,MainDashboard.class);
        startActivity(i);
        finish();
    }
}


