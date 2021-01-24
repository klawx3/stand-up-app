package com.example.notificacionesexample;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    Button personal,global;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        personal = findViewById(R.id.btn_Personal);
        global = findViewById(R.id.btn_global);

        FirebaseMessaging.getInstance().subscribeToTopic("enviarATodos").addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(MainActivity.this, "Suiscrito a Enviar a todos", Toast.LENGTH_SHORT).show();
            }
        });

        personal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                llamarPersonal();
                Toast.makeText(MainActivity.this,"El boton funciona",Toast.LENGTH_SHORT).show();
            }
        });

        global.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                llamarGlobal();
            }
        });


    }

    private void llamarGlobal() {
        RequestQueue myrequest = Volley.newRequestQueue(getApplicationContext());
        JSONObject json = new JSONObject();

        try {



            json.put("to","/topics/"+"enviarATodos");
            JSONObject notificacion = new JSONObject();
            notificacion.put("Titulo","Ponte de Pie");
            notificacion.put("Detalle","Es hora de trabajar de pie");

            json.put("data",notificacion);

            String URL="https://fcm.googleapis.com/fcm/send";

            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST,URL,json,null,null){
                @Override
                public Map<String, String> getHeaders() {
                    Map<String, String> header = new HashMap<>();

                    header.put("content-type","application/json");
                    header.put("authorization","key=AAAA0SS_CR0:APA91bGtwkbPLjdchmw5I9m7FS1ESYY74K7BTdlLwuYcW8Y6ahGJ3Fl6d_rf7iljsgyqBH6-GFYtv2XKgpN1gbuDf9KkDkSL6BUtJILliqEX7ZwiZtF82QIMUNkbZ0-ambFfVPwEGUFe"); //la key es la del proyecto de firebase
                    return  header;

                }
            };

            myrequest.add(request);

        }catch (JSONException e){
            e.printStackTrace();
        }

    }

    private void llamarPersonal()   {
        RequestQueue myrequest = Volley.newRequestQueue(getApplicationContext());
        JSONObject json = new JSONObject();

        try {
            String token = ""; //esta en la parte de database de firebase del proyecto le llegara a todos los usuarios con ese token


            json.put("to",token);
            JSONObject notificacion = new JSONObject();
            notificacion.put("Titulo","Ponte de Pie");
            notificacion.put("Detalle","Es hora de trabajar de pie");

            json.put("data",notificacion);

            String URL="https://fcm.googleapis.com/fcm/send";

            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST,URL,json,null,null){
                @Override
                public Map<String, String> getHeaders() {
                    Map<String, String> header = new HashMap<>();

                    header.put("content-type","application/json");
                    header.put("authorization","key=AAAA0SS_CR0:APA91bGtwkbPLjdchmw5I9m7FS1ESYY74K7BTdlLwuYcW8Y6ahGJ3Fl6d_rf7iljsgyqBH6-GFYtv2XKgpN1gbuDf9KkDkSL6BUtJILliqEX7ZwiZtF82QIMUNkbZ0-ambFfVPwEGUFe"); //la key es la del proyecto de firebase
                    return  header;

                }
            };

            myrequest.add(request);

        }catch (JSONException e){
            e.printStackTrace();
        }

    }

}