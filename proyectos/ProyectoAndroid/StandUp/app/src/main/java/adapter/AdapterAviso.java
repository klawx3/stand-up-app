package adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.santotomas.stand_up.R;

import java.util.List;

import pojos.Avisos;

public class AdapterAviso extends RecyclerView.Adapter<AdapterAviso.viewHolderAvisos> {
    public static List<Avisos> avisoList;

    public AdapterAviso(List<Avisos> aviso) {
        this.avisoList = aviso;
    }

    @NonNull
    @Override
    public viewHolderAvisos onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_avisos,parent,false);
        viewHolderAvisos holder = new viewHolderAvisos(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolderAvisos holder, int position) {
        Avisos av = avisoList.get(position);

        holder.txtHoraIn.setText("Hora inicio: " + av.getHora()+"hrs");
        holder.txtmsjeinicio.setText("Mensaje Inicio: " + av.getMensaje());
        holder.txtHoraFin.setText("Hora Fin: " + av.getHorafin()+"hrs");
        holder.txtmsjefin.setText("Mensaje Fin: " + av.getMensajefin());


    }

    @Override
    public int getItemCount() {
        return avisoList.size();
    }

    public class viewHolderAvisos extends RecyclerView.ViewHolder {
        TextView txtHoraIn, txtmsjeinicio, txtHoraFin, txtmsjefin;

        public viewHolderAvisos(@NonNull View itemView) {
            super(itemView);

            txtHoraIn = itemView.findViewById(R.id.txtHoraIn);
            txtmsjeinicio = itemView.findViewById(R.id.txtmsjeinicio);
            txtHoraFin = itemView.findViewById(R.id.txtHoraFin);
            txtmsjefin = itemView.findViewById(R.id.txtmsjefin);
        }
    }
}
