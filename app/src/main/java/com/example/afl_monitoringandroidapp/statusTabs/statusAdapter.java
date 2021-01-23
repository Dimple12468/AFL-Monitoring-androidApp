package com.example.afl_monitoringandroidapp.statusTabs;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.afl_monitoringandroidapp.R;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class statusAdapter extends RecyclerView.Adapter<statusAdapter.ViewHolder> {

    public ArrayList<section> sectionList;
    private Context context;
    private ItemAdapter itemAdapter;

    public statusAdapter(Context context, ArrayList<section> sections) {
        sectionList = sections;
        this.context = context;
    }

    @NonNull
    @Override
    public statusAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.status_adapter_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull statusAdapter.ViewHolder holder, int position) {
        section section = sectionList.get(position);
        holder.bind(section);
    }

    @Override
    public int getItemCount() {
        return sectionList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView sectionName;
        private RecyclerView itemRecyclerView;
        private String fromdate;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            sectionName = itemView.findViewById(R.id.sortedDate);
            itemRecyclerView = itemView.findViewById(R.id.sortedRVData);
        }

        public void bind(section section) {
            String fromDateFormat = "yyyy-MM-dd";
            String fromdate = section.getSectionTitle();
            String CheckFormat = "dd MMM yyyy";
            String dateStringFrom;
            DateFormat FromDF = new SimpleDateFormat(fromDateFormat);
            FromDF.setLenient(false);  // this is important!
            Date FromDate = null;
            try {
                FromDate = FromDF.parse(fromdate);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            dateStringFrom = new SimpleDateFormat(CheckFormat).format(FromDate);
            String xy = dateStringFrom.toString();
            sectionName.setText(xy);
            //sectionName.setText(section.getSectionTitle());
            //Toast.makeText(context, "Section name set", Toast.LENGTH_LONG).show();
            // RecyclerView for items
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
            itemRecyclerView.setLayoutManager(linearLayoutManager);
            itemAdapter = new ItemAdapter(context,section.getDid(), section.getDlocation_name(), section.getDlocation_address(), section.getDadoName(), section.getDddaName(),section.getIsPending(),section.getIsOngoing(),section.getIsCompleted());
            itemRecyclerView.setAdapter(itemAdapter);

        }
    }
}
