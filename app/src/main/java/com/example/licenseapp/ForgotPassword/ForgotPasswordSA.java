package com.example.licenseapp.ForgotPassword;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.example.licenseapp.Database;
import com.example.licenseapp.Login.Login;
import com.example.licenseapp.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class ForgotPasswordSA extends AppCompatActivity {


    private TextView btnForgotPasswordSA;
    private Database db;
    private TextInputLayout tilForgotEmail;
    private TextInputEditText tietForgotEmail;
    private String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password_sa);

        tilForgotEmail = findViewById(R.id.forgotEmailBox);
        tietForgotEmail = findViewById(R.id.forgotEmail);
        db = new Database(ForgotPasswordSA.this);

        btnForgotPasswordSA = findViewById(R.id.btnForgotPasswordSA);
        btnForgotPasswordSA.setOnClickListener(v->{
            //IF USER ENTERED A VALID EMAIL AND IF EMAIL IS FOUND IN DB THEN
            if(isEmailValid()){
                goToActivity(ForgotPasswordSB.class,'L');
            }
        });
    }

    //Responsible for going to a particular activity
    private void goToActivity(Class c, char animationStyle){
        Intent intent = new Intent(ForgotPasswordSA.this, c);
        //ADDING EMAIL FOR NEXT ACTIVITY
        intent.putExtra("email",this.email);
        //==========================================
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
        goToActivity(Login.class,'R');
    }


    //VALIDATION
    private boolean isEmailValid(){
        boolean isValid = true;
        //EMAIL
        email = tietForgotEmail.getText().toString().trim();
        if(email.isEmpty() || email.length()<10 || isNumeric(email) || !isEmailValid(email)){
            isValid = false;
            tilForgotEmail.setErrorEnabled(true);
            tilForgotEmail.setError("Please enter a valid email");
        }else if(!db.userExists(email)){
            isValid = false;
            tilForgotEmail.setErrorEnabled(true);
            tilForgotEmail.setError("Email not found");
        }else{
            isValid = true;
            tilForgotEmail.setErrorEnabled(false);
            tilForgotEmail.setError("");
        }
        return isValid;
    }

    private boolean isEmailValid(String email) {
        String regex = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
        return email.matches(regex);
    }

    private static boolean isNumeric(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch(NumberFormatException e){
            return false;
        }
    }

}