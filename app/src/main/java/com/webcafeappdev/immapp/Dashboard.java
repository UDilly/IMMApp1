package com.webcafeappdev.immapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Dashboard extends AppCompatActivity {

    Button c1,c2,c3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        c1 = (Button)findViewById(R.id.c1);
        c2 = (Button)findViewById(R.id.c2);
        c3 = (Button)findViewById(R.id.c3);
//opens the Incident list view activity

        c1.setOnClickListener(new View.OnClickListener() {

            @Override
           public void onClick(View view) {
               Intent intent = new Intent(Dashboard.this,incidentList.class);
               startActivity(intent);
           }
        });
//opens the Map view activity

        c2.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Dashboard.this, MapsActivity.class);
                startActivity(intent);
            }

          });
//opens the Record incident form

        c3.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view){
                Intent intent = new Intent (Dashboard.this,MainActivity.class);
                startActivity(intent);
            }
        });
      }
    }

