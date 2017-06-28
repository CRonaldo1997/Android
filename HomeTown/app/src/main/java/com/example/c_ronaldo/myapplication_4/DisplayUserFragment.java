package com.example.c_ronaldo.myapplication_4;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import org.json.JSONException;
import org.json.JSONObject;


public class DisplayUserFragment extends ListFragment implements AdapterView.OnItemClickListener{

//    String example[] = {"a","b","ca"};
    static ArrayAdapter<String> userAdapter;

    public DisplayUserFragment() {
        // Required empty public constructor
    }

    public interface userSelectedListener{
        void userSelected(int id);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_display_user, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        userAdapter = new ArrayAdapter<>(getActivity(),android.R.layout.simple_list_item_1,DisplayUsersActivity.userList);
        setListAdapter(userAdapter);
        getListView().setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Log.i("rew", "position of click " + position );
        Log.i("rew", "id of user " + id );
        DisplayUserFragment.userSelectedListener userListener = (DisplayUserFragment.userSelectedListener)getActivity();
        userListener.userSelected((int)id);
    }
}
