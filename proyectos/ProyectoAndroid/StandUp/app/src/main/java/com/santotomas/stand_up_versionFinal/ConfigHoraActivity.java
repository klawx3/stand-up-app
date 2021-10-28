package com.santotomas.stand_up_versionFinal;

import androidx.appcompat.app.AppCompatActivity;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

public class ConfigHoraActivity extends AppCompatActivity {

    Button btn_volver,btn_seleccionarhora,btn_confirmar;
    TextView txt_horaseleccionada;
    int horaCONF,minCONF;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config_hora);

        btn_volver = findViewById(R.id.btn_CONFIGHORA_volver);
        btn_seleccionarhora = findViewById(R.id.btn_CONFIGHORA_seleccionarhora);
        btn_confirmar = findViewById(R.id.btn_CONFIGHORA_confirmar);

        txt_horaseleccionada = findViewById(R.id.txt_CONFIGHORA_horaselccionada);

        Calendar actual = Calendar.getInstance();//configurar la fecha y la hora actual del dispositivo (los picker)
        Calendar reloj = Calendar.getInstance();

        btn_volver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goHome();
            }
        });

        btn_seleccionarhora.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                horaCONF = actual.get(Calendar.HOUR_OF_DAY);
                minCONF = actual.get(Calendar.MINUTE);
                int seg = 00;

                TimePickerDialog timePickerDialog1 = new TimePickerDialog(v.getContext(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int ho, int mi) {
                        reloj.set(Calendar.HOUR_OF_DAY,ho);
                        reloj.set(Calendar.MINUTE,mi);
                        reloj.set(Calendar.SECOND,seg);

                        txt_horaseleccionada.setText(String.format("%02d:%02d",ho,mi));
                    }
                },horaCONF,minCONF,true);
                timePickerDialog1.show();
            }
        });

        btn_confirmar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ConfigHoraActivity.this, "¡Hora configurada!", Toast.LENGTH_SHORT).show();
                goHome();
                //************************ FALTA GENERAR UNA NOTIFICACIÓN A DIARIO, TAL COMO LO HACÍAMOS AL PRINCIPIO CON UNA CUENTA REGRESIVA
                //************************ QUE NOTIFIQUE AL USUARIO QUE DEBE ENVIAR EL TIEMPO DE TRABAJO DE PIE QUE REALIZÓ
                //************************ EN EL LAYOUT DICE QUE LA HORA POR DEFECTO ES A LAS 11PM

                //************************ PARA PODER GENERAR LA NOTIFICACIÓN DIARIA, SABIENDO QUE HAY QUE BORRAR TODAS LAS ALERTAS ACTIVAS ANTES DE CARGAR LAS DEL DIA SIG
                //************************ SE PUEDE CREAR UN APARTADO EN AVISOS, DE MOMENTO EXISTEN LAS "ESPECIFICAS" Y DIVIDIDA POR DÍA, SE PUEDE CREAR UN CHILD "RECORDATORIO"
                //************************ EL CUAL, CUANDO SE LE HAGA UN INSERT, SE SOBREESCRIBAN LOS DATOS Y AHÍ ALMACENAR LA HORA DE LA NOTIFICACIÓN DE RECORDATORIO
            }
        });

    }//------------------------------------

    private void goHome() {
        Intent i = new Intent(this,HomeActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
    }
}