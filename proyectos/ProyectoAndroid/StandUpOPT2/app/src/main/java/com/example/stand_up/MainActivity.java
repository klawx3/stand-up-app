package com.example.stand_up;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mfirebaseAuth;
    private  FirebaseAuth.AuthStateListener mautListener;
    public static final int SIGN_IN =1;

    List<AuthUI.IdpConfig> providers = Collections.singletonList(
            new AuthUI.IdpConfig.GoogleBuilder().build()
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        setTheme(R.style.SpashTheme);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mfirebaseAuth = FirebaseAuth.getInstance();

        mautListener = firebaseAuth -> {
            FirebaseUser use = firebaseAuth.getCurrentUser();
            if (use != null){
                home();
            }else {
                startActivityForResult(
                        AuthUI.getInstance()
                                .createSignInIntentBuilder()
                                .setAvailableProviders(providers)
                                .setIsSmartLockEnabled(false)
                                .build(),SIGN_IN
                );
            }
        };

    }//fin

    @Override
    protected void onResume() {
        super.onResume();
        mfirebaseAuth.addAuthStateListener(mautListener);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mfirebaseAuth.removeAuthStateListener(mautListener);
    }

    private void home() {
        Intent i = new Intent(this,homeActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
    }
}