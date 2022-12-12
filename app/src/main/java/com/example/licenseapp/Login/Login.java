package com.example.licenseapp.Login;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.licenseapp.Dashboard;
import com.example.licenseapp.Database;
import com.example.licenseapp.ForgotPassword.ForgotPasswordSA;
import com.example.licenseapp.R;
import com.example.licenseapp.Signup.SignupSA;
import com.example.licenseapp.Signup.UserModel;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.io.File;

public class Login extends AppCompatActivity implements View.OnClickListener {

    private TextView btnForgotPassword;
    private TextView btnGoToSignup;
    private Button btnLogin;

    private TextInputLayout loginEmailBox;
    private TextInputLayout loginPasswordBox;
    private TextInputEditText loginEmail;
    private TextInputEditText loginPassword;

    private SharedPreferences spSignupSA,spSignupSB;
    private SharedPreferences loginSP;
    private Switch aswitch;
    private Database db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //INTIALISING COMPONENTS
        initComponents();
        //CLEARING SHAREDPREFERENCES OF SIGNUP
        clearData();
        //SETTING LISTENERS
        setListeners();

        //SAMPLE DATA
//        loginEmail.setText("bruce@wayne.com");
//        loginPassword.setText("12345678");
    }



    //Overriding on click listener
    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.btnForgotPassword:
                goToActivity(ForgotPasswordSA.class,'L');
                break;
            case R.id.dontHaveAccountButton:
                goToActivity(SignupSA.class,'L');
                break;
            case R.id.btnLogin:
                if(inputIsValid()){
                    UserModel user = db.getUser(this.email);
                    if(aswitch.isChecked()){
                        //KEEP USER LOGGED IN STATE
                        user.setKeepLoggedIn(1);
                        db.updateUser(user);
                        storeEmail();
                    }else{
                        user.setKeepLoggedIn(0);
                        db.updateUser(user);
                        removeEmail();
                        storeEmail();
                    }
                    goToActivity(Dashboard.class,'L');
                }
                break;
        }
    }



    //FIELD VALIDATION
    private String email, password;
    private boolean inputIsValid(){
        boolean isValid = true;
        try{
            if(!isEmailValid()) isValid = false;
            //SO IF EMAIL EXISTS THEN ONLY CHECK IF PASSWORD IS VALID
            //OTHERWISE JUST IGNORE
            if(isEmailValid()){
                if(!isPasswordValid()) isValid = false;
            }
        }catch (Exception e){
            e.printStackTrace();
            Toast.makeText(this, "Something went wrong!",Toast.LENGTH_SHORT).show();
        }
        return isValid;
    }



    private boolean isEmailValid(){
        boolean isValid = true;
        //EMAIL
        email = loginEmail.getText().toString().trim().toLowerCase();
        if(email.isEmpty() || email.length()<10 || isNumeric(email) || !isEmailValid(email)){
            isValid = false;
            loginEmailBox.setErrorEnabled(true);
            loginEmailBox.setError("Please enter a valid email");
        }else if(!db.userExists(email)){
            isValid = false;
            loginEmailBox.setErrorEnabled(true);
            loginEmailBox.setError("User does not exist");
        }else{
            isValid = true;
            loginEmailBox.setErrorEnabled(false);
            loginEmailBox.setError("");
        }
        return isValid;
    }




    private boolean isPasswordValid(){
        boolean isValid = true;
        //PASSWORD
        password = loginPassword.getText().toString().trim();
        email = loginEmail.getText().toString().trim();
        if(password.isEmpty() || password.length()<6){
            isValid = false;
            loginPasswordBox.setErrorEnabled(true);
            loginPasswordBox.setError("Password must at least be 6 characters");
        }else
            //CHECKING IF USER LOGIN DETAILS MATCH
            if(!db.verifyUser(email,password)){
            isValid = false;
            loginPasswordBox.setErrorEnabled(true);
            loginPasswordBox.setError("Invalid password!");
        }else{
            isValid = true;
            loginPasswordBox.setErrorEnabled(false);
            loginPasswordBox.setError("");
        }
        return isValid;
    }




    private static boolean isNumeric(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch(NumberFormatException e){
            return false;
        }
    }



    private boolean isEmailValid(String email) {
        String regex = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
        return email.matches(regex);
    }



    //Responsible for going to a particular activity
    private void goToActivity(Class c, char animationStyle){
        Intent intent = new Intent(Login.this, c);
        startActivity(intent);
        if(animationStyle=='L'){
            overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
        }else if(animationStyle=='R'){
            overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
        }
        finish();
    }



    //Initializing components
    private void initComponents(){
        btnForgotPassword = findViewById(R.id.btnForgotPassword);
        btnGoToSignup = findViewById(R.id.dontHaveAccountButton);
        btnLogin = findViewById(R.id.btnLogin);
        loginEmail = findViewById(R.id.loginEmailText);
        loginPassword = findViewById(R.id.loginPasswordText);
        loginEmailBox = findViewById(R.id.loginEmailBox);
        loginPasswordBox = findViewById(R.id.loginPasswordBox);
        spSignupSA = getSharedPreferences("SignupSA", Context.MODE_PRIVATE);
        spSignupSB = getSharedPreferences("SignupSB", Context.MODE_PRIVATE);
        loginSP = getSharedPreferences("LoginSP", Context.MODE_PRIVATE);
        db = new Database(Login.this);
        aswitch = findViewById(R.id.swithRem);
    }


    //STORING EMAIL IN SP
    private void storeEmail(){
        SharedPreferences.Editor editor = loginSP.edit();
        editor.putString("userEmail",this.email);
        editor.commit();
    }

    //REMOVE EMAIL IN SP
    public void removeEmail(){
      loginSP.edit().clear().commit();
    }


    //Setting listeners
    private void setListeners(){
        btnForgotPassword.setOnClickListener(this);
        btnGoToSignup.setOnClickListener(this);
        btnLogin.setOnClickListener(this);
    }



    public void clearData(){
        spSignupSA.edit().clear().commit();
        spSignupSB.edit().clear().commit();
    }






}