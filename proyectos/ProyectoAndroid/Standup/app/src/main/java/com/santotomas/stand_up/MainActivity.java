package com.santotomas.stand_up;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import adapter.AdapterAviso;
import scripts.Avisos;
import scripts.Data;

public class MainActivity extends AppCompatActivity {

    private GoogleSignInClient mGoogleSignInClient;

    SignInButton btn_inicar;

    //-----------------------------------
    Calendar c = Calendar.getInstance();
    Data d = new Data();
    //-----------------------------------

    private FirebaseAuth mfirebaseAuth;
    private FirebaseAuth.AuthStateListener mauthStateListener;
    public static final int SIGN_IN = 1;

    List<AuthUI.IdpConfig> providers = Arrays.asList(
            new AuthUI.IdpConfig.GoogleBuilder().build()
    );

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser user = mfirebaseAuth.getCurrentUser();
        if(user != null){
            Intent intent = new Intent(getApplicationContext(),HomeActivity.class);
            startActivity(intent);
        }
    }

    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.SplashTheme);
        getSupportActionBar().hide();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        createRequest();

        mfirebaseAuth = FirebaseAuth.getInstance();


        btn_inicar = (SignInButton) findViewById(R.id.btn_google_inicar);
        btn_inicar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { signIn();}
        });

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.txt_actionbar);

    }

    private void createRequest() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, SIGN_IN);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mfirebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mfirebaseAuth.getCurrentUser();
                            FirebaseDatabase database = FirebaseDatabase.getInstance();

                            //----------------------------------------------------------------------
                            DatabaseReference ref_user = database.getReference("Users").child(user.getUid()).child("Avisos");

                            ref_user.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if (snapshot.exists()){
                                        for (DataSnapshot snapshots : snapshot.getChildren()){
                                            Avisos av = snapshots.getValue(Avisos.class);

                                            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
                                            String fecha_act = format.format(c.getTime());

                                            int hora_act = c.get(Calendar.HOUR_OF_DAY);
                                            int min_act = c.get(Calendar.MINUTE);

                                            String hora_compl_act = String.format("%02d:%02d",hora_act,min_act);

                                            if(d.compararFechas(fecha_act,av.getDia()) && d.compararHoras(hora_compl_act,av.getHora())){
                                                System.out.println("Fecha: "+av.getDia()+" ///// Hora: "+av.getHora());

                                                int random = (int)(Math.random()*50+1);
                                                String tag = AlertActivity.GenerateKey();

                                                androidx.work.Data data1 = AlertActivity.GuardarData(av.getTitulo(),av.getMensaje(), random);
                                                androidx.work.Data data2 = AlertActivity.GuardarData(av.getTitulo(),av.getMensajefin(), random);

                                                SimpleDateFormat sdf = new SimpleDateFormat("%02d:%02d");
                                                Calendar calIN = Calendar.getInstance();
                                                Calendar calFIN = Calendar.getInstance();

                                                String[] hora_in = av.getHora().split(":");
                                                int hora1 = Integer.parseInt(hora_in[0]);
                                                int min1 = Integer.parseInt(hora_in[1]);

                                                String[] hora_fin = av.getHorafin().split(":");
                                                int hora2 = Integer.parseInt(hora_fin[0]);
                                                int min2 = Integer.parseInt(hora_fin[1]);

                                                int segundos = 00;

                                                calIN.set(Calendar.HOUR_OF_DAY,hora1);
                                                calIN.set(Calendar.MINUTE,min1);
                                                calIN.set(Calendar.SECOND,segundos);

                                                calFIN.set(Calendar.HOUR_OF_DAY,hora2);
                                                calFIN.set(Calendar.MINUTE,min2);
                                                calFIN.set(Calendar.SECOND,segundos);

                                                long alertin = (calIN.getTimeInMillis() - System.currentTimeMillis());
                                                long alertf = (calFIN.getTimeInMillis() - System.currentTimeMillis());

                                                WorkManagmernoti.GuardarNotificacion((int) alertin,data1,tag);
                                                WorkManagmernoti.GuardarNotificacion((int) alertf,data2,tag);
                                            }

                                        }
                                    }

                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                            //----------------------------------------------------------------------
                            Intent intent = new Intent(getApplicationContext(),HomeActivity.class);
                            startActivity(intent);
                            Toast.makeText(MainActivity.this, "Sesión iniciada", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(MainActivity.this, "Acceso denegado", Toast.LENGTH_SHORT).show();
                        }


                    }
                });
    }

    private void goHome() {
        Intent i = new Intent(this,HomeActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
        }
    }


