package com.example.licenseapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.licenseapp.Login.Login;
import com.example.licenseapp.Signup.UserModel;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {


    private Handler handler = new Handler();
    private ProgressBar progressBar;
    private int progressBarCount = 0;
    private Timer timer;
    private TextView progressText;
    private Database db;
    private SharedPreferences loginSP;
    private UserModel user;
    private int loggedInState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        showProgressBar();
        db = new Database(MainActivity.this);
        loginSP = getSharedPreferences("LoginSP", Context.MODE_PRIVATE);
        String email = "";

        if(!loginSP.contains("userEmail")){
            gotoLoginScreen();
        }
        if(loginSP.contains("userEmail")){
            email = loginSP.getString("userEmail","");
            UserModel user = db.getUser(email);
            user.setKeepLoggedIn(1);
            db.updateUser(user);
            gotToDashboard();
        }
    }


    private void showProgressBar(){
        progressBar = findViewById(R.id.progressBar);
        timer = new Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                progressBarCount++;
                progressBar.setProgress(progressBarCount);
                if(progressBarCount==100){
                    timer.cancel();
                }
            }
        };
        timer.schedule(timerTask,0,2000);
    }

    private void gotoLoginScreen(){
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(MainActivity.this, Login.class);
                startActivity(intent);
                finish();
            }
        },3000);
    }

    private void gotToDashboard(){
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(MainActivity.this, Dashboard.class);
                startActivity(intent);
                finish();
            }
        },3000);
    }
}