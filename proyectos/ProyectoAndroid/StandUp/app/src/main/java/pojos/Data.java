package pojos;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.santotomas.stand_up.AlertActivity;

import java.util.ArrayList;
import java.util.List;

public class Data {
    int cont =0;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference ref_user = database.getReference("Users").child(user.getUid());

    ArrayList<Avisos> list;
    AlertActivity alert = new AlertActivity();


    public void insertAlertas(String ni, String nf, String titulo, String mi, String mf){
        cont++;
        DatabaseReference A = database.getReference("Users").child(user.getUid()).child("Avisos");
        A.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Avisos aviso = new Avisos(titulo,ni,mi,nf,mf);
                A.child("Aviso " + cont).setValue(aviso);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }



    public void userDataBase(){
        ref_user.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!snapshot.exists()){
                    Users users= new Users(
                            user.getUid(),
                            user.getDisplayName(),
                            user.getEmail());
                    ref_user.setValue(users);

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }






}
