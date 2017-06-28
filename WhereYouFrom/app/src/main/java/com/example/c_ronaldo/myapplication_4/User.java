package com.example.c_ronaldo.myapplication_4;

/**
 * Created by C_Ronaldo on 4/11/17.
 */

public class User {
    public String userNickname;
    public String userCountry;
    public String userState;
    public String userCity;
    public String userYear;
    public String userLongitude;
    public String userLatitude;
    public String userId;

    public User(){}

    public User(String nickname, String country, String state, String city, String year, String longitude, String latitude, String id){
        this.userNickname = nickname;
        this.userCountry = country;
        this.userState = state;
        this.userYear = year;
        this.userLongitude = longitude;
        this.userLatitude = latitude;
        this.userId = id;
        this.userCity = city;

    }
}
