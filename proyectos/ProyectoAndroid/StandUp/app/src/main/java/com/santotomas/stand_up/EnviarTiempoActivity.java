package com.santotomas.stand_up;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Calendar;

import scripts.Data;

public class EnviarTiempoActivity extends AppCompatActivity {

    Button btn_volver,btn_enviar;
    EditText txt_tiempo_minutos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enviar_tiempo);
        Data data = new Data();

        btn_volver = findViewById(R.id.btn_ENVIARTIEMPO_volver);
        btn_enviar = findViewById(R.id.btn_ENVIARTIEMPO_enviar);

        txt_tiempo_minutos = findViewById(R.id.txt_ENVIARTIEMPO_tiempoMIN);
        Calendar actual = Calendar.getInstance();

        btn_volver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goHome();
            }
        });

        btn_enviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(txt_tiempo_minutos.getText())){
                    Toast.makeText(EnviarTiempoActivity.this, "Ingrese tiempo valido.", Toast.LENGTH_SHORT).show();
                }else{
                    data.insertTiempo(txt_tiempo_minutos.getText().toString(),actual.getTime().toString());
                }

                //************************** VER SI SE PUEDE RESCATAR EL TIEMPO EN LA BD PARA QUE CUANDO SE HAGA EL PROXIMO INSERT SE SUME CON LO QUE EL USUARIO ENTREGA
                //************************** AL MISMO TIEMPO RESCATAR LOS DATOS DE EL ULTIMO INSERT PARA MOSTRAR EL TRABAJO TOTAL (SI NO SE LOGRA HACER LO BORRARE)

                //************************** FALTA ENVIAR LOS DATOS A FIREBASE, SE ENVIARÁ EL VALOR DEL TXT DEL LAYOUT + LA FECHA ACTUAL (EL VALOR DEL TXT NO DEBE SUPERAR LOS 1440 MINUTOS, UN IF)
                //************************** POSTERIOR A ENVIAR LOS DATOS, SE DEBEN CARGAR LAS ALERTAS DEL DÍA SIGUIENTE, ES DECIR; ACTUAL+1
                //************************** ADEMÁS, CARGAR TODAS LAS NOTIFICACIONES DEL CHILD "ESPECIFICOS" QUE CUMPLAN CON FECHA Y HORA POSTERIOR A LA ACTUAL
                //************************** ESTE CÓDIGO DEBERÍA SERVIR, FALTARÍA MANIPULAR LAS VARIABLES:
                /*
                                            if(d.compararFechas(fecha_act,av.getDia()) && d.compararHoras(hora_compl_act,av.getHora())){
                                                System.out.println("Fecha: "+av.getDia()+" ///// Hora: "+av.getHora());

                                                int random = (int)(Math.random()*50+1);
                                                String tag = AlertActivity.GenerateKey();

                                                androidx.work.Data data1 = AlertActivity.GuardarData(av.getTitulo(),av.getMensaje(), random);
                                                androidx.work.Data data2 = AlertActivity.GuardarData(av.getTitulo(),av.getMensajefin(), random);

                                                SimpleDateFormat sdf = new SimpleDateFormat("%02d:%02d");
                                                Calendar calIN = Calendar.getInstance();
                                                Calendar calFIN = Calendar.getInstance();

                                                String[] hora_in = av.getHora().split(":");
                                                int hora1 = Integer.parseInt(hora_in[0]);
                                                int min1 = Integer.parseInt(hora_in[1]);

                                                String[] hora_fin = av.getHorafin().split(":");
                                                int hora2 = Integer.parseInt(hora_fin[0]);
                                                int min2 = Integer.parseInt(hora_fin[1]);

                                                int segundos = 00;

                                                calIN.set(Calendar.HOUR_OF_DAY,hora1);
                                                calIN.set(Calendar.MINUTE,min1);
                                                calIN.set(Calendar.SECOND,segundos);

                                                calFIN.set(Calendar.HOUR_OF_DAY,hora2);
                                                calFIN.set(Calendar.MINUTE,min2);
                                                calFIN.set(Calendar.SECOND,segundos);

                                                long alertin = (calIN.getTimeInMillis() - System.currentTimeMillis());
                                                long alertf = (calFIN.getTimeInMillis() - System.currentTimeMillis());

                                                WorkManagmernoti.GuardarNotificacion((int) alertin,data1,tag);
                                                WorkManagmernoti.GuardarNotificacion((int) alertf,data2,tag);
                                            }
                                            */
            }
        });

    }//--------------------

    private void goHome() {
        Intent i = new Intent(this,HomeActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
    }

}