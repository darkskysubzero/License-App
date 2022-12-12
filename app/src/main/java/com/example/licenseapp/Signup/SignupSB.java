package com.example.licenseapp.Signup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.licenseapp.Database;
import com.example.licenseapp.Login.Login;
import com.example.licenseapp.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;

public class SignupSB extends AppCompatActivity {

    private Button btnSignupSB;
    private AppCompatImageButton btnSignupSBBack;
    private TextView haveAccountBtn;
    private SharedPreferences spSignupSA,spSignupSB;
    private Database db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_sb);
        //INIT FIELDS
        initFields();
        addGenderToList();
        addProvinceToList();
        //LOADING SAMPLE DATA
        //loadData();

        //RETRIEVING DATA IF ANY?
        retrieveData();

        //GO BACK TO STAGE A
        btnSignupSBBack.setOnClickListener(v->{
            storeData();
            goToActivity(SignupSA.class,'R');
        });

        //GO BACK TO SIGN IN
        haveAccountBtn.setOnClickListener(v->{
            goToActivity(Login.class,'L');
        });

        //FINISH SIGN UP
        btnSignupSB.setOnClickListener(v->{
            //IF INPUT ALL INPUT IS VALID THEN SIGNUP
            if(inputIsValid()){
                //GETTING ALL VALIDATED DATA TO BE SAVED TO DATABASE
                String imagePath = spSignupSA.getString("imagePathTemp","");
                String userName = spSignupSA.getString("userName","");
                String userSurname = spSignupSA.getString("userSurname","");
                String userEmail = spSignupSA.getString("userEmail","");
                String userIDNumber = spSignupSA.getString("userIDNumber","");
                String userGender = gender;
                String userDOB = year+"/"+month+"/"+day;
                String userProvince = province;
                String userCity = city;
                String userAddress = address;
                String userPassword = password;

                //DISPLAYING DATA FOR TESTING PURPOSES
                String  buffer = imagePath+"\n"+userName+"\n"+userSurname+
                        "\n"+userEmail+"\n"+userIDNumber+"\n"+userGender+"\n"+
                        userDOB+"\n"+userProvince+"\n"+userCity+"\n"+userAddress+"\n"+
                        userPassword;
                System.out.println(buffer);

                //MOVE IMAGE FIRST TO NEW LOCATION
                ContextWrapper cw = new ContextWrapper(getApplicationContext());
                File tempFilePath = new File(imagePath);
                File newFilePath = new File(cw.getDir("profile-images",Context.MODE_PRIVATE),userEmail+".jpg");
                try {
                    moveFileToStorage(tempFilePath,newFilePath);
                    deleteTempImage(imagePath);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                //SAVE DETAILS TO DATABASE (CHECK IF USER EXISTS)
                /*
                    EXPLAINATION: creating a new user model then adding it to the database.
                 */
                UserModel user = new UserModel();
                user.setImage(newFilePath.toString());
                user.setName(userName);
                user.setSurname(userSurname);
                user.setEmail(userEmail.toLowerCase());
                user.setIdNumber(userIDNumber);
                user.setGender(userGender);
                user.setDob(userDOB);
                user.setProvince(userProvince);
                user.setCity(userCity);
                user.setAddress(userAddress);
                user.setPassword(userPassword);
                user.setKeepLoggedIn(0);
                db.addUser(user);

                //DISPLAYING ALL IN CONSOLE
                System.out.println("All user count "+db.getTotalUsers());
                List<UserModel> userList = db.getAllUsers();
                for(UserModel u:userList){
                    System.out.println("Name : "+u.getName()+"\nSurname : "+u.getSurname());
                }

                goToActivity(Login.class,'L');
            }
        });
    }

    public void initFields(){
        btnSignupSBBack = findViewById(R.id.btnSignupSBBack);
        haveAccountBtn = findViewById(R.id.haveAccountButton);
        btnSignupSB = findViewById(R.id.btnSignupSB);
        tilGender = findViewById(R.id.signupGenderBox);
        tilYear = findViewById(R.id.signupYearBox);
        tilMonth = findViewById(R.id.signupMonthBox);
        tilDay = findViewById(R.id.signupDayBox);
        tilProvince = findViewById(R.id.signupProvinceBox);
        tilCity = findViewById(R.id.signupCityBox);
        tilDay = findViewById(R.id.signupDayBox);
        tillAddress = findViewById(R.id.signupAddressBox);
        tillPassword = findViewById(R.id.signupPasswordBox);
        tillCPassword = findViewById(R.id.signupConfirmPasswordBox);
        tietYear = findViewById(R.id.signupYear);
        tietMonth = findViewById(R.id.signupMonth);
        tietDay = findViewById(R.id.signupDay);
        tietAddress = findViewById(R.id.signupAddress);
        tietPassword = findViewById(R.id.signupPassword);
        tietCPassword = findViewById(R.id.signupConfirmPassword);
        actvGender = findViewById(R.id.signupGender);
        actvProvince = findViewById(R.id.signupProvince);
        actvCity = findViewById(R.id.signupCity);
        spSignupSA = getSharedPreferences("SignupSA", Context.MODE_PRIVATE);
        spSignupSB = getSharedPreferences("SignupSB", Context.MODE_PRIVATE);
        db = new Database(SignupSB.this);

    }

    //VALIDATION
    private TextInputLayout tilGender,tilYear,tilMonth,tilDay,
            tilProvince, tilCity,tillAddress, tillPassword,tillCPassword;
    private TextInputEditText tietYear, tietMonth, tietDay, tietAddress,tietPassword,tietCPassword;
    private AutoCompleteTextView actvGender,actvProvince,actvCity;
    private String gender="", province="",city="", address, password, cpassword, year,month,day;

    private boolean inputIsValid(){
        boolean isValid = true;
        try{
            if(!isGenderValid()) isValid = false;
            if(!isYearValid()) isValid = false;
            if(!isMonthValid()) isValid = false;
            if(!isDayValid()) isValid = false;
            if(!isProvinceValid()) isValid = false;
            if(!isCityValid()) isValid = false;
            if(!isAddressValid()) isValid = false;
            if(!isPasswordValid()) isValid = false;
            if(!isCPasswordValid()) isValid = false;

        }catch (Exception e){
            e.printStackTrace();
            Toast.makeText(this, "Something went wrong!",Toast.LENGTH_SHORT).show();
        }
        return isValid;
    }

    private boolean isGenderValid(){
        boolean isValid = true;
        //GENDER
        if(gender.isEmpty()){
            isValid = false;
            tilGender.setErrorEnabled(true);
            tilGender.setError("Please select a gender");
        }else{
            isValid = true;
            tilGender.setErrorEnabled(false);
            tilGender.setError("");
        }
        return isValid;
    }

    private boolean isYearValid(){
        boolean isValid = true;
        //YEAR
        year = tietYear.getText().toString();
        if(year.isEmpty() || year.length()!=4){
            isValid = false;
            tilYear.setErrorEnabled(true);
            tilYear.setError("Example: 2005");
        }else{
            isValid = true;
            tilYear.setErrorEnabled(true);
            tilYear.setError("");
        }
        return isValid;
    }

    private boolean isMonthValid(){
        boolean isValid = true;
        //MONTH
        month = tietMonth.getText().toString();
        if(month.isEmpty() || month.length()!=2  || Integer.parseInt(month)<1
        || Integer.parseInt(month)>12  ){
            isValid = false;
            tilMonth.setErrorEnabled(true);
            tilMonth.setError("Example: 09");
        }else{
            isValid = true;
            tilMonth.setErrorEnabled(true);
            tilMonth.setError("");
        }
        return isValid;
    }
    
    private boolean isDayValid(){
        boolean isValid = true;
        //DAY
        day = tietDay.getText().toString();
        if(day.isEmpty() || day.length()!=2 || Integer.parseInt(day)<1 || Integer.parseInt(day)>31){
            isValid = false;
            tilDay.setErrorEnabled(true);
            tilDay.setError("Example: 30");
        }else{
            isValid = true;
            tilDay.setErrorEnabled(true);
            tilDay.setError("");
        }
        return isValid;
    }


    private boolean isProvinceValid(){
        boolean isValid = true;
        //PROVINCE
        if(province.isEmpty()){
            isValid = false;
            tilProvince.setErrorEnabled(true);
            tilProvince.setError("Please select a Province");
        }else{
            isValid = true;
            tilProvince.setErrorEnabled(false);
            tilProvince.setError("");
        }
        return isValid;
    }

    private boolean isCityValid(){
        boolean isValid = true;
        //CITY
        if(city.isEmpty()){
            isValid = false;
            tilCity.setErrorEnabled(true);
            tilCity.setError("Please select a City");
        }else{
            isValid = true;
            tilCity.setErrorEnabled(false);
            tilCity.setError("");
        }
        return isValid;
    }

    private boolean isAddressValid(){
        boolean isValid = true;
        //ADDRESS
        address = tietAddress.getText().toString().trim();
        if(address.isEmpty() || address.length()<10){
            isValid = false;
            tillAddress.setErrorEnabled(true);
            tillAddress.setError("Please enter a valid address");
        }else{
            isValid = true;
            tillAddress.setErrorEnabled(false);
            tillAddress.setError("");
        }
        return isValid;
    }


    private boolean isPasswordValid(){
        boolean isValid = true;
        //PASSWORD
        password = tietPassword.getText().toString().trim();
        if(password.isEmpty() || password.length()<6){
            isValid = false;
            tillPassword.setErrorEnabled(true);
            tillPassword.setError("Password must atleast be 6 characters");
        }else{
            isValid = true;
            tillPassword.setErrorEnabled(false);
            tillPassword.setError("");
        }
        return isValid;
    }

    private boolean isCPasswordValid(){
        boolean isValid = true;
        //CPASSWORD
        cpassword = tietCPassword.getText().toString().trim();
        if(!cpassword.equals(password)){
            isValid = false;
            tillCPassword.setErrorEnabled(true);
            tillCPassword.setError("Passwords do not match");
        }else{
            isValid = true;
            tillCPassword.setErrorEnabled(false);
            tillCPassword.setError("");
        }
        return isValid;
    }

    private void storeData(){
        SharedPreferences.Editor editor = spSignupSB.edit();
        editor.putString("userGender",this.gender);
        editor.putString("userYear",this.year);
        editor.putString("userMonth",this.month);
        editor.putString("userDay",this.day);
        editor.putString("userProvince",this.province);
        editor.putString("userCity",this.city);
        editor.putString("userAddress",this.address);
        editor.putString("userPassword",this.password);
        editor.commit();
    }

    //LOADING DEFAULT DATA
    private void loadData(){
        actvGender.setText("Male",false);
        gender = "Male";
        tietYear.setText("2009");
        tietMonth.setText("09");
        tietDay.setText("15");
        actvProvince.setText("Gauteng",false);
        province = "Gauteng";
        actvCity.setText("Benoni");
        city = "Benoni";
        tietAddress.setText("Forest Street 17");
        tietPassword.setText("0000000000");
        tietCPassword.setText("0000000000");
    }

    //RETRIEVING DATA JUST IN CASE USER GOES BACK TO PREVIOUS STAGE
    private void retrieveData(){
        actvGender.setText(spSignupSB.getString("userGender",""),false);
        gender = spSignupSB.getString("userGender","");
        tietYear.setText(spSignupSB.getString("userYear",""));
        tietMonth.setText(spSignupSB.getString("userMonth",""));
        tietDay.setText(spSignupSB.getString("userDay",""));
        actvProvince.setText(spSignupSB.getString("userProvince",""),false);
        province = spSignupSB.getString("userProvince","");
        actvCity.setText(spSignupSB.getString("userCity",""));
        city =spSignupSB.getString("userCity","");
        tietAddress.setText(spSignupSB.getString("userAddress",""));
        tietPassword.setText(spSignupSB.getString("userPassword",""));
        tietCPassword.setText(spSignupSB.getString("userPassword",""));
    }



        private void moveFileToStorage(File sourceFile, File destFile) throws IOException {
            if (!destFile.getParentFile().exists())
                destFile.getParentFile().mkdirs();
            if (!destFile.exists()) {
                destFile.createNewFile();
            }
            FileChannel source = null;
            FileChannel destination = null;
            try {
                source = new FileInputStream(sourceFile).getChannel();
                destination = new FileOutputStream(destFile).getChannel();
                destination.transferFrom(source, 0, source.size());
            } finally {
                if (source != null) {
                    source.close();
                }
                if (destination != null) {
                    destination.close();
                }
            }
        }

        private void deleteTempImage(String tempImagePath){
            File file = new File(tempImagePath);
            if (file.exists()){
                file.delete();
            }
        }




    //Responsible for going to a particular activity
    private void goToActivity(Class c, char animationStyle){
        Intent intent = new Intent(SignupSB.this, c);
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
        storeData();
        goToActivity(SignupSA.class,'R');
    }


    private void addGenderToList(){
        String [] genders = new String[]{"Male","Female"};
        ArrayAdapter adapter = new ArrayAdapter(this,R.layout.drop_down_item,genders);
        AutoCompleteTextView autoCompleteTextView = findViewById(R.id.signupGender);
        autoCompleteTextView.setAdapter(adapter);
        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                gender = parent.getItemAtPosition(position).toString();
            }
        });
    }

    private void addProvinceToList(){
        String [] provinces = new String[]{"Gauteng",
                "Mpumalanga",
                "KwaZulu-Natal",
                "North West",
                "Limpopo",
                "Western Cape",
                "Free State",
                "Eastern Cape",
                "Northern Cape"};
        ArrayAdapter adapter = new ArrayAdapter(this,R.layout.drop_down_item,provinces);
        AutoCompleteTextView autoCompleteTextView = findViewById(R.id.signupProvince);
        autoCompleteTextView.setAdapter(adapter);
        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                province = parent.getItemAtPosition(position).toString();
                addCityToList();
            }
        });
    }

    private void addCityToList(){
        ArrayList<String> cities = new ArrayList<String>();
        if(province.equals("Gauteng")){
                cities.add("Benoni");
                cities.add("Boksburg");
                cities.add("Brakpan");
                cities.add("Carletonville");
                cities.add("Germiston");
                cities.add("Johannesburg");
                cities.add("Krugersdorp");
                cities.add("Pretoria");
                cities.add("Randburg");
                cities.add("Randfontein");
                cities.add("Roodepoort");
                cities.add("Soweto");
                cities.add("Springs");
                cities.add("Vanderbijlpark");
                cities.add("Vereeniging");
        }else if(province.equals("Mpumalanga")){
                cities.add("Emalahleni");
                cities.add("Nelspruit");
                cities.add("Secunda");
        }else if(province.equals("KwaZulu-Natal")){
                cities.add("Durban");
                cities.add("Empangeni");
                cities.add("Ladysmith");
                cities.add("Pietermaritzburg");
                cities.add("Pinetown");
                cities.add("Ulundi");
                cities.add("Umlazi");
        }else if(province.equals("North West")){
                cities.add("Klerksdorp");
                cities.add("Mahikeng");
                cities.add("Mmabatho");
                cities.add("Potchefstroom");
                cities.add("Rustenburg");
        }else if(province.equals("Limpopo")){
                cities.add("Giyani");
                cities.add("Lebowakgomo");
                cities.add("Musina");
                cities.add("Phalaborwa");
                cities.add("Polokwane");
                cities.add("Seshego");
                cities.add("Sibasa");
                cities.add("Thabazimbi");
        }else if(province.equals("Western Cape")){
                cities.add("Bellville");
                cities.add("Cape Town");
                cities.add("Constantia");
                cities.add("George");
                cities.add("Hopefield");
                cities.add("Paarl");
                cities.add("Simon’s Town");
                cities.add("Stellenbosch");
                cities.add("Swellendam");
                cities.add("Worcester");
        }else if(province.equals("Free State")){
                cities.add("Bethlehem");
                cities.add("Bloemfontein");
                cities.add("Jagersfontein");
                cities.add("Kroonstad");
                cities.add("Odendaalsrus");
                cities.add("Parys");
                cities.add("Phuthaditjhaba");
                cities.add("Sasolburg");
                cities.add("Virginia");
                cities.add("Welkom");
        }else if(province.equals("Eastern Cape")){
                cities.add("Alice");
                cities.add("Butterworth");
                cities.add("East London");
                cities.add("Graaff-Reinet");
                cities.add("Grahamstown");
                cities.add("King William’s Town");
                cities.add("Mthatha");
                cities.add("Port Elizabeth");
                cities.add("Queenstown");
                cities.add("Uitenhage");
                cities.add("Zwelitsha");
        }else if(province.equals("Northern Cape")){
                cities.add("Kimberley");
                cities.add("Kuruman");
                cities.add("Port Nolloth");
        }

        ArrayAdapter adapter = new ArrayAdapter(this,R.layout.drop_down_item,cities);
        AutoCompleteTextView autoCompleteTextView = findViewById(R.id.signupCity);
        autoCompleteTextView.setAdapter(adapter);
        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                city = parent.getItemAtPosition(position).toString();
            }
        });
    }
}