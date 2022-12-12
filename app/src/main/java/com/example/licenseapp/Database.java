package com.example.licenseapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.licenseapp.Messages.MessagesModel;
import com.example.licenseapp.Signup.UserModel;

import java.util.ArrayList;
import java.util.List;

public class Database extends SQLiteOpenHelper {

    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "license_db";

    public Database( Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(createUsers());
        db.execSQL(createLearnerApplications());
        db.execSQL(createDriverApplications());
        db.execSQL(createMessages());
        db.execSQL(createPayments());
        db.execSQL(createRenewalApplications());
        db.execSQL(createDelivery());
        db.execSQL(createLocations());
    }

    public String createUsers(){
        return "create table if not exists Users(\n" +
                "\tuserID integer not null primary key autoincrement,\n" +
                "\tuserImage text not null,\n" +
                "\tuserName text not null,\n" +
                "\tuserSurname text not null,\n" +
                "\tuserEmail text not null,\n" +
                "\tuserIDNumber text not null,\n" +
                "\tuserGender text not null,\n" +
                "\tuserDOB text not null,\n" +
                "\tuserProvince text not null,\n" +
                "\tuserCity text not null,\n" +
                "\tuserAddress text not null,\n" +
                "\tuserPassword text not null,\n" +
                "\tkeepLoggedIn integer not null\n" +
                ")";
    }

    public String createLearnerApplications(){
        return "create table if not exists learnerApplications(\n" +
                "\tappID integer not null primary key AUTOINCREMENT,\n" +
                "\tappDate text not null,\n" +
                "\tuserEmail text not null\n" +
                ")";
    }

    public String createDriverApplications(){
        return "create table if not exists driverApplications(\n" +
                "\tappID integer not null primary key AUTOINCREMENT,\n" +
                "\tappDate text not null,\n" +
                "\tuserEmail text not null\n" +
                ")";
    }

    public String createMessages(){
        return "create table if not exists messages(\n" +
                "\tmID integer not null primary key AUTOINCREMENT,\n" +
                "\tmTitle text not null,\n" +
                "\tmDescr text not null,\n" +
                "\tmDate text not null,\n" +
                "\tuserEmail text not null,\n" +
                "\tread integer not null\n" +
                ")";
    }

    public String createPayments(){
        return "create table if not exists payments(\n" +
                "\tpID integer not null primary key AUTOINCREMENT,\n" +
                "\tpType text not null,\n" +
                "\tpAmount decimal not null, \n"+
                "\tpDate text not null,\n" +
                "\tuserEmail text not null\n" +
                ")";
    }

    public String createRenewalApplications(){
        return "create table if not exists renewalApplications(\n" +
                "\tappID integer not null primary key,\n" +
                "\tuserEmail text not null,\n" +
                "\tpoa text not null,\n" +
                "\tfingerprints text not null,\n" +
                "\teyetest text not null,\n" +
                "\tidphoto text not null\n" +
                ")";
    }

    public String createDelivery(){
        return "create table if not exists delivery(\n" +
                "\tdID integer not null primary key,\n" +
                "\tdItem text not null,\n" +
                "\tdAddress text not null,\n" +
                "\tuserEmail text not null\n" +
                ")\n";
    }
    public String createLocations(){
        return "\n" +
                "create table if not exists locations(\n" +
                "\tlID integer not null primary key AUTOINCREMENT,\n" +
                "\tlType text not null,\n" +
                "\tlPro text not null,\n" +
                "\tlCit text not null,\n" +
                "\tlAdd text not null,\n" +
                "\tlDate text not null,\n" +
                "\tuserEmail text not null\n" +
                ")";
    }


    public void addUser(UserModel user){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("userImage",user.getImage());
        cv.put("userName",user.getName());
        cv.put("userSurname",user.getSurname());
        cv.put("userEmail",user.getEmail());
        cv.put("userIDNumber",user.getIdNumber());
        cv.put("userGender",user.getGender());
        cv.put("userDOB",user.getDob());
        cv.put("userProvince",user.getProvince());
        cv.put("userCity",user.getCity());
        cv.put("userAddress",user.getAddress());
        cv.put("userPassword",user.getPassword());
        cv.put("keepLoggedIn",user.getKeepLoggedIn());
        db.insert("Users",null,cv);
        db.close();
    }


    public void addLearnerApplication(String date, String email){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("appDate",date);
        cv.put("userEmail",email);
        db.insert("learnerApplications",null,cv);
        db.close();
    }



    public void addDriverApplication(String date, String email){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("appDate",date);
        cv.put("userEmail",email);
        db.insert("driverApplications",null,cv);
        db.close();
    }

    public void addMessage(String title, String description, String date, String email, int read){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("mTitle",title);
        cv.put("mDescr",description);
        cv.put("mDate",date);
        cv.put("userEmail",email);
        cv.put("read",read);
        db.insert("messages",null,cv);
    }

