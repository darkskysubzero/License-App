package com.example.licenseapp.ForgotPassword;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.example.licenseapp.Database;
import com.example.licenseapp.Login.Login;
import com.example.licenseapp.R;
import com.example.licenseapp.Signup.UserModel;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class ForgotPasswordSB extends AppCompatActivity {



    private TextInputLayout tilForgotPassword;
    private TextInputEditText tietForgotPassword;
    private TextView btnForgotPasswordSB;
    private Database db;
    private String password;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password_sb);

        Intent i = getIntent();
        String e = i.getStringExtra("email");
        System.out.println("Email : "+e);

        db = new Database(ForgotPasswordSB.this);
        tilForgotPassword = findViewById(R.id.forgotPasswordBox);
        tietForgotPassword = findViewById(R.id.forgotPassword);

        btnForgotPasswordSB = findViewById(R.id.btnForgotPasswordSB);
        btnForgotPasswordSB.setOnClickListener(v->{
            if(isPasswordValid()){
                UserModel user = db.getUser(e);
                user.setPassword(this.password);
                db.updateUser(user);
                goToActivity(Login.class,'L');
            }
        });
    }


    //VALIDATION
    private boolean isPasswordValid(){
        boolean isValid = true;
        //PASSWORD
        password = tietForgotPassword.getText().toString().trim();
        if(password.isEmpty() || password.length()<6){
            isValid = false;
            tilForgotPassword.setErrorEnabled(true);
            tilForgotPassword.setError("Password must atleast be 6 characters");
        }else{
            isValid = true;
            tilForgotPassword.setErrorEnabled(false);
            tilForgotPassword.setError("");
        }
        return isValid;
    }

    //Responsible for going to a particular activity
    private void goToActivity(Class c, char animationStyle){
        Intent intent = new Intent(ForgotPasswordSB.this, c);
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
        goToActivity(ForgotPasswordSA.class,'R');

    }
}