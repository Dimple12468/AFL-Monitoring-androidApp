package com.example.afl_monitoringandroidapp;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;

public class ReportImageRecyAdapter extends RecyclerView.Adapter<ReportImageRecyAdapter.CustomViewHolder> {
    private Context mContext;
    private ArrayList<String> mImages;

    //tag
    private final String TAG = "report_adapter";

    public ReportImageRecyAdapter(Context mContext, ArrayList<String> mImages) {
        this.mContext = mContext;
        this.mImages = mImages;
    }

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder: rectest1 "+ mContext+mImages);
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.ado_report_image_item, parent, false);
        return new CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {
        Glide.with(mContext).load(mImages.get(position))
                .apply(new RequestOptions().error(R.mipmap.no_entry_background))
                .into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return mImages.size();
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView imageView;
        ImageButton cross;

        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.ado_report_image);
            cross = itemView.findViewById(R.id.cross);
            cross.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = this.getAdapterPosition();
            //Toast.makeText(mContext,"clickdc "+position,Toast.LENGTH_LONG).show();
            mImages.remove(position);
            notifyItemRemoved(getAdapterPosition());
            notifyItemRangeChanged(getAdapterPosition(),mImages.size());
            Toast.makeText(mContext,"clickdc "+mImages,Toast.LENGTH_LONG).show();
            Log.d("array","array is "+mImages.toString()+" position is "+getAdapterPosition());

        }
    }
}
