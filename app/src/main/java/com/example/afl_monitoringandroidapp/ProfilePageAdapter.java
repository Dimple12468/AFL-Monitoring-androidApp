package com.example.afl_monitoringandroidapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ProfilePageAdapter extends RecyclerView.Adapter<ProfilePageAdapter.ViewHolder> {
    Context context;
    ArrayList<String> mlocation;
    ArrayList<String> mdistrict;
    private boolean isComplete = false;
    private boolean isOngoing = false;
    private boolean isPending = false;

    public ProfilePageAdapter(Context context,ArrayList<String> mlocation,ArrayList<String> mdistrict,boolean isPending,boolean isOngoing,boolean isComplete){
        this.context = context;
        this.mlocation = mlocation;
        this.mdistrict = mdistrict;
        this.isPending = isPending;
        this.isOngoing = isOngoing;
        this.isComplete = isComplete;
    }


    @NonNull
    @Override
    public ProfilePageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.profile_page_adapter,parent,false);
        return new ViewHolder(view);

        //return null;
    }

    @Override
    public void onBindViewHolder(@NonNull ProfilePageAdapter.ViewHolder holder, int position) {
        holder.tvlocation.setText(mlocation.get(position));
        holder.tvdistrict.setText(mdistrict.get(position));
        holder.tvimage.setImageResource(R.drawable.detail_pic);

    }

    @Override
    public int getItemCount() {
        return mdistrict.size();
        //return 0;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvlocation,tvdistrict;
        ImageView tvimage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvlocation = itemView.findViewById(R.id.profile_location);
            tvdistrict = itemView.findViewById(R.id.profile_district);
            tvimage = itemView.findViewById(R.id.imageView9);
        }
    }
}
