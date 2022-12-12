package com.example.licenseapp.Messages;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.example.licenseapp.Dashboard;
import com.example.licenseapp.Database;
import com.example.licenseapp.R;
import com.example.licenseapp.Signup.UserModel;

import java.util.ArrayList;
import java.util.List;

public class Messages extends AppCompatActivity {

    private AppCompatImageButton btnMessagesBack;
    private ArrayList<MessagesModel> messagesModels = new ArrayList<>();
    private RecyclerView recyclerView;
    private MessagesAdapter adapter;
    private Database db;
    private SharedPreferences loginSP;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages);

        db = new Database(Messages.this);

        loginSP = getSharedPreferences("LoginSP", Context.MODE_PRIVATE);
        btnMessagesBack = findViewById(R.id.btnMessagesBack);
        btnMessagesBack.setOnClickListener(v->{
            goToActivity(Dashboard.class,'R');
        });

        //SHOW MESSAGE LIST
        setUpMessagesModels();

        //MARK READ ALL MESSAGES
        db.readAllMessages(loginSP.getString("userEmail",""));

        recyclerView = findViewById(R.id.recyclerView);
        adapter = new MessagesAdapter(this,messagesModels);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void setUpMessagesModels(){
        String email = loginSP.getString("userEmail","");

//        String [] messageIDS = {"1","2","3"};
//        String [] messageText = {"Lincense Renewal","Payment Made","In Process"};
//        String [] messageDescription = {
//                "Please wait for further instructions.",
//                "Thank you for your payment of R70.",
//                "Your application is being processed. Please wait for further notice."};
//        for (int x=0; x<messageIDS.length; x++){
//            messagesModels.add(new MessagesModel(messageIDS[x],messageText[x],messageDescription[x]));
//        }

        //GETS LIST OF ALL MESSAGES AND DISPLAYS IN LIST
        List<MessagesModel> messages = db.getMessages(email);
        for(MessagesModel m:messages){
            messagesModels.add(m);
        }
    }

    private void goToActivity(Class c, char animationStyle){
        Intent intent = new Intent(Messages.this, c);
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
        goToActivity(Dashboard.class,'R');
    }
}