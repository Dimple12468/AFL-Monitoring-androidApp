 package com.example.afl_monitoringandroidapp;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

 public class DistrictAdoAdapter extends RecyclerView.Adapter<DistrictAdoAdapter.ViewHolder> implements Filterable {

    // View view1;
     ArrayList<String> mtextview1;
     ArrayList<String> mtextview2;
     ArrayList<String> image_URL;

     ArrayList<String> mtextview1_all_ado;
     ArrayList<String> mtextview1_all;
     public boolean mShowShimmer = true;
     Context mcontext;
     private boolean isDdoFragment;
     ArrayList<String> mUserId;
     private ArrayList<String> mPkList;
     private ArrayList<String> mDdoNames;
     private ArrayList<String> mDistrictNames;
     private boolean isBusy = false;
     private String TAG = "DistrictAdoAdapter";
     private TextView tv3;
     private boolean is_settings_clicked = false;

     //DDA->pending->assign
     private ArrayList<String> mtextview1_dda;
     private Map<Integer, ArrayList<String>> mtextview2_dda;
     private String locationID;
     private int currentAdo_Pos = -1;
     private ArrayList<String> AdoId;
     boolean isDDAuser = false;


     public DistrictAdoAdapter(Context mcontext, ArrayList<String> mtextview1, ArrayList<String> mtextview2,
                               ArrayList<String> mUserId, boolean isDdoFragment, ArrayList<String> mPkList, ArrayList<String> mDdoNames, ArrayList<String> mDistrictNames, ArrayList<String> image_url){// boolean is_settings_clicked) {
         this.mtextview1 = mtextview1;
         this.mtextview2 = mtextview2;
         this.mcontext = mcontext;
         this.isDdoFragment = isDdoFragment;
         this.mUserId = mUserId;
         this.mPkList = mPkList;
         this.mDdoNames = mDdoNames;
         this.mDistrictNames = mDistrictNames;
         this.image_URL = image_url;

         Log.d("Image URL " + TAG , "in constructor constructor: "+ image_url);

         //this.mtextview1_all_ado = new ArrayList<>(mtextview1);
     }

     public DistrictAdoAdapter(Context mcontext, ArrayList<String> mtextview1, ArrayList<String> mtextview2,
                               ArrayList<String> mUserId, boolean isDdoFragment, ArrayList<String> pkList, ArrayList<String> image_URL) {
         this.mtextview1 = mtextview1;
         this.mtextview2 = mtextview2;
         this.mUserId = mUserId;
         this.mcontext = mcontext;
         this.isDdoFragment = isDdoFragment;
         mPkList = pkList;
         this.image_URL = image_URL;
     }

     public DistrictAdoAdapter(Context mcontext, ArrayList<String> mtextview1, ArrayList<String> mtextview2,
                               ArrayList<String> mUserId, boolean isDdoFragment, ArrayList<String> pkList, boolean is_settings_clicked) {
         this.mtextview1 = mtextview1;
         this.mtextview2 = mtextview2;
         this.mUserId = mUserId;
         this.mcontext = mcontext;
         this.isDdoFragment = isDdoFragment;
         mPkList = pkList;
         this.is_settings_clicked = is_settings_clicked;
         //this.mtextview1_all = new ArrayList<>(mtextview1);
     }

     //constructor for radio button
     public DistrictAdoAdapter(Context mcontext, ArrayList<String> mtextview1, ArrayList<String> mtextview2,
                               ArrayList<String> mUserId, boolean isDdoFragment, ArrayList<String> mPkList, ArrayList<String> mDdoNames, ArrayList<String> mDistrictNames, Boolean is_settings_clicked) {
         this.mcontext = mcontext;
         this.mtextview1 = mtextview1;
         this.mtextview2 = mtextview2;
         this.isDdoFragment = isDdoFragment;
         this.mUserId = mUserId;
         this.mPkList = mPkList;
         this.mDdoNames = mDdoNames;
         this.mDistrictNames = mDistrictNames;
         this.is_settings_clicked = is_settings_clicked;


     }

     //constructor for DDA(assign ado (background))
     public DistrictAdoAdapter(Context mcontext, ArrayList<String> mtextview1, Map<Integer, ArrayList<String>> mtextview2) {
         this.mcontext = mcontext;
         this.mtextview1_dda = mtextview1;
         this.mtextview2_dda = mtextview2;
         AdoId = new ArrayList<>();
     }

     public DistrictAdoAdapter(boolean isDDAuser){
         this.isDDAuser=isDDAuser;
     }

     public void getLocationID(String lid){
         this.locationID = lid;
     }

     public void getCurrentADO(int pos) {
         currentAdo_Pos = pos;
     }

     public void getAdoId(String adoid){
         this.AdoId.add(adoid);
     }




     @NonNull
     @Override
     public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
         View view1 = LayoutInflater.from(mcontext).inflate(R.layout.district_ado_adapter,parent,false);
         return new ViewHolder(view1);
     }

     @Override
     public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
         holder.tv1.setBackground(null);
         holder.tv2.setBackground(null);
         holder.tv1.setText(mtextview1.get(position));
         holder.tv2.setText(mtextview2.get(position));
 //        holder.imageView.setImageResource(Integer.parseInt(image.get(position)));

         holder.tv2.setVisibility(View.GONE);


         if (!isDdoFragment) {
             holder.districtTextview.setText("DDA : " + mDdoNames.get(position).toUpperCase());/* + " (" + mDistrictNames.get(position) + ")");*/
             holder.districtTextview.setBackground(null);
         } else {
             holder.districtTextview.setVisibility(View.GONE);
             if (isDDAuser){
                 holder.tv1.setText(mtextview1_dda.get(position));
             }
         }



     }

     @Override
     public int getItemCount() {
         return  mtextview1.size();
     }

     //search filter in toolbar
     @Override
     public Filter getFilter() {
         return filter;
     }

     Filter filter = new Filter() {
         @Override
         protected FilterResults performFiltering(CharSequence constraint) {

             ArrayList<String> filtered_list_ado;
             if (!isDdoFragment) {
                 filtered_list_ado = new ArrayList<>();
                 if (constraint.toString().isEmpty()) {
                     filtered_list_ado.addAll(mtextview1_all_ado);
                 } else {
                     for (String address_ddo : mtextview1_all_ado) {
                         if (address_ddo.toLowerCase().contains(constraint.toString().toLowerCase().trim())) {
                             filtered_list_ado.add(address_ddo);
                         }//todo add message for no reults found
                     }
                 }
             }else {
                 filtered_list_ado = new ArrayList<>();
                 if (constraint.toString().isEmpty()) {
                     filtered_list_ado.addAll(mtextview1_all);
                 } else {
                     for (String address_ddo : mtextview1_all) {
                         if (address_ddo.toLowerCase().contains(constraint.toString().toLowerCase().trim())) {
                             filtered_list_ado.add(address_ddo);
                         }
                     }
                 }
             }

             FilterResults filterResults_ddo = new FilterResults();
             filterResults_ddo.count = filtered_list_ado.size();
             filterResults_ddo.values = filtered_list_ado;

             return filterResults_ddo;
         }

         @Override
         protected void publishResults(CharSequence constraint, FilterResults results) {

             if (!isDdoFragment) {
                 mtextview1.clear();
                 mtextview1.addAll((Collection<? extends String>) results.values);
                 notifyDataSetChanged();
             }else {
                 mtextview1.clear();
                 mtextview1.addAll((Collection<? extends String>) results.values);
                 notifyDataSetChanged();
             }
         }
     };

     public void show_suggestions(ArrayList<String> username) {

         if (!isDdoFragment) {
             this.mtextview1_all_ado = new ArrayList<>(username);
         }
         else {
             this.mtextview1_all = new ArrayList<>(username);
         }
     }
     //end of search suggestions filter

     public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
         ImageView imageView;
         TextView tv1;
         TextView tv2;
         RelativeLayout relativeLayout;
         TextView districtTextview;
         RadioButton radioButton;

         public ViewHolder(@NonNull View itemView) {
             super(itemView);
             itemView.setOnClickListener(this);
             imageView = itemView.findViewById(R.id.imageUser_);
             tv1= itemView.findViewById(R.id.tvuser);
             tv2= itemView.findViewById(R.id.tvinfo);
             relativeLayout = itemView.findViewById(R.id.relativeLayout2);
             districtTextview = itemView.findViewById(R.id.district_info);
             radioButton = itemView.findViewById(R.id.offer_select);

             if (is_settings_clicked){
                 radioButton.setVisibility(View.VISIBLE);
             }

             String newImageLink = "null";
             String toString = image_URL.get(0);
             Log.d(TAG,"toString " + toString);
             Log.d(TAG,"image URL in: " + image_URL);

             char anc = toString.charAt(4);
             Log.d(TAG,"image URL with anc: " + anc);
             int comp = Character.compare(anc, 's');
             if(comp!=0){
                  newImageLink = "https" + toString.substring(4);
                  Log.d(TAG,"image URL with s: " + newImageLink);
             }
             Picasso.get().load(newImageLink).error(R.drawable.user_image).into(imageView,new com.squareup.picasso.Callback() {
                 @Override
                 public void onSuccess() {
                 }
                 @Override
                 public void onError(Exception e) {
                     Log.d("error with image link: ",e.getMessage());
                 }
             });

 //            new DownloadImage(imageView,image);
 //            Log.d("Image URL in " + TAG, "image url is:" + image_URL);
 //            new DownloadImage(imageView,image_URL).execute();

 //            Glide.with(mcontext).load(image_URL).into(imageView);
         }
         @Override
         public void onClick(View v) {
             /*
             Intent intent =  new Intent(mcontext, AdoDdo_Activity.class);
             intent.putExtra("Id", mUserId.get(this.getAdapterPosition()));
             if(isDdoFragment) {
                 intent.putExtra("isDdo", true);
             }
             else {
                 intent.putExtra("isDdo", false);
             }
             intent.putExtra("name", mtextview1.get(this.getAdapterPosition()));
             mcontext.startActivity(intent);

              */


         }
     }

 }
