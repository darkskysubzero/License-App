package com.example.licenseapp.Signup;

public class UserModel {
    String image = "";
    String name = "";
    String surname = "";
    String email = "";
    String idNumber = "";
    String gender = "";
    String dob = "";
    String province = "";
    String city = "";
    String address = "";
    String password = "";
    int keepLoggedIn = 0;

    public UserModel(String image, String name, String surname, String email, String idNumber, String gender, String dob, String province, String city, String address, String password, int keepLoggedIn) {
        this.image = image;
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.idNumber = idNumber;
        this.gender = gender;
        this.dob = dob;
        this.province = province;
        this.city = city;
        this.address = address;
        this.password = password;
        this.keepLoggedIn = keepLoggedIn;
    }

    public UserModel(){

    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getIdNumber() {
        return idNumber;
    }

    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getKeepLoggedIn() {
        return keepLoggedIn;
    }

    public void setKeepLoggedIn(int keepLoggedIn) {
        this.keepLoggedIn = keepLoggedIn;
    }
}
