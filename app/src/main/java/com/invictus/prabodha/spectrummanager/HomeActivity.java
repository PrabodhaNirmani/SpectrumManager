package com.invictus.prabodha.spectrummanager;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.invictus.prabodha.spectrummanager.Client.ClientActivity;
import com.invictus.prabodha.spectrummanager.Server.ControlActivity;

import java.util.HashMap;

public class HomeActivity extends AppCompatActivity {


    private int myChannel;
    static HashMap<String,Integer> deviceList;
    private static final String TAG = "HomeActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        initializeUI();
    }


    private void initializeUI(){
        Button centralUser = findViewById(R.id.central_user);
        Button otherUser = findViewById(R.id.other_user);

        deviceList = new HashMap<>();
        centralUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(HomeActivity.this, ControlActivity.class);
                startActivity(myIntent);
            }
        });

        otherUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(HomeActivity.this, ClientActivity.class);
                startActivity(myIntent);

            }
        });

    }
}
