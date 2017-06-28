package com.example.c_ronaldo.myapplication_4;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class chatListActivity extends AppCompatActivity {
    ListView chatListView;
    static ListView chatHisListView;
    static String userToChat;
    static String currentUser;
    String currentEmail;
    List<String> chatList = new ArrayList<>();
    static List<String> chatHisList = new ArrayList<>();
    ArrayAdapter<String> adapter;
    static ArrayAdapter<String> chatHisAdapter;
    public FirebaseAuth firebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_list);
        chatListView = (ListView) findViewById(R.id.chatListView);
        chatHisListView = (ListView) findViewById(R.id.historyListView);
        firebaseAuth = FirebaseAuth.getInstance();
        currentEmail = firebaseAuth.getCurrentUser().getEmail();

        LoadAllUsersInFirebase();
//        loadHistoryUsers();

        adapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,chatList);
        chatHisAdapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,chatHisList);
        chatListView.setAdapter(adapter);
        chatHisListView.setAdapter(chatHisAdapter);

        chatListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
             String itemValue =  (String)chatListView.getItemAtPosition(position);
                userToChat = itemValue;
                Log.i("rew",userToChat+"clicked");
//
                //write userToChat to firebase

                FirebaseDatabase fireDB = FirebaseDatabase.getInstance();
                DatabaseReference hisUserRef = fireDB.getReference("HistoryUser");
                hisUserRef.child(chatListActivity.currentUser).child(hisUserRef.push().getKey()).setValue(chatListActivity.userToChat);
                hisUserRef.child(chatListActivity.userToChat).child(hisUserRef.push().getKey()).setValue(chatListActivity.currentUser);


                Intent goToChat = new Intent(getApplication(),ChatActivity.class);
                startActivity(goToChat);
            }
        });

        chatHisListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String clickedUser =  (String)chatHisListView.getItemAtPosition(position);
                userToChat = clickedUser;
                Log.i("rew",userToChat+"clicked");
                Intent goToChat = new Intent(getApplication(),ChatActivity.class);
                startActivity(goToChat);
            }
        });
    }

    public void loadHistoryUsers(){
        FirebaseDatabase fireDB = FirebaseDatabase.getInstance();
        DatabaseReference hisRef = fireDB.getReference("HistoryUser/"+currentUser);
        Log.i("jojo","get current user is "+currentUser);
        ValueEventListener historyUserListener = new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot snapshot) {
                chatHisList.clear();
                for (DataSnapshot postSnapshot: snapshot.getChildren()) {
                    Log.i("jojo","get history users are "+postSnapshot.getValue().toString());
                    if(!chatHisList.contains(postSnapshot.getValue().toString())) {
                        chatHisList.add(postSnapshot.getValue().toString());
                    }
                }
                Collections.reverse(chatHisList);
                chatHisAdapter.notifyDataSetChanged();
            }

            public void onCancelled(DatabaseError firebaseError) {
                Log.i("rew","The read failed: " + firebaseError.toException());
            }
        };
        hisRef.addValueEventListener(historyUserListener);
    }



    public void LoadAllUsersInFirebase(){
        ValueEventListener newUserListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                chatList.clear();
                Log.i("rew", "There are " + snapshot.getChildrenCount() + " newUser");
                for (DataSnapshot postSnapshot: snapshot.getChildren()) {
                    String oneEmail = postSnapshot.child("email").getValue().toString();
                    Log.i("xxooxx","the user in firebase is who?"+postSnapshot.getKey()+"email is "+oneEmail);
                    Log.i("xxooxx","currentEmail is "+currentEmail);
                    if(oneEmail.equals(currentEmail)){
                       currentUser = postSnapshot.child("nickName").getValue().toString();
                        Log.i("xxooxx","current user is who? "+currentUser);
                    }
                    chatList.add(postSnapshot.child("nickName").getValue().toString());
                }
                //get current user then go to get chat history!
                loadHistoryUsers();
                chatList.remove(currentUser);
                adapter.notifyDataSetChanged();
            }
            public void onCancelled(DatabaseError firebaseError) {
                Log.i("rew","The read failed: " + firebaseError.toException());
            }
        };
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference newUser = database.getReference("newUser");
        newUser.addValueEventListener(newUserListener);
    }

    public void showMapClicked(View button){
        Intent firebaseUserGo = new Intent(this, FirebaseUserMapsActivity.class);
        startActivity(firebaseUserGo);
    }


    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.logoutmenu, menu);
        return true;
    }

    public void logoutClicked(MenuItem selectedMenu) {
        Log.i("menu","menu clicked!");
        Toast.makeText(getApplication(), "Signed out!", Toast.LENGTH_LONG).show();
        firebaseAuth.signOut();
        finish();
    }

//    @Override
//    protected void onStop() {
//        super.onStop();
//        Toast.makeText(getApplication(), "Signed out!", Toast.LENGTH_LONG).show();
//        firebaseAuth.signOut();
//    }
}
