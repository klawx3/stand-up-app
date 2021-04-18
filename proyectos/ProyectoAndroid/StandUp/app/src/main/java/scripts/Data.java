package scripts;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Data {
    
    int value;
    String a;

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference ref_user = database.getReference("Users").child(user.getUid());
    DatabaseReference avis = database.getReference("Users").child(user.getUid()).child("Avisos");

    List<Integer> list_lastaviso = new ArrayList<>();

    public void basicQueryValueListener() {
        Query myTopPostsQuery = avis.limitToLast(1);
        myTopPostsQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    String key = postSnapshot.getKey();
                    String[] arr = key.split(" ");
                    list_lastaviso.clear();
                    list_lastaviso.add(Integer.parseInt(arr[1]));
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("Falló");
            }
        });
    }

    public void insertAlertas(CharSequence ni, CharSequence nf, String titulo, String mi, String mf){
        DatabaseReference A = database.getReference("Users").child(user.getUid()).child("Avisos");
        basicQueryValueListener();

        A.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (int val : list_lastaviso){
                    value = val;
                    System.out.println(value);
                }

                value++;
                String nii= String.valueOf(ni);
                String nff= String.valueOf(nf);

                Avisos aviso = new Avisos(titulo,nii,mi,nff,mf);
                A.child("Aviso " + value).setValue(aviso);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                //TO DO
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
