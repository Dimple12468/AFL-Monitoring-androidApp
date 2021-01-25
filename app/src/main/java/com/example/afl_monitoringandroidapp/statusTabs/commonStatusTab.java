package com.example.afl_monitoringandroidapp.statusTabs;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.afl_monitoringandroidapp.Globals;
import com.example.afl_monitoringandroidapp.R;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class commonStatusTab extends Fragment {

    private String statusURL,nextURL,key;
    private static final String ARG_TAB_NUMBER = "tab_number";
    private String TAG = "STATUS: ";
    private StatusViewModel statusViewModel;
    private RecyclerView recyclerView;
    private ProgressBar pbCenterLoading,pbBottomLoading;
    private SwipeRefreshLayout swipeRefreshLayout;
//    private TextView textView;
    private int tabCount;
    String text = null;
    private statusAdapter statusAdapter;
    private LinearLayoutManager linearLayoutManager;
    private ArrayList<section> sections = new ArrayList<>();
    private boolean isNextBusy = false, doubleBackToExitPressedOnce = false;
    private boolean isPending, isOngoing, isCompleted;
    String predate1;
    private ArrayList<String> date;
    private ArrayList<String> ADOName,DDAName;
    private ArrayList<String> mID_,ADOId,DDAId;
    private ArrayList<String> address;

    private ArrayList<String> dateCombine,ADONameCombine,DDANameCombine,AddressCombine;
    private String commonDate;

    public commonStatusTab() {
        // Required empty public constructor
    }

    public static commonStatusTab newInstance(int tabCnt) {
        commonStatusTab fragment = new commonStatusTab();
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_TAB_NUMBER, tabCnt);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        statusViewModel = new ViewModelProvider(this).get(StatusViewModel.class);
        tabCount = 1;
        if (getArguments() != null) {
            tabCount = getArguments().getInt(ARG_TAB_NUMBER);
        }
        statusViewModel.setIndex(tabCount);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_status_common, container, false);
//        TextView textView = root.findViewById(R.id.statusCompleted);
//        textView = root.findViewById(R.id.dateHeader);
        recyclerView = root.findViewById(R.id.statusRView);
        pbCenterLoading = root.findViewById(R.id.pb_centerLoading);
        pbBottomLoading = root.findViewById(R.id.pb_bottomLoading);
        swipeRefreshLayout = root.findViewById(R.id.refreshPull);

        this.date = new ArrayList<>();
        this.ADOName = new ArrayList<>();
        this.DDAName = new ArrayList<>();
        this.mID_ = new ArrayList<>();
        this.ADOId = new ArrayList<>();
        this.DDAId = new ArrayList<>();
        this.address = new ArrayList<>();

        this.dateCombine = new ArrayList<>();
        this.ADONameCombine = new ArrayList<>();
        this.DDANameCombine = new ArrayList<>();
        this.AddressCombine = new ArrayList<>();


        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener(){
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                int topRowVerticalPosition =
                        (recyclerView == null || recyclerView.getChildCount() == 0) ? 0 : recyclerView.getChildAt(0).getTop();
                swipeRefreshLayout.setEnabled(topRowVerticalPosition >= 0);
                //swipeRefreshLayout.setRefreshing(false);

            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }
        });

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getFragmentManager().beginTransaction().detach(commonStatusTab.this).attach(commonStatusTab.this).commit();
            }
        });

        switch (tabCount){
            case 1:
                isPending = true; isOngoing = false; isCompleted = false;
                statusURL = Globals.pendingDatewiseList;
//                getLocations(statusURL);
                getdata(statusURL);
//                combineData();
                break;
            case 2:
                isPending = false; isOngoing = true; isCompleted = false;
                statusURL = Globals.ongoingDatewiseList;
//                getLocations(statusURL);
                getdata(statusURL);
//                combineData();
                break;
            case 3:
                isPending = false; isOngoing = false; isCompleted = true;
                statusURL = Globals.completedDatewiseList;
//                getLocations(statusURL);
                getdata(statusURL);
//                combineData();
                break;
        }


        statusAdapter = new statusAdapter(getActivity(),sections);
        recyclerView.setAdapter(statusAdapter);
        statusAdapter.notifyDataSetChanged();

        linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        DividerItemDecoration divider = new DividerItemDecoration(getActivity(), linearLayoutManager.getOrientation());
        recyclerView.addItemDecoration(divider);