    public void addPayment(String type, double amount, String date, String email){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("pType",type);
        cv.put("pAmount",amount);
        cv.put("pDate",date);
        cv.put("userEmail",email);
        db.insert("payments",null,cv);
        db.close();
    }

    public void addDelivery(String dItem, String dAddress, String email){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("dItem",dItem);
        cv.put("dAddress",dAddress);
        cv.put("userEmail",email);
        db.insert("delivery",null,cv);
        db.close();
    }

    public void addRenewalApplication(String userEmail, String poa, String fingerprints, String eyetest, String idphoto){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("userEmail",userEmail);
        cv.put("poa",poa);
        cv.put("fingerprints",fingerprints);
        cv.put("eyetest",eyetest);
        cv.put("idphoto",idphoto);
        db.insert("renewalApplications",null,cv);
        db.close();
    }

    public void addLocation(String type, String pro, String cit, String add, String date, String email){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("lType",type);
        cv.put("lPro",pro);
        cv.put("lCit",cit);
        cv.put("lAdd",add);
        cv.put("lDate",date);
        cv.put("userEmail",email);
        db.insert("locations",null,cv);
        db.close();
    }



    public boolean userExists(String email){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select userEmail from Users where userEmail='"+email+"'",
                null);
        if(cursor.getCount()>0){
            return true;
        }
        return false;
    }

    public boolean learnerApplicationExists(String email){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select userEmail from learnerApplications where userEmail='"+email+"'",
                null);
        if(cursor.getCount()>0){
            return true;
        }
        return false;
    }

    public boolean renewalApplicationExists(String email){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select userEmail from renewalApplications where userEmail='"+email+"'",
                null);
        if(cursor.getCount()>0){
            return true;
        }
        return false;
    }

    public boolean verifyUser(String email,String password){
        UserModel user = getUser(email);
        if(user.getPassword().equals(password)){
            return true;
        }
        return false;
    }


    public UserModel getUser(String email){
        SQLiteDatabase db = this.getReadableDatabase();
        email = email.toLowerCase();
        Cursor cursor = db.query("Users",
                new String[]{"userID","userImage","userName","userSurname","userEmail","userIDNumber",
                        "userGender","userDOB","userProvince","userCity","userAddress","userPassword","keepLoggedIn"},
                "userEmail=?",new String[]{email},null,null,null);
        if(cursor!=null){
            cursor.moveToFirst();
        }
        UserModel userModel = new UserModel(cursor.getString(1),
                cursor.getString(2),
                cursor.getString(3),
                cursor.getString(4),
                cursor.getString(5),
                cursor.getString(6),
                cursor.getString(7),
                cursor.getString(8),
                cursor.getString(9),
                cursor.getString(10),
                cursor.getString(11),
                cursor.getInt(12)
        );
        db.close();
        return userModel;
    }



    public List<UserModel> getAllUsers(){
        List<UserModel> userList = new ArrayList<>();
        String query = "select * from Users";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query,null);
        if(cursor.moveToFirst()){
            do{
                UserModel userModel = new UserModel(cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getString(4),
                        cursor.getString(5),
                        cursor.getString(6),
                        cursor.getString(7),
                        cursor.getString(8),
                        cursor.getString(9),
                        cursor.getString(10),
                        cursor.getString(11),
                        cursor.getInt(12)
                );
                userList.add(userModel);
            }while (cursor.moveToNext());
        }
        db.close();
        return userList;
    }

    public List<MessagesModel> getMessages(String email){
        List<MessagesModel> messageList = new ArrayList<>();
        String query = "select * from messages where userEmail='"+email+"'";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query,null);
        if(cursor.moveToFirst()){
            do {
                MessagesModel message = new MessagesModel(cursor.getString(0),
                        cursor.getString(2),
                        cursor.getString(1));
                messageList.add(message);
            }while (cursor.moveToNext());
        }
        db.close();
        return messageList;
    }

    public int updateUser(UserModel user){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("userImage",user.getImage());
        cv.put("userName",user.getName());
        cv.put("userSurname",user.getSurname());
        cv.put("userEmail",user.getEmail());
        cv.put("userIDNumber",user.getIdNumber());
        cv.put("userGender",user.getGender());
        cv.put("userDOB",user.getDob());
        cv.put("userProvince",user.getProvince());
        cv.put("userCity",user.getCity());
        cv.put("userAddress",user.getAddress());
        cv.put("userPassword",user.getPassword());
        cv.put("keepLoggedIn",user.getKeepLoggedIn());
        return db.update("Users",cv, "userEmail=?",new String[]{user.getEmail()});
    }

    public void deleteUser(String userEmail){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("Users","userEmail=?",new String[]{userEmail});
        db.close();
    }

    public int getTotalUsers(){
        String query = "select * from Users";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query,null);
        return cursor.getCount();
    }

    public int getUnreadMessages(String email){
        String query = "select * from messages where userEmail='"+email+"' and read=0";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query,null);
        return cursor.getCount();
    }

    public int readAllMessages(String email){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("read",1);
        return db.update("messages",cv,"userEmail=?",new String[]{email});
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {}
}
