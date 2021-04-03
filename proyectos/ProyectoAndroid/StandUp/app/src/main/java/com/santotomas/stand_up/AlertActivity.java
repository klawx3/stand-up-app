package com.santotomas.stand_up;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.TextView;
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

import adapter.AdapterAviso;
import pojos.Avisos;
import pojos.Data;

public class AlertActivity extends AppCompatActivity implements NumberPicker.OnValueChangeListener {
     int cont = 0;
    TextView txt_user, txt_mail,txt_horarios,txt_msje_inicio,txt_mensaje_fin;
    Button btn_volver,btn_confirmar;
    NumberPicker n_inicio, n_fin;
    EditText msje_inicio,msje_fin,txtAviso;


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

        n_inicio = findViewById(R.id.number_inicio);
        n_fin = findViewById(R.id.number_fin);
        btn_confirmar = findViewById(R.id.btn_confirmar);
        txt_horarios = findViewById(R.id.txt_verhorarios);
        txt_msje_inicio = findViewById(R.id.mostrar_result_incio);
        txt_mensaje_fin = findViewById(R.id.mostrar_result_fin);
        txtAviso = findViewById(R.id.txtTitulo);

        msje_inicio = findViewById(R.id.txt_msje_inicio);
        msje_fin = findViewById(R.id.txt_msje_fin);

        btn_volver = findViewById(R.id.btn_volver);


        Data data = new Data();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseDatabase database = FirebaseDatabase.getInstance();

        n_inicio.setMinValue(1);
        n_inicio.setMaxValue(24);
        n_inicio.setOnValueChangedListener(this);


        btn_volver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goHome();
            }
        });

        btn_confirmar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(TextUtils.isEmpty(msje_inicio.getText().toString()) || TextUtils.isEmpty(msje_fin.getText().toString()) || n_fin.getValue() == 0){
                    if (TextUtils.isEmpty(msje_inicio.getText().toString()) || TextUtils.isEmpty(msje_fin.getText().toString())) {
                        Toast.makeText(AlertActivity.this, "Ingrese mensajes.", Toast.LENGTH_SHORT).show();

                    }
                    if(n_fin.getValue() == 0){
                        Toast.makeText(AlertActivity.this, "Ingrese hora de fin válida.", Toast.LENGTH_SHORT).show();
                    }
                }else{

                    Toast.makeText(AlertActivity.this, "Entró.", Toast.LENGTH_SHORT).show();
                    DatabaseReference A = database.getReference("Users").child(user.getUid()).child("Avisos");
                    A.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                            data.insertAlertas(n_inicio.getValue(),n_fin.getValue(),txtAviso.getText().toString(),msje_inicio.getText().toString(),msje_fin.getText().toString());

                            createNotificationChannel();
                            createNotification();

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


    @Override
    public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
        n_fin.setMinValue(newVal+1);
        if (newVal != 24){
            n_fin.setMaxValue(24);
        }else {
            newVal-=1;
        }
    }


    private void createNotificationChannel(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            CharSequence name = "Noticacion";
            NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID, name, NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(notificationChannel);
        }
    }

    private void createNotification(){
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID);
        builder.setSmallIcon(R.drawable.ic_baseline_airline_seat_flat_angled_24);
        builder.setContentTitle("Título de notificación");
        builder.setContentText("Texto de notificación");
        builder.setPriority(NotificationCompat.PRIORITY_MAX);
        builder.setVibrate(new long[]{1000,1000,1000,1000,1000});
        builder.setDefaults(Notification.DEFAULT_SOUND);

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(getApplicationContext());
        notificationManagerCompat.notify(NOTIFICACION_ID, builder.build());
    }
}