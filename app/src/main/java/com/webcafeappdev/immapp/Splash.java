package com.webcafeappdev.immapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class Splash extends AppCompatActivity {
private TextView txv;
private ImageView logo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        txv =(TextView)findViewById(R.id.txv);
        logo=(ImageView) findViewById(R.id.logo);
        Animation nkala= AnimationUtils.loadAnimation(this,R.anim.mytransition);
        txv.startAnimation(nkala);
        logo.startAnimation(nkala);
        final Intent i = new Intent(this,Logon.class);
        Thread timer =new Thread() {
            public void run() {
                try {
                    sleep(6000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    startActivity(i);
                    finish();
                }
            }
        };
            timer.start();
}
}

