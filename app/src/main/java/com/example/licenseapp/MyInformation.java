package com.example.licenseapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.licenseapp.Signup.SignupSA;
import com.example.licenseapp.Signup.UserModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.io.File;
import java.io.FileOutputStream;

public class MyInformation extends AppCompatActivity {

    private AppCompatImageButton btnInformationBack;
    private FloatingActionButton btnChangeProfileImage;
    private TextInputEditText name,surname,password,address;
    private ImageView profileImage;
    private Database db;
    private SharedPreferences loginSP;
    private String  newImage,email;
    private AlertDialog.Builder builder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_information);

        db = new Database(MyInformation.this);

        btnInformationBack = findViewById(R.id.btnInformationBack);
        btnChangeProfileImage = findViewById(R.id.btnChangeImage);
        profileImage = findViewById(R.id.newProfileImage);
        name = findViewById(R.id.infoName);
        surname = findViewById(R.id.infoSurname);
        password = findViewById(R.id.infoPassword);
        address = findViewById(R.id.infoAddress);
        loginSP = getSharedPreferences("LoginSP", Context.MODE_PRIVATE);
        newImage = "";

        //SHOWING USER DATA
        loadData();

        //UPDATING DATA
        btnChangeProfileImage.setOnClickListener(v->{
            chooseProfilePicture();
        });

        name.setOnClickListener(v->{
            newInfo("Name");
        });
        surname.setOnClickListener(v->{
            newInfo("Surname");
        });
        password.setOnClickListener(v->{
            newInfo("Password");
        });
        address.setOnClickListener(v->{
            newInfo("Address");
        });

        btnInformationBack.setOnClickListener(v->{
            goToActivity(Dashboard.class,'R');
        });
    }

    private void goToActivity(Class c, char animationStyle){
        Intent intent = new Intent(MyInformation.this, c);
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

    //LOADING USER DATA FIRST
    private void loadData(){
        String email = loginSP.getString("userEmail","");
        UserModel user = db.getUser(email);
        profileImage.setImageURI(Uri.parse(user.getImage()));
        name.setText(user.getName());
        surname.setText(user.getSurname());
        password.setText(user.getPassword());
        address.setText(user.getAddress());
        this.email = email;
    }

    private void newInfo(String type){
        String title = "";
        String message = "";

        if(type.equals("Name")){
            title = "Change name";
            message = "Please enter your new name";
        }

        if(type.equals("Surname")){
            title = "Change Surname";
            message = "Please enter your new surname";
        }

        if(type.equals("Password")){
            title = "Change Password";
            message = "Please enter your new password";
        }

        if (type.equals("Address")){
            title = "Change Address";
            message = "Please enter your new address";
        }

        builder = new AlertDialog.Builder(this);
        builder.setTitle(title);
        builder.setMessage(message);
        EditText edit = new EditText(this);
        builder.setView(edit);
        builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String newValue= edit.getText().toString();
                boolean isValid = true;

                if(type.equals("Name") && (newValue.equals("")||newValue.length()<2)){
                    isValid = false;
                    Toast.makeText(getApplicationContext(),"Please enter a valid name!",Toast.LENGTH_SHORT).show();
                }if(type.equals("Surname") && (newValue.equals("")||newValue.length()<2)) {
                    isValid = false;
                    Toast.makeText(getApplicationContext(),"Please enter a valid surname!",Toast.LENGTH_SHORT).show();
                }if(type.equals("Password")&& (newValue.equals("") || newValue.length()<6)){
                    isValid = false;
                    Toast.makeText(getApplicationContext(),"Please enter a valid password !",Toast.LENGTH_SHORT).show();
                }if(type.equals("Address")&& (newValue.equals("") || newValue.length()<10)){
                    isValid = false;
                    Toast.makeText(getApplicationContext(),"Please enter a valid address!",Toast.LENGTH_SHORT).show();
                }

                if(isValid){
                    UserModel user = db.getUser(email);
                    if(type.equals("Name")){
                        user.setName(newValue);
                        db.updateUser(user);
                    }
                    if(type.equals("Surname")){
                        user.setSurname(newValue);
                        db.updateUser(user);
                    }
                    if(type.equals("Password")){
                        user.setPassword(newValue);
                        db.updateUser(user);
                    }
                    if(type.equals("Address")){
                        user.setAddress(newValue);
                        db.updateUser(user);
                    }
                    loadData();
                }
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }



    //USED TO OPEN DIALOG TO CHOOSE PROFILE PICTURE
    private void chooseProfilePicture(){
        AlertDialog.Builder builder = new AlertDialog.Builder(MyInformation.this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.alert_dialog_profile_picture,null);
        builder.setCancelable(true);
        builder.setView(dialogView);

        AlertDialog alertDialogProfilePicture = builder.create();
        alertDialogProfilePicture.show();

        //setting listeners to each button(image)
        ImageView camera = dialogView.findViewById(R.id.cameraPic);
        ImageView gallery = dialogView.findViewById(R.id.galleryPic);
        camera.setOnClickListener(v->{
            //open camera
            if(checkAndRequestPermissions()){
                takePictureFromCamera();
                alertDialogProfilePicture.cancel();
            }
        });

        gallery.setOnClickListener(v->{
            //open gallery
            if(checkAndRequestPermissions()){
                takePictureFromGallery();
                alertDialogProfilePicture.cancel();
            }
        });
    }

    private void takePictureFromGallery(){
        Intent pickPhoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(pickPhoto,100); //request code can be anything
    }

    private void takePictureFromCamera(){
        Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if(takePicture.resolveActivity(getPackageManager())!=null){
            startActivityForResult(takePicture,200);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode){
            case 100:
                if(resultCode==RESULT_OK){
                    Uri selectedImageURI = data.getData();
                    profileImage.setImageURI(selectedImageURI);
                    newImage = saveImage(profileImage);

                    //updating user image path in database
                    UserModel user = db.getUser(this.email);
                    user.setImage(newImage);
                    db.updateUser(user);
                    System.out.println("new image :" +newImage);
                }
                break;
            case 200:
                if(resultCode==RESULT_OK){
                    Bundle bundle = data.getExtras();
                    Bitmap bitmapImage = (Bitmap) bundle.get("data");
                    profileImage.setImageBitmap(bitmapImage);
                    newImage = saveImage(profileImage);

                    //updating user image path in database
                    UserModel user = db.getUser(this.email);
                    user.setImage(newImage);
                    db.updateUser(user);
                    System.out.println("new image :" +newImage);
                }
                break;
        }
    }

    private boolean checkAndRequestPermissions(){
        if(Build.VERSION.SDK_INT>=23){
            int cameraPermission = ActivityCompat.checkSelfPermission(MyInformation.this, Manifest.permission.CAMERA);
            if(cameraPermission== PackageManager.PERMISSION_DENIED){
                ActivityCompat.requestPermissions(MyInformation.this, new String[]{Manifest.permission.CAMERA},20);
                return false;
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==20 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
            takePictureFromCamera();
        }else{
            Toast.makeText(MyInformation.this, "Permission not granted!",Toast.LENGTH_SHORT).show();
        }
    }

    private String saveImage(ImageView imageView){
        Drawable drawable = imageView.getDrawable();
        Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
        ContextWrapper cw = new ContextWrapper(getApplicationContext());
        File directory = cw.getDir("profile-images",Context.MODE_PRIVATE);
        File file = new File(directory,this.email+".jpg");
        //OVERWRITES EXISTING ONES
            FileOutputStream fos = null;
            try{
                fos = new FileOutputStream(file);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                fos.flush();
                fos.close();
            }catch (Exception e){e.printStackTrace();}

        return file.getPath();
    }
}