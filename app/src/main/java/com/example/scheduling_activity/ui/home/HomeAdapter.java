package com.example.scheduling_activity.ui.home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.scheduling_activity.R;
import com.example.scheduling_activity.ui.database.agenda.AgendaTable;

import java.util.List;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.ViewHolder> {

    private List<AgendaTable> mData;
    private LayoutInflater mInflater;

    public HomeAdapter(Context context, List<AgendaTable> data){
        this.mInflater = LayoutInflater.from(context);
        this.mData =  data;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_agenda, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.binding(mData.get(position));
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        private TextView textName;
        private TextView textJenis;
        private TextView textStatus;
        private TextView textAwal;
        private TextView textAkhir;
        private TextView textJarak;
        private TextView textJabatan;
        private TextView textAbsensi;
        private TextView textUrgensi;
        private TextView textPrioritas;

       public ViewHolder(@NonNull View itemView) {
           super(itemView);
           textName = itemView.findViewById(R.id.textName);
           textJenis = itemView.findViewById(R.id.textJenis);
           textJarak = itemView.findViewById(R.id.textJarak);
           textAwal = itemView.findViewById(R.id.textMulai);
           textAkhir = itemView.findViewById(R.id.textAkhir);
           textJabatan = itemView.findViewById(R.id.textJabatan);
           textStatus = itemView.findViewById(R.id.textStatus);
           textAbsensi = itemView.findViewById(R.id.textAbsensi);
           textUrgensi = itemView.findViewById(R.id.textUrgensi);
           textPrioritas = itemView.findViewById(R.id.textPrioritas);
       }

       public void binding(AgendaTable agenda){
           textName.setText(agenda.getName());
           textJenis.setText(agenda.getMeeting());
           textJarak.setText(agenda.getJarak());
           textAwal.setText(agenda.getAwal());
           textAkhir.setText(agenda.getAkhir());
           textJabatan.setText(agenda.getJabatan());
           textStatus.setText(agenda.getStatus());
           textAbsensi.setText(agenda.getAbsensi());
           textUrgensi.setText(agenda.getUrgensi());
           textPrioritas.setText(agenda.getPrioritas());

       }
   }
}
