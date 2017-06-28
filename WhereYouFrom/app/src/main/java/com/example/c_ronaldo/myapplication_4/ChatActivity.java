package com.example.c_ronaldo.myapplication_4;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.example.c_ronaldo.myapplication_4.chatListActivity.currentUser;
import static com.example.c_ronaldo.myapplication_4.chatListActivity.userToChat;

public class ChatActivity extends AppCompatActivity {
    ListView messageList;
    EditText message;
    List<String> mscontentList = new ArrayList<>();
    ArrayAdapter<String> chatAdapter;

    String messageContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        messageList = (ListView) findViewById(R.id.ctListView);
        message = (EditText) findViewById(R.id.msText);

        chatAdapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,mscontentList);
        messageList.setAdapter(chatAdapter);

        FirebaseDatabase fireDB = FirebaseDatabase.getInstance();
        DatabaseReference DBRef = fireDB.getReference("chatchat/"+currentUser+"/"+userToChat);


        ValueEventListener chatSessionListener = new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot snapshot) {
                mscontentList.clear();
                for (DataSnapshot postSnapshot: snapshot.getChildren()) {
                    if(postSnapshot.getValue()!=null){
                        mscontentList.add(postSnapshot.getValue().toString());
                    }
                    else{
                        //

                    }
                }
                Collections.reverse(mscontentList);
                chatAdapter.notifyDataSetChanged();
            }

            public void onCancelled(DatabaseError firebaseError) {
                Log.i("rew","The read failed: " + firebaseError.toException());
            }

        };
        DBRef.addValueEventListener(chatSessionListener);

    }

    public void sendClicked(View button) {

        messageContent = chatListActivity.currentUser+":  " + message.getText().toString();

        FirebaseDatabase fireDB = FirebaseDatabase.getInstance();
        DatabaseReference DBRef = fireDB.getReference("chatchat");

        DBRef.child(chatListActivity.userToChat).child(chatListActivity.currentUser).child(DBRef.push().getKey()).setValue(messageContent);
        DBRef.child(chatListActivity.currentUser).child(chatListActivity.userToChat).child(DBRef.push().getKey()).setValue(messageContent);
        message.setText("");

    }

}
