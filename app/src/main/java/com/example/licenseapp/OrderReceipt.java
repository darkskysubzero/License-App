package com.example.licenseapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

public class OrderReceipt extends AppCompatActivity {

    private Button btnContinue;
    private TextView receiptResult,receiptInstruction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_receipt);

        btnContinue = findViewById(R.id.btnContinue);
        btnContinue.setOnClickListener(v->{
            goToActivity(Dashboard.class, 'L');
        });

        receiptInstruction = findViewById(R.id.receiptInstruction);
        receiptResult = findViewById(R.id.receiptResult);

        receiptInstruction.setText(getIntent().getStringExtra("instruction"));
        receiptResult.setText(getIntent().getStringExtra("result"));



    }

    private void goToActivity(Class c, char animationStyle){
        Intent intent = new Intent(OrderReceipt.this, c);
        startActivity(intent);
        if(animationStyle=='L'){
            overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
        }else if(animationStyle=='R'){
            overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
        }
        finish();
    }

}