package com.example.afl_monitoringandroidapp.adminTabs;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
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
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.afl_monitoringandroidapp.Globals;
import com.example.afl_monitoringandroidapp.InitialPage;
import com.example.afl_monitoringandroidapp.R;
import com.example.afl_monitoringandroidapp.RecyclerViewAdapter_district;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class adminAdo extends Fragment {

    private adminAdoViewModel adminAdoViewModel;

    private ArrayList<String> username;
    private ArrayList<String> userinfo;
    private ArrayList<String> mUserId;
    private ArrayList<String> mPkList;
    private ArrayList<String> mDdoNames;
    private ArrayList<String> mDistrictNames;
    private ArrayList<String> mdistrictlist;
    boolean doubleBackToExitPressedOnce = false;

    private String district_list_url;

    private String token;
    private GridLayoutManager gridlayout;

    //private View view;
    private final String TAG = "ado_fragment";
    private RecyclerView Rview;
    private AlertDialog dialog;

    private RecyclerView adolist;
    private RecyclerViewAdapter_district customadapter;
    //private RecyclerViewAdapter_district customadapter;
    ProgressBar spinner;

    MenuItem searchItem;
    MenuItem searchItem_filter;
    TextView title_top;

    public adminAdo() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        adminAdoViewModel = new ViewModelProvider(this).get(adminAdoViewModel.class);
        View view = inflater.inflate(R.layout.fragment_admin_ado, container, false);

        /*
        final TextView textView = root.findViewById(R.id.adm_ado);
        adminAdoViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });

         */
        spinner = view.findViewById(R.id.ado_progress);
        spinner.setVisibility(View.VISIBLE);
        adolist = view.findViewById(R.id.adolist);

        //district_list_url = Globals.districtUrl;                 //= "http://18.224.202.135/api/district/";
        district_list_url = Globals.districtUrl;
        Log.d(TAG, district_list_url);

        username = new ArrayList<>();
        userinfo = new ArrayList<>();
        mUserId = new ArrayList<>();
        mPkList = new ArrayList<>();
        mDdoNames = new ArrayList<>();
        mDistrictNames = new ArrayList<>();
        mdistrictlist = new ArrayList<>();

        Toolbar toolbar = (Toolbar) view.findViewById(R.id.app__bar_ado);
        AppCompatActivity appCompatActivity = (AppCompatActivity) getActivity();
        appCompatActivity.setSupportActionBar(toolbar);
        setHasOptionsMenu(true);
        title_top = view.findViewById(R.id.topTitleName);
        appCompatActivity.getSupportActionBar().setDisplayShowTitleEnabled(false);

        // title_top = view.findViewById(R.id.app_name);
        if (view.isEnabled()) {
            title_top.setText("ADO");
        } else {
            title_top.setText("AFL Monitoring");
        }

        SharedPreferences preferences = getActivity().getSharedPreferences("tokenFile", Context.MODE_PRIVATE);
        token = preferences.getString("key", "");

        gridlayout = new GridLayoutManager(getActivity(), 1);
        adolist.setLayoutManager(gridlayout);
        getData();

        return view;
    }


    public void getData() {
        RequestQueue district_requestQueue = Volley.newRequestQueue(getActivity());

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, district_list_url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                if (response.length() == 0) {
                    //todo add image
                    /////////view.setBackground(getActivity().getResources().getDrawable(R.drawable.svg_nothing_toshow_1));
                }

                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject singleObject = response.getJSONObject(i);
                        if (singleObject.getString("district").equalsIgnoreCase("gurugram"))
                            continue;
                        mdistrictlist.add(singleObject.getString("district"));
                    } catch (JSONException e) {
                        Toast.makeText(getActivity(), "You encountered an exception", Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    }
                }

                Collections.sort(mdistrictlist);

                //customadapter = new RecyclerViewAdapter_district(getActivity(), mdistrictlist);
                customadapter = new RecyclerViewAdapter_district(getContext(),mdistrictlist);
                adolist.setAdapter(customadapter);
                spinner.setVisibility(View.GONE);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                spinner.setVisibility(View.GONE);
                //Toast.makeText(getActivity(), "something went wrong", Toast.LENGTH_LONG).show();
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    //This indicates that the reuest has either time out or there is no connection
                    /***********
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
                    spinner.setVisibility(View.VISIBLE);
                    getData();
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
                     */


                } else if (error instanceof AuthFailureError) {
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

            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> map = new HashMap<>();
                map.put("Authorization", "Token " + token);
                return map;
            }
        };

        district_requestQueue.add(jsonArrayRequest);


    }

    /********
     public void InitializeFragment(Fragment fragment) {


     AppCompatActivity activity = (AppCompatActivity) fragment.getContext();
     activity.getSupportFragmentManager().beginTransaction().add(R.id.frameLayout, fragment, "a").addToBackStack("a").commit();


     }
     */
}
