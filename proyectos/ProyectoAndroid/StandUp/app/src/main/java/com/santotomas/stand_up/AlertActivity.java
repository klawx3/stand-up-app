package com.santotomas.stand_up;

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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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

public class AlertActivity extends AppCompatActivity {

    TextView txt_Dia,n_inicio, n_fin;
    Button btn_volver,btn_confirmar,btn_seleccionHoraIN,btn_seleccionHoraFin,btn_selecDia;
    EditText msje_inicio,msje_fin,txtAviso;
    int hora,minuto,dia,mes,anio;

    CheckBox ch_Lunes,ch_Martes,ch_Miercoles,ch_Jueves,ch_Viernes,ch_Sabado,ch_Domingo;

    private PendingIntent pendingIntent;
    private final static String CHANNEL_ID = "NOTIFICACION";
    private final static int NOTIFICACION_ID = 0;
    String fec_act;

    @SuppressLint({"WrongConstant", "WrongViewCast"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alert);

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
                goHome();
            }
        });

        btn_confirmar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                                    //Se valida que existan días seleccionados para generar notificación. //
                if(ch_Lunes.isChecked() || ch_Martes.isChecked() || ch_Miercoles.isChecked() ||ch_Jueves.isChecked() ||
                        ch_Viernes.isChecked() || ch_Sabado.isChecked() || ch_Domingo.isChecked()){

                                    //Se valida que exista información necesaria para generar notificación. //
                    if(TextUtils.isEmpty(msje_inicio.getText().toString()) || TextUtils.isEmpty(msje_fin.getText().toString()) || TextUtils.isEmpty(n_fin.getText())){
                        if (TextUtils.isEmpty(msje_inicio.getText().toString()) || TextUtils.isEmpty(msje_fin.getText().toString())) {
                            Toast.makeText(AlertActivity.this, "Ingrese mensajes.", Toast.LENGTH_SHORT).show();

                        }
                        if(TextUtils.isEmpty(n_fin.getText())){
                            Toast.makeText(AlertActivity.this, "Ingrese hora de fin válida.", Toast.LENGTH_SHORT).show();
                        }
                    }else{

                        int random = (int)(Math.random()*50+1);

                        String tag = GenerateKey();
                        //long alertin = (calendar.getTimeInMillis() - System.currentTimeMillis());
                        //long alertf = (calendar_fn.getTimeInMillis() - System.currentTimeMillis());

                        if(ch_Lunes.isChecked()){
                            //DatabaseReference A = database.getReference("Users").child(user.getUid()).child("Avisos").child("Lunes");
                            data.insertAlertas(n_inicio.getText(),n_fin.getText(),txtAviso.getText().toString(),
                                    msje_inicio.getText().toString(),msje_fin.getText().toString(),"Lunes");
                        }
                        if(ch_Martes.isChecked()){
                            //DatabaseReference A = database.getReference("Users").child(user.getUid()).child("Avisos").child("Martes");
                            data.insertAlertas(n_inicio.getText(),n_fin.getText(),txtAviso.getText().toString(),
                                    msje_inicio.getText().toString(),msje_fin.getText().toString(),"Martes");
                        }
                        if(ch_Miercoles.isChecked()){
                            //DatabaseReference A = database.getReference("Users").child(user.getUid()).child("Avisos").child("Miercoles");
                            data.insertAlertas(n_inicio.getText(),n_fin.getText(),txtAviso.getText().toString(),
                                    msje_inicio.getText().toString(),msje_fin.getText().toString(),"Miercoles");
                        }
                        if(ch_Jueves.isChecked()){
                            //DatabaseReference A = database.getReference("Users").child(user.getUid()).child("Avisos").child("Jueves");
                            data.insertAlertas(n_inicio.getText(),n_fin.getText(),txtAviso.getText().toString(),
                                    msje_inicio.getText().toString(),msje_fin.getText().toString(),"Jueves");
                        }
                        if(ch_Viernes.isChecked()){
                            //DatabaseReference A = database.getReference("Users").child(user.getUid()).child("Avisos").child("Viernes");
                            data.insertAlertas(n_inicio.getText(),n_fin.getText(),txtAviso.getText().toString(),
                                    msje_inicio.getText().toString(),msje_fin.getText().toString(),"Viernes");
                        }
                        if(ch_Sabado.isChecked()){
                            //DatabaseReference A = database.getReference("Users").child(user.getUid()).child("Avisos").child("Sabado");
                            data.insertAlertas(n_inicio.getText(),n_fin.getText(),txtAviso.getText().toString(),
                                    msje_inicio.getText().toString(),msje_fin.getText().toString(),"Sabado");
                        }
                        if(ch_Domingo.isChecked()){
                            //DatabaseReference A = database.getReference("Users").child(user.getUid()).child("Avisos").child("Domingo");
                            data.insertAlertas(n_inicio.getText(),n_fin.getText(),txtAviso.getText().toString(),
                                    msje_inicio.getText().toString(),msje_fin.getText().toString(),"Domingo");
                        }

                        goHome();

                        /*

                        DatabaseReference A = database.getReference("Users").child(user.getUid()).child("Avisos");
                        A.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {

                                if(data.compararHoras(n_inicio.getText().toString(),n_fin.getText().toString()) && data.compararFechas(fec_act,txt_Dia.getText().toString())){

                                    androidx.work.Data data1 = GuardarData(txtAviso.getText().toString(),msje_inicio.getText().toString(), random);
                                    androidx.work.Data data2 = GuardarData(txtAviso.getText().toString(),msje_fin.getText().toString(), random);

                                    WorkManagmernoti.GuardarNotificacion((int) alertin,data1,tag);
                                    WorkManagmernoti.GuardarNotificacion((int) alertf,data2,tag);

                                    Toast.makeText(AlertActivity.this, "Aviso creado.", Toast.LENGTH_SHORT).show();

                                    //data.insertAlertas(n_inicio.getText(),n_fin.getText(),txtAviso.getText().toString(),msje_inicio.getText().toString(),msje_fin.getText().toString(),txt_Dia.getText().toString());
                                    goHome();
                                }else
                                    Toast.makeText(AlertActivity.this, "Asegurese que la hora final es posterior a la inicial y la fecha posterior a la actual", Toast.LENGTH_SHORT).show();


                            }


                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                        */
                    }
                }else{
                    Toast.makeText(AlertActivity.this, "POR FAVOR SELECCIONE DIA(S) A NOTIFICAR", Toast.LENGTH_SHORT).show();
                }

            }
        });
    } // ---------------------------

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.items_menu,menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.item_cerrar:
                AuthUI.getInstance()
                        .signOut(this)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                finish();
                                Toast.makeText(AlertActivity.this, "Sesión cerrada.", Toast.LENGTH_SHORT).show();
                                backLogin();
                            }
                        });
        }
        return super.onOptionsItemSelected(item);
    }

    private void backLogin() {
        Intent i = new Intent(this,MainActivity.class);
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