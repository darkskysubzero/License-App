package com.example.licenseapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.licenseapp.Login.Login;
import com.example.licenseapp.Messages.Messages;
import com.example.licenseapp.Signup.UserModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class Dashboard extends AppCompatActivity implements View.OnClickListener, NavigationBarView.OnItemSelectedListener {

    private CardView tile1, tile2, tile3, tile4, tile5, tile6;
    private BottomNavigationView bottomNavigationView;

    private SharedPreferences loginSP;

    private ImageView profileImage;
    private TextView profileName,profileEmail,messageCount;
    private Database db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        initComponents();
        setListeners();

        /*
            show user details when user logs in
         */
        String emailLoggedIn = loginSP.getString("userEmail","");
        UserModel user = db.getUser(emailLoggedIn);
        int unreadMessages = db.getUnreadMessages(emailLoggedIn);
        profileName.setText(user.getName()+" "+user.getSurname());
        profileEmail.setText(user.getEmail());
        profileImage.setImageURI(Uri.parse(user.getImage()));
        messageCount.setText("Unread messages : "+unreadMessages);



    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tile1:
                goToActivity(RenewLicense.class, 'L');
                break;
            case R.id.tile2:
                goToActivity(BookLearners.class, 'L');
                break;
            case R.id.tile3:
                //goToActivity(BookDriving.class, 'L');
                break;
            case R.id.tile4:
                goToActivity(MyInformation.class,'L');
                break;
            case R.id.tile5:
                goToActivity(Messages.class,'L');
                break;
            case R.id.tile6:
                //Toast.makeText(Dashboard.this, "Tile 6",Toast.LENGTH_SHORT).show();
                break;
        }

    }

    //Initializing components
    private void initComponents(){
        tile1 = findViewById(R.id.tile1);
        tile2 = findViewById(R.id.tile2);
        tile3 = findViewById(R.id.tile3);
        tile4 = findViewById(R.id.tile4);
        tile5 = findViewById(R.id.tile5);
        tile6 = findViewById(R.id.tile6);
        bottomNavigationView = findViewById(R.id.bottomNavigationBar);
        db = new Database(Dashboard.this);
        loginSP = getSharedPreferences("LoginSP", Context.MODE_PRIVATE);
        profileImage = findViewById(R.id.profileImage);
        profileName = findViewById(R.id.profileName);
        profileEmail = findViewById(R.id.profileEmail);
        messageCount = findViewById(R.id.messageCount);
    }

    //Setting listeners
    private void setListeners(){
        tile1.setOnClickListener(this);
        tile2.setOnClickListener(this);
        tile3.setOnClickListener(this);
        tile4.setOnClickListener(this);
        tile5.setOnClickListener(this);
        tile6.setOnClickListener(this);
        bottomNavigationView.setOnItemSelectedListener(this);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==R.id.logoutItem){
            removeEmail();
            goToActivity(Login.class,'R');
        }
        return true;
    }

    //Responsible for going to a particular activity
    private void goToActivity(Class c, char animationStyle){
        Intent intent = new Intent(Dashboard.this, c);
        startActivity(intent);
        if(animationStyle=='L'){
            overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
        }else if(animationStyle=='R'){
            overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
        }
        finish();
    }

    //REMOVE EMAIL IN LOGIN SP (SO WONT BE AUTOMATICALLY LOGGED IN)
    public void removeEmail(){
        loginSP.edit().clear().commit();
    }


}