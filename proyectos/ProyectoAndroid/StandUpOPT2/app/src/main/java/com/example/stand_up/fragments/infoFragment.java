package com.example.stand_up.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.stand_up.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class infoFragment extends Fragment {

    public infoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();


        View view = inflater.inflate(R.layout.fragment_info, container, false);

        TextView tc_user = view.findViewById(R.id.tv_user);
        ImageView img_user = view.findViewById(R.id.img_user);

        tc_user.setText(user.getDisplayName());
        Glide.with(this).load(user.getPhotoUrl()).into(img_user);


        return view;
    }
}