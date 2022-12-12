package com.example.licenseapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Payment extends AppCompatActivity {

    private AppCompatImageButton btnPaymentBack;
    private Button btnFinishPayment;
    private TextView paymentAmount;
    private Database db;
    private SharedPreferences loginSP, bookLearnersSP, renewSP;
    private String rootClass,price,email,province,city,place,date,messageTitle, messageDescription;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        db = new Database(Payment.this);
        loginSP = getSharedPreferences("LoginSP", Context.MODE_PRIVATE);
        bookLearnersSP = getSharedPreferences("BookLearnersSP",Context.MODE_PRIVATE);
        renewSP = getSharedPreferences("RenewSP", Context.MODE_PRIVATE);
        tielCardNumber = findViewById(R.id.cardNumberBox);
        tietCardNumber = findViewById(R.id.cardNumber);
        tielNameOnCard = findViewById(R.id.nameOnCardBox);
        tietNameOnCard = findViewById(R.id.nameOnCard);
        tielCardYear = findViewById(R.id.cardYearBox);
        tietCardYear = findViewById(R.id.cardYear);
        tielCardMonth = findViewById(R.id.cardMonthBox);
        tietCardMonth = findViewById(R.id.cardMonth);
        tielCardDay = findViewById(R.id.cardDayBox);
        tietCardDay = findViewById(R.id.cardDay);
        tielCardCVV = findViewById(R.id.cvvBox);
        tietCardCVV = findViewById(R.id.cvv);
        tielCardAddress = findViewById(R.id.deliveryAddressBox);
        tietCardAddress = findViewById(R.id.deliveryAddress);

        tietCardYear.setInputType(InputType.TYPE_CLASS_NUMBER);
        tietCardMonth.setInputType(InputType.TYPE_CLASS_NUMBER);
        tielCardDay.setVisibility(View.INVISIBLE);
        tielCardDay.setEnabled(false);

        rCollect = findViewById(R.id.collect);
        rCollect.setOnClickListener(v->{
            tielCardAddress.setErrorEnabled(false);
            tielCardAddress.setEnabled(false);
            tietCardAddress.setEnabled(false);
            choice = "Collect";
        });
        rDeliver = findViewById(R.id.deliver);
        rDeliver.setOnClickListener(v->{
            tielCardAddress.setErrorEnabled(true);
            tielCardAddress.setEnabled(true);
            tietCardAddress.setEnabled(true);
            choice = "Deliver";
        });

        btnPaymentBack = findViewById(R.id.btnPaymentBack);
        btnPaymentBack.setOnClickListener(v->{
            goBack();
        });

        btnFinishPayment = findViewById(R.id.btnFinishPayment);
        paymentAmount = findViewById(R.id.paymentAmount);

        //LOADING SAMPLE DATA
        tietNameOnCard.setText("Bruce Wayne");
        tietCardNumber.setText("0000000000000");
        tietCardMonth.setText("05");
        tietCardYear.setText("24");
        tietCardCVV.setText("848");
        rCollect.setChecked(true);





        //DETAILS
         rootClass = getIntent().getStringExtra("rootClass");
         price = getIntent().getStringExtra("price");
         email = loginSP.getString("userEmail","");
         province = "";
         city = "";
         place = "";
         date = "";
         messageTitle = "";
         messageDescription = "";
         SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
         Date d = new Date();
         String currentDate = formatter.format(d);

        //DETERMINE ROOT CLASS THEN PERFORM ACTIONS ACCORDINGLY
        //IF ROOT CLASS WAS LEARNERS THEN DO THE FOLLOWING
        if(rootClass.equals("BookA")){
            province = bookLearnersSP.getString("province","");
            city = bookLearnersSP.getString("city","");
            place = bookLearnersSP.getString("place","");
            date = bookLearnersSP.getString("date","");
            messageTitle = "New learners application";
            messageDescription = "Thank you for your payment of "+price;
            //TESTING
            System.out.println("Root Class : "+rootClass);
            System.out.println("Price : "+price);
            System.out.println("Email : "+ email);
            System.out.println("Province : "+ province);
            System.out.println("City : "+city);
            System.out.println("Place : "+place);
            System.out.println("Date : "+date);
        }else if(rootClass.equals("Renew")){
            messageTitle = "New renewal application";
            messageDescription = "Thank you for your payment of "+price;
        }else if(rootClass.equals("BookB")){

        }



        paymentAmount.setText(price);
        //IF FINISH PAYMENT BUTTON IS CLICKED
        btnFinishPayment.setOnClickListener(v->{
            //VALIDATE CARD DETAILS
            if(inputIsValid()){
                //ADD TO LEARNER_APPLICATIONS DATABASE
                if(rootClass.equals("BookA")){
                    db.addLearnerApplication(currentDate,email);
                    db.addMessage(messageDescription,messageTitle,currentDate,email,0);
                    db.addPayment("Learner Application",30,currentDate,email);
                    //ADD TO DELIVERY DATABASE IF ENABLED
                    if(choice.equals("Deliver")){
                        db.addDelivery("Learners",address,email);
                    }
                    System.out.println("province "+province);
                    System.out.println("city "+city);
                    System.out.println("place "+place);
                    System.out.println("date "+date);
                    System.out.println("email "+email);

                    db.addLocation("Learners",province,city,place,date,email);
                }else if(rootClass.equals("BookB")){
                    //LEFT THIS OUT DUE TO THIS FUNCTION BEING IDENTICAL TO THE ABOVE ONE
                    //==================================================================
                }else if(rootClass.equals("Renew")){
                    String path1 = renewSP.getString("path1","");
                    String path2 = renewSP.getString("path2","");
                    String path3 = renewSP.getString("path3","");
                    String path4 = renewSP.getString("path4","");
                    String newPaths[] = savePDFFiles(path1,path2,path3,path4);
                    db.addRenewalApplication(email,newPaths[0],newPaths[1],newPaths[2],newPaths[3]);
                    db.addMessage(messageDescription,messageTitle,currentDate,email,0);
                    db.addPayment("Renewal Application",70,currentDate,email);
                }

                goToActivity(OrderReceipt.class,'L');
            }
        });

    }

    private String cardNumber, nameOnCard, cardYear,cardMonth,cardDay,cvv,choice="",address;
    private TextInputLayout tielCardNumber, tielNameOnCard, tielCardYear, tielCardMonth,tielCardDay,
            tielCardCVV,tielCardAddress;
    private TextInputEditText tietCardNumber, tietNameOnCard, tietCardYear, tietCardMonth, tietCardDay,
            tietCardCVV,tietCardAddress;
    private RadioButton rCollect, rDeliver;


    //VALIDATES FIELDS
    private boolean inputIsValid(){
        boolean isValid = true;
        try {
            if(!isCardNumberValid()) isValid = false;
            if(!isCardNameValid()) isValid = false;
            if(!isCardYearValid()) isValid = false;
            if(!isCardMonthValid()) isValid = false;
            if(!isCVVValid()) isValid = false;
            if(!isChoiceValid()) isValid = false;

        } catch (Exception e){
            e.printStackTrace();
            Toast.makeText(this, "Something went wrong!",Toast.LENGTH_SHORT).show();
        }
        return isValid;
    }

    //VALIDATING CARD NUMBER
    private boolean isCardNumberValid(){
        boolean isValid = true;
        cardNumber = tietCardNumber.getText().toString().trim();
        if(cardNumber.equals("") || cardNumber.length()!=13){
            isValid = false;
            tielCardNumber.setErrorEnabled(true);
            tielCardNumber.setError("Invalid card number");
        }else{
            isValid = true;
            tielCardNumber.setErrorEnabled(false);
            tielCardNumber.setError("");
        }

        return isValid;
    }

    //VALIDATING CARD NAME
    private boolean isCardNameValid(){
        boolean isValid = true;
        nameOnCard = tietNameOnCard.getText().toString().trim();
        if(nameOnCard.equals("") || nameOnCard.length()<2 || isNumeric(nameOnCard)){
            isValid = false;
            tielNameOnCard.setErrorEnabled(true);
            tielNameOnCard.setError("Invalid name");
        }else{
            isValid = true;
            tielNameOnCard.setErrorEnabled(false);
            tielNameOnCard.setError("");
        }
        return isValid;
    }



    //VALIDATING CARD YEAR
    private boolean isCardYearValid(){
        boolean isValid = true;
        cardYear = tietCardYear.getText().toString().trim();
        if(cardYear.equals("") || cardYear.length()!=2 || !isNumeric(cardYear)){
            isValid = false;
            tielCardYear.setErrorEnabled(true);
            tielCardYear.setError("Example: 24");
        }else{
            isValid = true;
            tielCardYear.setErrorEnabled(false);
            tielCardYear.setError("");
        }

        return isValid;
    }

    //VALIADTING CARD MONTH
    private boolean isCardMonthValid(){
        boolean isValid = true;
        cardMonth = tietCardMonth.getText().toString().trim();
        if(cardMonth.equals("") || cardMonth.length()!=2 || !isNumeric(cardMonth)){
            isValid = false;
            tielCardMonth.setErrorEnabled(true);
            tielCardMonth.setError("Example: 08");
        }else{
            isValid = true;
            tielCardMonth.setErrorEnabled(false);
            tielCardMonth.setError("");
        }
        return isValid;
    }


    //VALIDATING CARD CVV
    private boolean isCVVValid(){
        boolean isValid = true;
        cvv = tietCardCVV.getText().toString().trim();
        if(cvv.length()!=3 || cvv.equals("")){
            isValid = false;
            tielCardCVV.setErrorEnabled(true);
            tielCardCVV.setError("Example: 383");
        }else{
            isValid = true;
            tielCardCVV.setErrorEnabled(false);
            tielCardCVV.setError("");
        }
        return isValid;
    }

    //VALIDATING CARD CHOICE
    private boolean isChoiceValid(){
        boolean isValid = true;
        if(rCollect.isChecked()){
            choice = "Collect";
        }
        if(rDeliver.isChecked()){
            choice = "Deliver";
            if(!isAddressValid()) isValid = false;
        }
        if(choice.equals("")){
            isValid = false;
            Toast.makeText(Payment.this, "Please select a delivery option!", Toast.LENGTH_SHORT).show();
        }else{
            isValid = true;
        }

        return isValid;
    }

    //VALIDATING CARD ADDRESS
    private boolean isAddressValid(){
        boolean isValid = true;
        address = tietCardAddress.getText().toString().trim();
        if(address.isEmpty()){
            isValid = false;
            tielCardAddress.setErrorEnabled(true);
            tielCardAddress.setError("Enter a valid address");
        }else{
            isValid = true;
            tielCardAddress.setErrorEnabled(false);
            tielCardAddress.setError("");
        }
        return isValid;
    }


    private void goToActivity(Class c, char animationStyle){
        Intent intent = new Intent(Payment.this, c);

        String rootClass = getIntent().getStringExtra("rootClass");
        if(rootClass.equals("Renew")){
            String renewResult = "";
            if(choice.equals("Deliver")){
                renewResult = "Your license will delivered to you.\n The processing time is 2-3 days";
            }else{
                renewResult = "You will be notified on where to collect your license!";
            }
            String renewInstructions = "Please wait for further instructions";
            intent.putExtra("result",renewResult);
            intent.putExtra("instruction",renewInstructions);
        }else{
            String bookResult = "You have successfully booked your spot!";
            String bookInstructions = "You will be notified soon with more details.";
            intent.putExtra("result",bookResult);
            intent.putExtra("instruction",bookInstructions);
        }

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
        goBack();
    }

    private void goBack(){
        String rootClass = getIntent().getStringExtra("rootClass");
        Toast.makeText(Payment.this,rootClass,Toast.LENGTH_SHORT).show();
        if(rootClass.equals("Renew")){
            goToActivity(RenewLicense.class, 'R');
        }else if(rootClass.equals("BookA")){
            goToActivity(BookLearners.class, 'R');
        }else if(rootClass.equals("BookB")){
            goToActivity(BookDriving.class, 'R');
        }
    }

    public static boolean isNumeric(String strNum) {
        if (strNum == null) {
            return false;
        }
        try {
            double d = Double.parseDouble(strNum);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }



    public String[] savePDFFiles(String p1, String p2, String p3, String p4){
        String p[] = new String[4];
        String email = loginSP.getString("userEmail","");
        ContextWrapper cw = new ContextWrapper(getApplicationContext());
        //SOURCES
        File proofofaddress = new File(p1);
        File fingerprints = new File(p2);
        File eyetest = new File(p3);
        File idphoto = new File(p4);

        //DESTINATIONS
        File d1 = new File(cw.getDir("user-uploads",Context.MODE_PRIVATE),email+"-poa.pdf");
        File d2 = new File(cw.getDir("user-uploads",Context.MODE_PRIVATE),email+"-fin.pdf");
        File d3 = new File(cw.getDir("user-uploads",Context.MODE_PRIVATE),email+"-eye.pdf");
        File d4 = new File(cw.getDir("user-uploads",Context.MODE_PRIVATE),email+"-idp.pdf");
        p[0] = d1.getPath();
        p[1] = d2.getPath();
        p[2] = d3.getPath();
        p[3] = d4.getPath();
        try {
            copyFile(proofofaddress,d1);
            copyFile(fingerprints,d2);
            copyFile(eyetest,d3);
            copyFile(idphoto,d4);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return p;
    }


    //COPIES THE FILE TO NEW DESTINATION
    public  void copyFile(File sourceFile, File destFile) throws IOException {
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

}