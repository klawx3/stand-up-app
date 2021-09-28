package com.santotomas.stand_up;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.text.SimpleDateFormat;
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
        Calendar cal = Calendar.getInstance();

        //*dia de la semana
        Calendar l = Calendar.getInstance();
        Calendar m = Calendar.getInstance();
        Calendar mi = Calendar.getInstance();
        Calendar j = Calendar.getInstance();
        Calendar vi = Calendar.getInstance();
        Calendar s = Calendar.getInstance();
        Calendar d = Calendar.getInstance();

        //*add
        l.add(Calendar.DAY_OF_WEEK,2);
        m.add(Calendar.DAY_OF_WEEK,3);
        mi.add(Calendar.DAY_OF_WEEK,4);
        j.add(Calendar.DAY_OF_WEEK,5);
        vi.add(Calendar.DAY_OF_WEEK,6);
        s.add(Calendar.DAY_OF_WEEK,7);
        d.add(Calendar.DAY_OF_WEEK,1);

        long hoy = cal.getTimeInMillis();
        long despues = cal.getTimeInMillis();

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
                    System.out.println("------------------------------");
                    System.out.println("entrooooooooooooooo");
                    System.out.println("--------------------");
                    System.out.println("--------------------");
                }else{
                    System.out.println("------------------------------");
                    System.out.println("entrooooooooooooooo else");
                    System.out.println("--------------------");
                    System.out.println("--------------------");

                    if(l.get(Calendar.DAY_OF_WEEK) == 2){

                        if(despues > hoy){
                            System.out.println("______________________________________");
                            System.out.println("lunes");
                            System.out.println("______________________________________");
                        }else {
                            System.out.println("______________________________________");
                            System.out.println("lunes ELSE");
                            System.out.println("______________________________________");
                        }
                    }else if (m.get(Calendar.DAY_OF_WEEK) == 3){

                        if(despues > hoy){
                            System.out.println("______________________________________");
                            System.out.println("martes");
                            System.out.println("______________________________________");
                        }else {
                            System.out.println("______________________________________");
                            System.out.println("martes ELSE");
                            System.out.println("______________________________________");
                        }

                    }else if (mi.get(Calendar.DAY_OF_WEEK) == 4){

                        if(despues > hoy){
                            System.out.println("______________________________________");
                            System.out.println("miercoles");
                            System.out.println("______________________________________");
                        }else {
                            System.out.println("______________________________________");
                            System.out.println("miercoles ELSE");
                            System.out.println("______________________________________");
                        }

                    }else if (j.get(Calendar.DAY_OF_WEEK) == 5){

                        if(despues > hoy){
                            System.out.println("______________________________________");
                            System.out.println("jueves");
                            System.out.println("______________________________________");
                        }else {
                            System.out.println("______________________________________");
                            System.out.println("jueves ELSE");
                            System.out.println("______________________________________");
                        }

                    }else if (vi.get(Calendar.DAY_OF_WEEK) == 6){

                        if(despues > hoy){
                            System.out.println("______________________________________");
                            System.out.println("viernes");
                            System.out.println("______________________________________");
                        }else {
                            System.out.println("______________________________________");
                            System.out.println("viernes ELSE");
                            System.out.println("______________________________________");
                        }

                    }else if (s.get(Calendar.DAY_OF_WEEK) == 7){

                        if(despues > hoy){
                            System.out.println("______________________________________");
                            System.out.println("sabado");
                            System.out.println("______________________________________");
                        }else {
                            System.out.println("______________________________________");
                            System.out.println("sabado ELSE");
                            System.out.println("______________________________________");
                        }

                    }else if (d.get(Calendar.DAY_OF_WEEK) == 1){

                        if(despues > hoy){
                            System.out.println("______________________________________");
                            System.out.println("domingo");
                            System.out.println("______________________________________");
                        }else {
                            System.out.println("______________________________________");
                            System.out.println("domingo ELSE");
                            System.out.println("______________________________________");
                        }

                    }
                    data.insertTiempo(txt_tiempo_minutos.getText().toString(),cal.getTime().toString());
                    System.out.println("FECHA HOY: " + String.valueOf(hoy)+"  "+new SimpleDateFormat("dd/MM/yyyy").format(hoy));
                    System.out.println("FECHA DESPUES: "+ String.valueOf(despues)+"  "+new SimpleDateFormat("dd/MM/yyyy").format(despues));
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