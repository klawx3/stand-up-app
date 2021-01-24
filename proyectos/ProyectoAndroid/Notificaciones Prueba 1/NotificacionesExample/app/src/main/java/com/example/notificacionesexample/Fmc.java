package com.example.notificacionesexample;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Random;

public class Fmc extends  FirebaseMessagingService{
    @Override
    //nos da el id del dispositivo y asi enviar una notificacion especifica
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
        Log.e("token", "el token es: "+s);
        savetoken(s);
    }

    private void savetoken(String s) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("token");
        ref.child("cordero").setValue(s);//hay que ocupar el id que se ocupa para el login en esta caso es con google el numero no un nombre
    }

    // recibe las notificaciones y los datos que lleguen
    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        String from = remoteMessage.getFrom();



        if(remoteMessage.getData().size() >0){
            String titulo = remoteMessage.getData().get("titulo");
            String detalle = remoteMessage.getData().get("Descripcion");

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O){
                versionMayor(titulo,detalle);
            }
            if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.O){

            }


        }

    }

    private void versionMayor(String titulo, String detalle) {
        String id ="Mensaje";

        NotificationManager nm=(NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this,id);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel nc = new NotificationChannel(id,"nuevo ",NotificationManager.IMPORTANCE_HIGH);
            nc.setShowBadge(true);
            nm.createNotificationChannel(nc);
        }

        builder.setAutoCancel(true)
                .setWhen(System.currentTimeMillis())
                .setContentTitle(titulo)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentIntent(clicNotificacion())
                .setContentText(detalle)
                .setContentInfo("nuevo");

        Random random = new Random();
        int idNotify = random.nextInt(8000);

        assert  nm !=null;
        nm.notify(idNotify,builder.build());

    }
    public PendingIntent clicNotificacion(){
        Intent nf = new Intent(getApplicationContext(), MainActivity.class);
        nf.putExtra("color","color");
        nf.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        return PendingIntent.getActivity(this, 0,nf,0);
    }
}
