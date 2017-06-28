package com.example.c_ronaldo.myapplication_4;

/**
 * Created by C_Ronaldo on 4/13/17.
 */

public class Person {
    String nickname;
    String country;
    String state;
    String city;
    String longitude;
    String latitude;

    Person(){}
    Person(String  nickname,
            String country,
            String state,
            String city,
            String longitude,
            String latitude ){
        this.nickname =  nickname;
        this.country = country;
        this.state = state;
        this.city = city;
        this.longitude = longitude;
        this.latitude = latitude;
    }
}
