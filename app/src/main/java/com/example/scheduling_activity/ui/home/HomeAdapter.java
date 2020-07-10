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

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView textName;
        private TextView textJenis;
        private TextView textStatus;
        private TextView textJarak;
        private TextView textJabatan;

       public ViewHolder(@NonNull View itemView) {
           super(itemView);
           textName = itemView.findViewById(R.id.textName);
           textJenis = itemView.findViewById(R.id.textJenis);
           textJarak = itemView.findViewById(R.id.textJarak);
           textJabatan = itemView.findViewById(R.id.textJabatan);
           textStatus = itemView.findViewById(R.id.textStatus);
       }

       public void binding(AgendaTable agenda){
           textName.setText(agenda.getName());
           textJenis.setText(agenda.getMeeting());
           textJarak.setText(agenda.getJarak());
           textJabatan.setText(agenda.getJabatan());
           textStatus.setText(agenda.getStatus());


       }
   }
}
