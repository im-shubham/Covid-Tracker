package com.shubham.covidtracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;

import java.util.logging.Handler;

public class Splash_activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        getSupportActionBar().hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Thread thread = new Thread(){
            public void run(){
                try{
                    sleep(5700);
                }catch (Exception e){
                    e.printStackTrace();

                }finally {
                    Intent intent =new Intent(Splash_activity.this,MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }

        }; thread.start();
    }
}