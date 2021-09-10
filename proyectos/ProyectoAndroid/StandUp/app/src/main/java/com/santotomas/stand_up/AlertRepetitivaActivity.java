package com.santotomas.stand_up;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.UUID;

import scripts.Data;

public class AlertRepetitivaActivity extends AppCompatActivity {

    TextView n_inicio, n_fin;
    Button btn_volver,btn_confirmar,btn_seleccionHoraIN,btn_seleccionHoraFin;
    EditText msje_inicio,msje_fin,txtAviso;
    int hora,minuto,dia,mes,anio;

    CheckBox ch_Lunes,ch_Martes,ch_Miercoles,ch_Jueves,ch_Viernes,ch_Sabado,ch_Domingo;

    private PendingIntent pendingIntent;
    private final static String CHANNEL_ID = "NOTIFICACION";
    private final static int NOTIFICACION_ID = 0;

    @SuppressLint({"WrongConstant", "WrongViewCast"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alert_repetitiva);

        Data data = new Data();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseDatabase database = FirebaseDatabase.getInstance();

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.txt_actionbar);

        n_inicio = findViewById(R.id.tv_horain);
        n_fin = findViewById(R.id.tv_horafin);

        txtAviso = findViewById(R.id.txtTitulo);
        msje_inicio = findViewById(R.id.txt_msje_inicio);
        msje_fin = findViewById(R.id.txt_msje_fin);

        btn_confirmar = findViewById(R.id.btn_confirmar);
        btn_seleccionHoraIN =  findViewById(R.id.btn_selechorain);
        btn_seleccionHoraFin =  findViewById(R.id.btn_selechorafin);
        btn_volver = findViewById(R.id.btn_volver);

        ch_Lunes = findViewById(R.id.checkBoxLunes);
        ch_Martes = findViewById(R.id.checkBoxMartes);
        ch_Miercoles = findViewById(R.id.checkBoxMiercoles);
        ch_Jueves = findViewById(R.id.checkBoxJueves);
        ch_Viernes = findViewById(R.id.checkBoxViernes);
        ch_Sabado = findViewById(R.id.checkBoxSabado);
        ch_Domingo = findViewById(R.id.checkBoxDomingo);

        Calendar actual = Calendar.getInstance();//configurar la fecha y la hora actual del dispositivo (los picker)
        Calendar calendar = Calendar.getInstance();//selecionar la fecha
        Calendar calendar_fn = Calendar.getInstance();//

