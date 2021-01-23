package com.example.afl_monitoringandroidapp.statusTabs;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.afl_monitoringandroidapp.R;

import java.util.ArrayList;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder>{

    ArrayList<String> mtextview1;
    ArrayList<String> mtextview2;
    ArrayList<String> mtextview3;
    private ArrayList<String> mIds;
    private ArrayList<String> mpkado;
    private ArrayList<String> mpkdda;
    private boolean isComplete = false;
    private boolean isOngoing = false;
    private boolean isPending = false;
    Context mcontext;
    ArrayList<String> mtextview3_address;

    public  ItemAdapter(Context mcontext, ArrayList<String> mtextview1, ArrayList<String> mtextview2, ArrayList<String> mtextview3, ArrayList<String> mIds, ArrayList<String> ado_pk, ArrayList<String> dda_pk, boolean isPending, boolean isOngoing, boolean isComplete) {
        this.mcontext = mcontext;
        // this.mtextview_letter = mtextview_letter;
        this.mtextview1 = mtextview1;
        this.mtextview2 = mtextview2;
        this.mtextview3 = mtextview3;
        this.isPending = isPending;
        this.isOngoing = isOngoing;
        this.isComplete = isComplete;
        mpkado = ado_pk;
        mpkdda = dda_pk;
        this.mIds = mIds;

        this.mtextview3_address = new ArrayList<>(mtextview3);
    }

    public ItemAdapter(Context context, ArrayList<String> did, ArrayList<String> dlocation_name, ArrayList<String> dlocation_address, ArrayList<String> dadoName, ArrayList<String> dDdaName, boolean isPending, boolean isOngoing, boolean isCompleted) {
        this.mtextview1 = dDdaName;
        this.mtextview2 = dadoName;
        this.mtextview3 = dlocation_address;
        this.isPending = isPending;
        this.isOngoing = isOngoing;
        this.isComplete = isCompleted;
        this.mcontext = context;
        this.mIds = did;


    }

    @NonNull
    @Override
    public ItemAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_adapter_layout, parent, false);
        return new ItemAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemAdapter.ViewHolder holder, int position) {
        String tv1 = mtextview1.get(position);
        String tv2 = mtextview2.get(position);
        String tv3 = mtextview3.get(position);
        String x= String.valueOf(mtextview3.get(position).charAt(0));
        holder.bind(tv1,tv2,tv3,x);
    }

    @Override
    public int getItemCount() {
        return mtextview1.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        //private TextView itemName;
        TextView tv_letter;
        TextView tv1;
        TextView tv2;
        TextView tv3;
        RelativeLayout parentnotassigned;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            parentnotassigned = itemView.findViewById(R.id.adminlocation);
            tv_letter = itemView.findViewById(R.id.my_letter);
            tv1 = itemView.findViewById(R.id.dda_name);
            tv2 = itemView.findViewById(R.id.ada_name);
            tv3 = itemView.findViewById(R.id.address);
            itemView.setOnClickListener(this);
            //itemName = itemView.findViewById(R.id.list_item_text_view);
        }
        public void bind(String tv1g,String tv2g, String tv3g,String letter){
            tv1.setText("DDA:     " + tv1g.toUpperCase());
            tv2.setText("ADO:     " + tv2g.toUpperCase());
            tv3.setText(tv3g);
            tv_letter.setText(letter);
//            if(is_DDA_user){
//                tv2.setVisibility(View.GONE);
//            }else{
//                tv2.setText("ADO:     " + tv2g.toUpperCase());
//            }

        }
        @Override
        public void onClick(View v) {
            }
        }
    }
