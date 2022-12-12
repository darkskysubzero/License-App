package com.example.licenseapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.OpenableColumns;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.licenseapp.Login.Login;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.hbisoft.pickit.PickiT;
import com.hbisoft.pickit.PickiTCallbacks;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.channels.FileChannel;
import java.util.ArrayList;

public class RenewLicense extends AppCompatActivity implements PickiTCallbacks{

    private AppCompatImageButton btnRenewLicenseBack;
    private Button btnFinishRenewLicense;
    private Button proofofaddressBtn, fingerprintsBtn, eyetestBtn, idphotoBtn;
    private TextView proofofaddressfileName, fingerprintsfileName, eyetestfileName, idphotofileName;
    private PickiT pickiT;
    private SharedPreferences renewSP, loginSP;
    private Database db;
    private String  p1="", p2="",p3="",p4="";
    private boolean pBtnClicked = false, fBtnClicked = false, eBtnClicked = false, iBtnClicked = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_renew_license);

        pickiT = new PickiT(this,this,this);
        renewSP = getSharedPreferences("RenewSP", Context.MODE_PRIVATE);
        loginSP = getSharedPreferences("LoginSP", Context.MODE_PRIVATE);
        db = new Database(RenewLicense.this);

        btnRenewLicenseBack = findViewById(R.id.btnRenewLicenseBack);
        btnRenewLicenseBack.setOnClickListener(v->{
            goToActivity(Dashboard.class,'R');
        });


        btnFinishRenewLicense = findViewById(R.id.btnFinishRenewLicense);


        //TEXTVIEWS FOR FILENAMES
        proofofaddressfileName = findViewById(R.id.proofofaddressfileName);
        fingerprintsfileName = findViewById(R.id.fingerprintfileName);
        eyetestfileName = findViewById(R.id.eyetestfileName);
        idphotofileName = findViewById(R.id.idphotoName);

        //BUTTONS TO UPLOAD
        proofofaddressBtn = findViewById(R.id.proofofaddressBtn);
        fingerprintsBtn = findViewById(R.id.fingerprintBtn);
        eyetestBtn = findViewById(R.id.eyetestBtn);
        idphotoBtn = findViewById(R.id.idphotoBtn);


        proofofaddressBtn.setOnClickListener(v->{
            loadFile(1);
            pBtnClicked = true;
        });

        fingerprintsBtn.setOnClickListener(v->{
            loadFile(2);
            fBtnClicked = true;
        });

        eyetestBtn.setOnClickListener(v->{
            loadFile(3);
            eBtnClicked = true;
        });

        idphotoBtn.setOnClickListener(v->{
            loadFile(4);
            iBtnClicked = true;
        });

        //DELETING SHAREDPREFERENCE WHEN SHOWN(deletes old paths)
        clearData();

        btnFinishRenewLicense.setOnClickListener(v->{

            if(db.renewalApplicationExists(loginSP.getString("userEmail",""))){
                Toast.makeText(this,"User renewal application already in process!",Toast.LENGTH_SHORT).show();
            }else{
                if(p1.isEmpty() || p2.isEmpty() || p3.isEmpty() || p4.isEmpty()){
                    Toast.makeText(this, "Please upload all the required files!",Toast.LENGTH_SHORT).show();
                }else{
                    //DISPLAYING SAMPLE PATHS IN CONSOLE
                    System.out.println("path 1 "+p1);
                    System.out.println("path 2 "+p2);
                    System.out.println("path 3 "+p3);
                    System.out.println("path 4 "+p4);

                    //SAVING PATHS TO SHARED PREFERENCES
                    SharedPreferences.Editor editor = renewSP.edit();
                    editor.putString("path1",this.p1);
                    editor.putString("path2",this.p2);
                    editor.putString("path3",this.p3);
                    editor.putString("path4",this.p4);
                    editor.commit();
                    goToActivity(Payment.class,'L',"Renew");
                }
            }

        });
    }


    private void clearData(){
        renewSP.edit().clear().commit();
    }
    public void loadFile(int reqCode){
        Intent i = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        i.addCategory(Intent.CATEGORY_OPENABLE);
        i.setType("application/pdf");
        i.putExtra(Intent.EXTRA_TITLE,"mypdf.pdf");
        startActivityForResult(i,reqCode);
    }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case 1:
                if(resultCode==RESULT_OK){
                    Uri uri = null;
                    if(data!=null){
                        try{
                            uri = data.getData();
                            pickiT.getPath(uri, Build.VERSION.SDK_INT);
                            proofofaddressfileName.setText(getNameFromURI(uri));

                        }catch (Exception e){e.printStackTrace();}
                    }
                }
                break;
            case 2:
                if(resultCode==RESULT_OK){
                    Uri uri = null;
                    if(data!=null){
                        try{
                            uri = data.getData();
                            pickiT.getPath(uri, Build.VERSION.SDK_INT);
                            fingerprintsfileName.setText(getNameFromURI(uri));

                        }catch (Exception e){e.printStackTrace();}
                    }
                }
                break;

            case 3:
                if(resultCode==RESULT_OK){
                    Uri uri = null;
                    if(data!=null){
                        try{
                            uri = data.getData();
                            pickiT.getPath(uri, Build.VERSION.SDK_INT);
                            eyetestfileName.setText(getNameFromURI(uri));
                        }catch (Exception e){e.printStackTrace();}
                    }
                }
                break;

            case 4:
                if(resultCode==RESULT_OK){
                    Uri uri = null;
                    if(data!=null){
                        try{
                            uri = data.getData();
                            pickiT.getPath(uri, Build.VERSION.SDK_INT);
                            idphotofileName.setText(getNameFromURI(uri));
                        }catch (Exception e){e.printStackTrace();}
                    }
                }
                break;
        }
    }

    @SuppressLint("Range")
    public String getNameFromURI(Uri uri) {
        Cursor c = getContentResolver().query(uri, null, null, null, null);
        c.moveToFirst();
        return c.getString(c.getColumnIndex(OpenableColumns.DISPLAY_NAME));
    }





    @Override
    public void onBackPressed() {
        goToActivity(Dashboard.class, 'R');
    }


    @Override
    public void PickiTonUriReturned() {

    }

    @Override
    public void PickiTonStartListener() {

    }

    @Override
    public void PickiTonProgressUpdate(int progress) {

    }

    @Override
    public void PickiTonCompleteListener(String path, boolean wasDriveFile, boolean wasUnknownProvider, boolean wasSuccessful, String Reason) {
        //paths.add(path);
        if(pBtnClicked){
            System.out.println("Path belongs to poa!");
            pBtnClicked = false;
            p1 = path;
        }

        if(fBtnClicked){
            System.out.println("Path belongs to finger!");
            fBtnClicked = false;
            p2 = path;
        }

        if(eBtnClicked){
            System.out.println("Path belongs to eye!");
            eBtnClicked = false;
            p3 = path;
        }

        if(iBtnClicked){
            System.out.println("Path belongs to id!");
            iBtnClicked = false;
            p4 = path;
        }

    }

    @Override
    public void PickiTonMultipleCompleteListener(ArrayList<String> paths, boolean wasSuccessful, String Reason) {

    }
















    private void goToActivity(Class c, char animationStyle){
        Intent intent = new Intent(RenewLicense.this, c);
        startActivity(intent);
        if(animationStyle=='L'){
            overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
        }else if(animationStyle=='R'){
            overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
        }
        finish();
    }

    private void goToActivity(Class c, char animationStyle, String rootClass){
        Intent intent = new Intent(RenewLicense.this, c);
        intent.putExtra("rootClass",rootClass);
        intent.putExtra("price","R70.00");
        startActivity(intent);
        if(animationStyle=='L'){
            overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
        }else if(animationStyle=='R'){
            overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
        }
        finish();
    }


}