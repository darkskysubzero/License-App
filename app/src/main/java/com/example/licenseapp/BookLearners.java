package com.example.licenseapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import com.example.licenseapp.Signup.SignupSB;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;

public class BookLearners extends AppCompatActivity {

    private AppCompatImageButton btnBookLearnersBack;
    private Button btnFinishBookLearners;

    private SharedPreferences loginSP, bookLearnersSP;
    private AutoCompleteTextView actvPlacesAvailable,actvProvince,actvCity, actvDatesAvailable;
    private TextInputLayout tilProvince, tilCity, tilPlaces, tilDates;
    private String province="", city="", placesAvailable="", datesAvailable="";
    private Database db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_learners);
        db = new Database(BookLearners.this);

        //TO GET EMAIL FOR THE USER
        loginSP = getSharedPreferences("LoginSP", Context.MODE_PRIVATE);
        bookLearnersSP = getSharedPreferences("BookLearnersSP",Context.MODE_PRIVATE);

        tilProvince = findViewById(R.id.bookLearnersProvinceBox);
        tilCity = findViewById(R.id.bookLearnersCityBox);
        tilPlaces = findViewById(R.id.bookLearnersPlacesAvailableBox);
        tilDates = findViewById(R.id.bookLearnersDatesAvailableBox);
        actvProvince = findViewById(R.id.bookLearnersProvince);
        actvCity = findViewById(R.id.bookLearnersCity);
        actvPlacesAvailable = findViewById(R.id.bookLearnersPlacesAvailable);
        actvDatesAvailable = findViewById(R.id.bookLearnersDatesAvailable);

        //THIS METHOD ADDS PROVINCES, CITIES, PLACES AVAILABLE
        addProvinceToList();
        addCityToList();

        //IF DATA ALREADY EXISTS LOAD IT BACK UP
        if(bookLearnersSP.contains("province")){
            retrieveData();
            addProvinceToList();
            addCityToList();
            addPlacesAvailable();
            addDatesAvailable();
        }


        btnBookLearnersBack = findViewById(R.id.btnBookLearnersBack);
        btnBookLearnersBack.setOnClickListener(v->{
            clearData();
            goToActivity(Dashboard.class, 'R');
        });

        btnFinishBookLearners = findViewById(R.id.btnFinishBookLearners);
        btnFinishBookLearners.setOnClickListener(v->{
            if(inputIsValid()){

                //AFTER DATA IS VALIDATED DO THIS
                //1 - STORE IT IN SP TO SAVE IN PAYMENT SECTION
                //2 - GO TO NEXT CLASS
                storeData();
                String email = loginSP.getString("userEmail","");
                if(db.learnerApplicationExists(email)){
                    Toast.makeText(getApplicationContext(),"User application is still being processed!",Toast.LENGTH_SHORT).show();
                }else{
                    goToActivity(Payment.class,'L',"BookA");
                }
            }
        });

    }

    private void storeData(){
        SharedPreferences.Editor editor = bookLearnersSP.edit();
        editor.putString("province",this.province);
        editor.putString("city",this.city);
        editor.putString("place",this.placesAvailable);
        editor.putString("date",this.datesAvailable);
        editor.commit();
    }


    private void retrieveData(){
        province = bookLearnersSP.getString("province","");
        city = bookLearnersSP.getString("city","");
        placesAvailable = bookLearnersSP.getString("place","");
        datesAvailable = bookLearnersSP.getString("date","");
        actvProvince.setText(province);
        actvCity.setText(city);
        actvPlacesAvailable.setText(placesAvailable);
        actvDatesAvailable.setText(datesAvailable);
    }

    private void clearData(){
        bookLearnersSP.edit().clear().commit();
    }

    private void goToActivity(Class c, char animationStyle){
        Intent intent = new Intent(BookLearners.this, c);
        startActivity(intent);
        if(animationStyle=='L'){
            overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
        }else if(animationStyle=='R'){
            overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
        }
        finish();
    }

    private void goToActivity(Class c, char animationStyle, String rootClass){
        Intent intent = new Intent(BookLearners.this, c);
        intent.putExtra("rootClass",rootClass);
        intent.putExtra("price","R30.00");
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
        clearData();
        goToActivity(Dashboard.class, 'R');
    }


    private boolean inputIsValid(){
        boolean isValid = true;
        try{
            if(!isProvinceValid()) isValid = false;
            if(!isCityValid()) isValid = false;
            if(!isPlaceValid()) isValid = false;
            if(!isDateValid()) isValid = false;

        }catch (Exception e){
            e.printStackTrace();
            Toast.makeText(this, "Something went wrong!",Toast.LENGTH_SHORT).show();
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

    private boolean isPlaceValid(){
        boolean isValid = true;
        //PLACES AVAILABLE
        if(placesAvailable.isEmpty()){
            isValid = false;
            tilPlaces.setErrorEnabled(true);
            tilPlaces.setError("Please select a Place");
        }else{
            isValid = true;
            tilPlaces.setErrorEnabled(false);
            tilPlaces.setError("");
        }
        return isValid;
    }

    private boolean isDateValid(){
        boolean isValid = true;
        //DATES AVAILABLE
        if(datesAvailable.isEmpty()){
            isValid = false;
            tilDates.setErrorEnabled(true);
            tilDates.setError("Please select a Date");
        }else{
            isValid = true;
            tilDates.setErrorEnabled(false);
            tilDates.setError("");
        }
        return isValid;
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
        AutoCompleteTextView autoCompleteTextView = findViewById(R.id.bookLearnersProvince);
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
        AutoCompleteTextView autoCompleteTextView = findViewById(R.id.bookLearnersCity);
        autoCompleteTextView.setAdapter(adapter);
        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                city = parent.getItemAtPosition(position).toString();
                addPlacesAvailable();
            }
        });
    }

    private void addPlacesAvailable(){
        String [] places = new String[]{"Place 1",
                "Place 2",
                "Place 3"};
        ArrayAdapter adapter = new ArrayAdapter(this,R.layout.drop_down_item,places);
        AutoCompleteTextView autoCompleteTextView = findViewById(R.id.bookLearnersPlacesAvailable);
        autoCompleteTextView.setAdapter(adapter);
        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                placesAvailable = parent.getItemAtPosition(position).toString();
                addDatesAvailable();
            }
        });
    }

    private void addDatesAvailable(){
        String [] places = new String[]{"5 August ",
                "15 September",
                "1 November"};
        ArrayAdapter adapter = new ArrayAdapter(this,R.layout.drop_down_item,places);
        AutoCompleteTextView autoCompleteTextView = findViewById(R.id.bookLearnersDatesAvailable);
        autoCompleteTextView.setAdapter(adapter);
        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                datesAvailable = parent.getItemAtPosition(position).toString();
            }
        });
    }


}