        btn_seleccionHoraFin.setEnabled(false);
        btn_seleccionHoraIN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hora = actual.get(Calendar.HOUR_OF_DAY);
                minuto = actual.get(Calendar.MINUTE);
                int segundero_inicio = 00;
                TimePickerDialog timePickerDialog = new TimePickerDialog(v.getContext(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int h, int m) {
                        calendar.set(Calendar.HOUR_OF_DAY,h);
                        calendar.set(Calendar.MINUTE,m);
                        calendar.set(Calendar.SECOND,segundero_inicio);

                        n_inicio.setText(String.format("%02d:%02d",h,m));
                    }
                },hora,minuto,true);
                timePickerDialog.show();
                btn_seleccionHoraFin.setEnabled(true);
            }
        });

        btn_seleccionHoraFin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hora = actual.get(Calendar.HOUR_OF_DAY);
                minuto = actual.get(Calendar.MINUTE);
                int seg = 00;

                TimePickerDialog timePickerDialog1 = new TimePickerDialog(v.getContext(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int ho, int mi) {
                        calendar_fn.set(Calendar.HOUR_OF_DAY,ho);
                        calendar_fn.set(Calendar.MINUTE,mi);
                        calendar_fn.set(Calendar.SECOND,seg);

                        n_fin.setText(String.format("%02d:%02d",ho,mi));
                    }
                },hora,minuto,true);
                timePickerDialog1.show();
            }
        });

        btn_volver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goTipoAlert();
            }
        });


        btn_confirmar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(TextUtils.isEmpty(n_fin.getText())){
                    Toast.makeText(AlertRepetitivaActivity.this, "Ingrese hora de fin válida.", Toast.LENGTH_SHORT).show();
                }else{
                    if(data.compararHoras(n_inicio.getText().toString(),n_fin.getText().toString())){
                        //Se valida que existan días seleccionados para generar notificación. //
                        if(ch_Lunes.isChecked() || ch_Martes.isChecked() || ch_Miercoles.isChecked() ||ch_Jueves.isChecked() ||
                                ch_Viernes.isChecked() || ch_Sabado.isChecked() || ch_Domingo.isChecked()){

                            if (TextUtils.isEmpty(msje_inicio.getText().toString())){
                                msje_inicio.setText("¡Ponte de pie!");
                            }

                            if(TextUtils.isEmpty(msje_fin.getText().toString())){
                                msje_fin.setText("¡Ya puedes sentarte!");
                            }

                            if (TextUtils.isEmpty(txtAviso.getText().toString())){
                                txtAviso.setText("STAND UP APP");
                            }

                            int random = (int)(Math.random()*50+1);

                            String tag = GenerateKey();
                            long alertin = (calendar.getTimeInMillis() - System.currentTimeMillis());
                            long alertf = (calendar_fn.getTimeInMillis() - System.currentTimeMillis());

                            if(ch_Lunes.isChecked()){
                                data.insertAlertas(n_inicio.getText(),n_fin.getText(),txtAviso.getText().toString(),
                                        msje_inicio.getText().toString(),msje_fin.getText().toString(),"Lunes");
                            }
                            if(ch_Martes.isChecked()){
                                data.insertAlertas(n_inicio.getText(),n_fin.getText(),txtAviso.getText().toString(),
                                        msje_inicio.getText().toString(),msje_fin.getText().toString(),"Martes");
                            }
                            if(ch_Miercoles.isChecked()){
                                data.insertAlertas(n_inicio.getText(),n_fin.getText(),txtAviso.getText().toString(),
                                        msje_inicio.getText().toString(),msje_fin.getText().toString(),"Miercoles");
                            }
                            if(ch_Jueves.isChecked()){
                                data.insertAlertas(n_inicio.getText(),n_fin.getText(),txtAviso.getText().toString(),
                                        msje_inicio.getText().toString(),msje_fin.getText().toString(),"Jueves");
                            }
                            if(ch_Viernes.isChecked()){
                                data.insertAlertas(n_inicio.getText(),n_fin.getText(),txtAviso.getText().toString(),
                                        msje_inicio.getText().toString(),msje_fin.getText().toString(),"Viernes");
                            }
                            if(ch_Sabado.isChecked()){
                                data.insertAlertas(n_inicio.getText(),n_fin.getText(),txtAviso.getText().toString(),
                                        msje_inicio.getText().toString(),msje_fin.getText().toString(),"Sabado");
                            }
                            if(ch_Domingo.isChecked()){
                                data.insertAlertas(n_inicio.getText(),n_fin.getText(),txtAviso.getText().toString(),
                                        msje_inicio.getText().toString(),msje_fin.getText().toString(),"Domingo");
                            }

                            androidx.work.Data data1 = GuardarData(txtAviso.getText().toString(),msje_inicio.getText().toString(), random);
                            androidx.work.Data data2 = GuardarData(txtAviso.getText().toString(),msje_fin.getText().toString(), random);

                            WorkManagmernoti.GuardarNotificacion((int) alertin,data1,tag);
                            WorkManagmernoti.GuardarNotificacion((int) alertf,data2,tag);

                            Toast.makeText(AlertRepetitivaActivity.this, "Aviso creado.", Toast.LENGTH_SHORT).show();
                            goHome();
                        }else{
                            Toast.makeText(AlertRepetitivaActivity.this, "POR FAVOR SELECCIONE DIA(S) A NOTIFICAR", Toast.LENGTH_SHORT).show();
                        }
                    }else
                    {
                        Toast.makeText(AlertRepetitivaActivity.this, "Asegurese que la hora final es posterior a la inicial.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    } // ---------------------------

    private void goTipoAlert(){
        Intent i = new Intent(this, TipoAlertActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
    }

    private void goHome() {
        Intent i = new Intent(this,HomeActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
    }

    public static String GenerateKey(){
        return UUID.randomUUID().toString();
    }

    public static androidx.work.Data GuardarData(String titulo, String detalle, int not){

        return new androidx.work.Data.Builder()
                .putString("titulo", titulo)
                .putString("detalle", detalle)
                .putInt("id_noti",not).build();
    }

}