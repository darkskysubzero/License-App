package com.example.licenseapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class BookDriving extends AppCompatActivity {

    private AppCompatImageButton btnBookDrivingBack;
    private Button btnFinishBookDriving;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_driving);

        btnBookDrivingBack = findViewById(R.id.btnBookDrivingBack);
        btnFinishBookDriving = findViewById(R.id.btnFinishBookDriving);

        btnBookDrivingBack.setOnClickListener(v->{
            goToActivity(Dashboard.class,'R');
        });

        btnFinishBookDriving.setOnClickListener(v->{
            goToActivity(Payment.class,'L',"BookB");
        });

    }

    private void goToActivity(Class c, char animationStyle){
        Intent intent = new Intent(BookDriving.this, c);
        startActivity(intent);
        if(animationStyle=='L'){
            overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
        }else if(animationStyle=='R'){
            overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
        }
        finish();
    }

    private void goToActivity(Class c, char animationStyle, String rootClass){
        Intent intent = new Intent(BookDriving.this, c);
        intent.putExtra("rootClass",rootClass);
        intent.putExtra("price","R30.00");
        startActivity(intent);
        if(animationStyle=='L'){
            overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
        }else if(animationStyle=='R'){
            overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
        }
        finish();
    }

    @Override
    public void onBackPressed() {
        goToActivity(Dashboard.class, 'R');
    }
}