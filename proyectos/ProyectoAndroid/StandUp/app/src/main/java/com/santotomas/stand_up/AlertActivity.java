package com.santotomas.stand_up;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.work.WorkManager;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.SurfaceHolder;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.bumptech.glide.Glide;
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

import org.w3c.dom.Text;

import java.util.Calendar;
import java.util.UUID;

import adapter.AdapterAviso;
import pojos.Avisos;
import pojos.Data;

public class AlertActivity extends AppCompatActivity {
     int cont = 0;
    TextView txt_user, txt_mail,txt_horarios,txt_msje_inicio,txt_mensaje_fin,n_inicio, n_fin;
    Button btn_volver,btn_confirmar,btn_seleccionHoraIN,btn_seleccionHoraFin;
    EditText msje_inicio,msje_fin,txtAviso;
    int hora,minuto;




    private PendingIntent pendingIntent;
    private final static String CHANNEL_ID = "NOTIFICACION";
    private final static int NOTIFICACION_ID = 0;


    @SuppressLint({"WrongConstant", "WrongViewCast"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alert);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.txt_actionbar);

        n_inicio = findViewById(R.id.tv_horain);
        n_fin = findViewById(R.id.tv_horafin);
        btn_confirmar = findViewById(R.id.btn_confirmar);
        txt_horarios = findViewById(R.id.txt_verhorarios);
        txt_msje_inicio = findViewById(R.id.mostrar_result_incio);
        txt_mensaje_fin = findViewById(R.id.mostrar_result_fin);
        txtAviso = findViewById(R.id.txtTitulo);

        btn_seleccionHoraIN =  findViewById(R.id.btn_selechorain);
        btn_seleccionHoraFin =  findViewById(R.id.btn_selechorafin);

        msje_inicio = findViewById(R.id.txt_msje_inicio);
        msje_fin = findViewById(R.id.txt_msje_fin);

        btn_volver = findViewById(R.id.btn_volver);


        Calendar actual = Calendar.getInstance();//configurar la fecha y la hora actual del dispositivo (los picker)
        Calendar calendar = Calendar.getInstance();//selecionar la fecha


        Data data = new Data();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseDatabase database = FirebaseDatabase.getInstance();





        btn_seleccionHoraIN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hora = actual.get(calendar.HOUR_OF_DAY);
                minuto = actual.get(calendar.MINUTE);

                TimePickerDialog timePickerDialog = new TimePickerDialog(v.getContext(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int h, int m) {
                        calendar.set(calendar.HOUR_OF_DAY,h);
                        calendar.set(calendar.MINUTE,m);


                        n_inicio.setText(String.format("%02d:%02d",h,m));


                    }
                },hora,minuto,true);
                timePickerDialog.show();
            }
        });

        btn_seleccionHoraFin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hora = actual.get(Calendar.HOUR_OF_DAY);
                minuto = actual.get(Calendar.MINUTE);

                TimePickerDialog timePickerDialog = new TimePickerDialog(v.getContext(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int ho, int mi) {
                        calendar.set(calendar.HOUR_OF_DAY,ho);
                        calendar.set(calendar.MINUTE,mi);


                        n_fin.setText(String.format("%02d:%02d",ho,mi));
                    }
                },hora,minuto,true);
                timePickerDialog.show();
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
                    long alertin = (calendar.getTimeInMillis() - System.currentTimeMillis());
                    long alertf = (calendar.getTimeInMillis() - System.currentTimeMillis());



                    androidx.work.Data data1 = GuardarData(txtAviso,msje_inicio, random);
                    androidx.work.Data data2 = GuardarData(txtAviso,msje_fin, random);

                    WorkManagmernoti.GuardarNotificacion((int) alertin,data1,tag);
                    WorkManagmernoti.GuardarNotificacion((int) alertf,data2,tag);

                    Toast.makeText(AlertActivity.this, "Entró.", Toast.LENGTH_SHORT).show();
                    DatabaseReference A = database.getReference("Users").child(user.getUid()).child("Avisos");
                    A.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                            data.insertAlertas(n_inicio.getText(),n_fin.getText(),txtAviso.getText().toString(),msje_inicio.getText().toString(),msje_fin.getText().toString());

                            //createNotificationChannel();
                            //createNotification();

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }
        });
    } // ------
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



    private String GenerateKey(){
        return UUID.randomUUID().toString();
    }

    private androidx.work.Data GuardarData(EditText titulo, EditText detalle, int not){

        return new androidx.work.Data.Builder()
                .putString("titulo", String.valueOf(titulo))
                .putString("detalle", String.valueOf(detalle))
                .putInt("id_noti",not).build();
    }


   //private void createNotificationChannel(){
     //   if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
       //     CharSequence name = "Noticacion";
         //   NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID, name, NotificationManager.IMPORTANCE_DEFAULT);
           // NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            //notificationManager.createNotificationChannel(notificationChannel);
        //}
    //}

    //private void createNotification(){
      //  NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID);
        //builder.setSmallIcon(R.drawable.ic_baseline_airline_seat_flat_angled_24);
        //builder.setContentTitle("Título de notificación");
        //builder.setContentText("Texto de notificación");
        //builder.setPriority(NotificationCompat.PRIORITY_MAX);
        //builder.setVibrate(new long[]{1000,1000,1000,1000,1000});
        //builder.setDefaults(Notification.DEFAULT_SOUND);

        //NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(getApplicationContext());
        //notificationManagerCompat.notify(NOTIFICACION_ID, builder.build());
    //}
}