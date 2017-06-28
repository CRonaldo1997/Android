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
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class FirebaseUserMapsActivity extends FragmentActivity implements OnMapReadyCallback,GoogleMap.OnMarkerClickListener {

    private GoogleMap mMap;
    String clickedNickName;
    String userNickname;
    String userCountry;
    String userState;
    String userCity;
    Double userLng;
    Double userLat;

    Double getedUserLat;
    Double getedUserLng;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firebase_user_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        //
        clickedNickName = marker.getTitle();
        Log.i("CAO!","clickedNickName is "+clickedNickName);
        chatListActivity.userToChat = clickedNickName;


        //write user history to db
        FirebaseDatabase fireDB = FirebaseDatabase.getInstance();
        DatabaseReference hisUserRef = fireDB.getReference("HistoryUser");
        hisUserRef.child(chatListActivity.currentUser).child(hisUserRef.push().getKey()).setValue(chatListActivity.userToChat);
        hisUserRef.child(chatListActivity.userToChat).child(hisUserRef.push().getKey()).setValue(chatListActivity.currentUser);




//        if(!chatListActivity.chatHisList.contains(clickedNickName)) {
//            chatListActivity.chatHisList.add(clickedNickName);
//            chatListActivity.chatHisAdapter.notifyDataSetChanged();
//        }

        Intent mapUserGoChat = new Intent(this,ChatActivity.class);
        startActivity(mapUserGoChat);
        return false;
    }

    public void getFirebaseUserLocation(){

        ValueEventListener peopleListener = new ValueEventListener() {
            public void onDataChange(DataSnapshot snapshot) {
                Log.i("xixi", "There are " + snapshot.getChildrenCount() + " people");
                for (DataSnapshot postSnapshot: snapshot.getChildren()) {
                    userNickname = postSnapshot.child("nickName").getValue().toString();
                    if(userNickname.equals(chatListActivity.currentUser)){
                        continue;
                    }
                    userCountry = postSnapshot.child("country").getValue().toString();
                    userState = postSnapshot.child("state").getValue().toString();
                    userCity = postSnapshot.child("city").getValue().toString();
                    userLng  = Double.parseDouble(postSnapshot.child("longitude").getValue().toString());
                    userLat = Double.parseDouble(postSnapshot.child("latitude").getValue().toString());
                    Log.i("xixirew","firebase user is "+userNickname+userCountry+userState+userCity+userLat+userLng);

                    if (userLat != 0 && userLng != 0) {
                        Log.i("xixirew", "user has Lng&Lat");
                        showSelectedUserWithLngLat();
                    } else {
                        Log.i("xixirew", "user has no Lng&Lat");
                        showSelectedUserWithoutLngLat();
                    }

                }
            }
            public void onCancelled(DatabaseError firebaseError) {
                Log.i("rew","The read failed: " + firebaseError.getMessage());
            }
        };

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference people = database.getReference("newUser");
        people.addValueEventListener(peopleListener);
    }

    public void showSelectedUserWithLngLat(){
        Log.i("rew","The invaliddd user has lng,lat "+userLat+"lng"+userLng);
        LatLng selectedUserLocation = new LatLng(userLat,userLng);
        mMap.addMarker(new MarkerOptions().position(selectedUserLocation).title(userNickname));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(selectedUserLocation));
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




    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnMarkerClickListener(this);

        // Add a marker in Sydney and move the camera
//        LatLng sydney = new LatLng(-34, 151);
//        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        getFirebaseUserLocation();
    }
}
