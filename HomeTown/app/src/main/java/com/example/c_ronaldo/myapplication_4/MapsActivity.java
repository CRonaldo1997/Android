package com.example.c_ronaldo.myapplication_4;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,GoogleMap.OnMapLongClickListener {

    private GoogleMap mMap;
    double userLng;
    double userLat;
    double getedUserLat;
    double getedUserLng;
    String userCountry;
    String userState;
    String userCity;
    String userNickname;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Log.i("rew","userID from activity is "+DisplayUsersActivity.selectedUserId);
        if(DisplayUsersActivity.jsonUsers!=null) {
            getSelectedUserInfo(DisplayUsersActivity.selectedUserId);
        }
    }

    public void getSelectedUserInfo(int id){

        try {
            String userStr = DisplayUsersActivity.jsonUsers.getString(id);
            Log.i("rew","selected user is dong"+userStr);
            JSONObject userJson = new JSONObject(userStr);
            String lng = userJson.getString("longitude");
            String lat = userJson.getString("latitude");
            Log.i("rew","get lng is "+lng);
            Log.i("rew","get lat is "+lat);
            userCountry = userJson.getString("country");
            userState = userJson.getString("state");
            userCity = userJson.getString("city");
            userNickname = userJson.getString("nickname");
            if(!lng.equals("0") && !lat.equals("0")){//has lng and lat
                userLng = Double.parseDouble(lng);
                userLat = Double.parseDouble(lat);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnMapLongClickListener(this);
        if(MainActivity.selectLatLngSwitch == true) {
            Toast.makeText(this, "Long click to select Lat&Lng!", Toast.LENGTH_LONG).show();
        }
        if(MainActivity.selectLatLngSwitch == false) {
            if (userLng != 0 && userLat != 0) {
                Log.i("rew", "user has Lng&Lat");
                showSelectedUserWithLngLat();
            } else {
                Log.i("rew", "user has no Lng&Lat");
                showSelectedUserWithoutLngLat();
            }
        }
        if(MainActivity.selectLatLngSwitch == false
                && DisplayUsersActivity.displayAllUserSwitch == true){
          showAllUsersInList();}
    }

    public void showAllUsersInList(){
        Log.i("rew","in showAllUsersInList, the length is "+DisplayUsersActivity.jsonUsers.length());
        for(int i=0;i<DisplayUsersActivity.jsonUsers.length();i++){
            getSelectedUserInfo(i);
            if (userLng != 0 && userLat != 0) {
                Log.i("rew", "user has Lng&Lat");
                showSelectedUserWithLngLat();
            } else {
                Log.i("rew", "user has no Lng&Lat");
                showSelectedUserWithoutLngLat();
            }
        }
    }

    public void showSelectedUserWithoutLngLat(){

            Geocoder locator = new Geocoder(this);
            try {
                String addressStr = userCity+", "+userState+", "+userCountry;
                Log.i("rew","no lng&lat, the address string is "+addressStr);
                List<Address> cityOfUser =
                        locator.getFromLocationName(addressStr,10);
                for (Address userLocation: cityOfUser) {
                    if (userLocation.hasLatitude())
                        Log.i("rew", "Lat iss" + userLocation.getLatitude());
                    getedUserLat = userLocation.getLatitude();
                    if (userLocation.hasLongitude())
                        Log.i("rew", "Long iss" + userLocation.getLongitude());
                    getedUserLng = userLocation.getLongitude();

                }
                if(getedUserLat != 0 && getedUserLng !=0) {
                    LatLng selectedUserLocation = new LatLng(getedUserLat, getedUserLng);
                    mMap.addMarker(new MarkerOptions().position(selectedUserLocation).title(userNickname));
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(selectedUserLocation));
                }else{
                    Toast.makeText(this, "Not valid location!", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception error) {
                Log.e("oops", "Address lookup Error", error);
            }
    }

    public void showSelectedUserWithLngLat(){
        Log.i("rew","The invaliddd user has lng,lat "+userLat+"lng"+userLng);
            LatLng selectedUserLocation = new LatLng(userLat,userLng);
            mMap.addMarker(new MarkerOptions().position(selectedUserLocation).title(userNickname));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(selectedUserLocation));
    }


    public void onMapLongClick(LatLng location){
        Toast.makeText(this, "Lat&Lng selected!", Toast.LENGTH_SHORT).show();
        Log.i("rew", "long pressed Location " + location.latitude + " longitude " + location.longitude );
        Intent passBack = getIntent();
        passBack.putExtra("longitude", location.longitude);
        passBack.putExtra("latitude", location.latitude);
        setResult(RESULT_OK, passBack);
        MainActivity.selectLatLngSwitch = false;
        finish();
    }
    public void onStop(){
        super.onStop();
        MainActivity.selectLatLngSwitch = false;
        DisplayUsersActivity.displayAllUserSwitch = false;
    }


}
