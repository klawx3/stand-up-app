package com.santotomas.stand_up;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Intent;
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

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.UUID;

import scripts.Data;

public class AlertEspecificaActivity extends AppCompatActivity {

    TextView txt_Dia,n_inicio, n_fin,txtDia;
    Button btn_volver,btn_confirmar,btn_seleccionHoraIN,btn_seleccionHoraFin,btn_selecDia;
    EditText msje_inicio,msje_fin,txtAviso;
    int hora,minuto,dia,mes,anio;

    private PendingIntent pendingIntent;
    private final static String CHANNEL_ID = "NOTIFICACION";
    private final static int NOTIFICACION_ID = 0;
    String fec_act;

    @SuppressLint({"WrongConstant", "WrongViewCast"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alert_especifica);

        Data data = new Data();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseDatabase database = FirebaseDatabase.getInstance();

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.txt_actionbar);

        n_inicio = findViewById(R.id.tv_horain);
        n_fin = findViewById(R.id.tv_horafin);
        txt_Dia = findViewById(R.id.tv_dia);

        txtAviso = findViewById(R.id.txtTitulo);
        msje_inicio = findViewById(R.id.txt_msje_inicio);
        msje_fin = findViewById(R.id.txt_msje_fin);

        btn_selecDia = findViewById(R.id.btn_selecDia);
        btn_confirmar = findViewById(R.id.btn_confirmar);
        btn_seleccionHoraIN =  findViewById(R.id.btn_selechorain);
        btn_seleccionHoraFin =  findViewById(R.id.btn_selechorafin);
        btn_volver = findViewById(R.id.btn_volver);


        Calendar actual = Calendar.getInstance();//configurar la fecha y la hora actual del dispositivo (los picker)
        Calendar calendar = Calendar.getInstance();//selecionar la fecha
        Calendar calendar_fn = Calendar.getInstance();//


        btn_selecDia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                anio = actual.get(Calendar.YEAR);
                mes = actual.get(Calendar.MONTH);
                dia = actual.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(v.getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        calendar.set(Calendar.DAY_OF_MONTH,dayOfMonth);
                        calendar.set(Calendar.MONTH,month);
                        calendar.set(Calendar.YEAR,year);

                        calendar_fn.set(Calendar.DAY_OF_MONTH,dayOfMonth);
                        calendar_fn.set(Calendar.MONTH,month);
                        calendar_fn.set(Calendar.YEAR,year);

                        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
                        String strDate = format.format(calendar.getTime());
                        txt_Dia.setText(strDate);

                        fec_act = format.format(actual.getTime());
                    }
                },anio,mes,dia);
                datePickerDialog.show();
            }
        });

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
                    Toast.makeText(AlertEspecificaActivity.this, "Ingrese hora de fin válida.", Toast.LENGTH_SHORT).show();
                }else{

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
                    long aleartf = (calendar_fn.getTimeInMillis() - System.currentTimeMillis());


                    DatabaseReference A = database.getReference("Users").child(user.getUid()).child("Avisos");
                    A.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                            if(data.compararHoras(n_inicio.getText().toString(),n_fin.getText().toString()) && data.compararFechas(fec_act,txt_Dia.getText().toString())){

                                data.insertAlertasEsp(n_inicio.getText(),n_fin.getText(),txtAviso.getText().toString(),msje_inicio.getText().toString(),msje_fin.getText().toString(),txt_Dia.getText().toString());
                                Toast.makeText(AlertEspecificaActivity.this, "Aviso creado.", Toast.LENGTH_SHORT).show();
                                goHome();
                                
                                androidx.work.Data data1 = GuardarData(txtAviso.getText().toString(),msje_inicio.getText().toString(), random);
                                androidx.work.Data data2 = GuardarData(txtAviso.getText().toString(),msje_fin.getText().toString(), random);

                                WorkManagmernoti.GuardarNotificacion((int) alertin,data1,tag);
                                WorkManagmernoti.GuardarNotificacion((int) aleartf,data2,tag);
                            }else
                                Toast.makeText(AlertEspecificaActivity.this, "Asegurese que la hora final es posterior a la inicial y la fecha posterior a la actual", Toast.LENGTH_SHORT).show();
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                        }
                    });
                }

            }
        });
    } // ---------------------------



    private void goHome() {
        Intent i = new Intent(this,HomeActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
    }

    private void goTipoAlert(){
        Intent i = new Intent(this, TipoAlertActivity.class);
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