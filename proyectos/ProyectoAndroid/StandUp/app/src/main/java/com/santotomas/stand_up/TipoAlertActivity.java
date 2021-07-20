package com.santotomas.stand_up;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

public class TipoAlertActivity extends AppCompatActivity {

    Button btn_alerta_repetitiva,btn_alerta_especifica,btn_volver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tipo_alert);

        btn_alerta_repetitiva = findViewById(R.id.btn_repetitiva);
        btn_alerta_especifica = findViewById(R.id.btn_especifica);
        btn_volver = findViewById(R.id.btn_volver);


        btn_alerta_repetitiva.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goLayoutRepetitiva();
            }
        });

        btn_alerta_especifica.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goLayoutEspecifica();
            }
        });

        btn_volver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goHome();
            }
        });

    } // ----------------------

    private void goLayoutRepetitiva() {
        Intent i = new Intent(this,AlertRepetitivaActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
    }

    private void goLayoutEspecifica() {
        Intent i = new Intent(this,AlertEspecificaActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
    }

    private void goHome() {
        Intent i = new Intent(this,HomeActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
    }
}