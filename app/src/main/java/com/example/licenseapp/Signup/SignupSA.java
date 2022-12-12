package com.example.licenseapp.Signup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.ContextWrapper;
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
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.licenseapp.Database;
import com.example.licenseapp.Login.Login;
import com.example.licenseapp.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.io.File;
import java.io.FileOutputStream;

public class SignupSA extends AppCompatActivity implements View.OnKeyListener{

    private FloatingActionButton btnChangeImageSA;
    private Button btnSignupSA;
    private AppCompatImageButton btnSignupSABack;
    private SharedPreferences sp;
    private ImageView profileImage;
    private Database db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_sa);
        //INITIALIZING ALL FIELDS
        initFields();
        //loadData();
        //CHANGE IMAGE BUTTON CLICKED
        btnChangeImageSA.setOnClickListener(v->{
            //CHANGE IMAGE
            chooseProfilePicture();
        });

        //SHARED PREFRENCE TO STORE USER DETAILS
        sp = getSharedPreferences("SignupSA", Context.MODE_PRIVATE);
        //RETRIEVING DATA
        retrieveData();
        //IF CONTINUE IS CLICKED
        btnSignupSA.setOnClickListener(v->{
            //EXPLAINATION==========================================================================
            /*
            *   So if the input is valid then store the input into sharedpreferences called
            *   SignupSA. And go to the next activity.
            * */
            //VALIDATE FIELDS
            if(inputIsValid()){
                saveImage(profileImage);                        //TEMP IMAGE
                storeData();                                    //TEMP DATA
                goToActivity(SignupSB.class,'L');   //NEXT STAGE
            }
        });
        //KEY LISTENERS (USER DOESNT HAVE TO WAIT TO PRESS BUTTON) VALIDATED DYNAMICALLY
        tietName.setOnKeyListener(this);
        tietSurname.setOnKeyListener(this);
        tietEmail.setOnKeyListener(this);
        tietNumber.setOnKeyListener(this);

        //GO BACK TO LOGIN SCREEN
        btnSignupSABack.setOnClickListener(v->{
            clearData();
            goToActivity(Login.class,'R');
        });
    }

    //INITIALIZING ALL FIELDS
    public void initFields(){
        btnChangeImageSA = findViewById(R.id.btnChangeImage);
        btnSignupSA = findViewById(R.id.btnSignupSA);
        btnSignupSABack = findViewById(R.id.btnSignupSABack);
        tilName = findViewById(R.id.signupNameBox);
        tietName = findViewById(R.id.signupName);
        tilSurname = findViewById(R.id.signupSurnameBox);
        tietSurname = findViewById(R.id.signupSurname);
        tilEmail = findViewById(R.id.signupEmailBox);
        tietEmail = findViewById(R.id.signupEmail);
        tilNumber = findViewById(R.id.signupIDNumberBox);
        tietNumber = findViewById(R.id.signupIDNumber);
        profileImage = findViewById(R.id.profileImage);
        db = new Database(SignupSA.this);
    }

    //GO TO A SPECIFIC ACTIVITY
    private void goToActivity(Class c, char animationStyle){
        Intent intent = new Intent(SignupSA.this, c);
        startActivity(intent);
        if(animationStyle=='L'){
            overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
        }else if(animationStyle=='R'){
            overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
        }
        finish();
    }

    //IF THE USER PRESSES BACK, DELETE TEMP IMAGE AND CLEAR DATA
    @Override
    public void onBackPressed() {
        deleteTempImage();
        clearData();
        goToActivity(Login.class,'R');
    }



    //VALIDATION
    private TextInputLayout tilName, tilSurname, tilEmail, tilNumber;
    private TextInputEditText tietName, tietSurname, tietEmail, tietNumber;
    private String name, surname, email, idNumber, imagePath;

    private boolean inputIsValid(){
        boolean isValid = true;
        try {
            if(!isNameValid()) isValid = false;
            if(!isSurnameValid()) isValid = false;
            if(!isEmailValid()) isValid = false;
            if(!isIDNumberValid()) isValid = false;
        } catch (Exception e){
            e.printStackTrace();
            Toast.makeText(this, "Something went wrong!",Toast.LENGTH_SHORT).show();
        }
        return isValid;
    }

    private boolean isNameValid(){
        boolean isValid = true;
        //NAME
        name = tietName.getText().toString().trim();
        if(name.isEmpty() || name.length()<3 || isNumeric(name)){
            isValid = false;
            tilName.setErrorEnabled(true);
            tilName.setError("Please enter a valid name");
        }else{
            isValid = true;
            tilName.setErrorEnabled(false);
            tilName.setError("");
        }
        return isValid;
    }

    private boolean isSurnameValid(){
        boolean isValid = true;
        //SURNAME
        surname = tietSurname.getText().toString().trim();
        if(surname.isEmpty() || surname.length()<3 || isNumeric(surname)){
            isValid = false;
            tilSurname.setErrorEnabled(true);
            tilSurname.setError("Please enter a valid surname");
        }else{
            isValid = true;
            tilSurname.setErrorEnabled(false);
            tilSurname.setError("");
        }
        return isValid;
    }


    private boolean isEmailValid(){
        boolean isValid = true;
        //EMAIL
        email = tietEmail.getText().toString().trim();
        if(email.isEmpty() || email.length()<10 || isNumeric(email) || !isEmailValid(email)){
            isValid = false;
            tilEmail.setErrorEnabled(true);
            tilEmail.setError("Please enter a valid email");
        }else if(db.userExists(email)){
            isValid = false;
            tilEmail.setErrorEnabled(true);
            tilEmail.setError("User already exists");
        }else{
            isValid = true;
            tilEmail.setErrorEnabled(false);
            tilEmail.setError("");
        }
        return isValid;
    }

    private boolean isIDNumberValid(){
        boolean isValid = true;
        //ID NUMBER
        idNumber = tietNumber.getText().toString().trim();
        if(idNumber.isEmpty() || idNumber.length()!=13 || !isNumeric(idNumber)){
            isValid = false;
            tilNumber.setErrorEnabled(true);
            tilNumber.setError("Please enter a valid ID number");
        }else{
            isValid = true;
            tilNumber.setErrorEnabled(false);
            tilNumber.setError("");
        }
        return isValid;
    }

    private void saveImage(ImageView imageView){
        Drawable drawable = imageView.getDrawable();
        Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
        ContextWrapper cw = new ContextWrapper(getApplicationContext());
        File directory = cw.getDir("profile-temp",Context.MODE_PRIVATE);
        File file = new File(directory,this.email+".jpg");
        this.imagePath = file.getPath();
        if(!file.exists()){
            FileOutputStream fos = null;
            try{
                fos = new FileOutputStream(file);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                fos.flush();
                fos.close();
            }catch (Exception e){e.printStackTrace();}
        }
    }
    private void storeData(){
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("userName",this.name);
        editor.putString("userSurname",this.surname);
        editor.putString("userEmail",this.email);
        editor.putString("userIDNumber",this.idNumber);
        editor.putString("imagePathTemp",this.imagePath);
        editor.commit();
    }

    private void retrieveData(){
        tietName.setText(sp.getString("userName",""));
        tietSurname.setText(sp.getString("userSurname",""));
        tietEmail.setText(sp.getString("userEmail",""));
        tietNumber.setText(sp.getString("userIDNumber",""));
    }

    private void clearData(){
        deleteTempImage();
        sp.edit().clear().commit();
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

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        if(v.getId()==tietName.getId()) isNameValid();
        if(v.getId()==tietSurname.getId()) isSurnameValid();
        if(v.getId()==tietEmail.getId()) isEmailValid();
        if(v.getId()==tietNumber.getId()) isIDNumberValid();
        return false;
    }

    //LOADING DEFAULT DATA FOR TESTING PURPOSE (REPLACE WITH RETRIEVE)
    private void loadData(){
        tietName.setText("Bruce");
        tietSurname.setText("Wayne");
        tietEmail.setText("bruce@wayne.com");
        tietNumber.setText("0000000000000");
    }
    //DELETE TEMP IMAGE IF USER CANCELS SIGNUP
    private void deleteTempImage(){
        String tempImagePath = sp.getString("imagePathTemp","default");
        //Toast.makeText(this,tempImagePath,Toast.LENGTH_SHORT).show();
        File file = new File(tempImagePath);
        if (file.exists()){
            file.delete();
        }
    }

    //USED TO OPEN DIALOG TO CHOOSE PROFILE PICTURE
    private void chooseProfilePicture(){
        AlertDialog.Builder builder = new AlertDialog.Builder(SignupSA.this);
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
                }
                break;
            case 200:
                if(resultCode==RESULT_OK){
                    Bundle bundle = data.getExtras();
                    Bitmap bitmapImage = (Bitmap) bundle.get("data");
                    profileImage.setImageBitmap(bitmapImage);
                }
                break;
        }
    }

    private boolean checkAndRequestPermissions(){
        if(Build.VERSION.SDK_INT>=23){
            int cameraPermission = ActivityCompat.checkSelfPermission(SignupSA.this, Manifest.permission.CAMERA);
            if(cameraPermission== PackageManager.PERMISSION_DENIED){
                ActivityCompat.requestPermissions(SignupSA.this, new String[]{Manifest.permission.CAMERA},20);
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
            Toast.makeText(SignupSA.this, "Permission not granted!",Toast.LENGTH_SHORT).show();
        }
    }
}