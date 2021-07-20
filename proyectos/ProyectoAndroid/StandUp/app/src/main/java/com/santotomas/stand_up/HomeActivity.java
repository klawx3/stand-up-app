package com.santotomas.stand_up;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.work.WorkManager;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;

import adapter.AdapterAviso;
import scripts.Avisos;
import scripts.Data;

public class HomeActivity extends AppCompatActivity {

    private final static String AVISOS = "avisos";

    ArrayList<Avisos> list;
    RecyclerView rv;
    SearchView serchv;
    AdapterAviso adapter;
    Calendar c = Calendar.getInstance();
    LinearLayoutManager lm;
    Data d = new Data();


    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference ref_user = database.getReference("Users").child(user.getUid());
    DatabaseReference A = database.getReference("Users").child(user.getUid()).child("Avisos");


    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.txt_actionbar);


        //obtenerDiaActual();

        cargaAlertas("Lunes");
        cargaAlertas("Martes");
        cargaAlertas("Miercoles");
        cargaAlertas("Jueves");
        cargaAlertas("Viernes");
        cargaAlertas("Sabado");
        cargaAlertas("Domingo");
        cargaAlertas("Especificos");
        buscarAlertas();

        d.userDataBase();

        FloatingActionButton fab = findViewById(R.id.agregar_aviso);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goTipoAlert();
            }
        });


    } // ----------------------------

    private void buscarAlertas(){
        serchv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                buscar(s);
                return true;
            }
        });


    }
    private void buscar(String s) {
        ArrayList<Avisos> alertList = new ArrayList<>();
        for (Avisos obj: list){
            if(obj.getTitulo().toLowerCase().contains(s.toLowerCase())){
                alertList.add(obj);
            }
        }
        AdapterAviso adapterAviso = new AdapterAviso(alertList);
        rv.setAdapter(adapterAviso);

    }
    private void cargaAlertas(String dia){
        DatabaseReference ref_user = database.getReference("Users").child(user.getUid()).child("Avisos").child(dia);
        rv = findViewById(R.id.rv);
        serchv = findViewById(R.id.search);
        lm = new LinearLayoutManager(this);

        rv.setLayoutManager(lm);
        list = new ArrayList<>();
        adapter = new AdapterAviso(list);
        rv.setAdapter(adapter);

        ref_user.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    for (DataSnapshot snapshots : snapshot.getChildren()){
                        Avisos av = snapshots.getValue(Avisos.class);
                        list.add(av);
                    }
                    adapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


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
                                Toast.makeText(HomeActivity.this, "Sesión cerrada.", Toast.LENGTH_SHORT).show();
                                backLogin();
                            }
                        });
            case R.id.item_alert:
                goTipoAlert();
                break;
            case R.id.item_nosotros:
                goNosotros();
                break;
            case R.id.item_confhora:
                goConfigHora();
                break;
            case R.id.item_enviartiempo:
                goEnviarTiempo();
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    private void backLogin() {
        Intent i = new Intent(this,MainActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
    }

    private void goConfigHora() {
        Intent i = new Intent(this,ConfigHoraActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
    }

    private void goTipoAlert(){
        Intent i = new Intent(this, TipoAlertActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
    }

    private void goNosotros(){
        Intent i = new Intent(this, NosotrosActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
    }

    private void goEnviarTiempo(){
        Intent i = new Intent(this, EnviarTiempoActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
    }


    private void cargarAlertas(Avisos av){
        int random = (int)(Math.random()*50+1);
        String tag = AlertRepetitivaActivity.GenerateKey();

        androidx.work.Data data1 = AlertRepetitivaActivity.GuardarData(av.getTitulo(),
                av.getMensaje(), random);
        androidx.work.Data data2 = AlertRepetitivaActivity.GuardarData(av.getTitulo(),
                av.getMensajefin(), random);

        int segundos = 00;

        String[] alerta_inicio = av.getHora().split(":");
        int hora_alertInicio = Integer.parseInt(alerta_inicio[0]);
        int minuto_alertInicio = Integer.parseInt(alerta_inicio[1]);

        String[] alerta_fin = av.getHorafin().split(":");
        int hora_alertFin = Integer.parseInt(alerta_fin[0]);
        int minuto_alertFin = Integer.parseInt(alerta_fin[1]);

        Calendar calendar_alert_INICIO = Calendar.getInstance();
        Calendar calendar_alert_FIN = Calendar.getInstance();

        calendar_alert_INICIO.set(Calendar.HOUR_OF_DAY,hora_alertInicio);
        calendar_alert_INICIO.set(Calendar.MINUTE,minuto_alertInicio);
        calendar_alert_INICIO.set(Calendar.SECOND,segundos);

        calendar_alert_FIN.set(Calendar.HOUR_OF_DAY,hora_alertFin);
        calendar_alert_FIN.set(Calendar.MINUTE,minuto_alertFin);
        calendar_alert_FIN.set(Calendar.SECOND,segundos);

        long millis_alert_INICIO = (calendar_alert_INICIO.getTimeInMillis() -
                System.currentTimeMillis());

        long millis_alert_FIN = (calendar_alert_FIN.getTimeInMillis() -
                System.currentTimeMillis());

        WorkManagmernoti.GuardarNotificacion((int) millis_alert_INICIO,data1,tag);
        WorkManagmernoti.GuardarNotificacion((int) millis_alert_FIN,data1,tag);
    }

    private void cargarAlertasxDia(int dia){

        if(dia == 1){
            //DOM
            DatabaseReference ref_user = database.getReference("Users").child(user.getUid()).
                    child("Avisos").child("Domingo");
            ref_user.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.exists()){
                        for (DataSnapshot snapshots : snapshot.getChildren()){
                            Avisos av = snapshots.getValue(Avisos.class);
                            cargarAlertas(av);
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        }
        if(dia == 2){
            //LUN
            DatabaseReference ref_user = database.getReference("Users").child(user.getUid()).
                    child("Avisos").child("Lunes");
            ref_user.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.exists()){
                        for (DataSnapshot snapshots : snapshot.getChildren()){
                            Avisos av = snapshots.getValue(Avisos.class);
                            cargarAlertas(av);
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
        if(dia == 3){
            //MAR
            DatabaseReference ref_user = database.getReference("Users").child(user.getUid()).
                    child("Avisos").child("Martes");
            ref_user.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.exists()){
                        for (DataSnapshot snapshots : snapshot.getChildren()){
                            Avisos av = snapshots.getValue(Avisos.class);
                            cargarAlertas(av);
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
        if(dia == 4){
            //MIE
            DatabaseReference ref_user = database.getReference("Users").child(user.getUid()).
                    child("Avisos").child("Miercoles");
            ref_user.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.exists()){
                        for (DataSnapshot snapshots : snapshot.getChildren()){
                            Avisos av = snapshots.getValue(Avisos.class);
                            cargarAlertas(av);
                        }
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
        if(dia == 5){
            //JUE
            DatabaseReference ref_user = database.getReference("Users").child(user.getUid()).
                    child("Avisos").child("Jueves");
            ref_user.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.exists()){
                        for (DataSnapshot snapshots : snapshot.getChildren()){
                            Avisos av = snapshots.getValue(Avisos.class);
                            cargarAlertas(av);
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
        if(dia == 6){
            //VIE
            DatabaseReference ref_user = database.getReference("Users").child(user.getUid()).
                    child("Avisos").child("Viernes");
            ref_user.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.exists()){
                        for (DataSnapshot snapshots : snapshot.getChildren()){
                            Avisos av = snapshots.getValue(Avisos.class);
                            cargarAlertas(av);
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
        if(dia == 7){
            //SAB
            DatabaseReference ref_user = database.getReference("Users").child(user.getUid()).
                    child("Avisos").child("Sabado");
            ref_user.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.exists()){
                        for (DataSnapshot snapshots : snapshot.getChildren()){
                            Avisos av = snapshots.getValue(Avisos.class);
                            cargarAlertas(av);
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }

    }

    public void obtenerDiaActual(){
        WorkManager.getInstance(HomeActivity.this).cancelAllWork();

        Calendar calendarReinicio = Calendar.getInstance();
        int valores = 00;

        String tag_recargar = "recargar";
        androidx.work.Data data_recargar = AlertRepetitivaActivity.GuardarData("null","null",1);

        calendarReinicio.set(Calendar.HOUR_OF_DAY,valores);
        calendarReinicio.set(Calendar.MINUTE,valores);
        calendarReinicio.set(Calendar.SECOND,valores);

        long recargar_notis = (calendarReinicio.getTimeInMillis() -
                System.currentTimeMillis());

        WorkManagmernoti.recargarNotificacion((int)recargar_notis,data_recargar,tag_recargar);

        Calendar actual = Calendar.getInstance();
        int today = actual.get(Calendar.DAY_OF_WEEK);
        cargarAlertasxDia(today);

    }


}