//        statusViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
//            }
//        });



//        TabPageAdapter position = new TabPageAdapter(getChildFragmentManager(),getContext());
//        switch (position.getPosition()){
//            case 0:
//                statusURL = Globals.pendingDatewiseList;
//                textView.setText("This is pending fragment");
//                break;
//            case 1:
//                statusURL = Globals.ongoingDatewiseList;
//                textView.setText("This is ongoing fragment");
//                break;
//            case 2:
//                statusURL = Globals.completedDatewiseList;
//                textView.setText("This is completed fragment");
//                break;
//
//        }
//        textView.setText("This is completed fragment.");
        return root;
    }

    public void getdata(String URL){
        this.statusURL = URL;
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, URL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject rootObject = new JSONObject(String.valueOf(response));
                    nextURL = rootObject.getString("next");

                    Log.d(TAG, "onResponse1: "+nextURL);
                    JSONArray resultsArray = rootObject.getJSONArray("results");
                    Log.d(TAG, "onResponse2: ");
                    if(resultsArray.length()== 0) {
                        statusAdapter.notifyDataSetChanged();
                        Log.d(TAG, "onResponse3: ");
//                        nothing_toshow_fragment abc = new nothing_toshow_fragment();
//                        AppCompatActivity activity = (AppCompatActivity) getContext();
//                        activity.getSupportFragmentManager().beginTransaction().replace(R.id.nothing,abc).commit();
                    }
                    for (int i = 0; i < resultsArray.length(); i++) {
                        JSONObject singleObject = resultsArray.getJSONObject(i);
                        String did = singleObject.getString("id");
                        String dlocation_name = singleObject.getString("village_name") + ", "+singleObject.getString("block");

                        String villName,distName;
                        try {
                            JSONObject villageOBJ = singleObject.getJSONObject("village_name");
                            villName = villageOBJ.getString("village");
                        } catch (JSONException e) {
                            villName = "NULL";
                        }

                        try {
                            JSONObject distOBJ = singleObject.getJSONObject("district");
                            distName = distOBJ.getString("district");
                        } catch (JSONException e) {
                            distName = "NULL";
                        }
                        String dlocation_address = villName + ", " + singleObject.getString("block") + ", " + distName ;
                        String ddate = singleObject.getString("acq_date");

                        String nameDDA,nameADO;
                        try {
                            JSONObject ddaOBJ = singleObject.getJSONObject("dda");
                            JSONObject userDDA = ddaOBJ.getJSONObject("user");
                            nameDDA = userDDA.getString("name");
                        } catch (JSONException e) {
                            nameDDA = "Not Assigned";
                        }

                        try {
                            JSONObject adoOBJ = singleObject.getJSONObject("ado");
                            JSONObject userADO = adoOBJ.getJSONObject("user");
                            nameADO = userADO.getString("name");
                        } catch (JSONException e) {
                            nameADO = "Not Assigned";
                        }
                        date.add(ddate);
                        ADOName.add(nameADO);
                        DDAName.add(nameDDA);
                        address.add(dlocation_address);
                        mID_.add(did);
                    }
                    if (!nextURL.equals("null")){
                        nextURL = "https" + nextURL.substring(4);
                        getdata(nextURL);
                    }
//                    Log.d(TAG,date + "\n" + address);
                    combineData();

                    statusAdapter.notifyDataSetChanged();
                    isNextBusy = false;
                    pbBottomLoading.setVisibility(View.GONE);
                    pbCenterLoading.setVisibility(View.GONE);
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity(),"An exception occurred",Toast.LENGTH_LONG).show();
                    Log.d(TAG, "onResponse: inside the exception" + e);
                    pbCenterLoading.setVisibility(View.GONE);
                    pbBottomLoading.setVisibility(View.GONE);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "onErrorResponse: inside the the error exception" + error);
                if(error instanceof NoConnectionError || error instanceof TimeoutError){
                    //Toast.makeText(getActivity(), "This error is no internet", Toast.LENGTH_LONG).show();
                    final BottomSheetDialog mBottomDialogNotificationAction = new BottomSheetDialog(getActivity());
                    View sheetView = getActivity().getLayoutInflater().inflate(R.layout.no_internet, null);
                    mBottomDialogNotificationAction.setContentView(sheetView);
                    mBottomDialogNotificationAction.setCancelable(false);
                    mBottomDialogNotificationAction.show();
                    // Remove default white color background

                    FrameLayout bottomSheet = (FrameLayout) mBottomDialogNotificationAction.findViewById(com.google.android.material.R.id.design_bottom_sheet);
                    bottomSheet.setBackground(null);


                    TextView close = sheetView.findViewById(R.id.close);
                    Button retry = sheetView.findViewById(R.id.retry);
                    retry.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mBottomDialogNotificationAction.dismiss();
                            pbCenterLoading.setVisibility(View.VISIBLE);
                            getFragmentManager().beginTransaction().detach(commonStatusTab.this).attach(commonStatusTab.this).commit();
                            //getData(url);
                        }
                    });
                    close.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (!doubleBackToExitPressedOnce) {
                                doubleBackToExitPressedOnce = true;
                                Toast toast = Toast.makeText(getActivity(), "Tap on Close App again to exit app", Toast.LENGTH_LONG);
                                toast.setGravity(Gravity.CENTER, 0, 0);
                                toast.show();


                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        doubleBackToExitPressedOnce = false;
                                    }
                                }, 3600);
                            } else {
                                mBottomDialogNotificationAction.dismiss();
                                Intent a = new Intent(Intent.ACTION_MAIN);//will exit app
                                a.addCategory(Intent.CATEGORY_HOME);
                                a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(a);
                            }
                        }

                    });
                }
                else if (error instanceof AuthFailureError) {
                    // Error indicating that there was an Authentication Failure while performing the request
                    Toast.makeText(getActivity(), "This error is case2", Toast.LENGTH_LONG).show();
                } else if (error instanceof ServerError) {
                    //Indicates that the server responded with a error response
                    Toast.makeText(getActivity(), "This error is server error", Toast.LENGTH_LONG).show();
                } else if (error instanceof NetworkError) {
                    //Indicates that there was network error while performing the request
                    Toast.makeText(getActivity(), "This error is case4", Toast.LENGTH_LONG).show();
                } else if (error instanceof ParseError) {
                    // Indicates that the server response could not be parsed
                    Toast.makeText(getActivity(), "This error is case5", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getActivity(), "An unknown error occurred.", Toast.LENGTH_SHORT).show();
                }
                pbCenterLoading.setVisibility(View.GONE);
                pbBottomLoading.setVisibility(View.GONE);
                isNextBusy = false;
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> map = new HashMap<>();
                SharedPreferences prefs = getActivity().getSharedPreferences("tokenFile", Context.MODE_PRIVATE);
                String token = prefs.getString("key", "");
                map.put("Authorization", "Token " + token);
                return map;
            }
        };

        requestQueue.add(jsonObjectRequest);
        jsonObjectRequest.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 50000;
            }

            @Override
            public int getCurrentRetryCount() {
                return 50000;
            }

            @Override
            public void retry(VolleyError error) throws VolleyError {

            }
        });

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            int totalCount, pastItemCount, visibleItemCount;

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                Log.d(TAG, "onScrolled: out DX " + dx + " DY " + dy);
                if (dy > 0) {
                    Log.d(TAG, "onScrolled: inside");
                    totalCount = linearLayoutManager.getItemCount();
                    pastItemCount = linearLayoutManager.findFirstVisibleItemPosition();
                    visibleItemCount = linearLayoutManager.getChildCount();
                    if ((pastItemCount + visibleItemCount) >= totalCount) {
                        if (!isNextBusy)
                            combineData();
                    }
                }
                super.onScrolled(recyclerView, dx, dy);
            }
        });

    }


    public void combineData(){
        sections = new ArrayList<>();
        int x;
        for (x=0;x<date.size();x++){
//            if (x==(date.size()-1)) {
//                Log.d(TAG,"in break");
//                break;
//            }
            if (x==0){
                commonDate = date.get(x);
                ADONameCombine.add(ADOName.get(x));
                DDANameCombine.add(DDAName.get(x));
                AddressCombine.add(address.get(x));
            }
            else if(date.get(x-1).equals(date.get(x))){
                Log.d(TAG,"in elseif");
                commonDate = date.get(x);
                ADONameCombine.add(ADOName.get(x));
                DDANameCombine.add(DDAName.get(x));
                AddressCombine.add(address.get(x));
            }
            else{
                Log.d(TAG,"in else");
                sections.add(new section(commonDate,mID_, AddressCombine, AddressCombine,ADONameCombine,DDANameCombine,isPending,isOngoing,isCompleted));
                commonDate = date.get(x);
                this.ADONameCombine = new ArrayList<>();
                this.DDANameCombine = new ArrayList<>();
                this.AddressCombine = new ArrayList<>();
                ADONameCombine.add(ADOName.get(x));
                DDANameCombine.add(DDAName.get(x));
                AddressCombine.add(address.get(x));
            }
        }
        sections.add(new section(commonDate,mID_, AddressCombine, AddressCombine,ADONameCombine,DDANameCombine,isPending,isOngoing,isCompleted));

//        statusAdapter.notifyDataSetChanged();
//        isNextBusy = false;
//        pbBottomLoading.setVisibility(View.GONE);
//        pbCenterLoading.setVisibility(View.GONE);
    }


    public void getLocations(String URL){
        sections = new ArrayList<>();
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, URL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject rootObject = new JSONObject(String.valueOf(response));
                    nextURL = rootObject.getString("next");
                    if (!nextURL.equals("null"))
                        nextURL = "https" + nextURL.substring(4);
                    Log.d(TAG, "onResponse1: "+nextURL);
                    JSONArray resultsArray = rootObject.getJSONArray("results");
                    Log.d(TAG, "onResponse2: "+resultsArray);
                    if(resultsArray.length()== 0){
                        statusAdapter.notifyDataSetChanged();
                        Log.d(TAG, "onResponse3: ");
//                        todo
//                        nothing_toshow_fragment abc = new nothing_toshow_fragment();
//                        AppCompatActivity activity = (AppCompatActivity) getContext();
//                        activity.getSupportFragmentManager().beginTransaction().replace(R.id.nothing,abc).commit();
                    }


                    String[][] arr = new String[6][resultsArray.length()];
                    for (int i = 0; i < resultsArray.length(); i++) {
                        JSONObject singleObject = resultsArray.getJSONObject(i);
                        String did = singleObject.getString("id");
                        String dlocation_name = singleObject.getString("village_name") + ", "+singleObject.getString("block");

                        String villName,distName;
                        try {
                            JSONObject villageOBJ = singleObject.getJSONObject("village_name");
                            villName = villageOBJ.getString("village");
                        } catch (JSONException e) {
                            villName = "NULL";
                        }

                        try {
                            JSONObject distOBJ = singleObject.getJSONObject("district");
                            distName = distOBJ.getString("district");
                        } catch (JSONException e) {
                            distName = "NULL";
                        }
                        String dlocation_address = villName + ", " + singleObject.getString("block") + ", " + distName ;
//                        String dlongitude = singleObject.getString("longitude");
//                        String dlatitude = singleObject.getString("latitude");
                        String ddate = singleObject.getString("acq_date");

                        String nameDDA,nameADO;
                        try {
                            JSONObject ddaOBJ = singleObject.getJSONObject("dda");
                            JSONObject userDDA = ddaOBJ.getJSONObject("user");
                            nameDDA = userDDA.getString("name");
                        } catch (JSONException e) {
                            nameDDA = "Not Assigned";
                        }

                        try {
                            JSONObject adoOBJ = singleObject.getJSONObject("ado");
                            JSONObject userADO = adoOBJ.getJSONObject("user");
                            nameADO = userADO.getString("name");
                        } catch (JSONException e) {
                            nameADO = "Not Assigned";
                        }


                        arr[0][i]=ddate;
                        arr[1][i]=did;
                        arr[2][i]=dlocation_name;
                        arr[3][i]=dlocation_address;
                        arr[4][i]=nameADO;
                        arr[5][i]=nameDDA;
                    }
                    Log.d(TAG, "onResponse4: ");
                    String inter;
                    for(int i=0;i<resultsArray.length()-1;i++){
                        for(int j=0;j<resultsArray.length()-i-1;j++){
                            String idate = arr[0][j];
                            String ndate = arr[0][j+1];
                            SimpleDateFormat sdfo = new SimpleDateFormat("yyyy-MM-dd");
                            // Get the two dates to be compared
                            Date d1 = null;
                            try {
                                d1 = sdfo.parse(idate);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            Date d2 = null;
                            try {
                                d2 = sdfo.parse(ndate);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            if (d1.compareTo(d2) < 0) {
                                for(int k=0;k<6;k++) {
                                    inter = arr[k][j];
                                    arr[k][j] = arr[k][j + 1];
                                    arr[k][j + 1] = inter;
                                }
                            }
                        }
                    }
                    Log.d(TAG, "onResponse5: ");
                    ArrayList<String> mDid = new ArrayList<>();
                    ArrayList<String> mDlocation_name = new ArrayList<>();
                    ArrayList<String> mDlocation_address = new ArrayList<>();
//                    ArrayList<String> mlatitude= new ArrayList<>();
//                    ArrayList<String> mlongitude= new ArrayList<>();
                    ArrayList<String> mNameDDA= new ArrayList<>();
                    ArrayList<String> mNameADO= new ArrayList<>();
                    String predate=arr[0][0];
                    //String predate = null;
                    String idate = "";
                    for(int i=0;i<resultsArray.length();i++){
                        idate = arr[0][i];
                        if(predate.equals(idate)){
                            mDid.add(arr[1][i]);
                            mDlocation_name.add(arr[2][i]);
                            mDlocation_address.add(arr[3][i]);
                            mNameADO.add(arr[4][i]);
                            mNameDDA.add(arr[5][i]);

                            //predate=idate;
                        }
                        else{
                            sections.add(new section(predate,mDid, mDlocation_name, mDlocation_address,mNameADO,mNameDDA,isPending,isOngoing,isCompleted));
                            mDid = new ArrayList<>();
                            mDlocation_name = new ArrayList<>();
                            mDlocation_address = new ArrayList<>();
                            mNameADO = new ArrayList<>();
                            mNameDDA = new ArrayList<>();
                            mDid.add(arr[1][i]);
                            mDlocation_name.add(arr[2][i]);
                            mDlocation_address.add(arr[3][i]);
                            mNameADO.add(arr[4][i]);
                            mNameDDA.add(arr[5][i]);
                            //date.equals(idate);
                        }
                        //predate.equals(idate);
                        predate=idate;
                    }
                    Log.d(TAG, "onResponse6: " + sections);
                    sections.add(new section(predate,mDid, mDlocation_name, mDlocation_address,mNameADO,mNameDDA,isPending,isOngoing,isCompleted));
                    Log.d(TAG, "onResponse7: " + sections);
                    //Toast.makeText(getActivity(),arr.toString(),Toast.LENGTH_LONG).show();

                    statusAdapter.notifyDataSetChanged();
                    isNextBusy = false;
                    pbBottomLoading.setVisibility(View.GONE);
                    pbCenterLoading.setVisibility(View.GONE);
                    Log.d(TAG, "onResponse8: ");
                    predate1=idate;


                } catch (JSONException e){
                    e.printStackTrace();
                    Toast.makeText(getActivity(),"An exception occured",Toast.LENGTH_LONG).show();
                    Log.d(TAG, "onResponse: inside the evception" + e);
                    pbCenterLoading.setVisibility(View.GONE);
                    pbBottomLoading.setVisibility(View.GONE);

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "onErrorResponse: inside the the error exception" + error);
                if(error instanceof NoConnectionError || error instanceof TimeoutError){
                    //Toast.makeText(getActivity(), "This error is no internet", Toast.LENGTH_LONG).show();
                    final BottomSheetDialog mBottomDialogNotificationAction = new BottomSheetDialog(getActivity());
                    View sheetView = getActivity().getLayoutInflater().inflate(R.layout.no_internet, null);
                    mBottomDialogNotificationAction.setContentView(sheetView);
                    mBottomDialogNotificationAction.setCancelable(false);
                    mBottomDialogNotificationAction.show();
                    // Remove default white color background

                    FrameLayout bottomSheet = (FrameLayout) mBottomDialogNotificationAction.findViewById(com.google.android.material.R.id.design_bottom_sheet);
                    bottomSheet.setBackground(null);


                    TextView close = sheetView.findViewById(R.id.close);
                    Button retry = sheetView.findViewById(R.id.retry);
                    retry.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mBottomDialogNotificationAction.dismiss();
                            pbCenterLoading.setVisibility(View.VISIBLE);
                            getFragmentManager().beginTransaction().detach(commonStatusTab.this).attach(commonStatusTab.this).commit();
                            //getData(url);
                        }
                    });
                    close.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (!doubleBackToExitPressedOnce) {
                                doubleBackToExitPressedOnce = true;
                                Toast toast = Toast.makeText(getActivity(), "Tap on Close App again to exit app", Toast.LENGTH_LONG);
                                toast.setGravity(Gravity.CENTER, 0, 0);
                                toast.show();


                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        doubleBackToExitPressedOnce = false;
                                    }
                                }, 3600);
                            } else {
                                mBottomDialogNotificationAction.dismiss();
                                Intent a = new Intent(Intent.ACTION_MAIN);//will exit app
                                a.addCategory(Intent.CATEGORY_HOME);
                                a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(a);
                            }
                        }

                    });
                }
                else if (error instanceof AuthFailureError) {
                    // Error indicating that there was an Authentication Failure while performing the request
                    Toast.makeText(getActivity(), "This error is case2", Toast.LENGTH_LONG).show();
                } else if (error instanceof ServerError) {
                    //Indicates that the server responded with a error response
                    Toast.makeText(getActivity(), "This error is server error", Toast.LENGTH_LONG).show();
                } else if (error instanceof NetworkError) {
                    //Indicates that there was network error while performing the request
                    Toast.makeText(getActivity(), "This error is case4", Toast.LENGTH_LONG).show();
                } else if (error instanceof ParseError) {
                    // Indicates that the server response could not be parsed
                    Toast.makeText(getActivity(), "This error is case5", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getActivity(), "An unknown error occurred.", Toast.LENGTH_SHORT).show();
                }
                pbCenterLoading.setVisibility(View.GONE);
                pbBottomLoading.setVisibility(View.GONE);
                isNextBusy = false;
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> map = new HashMap<>();
                SharedPreferences prefs = getActivity().getSharedPreferences("tokenFile", Context.MODE_PRIVATE);
                String token = prefs.getString("key", "");
                map.put("Authorization", "Token " + token);
                return map;
            }
        };

        requestQueue.add(jsonObjectRequest);
        jsonObjectRequest.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 50000;
            }

            @Override
            public int getCurrentRetryCount() {
                return 50000;
            }

            @Override
            public void retry(VolleyError error) throws VolleyError {

            }
        });

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            int totalCount, pastItemCount, visibleItemCount;

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                Log.d(TAG, "onScrolled: out DX " + dx + " DY " + dy);
                if (dy > 0) {
                    Log.d(TAG, "onScrolled: inside");
                    totalCount = linearLayoutManager.getItemCount();
                    pastItemCount = linearLayoutManager.findFirstVisibleItemPosition();
                    visibleItemCount = linearLayoutManager.getChildCount();
                    if ((pastItemCount + visibleItemCount) >= totalCount) {
                        if (!isNextBusy)
                            loadNextLocations();
                    }
                }
                super.onScrolled(recyclerView, dx, dy);
            }
        });

    }

    public void loadNextLocations() {
//        if (!nextURL.equals("null")) {
//            getNextLocations();
//        }
    }

    public void getNextLocations(){
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        pbBottomLoading.setVisibility(View.VISIBLE);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, nextURL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    Log.d(TAG, "onResponse1: ");
                    JSONObject rootObject = new JSONObject(String.valueOf(response));
                    nextURL = rootObject.getString("next");
                    if (!nextURL.equals("null"))
                        nextURL = "https" + nextURL.substring(4);
                    JSONArray resultsArray = rootObject.getJSONArray("results");
                    Log.d(TAG, "onResponse2: ");
                    String[][] arr = new String[6][resultsArray.length()];
                    for (int i = 0; i < resultsArray.length(); i++) {
                        JSONObject singleObject = resultsArray.getJSONObject(i);
                        String did = singleObject.getString("id");
                        String dlocation_name = singleObject.getString("village_name") + ", "+singleObject.getString("block");

                        String villName,distName;
                        try {
                            JSONObject villageOBJ = singleObject.getJSONObject("village_name");
                            villName = villageOBJ.getString("village");
                        } catch (JSONException e) {
                            villName = "NULL";
                        }

                        try {
                            JSONObject distOBJ = singleObject.getJSONObject("district");
                            distName = distOBJ.getString("district");
                        } catch (JSONException e) {
                            distName = "NULL";
                        }
                        String dlocation_address = villName + ", " + singleObject.getString("block") + ", " + distName ;

//                        String dlongitude = singleObject.getString("longitude");
//                        String dlatitude = singleObject.getString("latitude");
                        String ddate = singleObject.getString("acq_date");

                        String nameDDA,nameADO;
                        try {
                            JSONObject ddaOBJ = singleObject.getJSONObject("dda");
                            JSONObject userDDA = ddaOBJ.getJSONObject("user");
                            nameDDA = userDDA.getString("name");
                        } catch (JSONException e) {
                            nameDDA = "Not Assigned";
                        }

                        try {
                            JSONObject adoOBJ = singleObject.getJSONObject("ado");
                            JSONObject userADO = adoOBJ.getJSONObject("user");
                            nameADO = userADO.getString("name");
                        } catch (JSONException e) {
                            nameADO = "Not Assigned";
                        }

                        arr[0][i]=ddate;
                        arr[1][i]=did;
                        arr[2][i]=dlocation_name;
                        arr[3][i]=dlocation_address;
                        arr[4][i]=nameADO;
                        arr[5][i]=nameDDA;
                    }
                    Log.d(TAG, "onResponse4: ");
                    String inter;
                    for(int i=0;i<resultsArray.length()-1;i++){
                        for(int j=0;j<resultsArray.length()-i-1;j++){
                            String idate = arr[0][j];
                            String ndate = arr[0][j+1];
                            SimpleDateFormat sdfo = new SimpleDateFormat("yyyy-MM-dd");
                            // Get the two dates to be compared
                            Date d1 = null;
                            try {
                                d1 = sdfo.parse(idate);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            Date d2 = null;
                            try {
                                d2 = sdfo.parse(ndate);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            if (d1.compareTo(d2) < 0) {
                                for(int k=0;k<6;k++) {
                                    inter = arr[k][j];
                                    arr[k][j] = arr[k][j + 1];
                                    arr[k][j + 1] = inter;
                                }
                            }
                        }
                    }
                    Log.d(TAG, "onResponse5: ");
                    ArrayList<String> mDid = new ArrayList<>();
                    ArrayList<String> mDlocation_name = new ArrayList<>();
                    ArrayList<String> mDlocation_address = new ArrayList<>();
//                    ArrayList<String> mlatitude= new ArrayList<>();
//                    ArrayList<String> mlongitude= new ArrayList<>();
                    ArrayList<String> mNameDDA= new ArrayList<>();
                    ArrayList<String> mNameADO= new ArrayList<>();
                    String predate=predate1;//arr[0][0];
                    //String predate = null;
                    for(int i=0;i<resultsArray.length();i++){
                        String idate = arr[0][i];
//                        if(predate1.equals(predate)&&i==0){
//                            Log.d("DIMPLE SEE: ","I am here" + predate + "\n" + predate1);
//                            mDid.add(arr[1][i]);
//                            mDlocation_name.add(arr[2][i]);
//                            mDlocation_address.add(arr[3][i]);
//                            mNameADO.add(arr[4][i]);
//                            mNameDDA.add(arr[5][i]);
//                        }
                        if(predate.equals(idate)){
                            mDid.add(arr[1][i]);
                            mDlocation_name.add(arr[2][i]);
                            mDlocation_address.add(arr[3][i]);
                            mNameADO.add(arr[4][i]);
                            mNameDDA.add(arr[5][i]);

                            //predate=idate;
                        }
                        else{
                            sections.add(new section(predate,mDid, mDlocation_name, mDlocation_address,mNameADO,mNameDDA,isPending,isOngoing,isCompleted));
                            mDid = new ArrayList<>();
                            mDlocation_name = new ArrayList<>();
                            mDlocation_address = new ArrayList<>();
                            mNameADO = new ArrayList<>();
                            mNameDDA = new ArrayList<>();
                            mDid.add(arr[1][i]);
                            mDlocation_name.add(arr[2][i]);
                            mDlocation_address.add(arr[3][i]);
                            mNameADO.add(arr[4][i]);
                            mNameDDA.add(arr[5][i]);
                            //date.equals(idate);
                        }
                        //predate.equals(idate);
                        predate=idate;
                    }
                    Log.d(TAG, "onResponse6: ");
                    sections.add(new section(predate,mDid, mDlocation_name, mDlocation_address,mNameADO,mNameDDA,isPending,isOngoing,isCompleted));
                    Log.d(TAG, "onResponse7: ");
                    //Toast.makeText(getActivity(),arr.toString(),Toast.LENGTH_LONG).show();

                    statusAdapter.notifyDataSetChanged();
                    isNextBusy = false;
                    pbBottomLoading.setVisibility(View.GONE);
                    pbCenterLoading.setVisibility(View.GONE);
                    Log.d(TAG, "onResponse8: ");


                } catch (JSONException e){
                    e.printStackTrace();
                    Toast.makeText(getActivity(),"An exception occurred",Toast.LENGTH_LONG).show();
                    Log.d(TAG, "onResponse: inside the exception" + e);
                    pbCenterLoading.setVisibility(View.GONE);
                    pbBottomLoading.setVisibility(View.GONE);

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "onErrorResponse: inside the the error exception" + error);
                if(error instanceof NoConnectionError || error instanceof TimeoutError){
                    //Toast.makeText(getActivity(), "This error is no internet", Toast.LENGTH_LONG).show();
                    final BottomSheetDialog mBottomDialogNotificationAction = new BottomSheetDialog(getActivity());
                    View sheetView = getActivity().getLayoutInflater().inflate(R.layout.no_internet, null);
                    mBottomDialogNotificationAction.setContentView(sheetView);
                    mBottomDialogNotificationAction.setCancelable(false);
                    mBottomDialogNotificationAction.show();
                    // Remove default white color background

                    FrameLayout bottomSheet = (FrameLayout) mBottomDialogNotificationAction.findViewById(com.google.android.material.R.id.design_bottom_sheet);
                    bottomSheet.setBackground(null);


                    TextView close = sheetView.findViewById(R.id.close);
                    Button retry = sheetView.findViewById(R.id.retry);
                    retry.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mBottomDialogNotificationAction.dismiss();
                            pbCenterLoading.setVisibility(View.VISIBLE);
                            getFragmentManager().beginTransaction().detach(commonStatusTab.this).attach(commonStatusTab.this).commit();
                            //getData(url);
                        }
                    });
                    close.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (!doubleBackToExitPressedOnce) {
                                doubleBackToExitPressedOnce = true;
                                Toast toast = Toast.makeText(getActivity(), "Tap on Close App again to exit app", Toast.LENGTH_LONG);
                                toast.setGravity(Gravity.CENTER, 0, 0);
                                toast.show();


                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        doubleBackToExitPressedOnce = false;
                                    }
                                }, 3600);
                            } else {
                                mBottomDialogNotificationAction.dismiss();
                                Intent a = new Intent(Intent.ACTION_MAIN);//will exit app
                                a.addCategory(Intent.CATEGORY_HOME);
                                a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(a);
                            }
                        }

                    });
                }
                else if (error instanceof AuthFailureError) {
                    // Error indicating that there was an Authentication Failure while performing the request
                    Toast.makeText(getActivity(), "This error is case2", Toast.LENGTH_LONG).show();
                } else if (error instanceof ServerError) {
                    //Indicates that the server responded with a error response
                    Toast.makeText(getActivity(), "This error is server error", Toast.LENGTH_LONG).show();
                } else if (error instanceof NetworkError) {
                    //Indicates that there was network error while performing the request
                    Toast.makeText(getActivity(), "This error is case4", Toast.LENGTH_LONG).show();
                } else if (error instanceof ParseError) {
                    // Indicates that the server response could not be parsed
                    Toast.makeText(getActivity(), "This error is case5", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getActivity(), "An unknown error occurred.", Toast.LENGTH_SHORT).show();
                }
                pbCenterLoading.setVisibility(View.GONE);
                pbBottomLoading.setVisibility(View.GONE);
                isNextBusy = false;
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> map = new HashMap<>();
                SharedPreferences prefs = getActivity().getSharedPreferences("tokenFile", Context.MODE_PRIVATE);
                String token = prefs.getString("key", "");
                map.put("Authorization", "Token " + token);
                return map;
            }
        };

        requestQueue.add(jsonObjectRequest);
        jsonObjectRequest.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 50000;
            }

            @Override
            public int getCurrentRetryCount() {
                return 50000;
            }

            @Override
            public void retry(VolleyError error) throws VolleyError {

            }
        });
        requestQueue.add(jsonObjectRequest);
        requestFinished(requestQueue);

    }

    private void requestFinished(RequestQueue queue) {

        queue.addRequestFinishedListener(new RequestQueue.RequestFinishedListener<Object>() {

            @Override
            public void onRequestFinished(Request<Object> request) {
                pbBottomLoading.setVisibility(View.GONE);
            }
        });

    }
}