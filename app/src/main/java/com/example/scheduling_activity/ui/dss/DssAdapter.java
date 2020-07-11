package com.example.scheduling_activity.ui.dss;

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

public class DssAdapter extends RecyclerView.Adapter<DssAdapter.ViewHolder> {

    private List<Result> mData;
    private LayoutInflater mInflater;

    public DssAdapter(Context context, List<Result> data){
        this.mInflater = LayoutInflater.from(context);
        this.mData =  data;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_dss, parent, false);
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
        private TextView textScore;

       public ViewHolder(@NonNull View itemView) {
           super(itemView);
           textName = itemView.findViewById(R.id.textName);
           textScore = itemView.findViewById(R.id.textScore);
       }

       public void binding(Result result){
           textName.setText(result.getName());
           textScore.setText(result.getScore());


       }
   }
}
