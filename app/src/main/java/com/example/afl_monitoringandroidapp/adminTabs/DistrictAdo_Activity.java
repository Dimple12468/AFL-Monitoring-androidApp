package com.example.afl_monitoringandroidapp.adminTabs;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.example.afl_monitoringandroidapp.DistrictAdoAdapter;
import com.example.afl_monitoringandroidapp.Globals;
import com.example.afl_monitoringandroidapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DistrictAdo_Activity extends AppCompatActivity {

    private ArrayList<String> username;
    private ArrayList<String> userinfo;
    private ArrayList<String> mUserId;
    private ArrayList<String> mPkList;
    private ArrayList<String> mDdoNames;
    private ArrayList<String> mDistrictNames;
    private ArrayList<String> image;
    private String ado_list;
    private String curr_dist;
    private String district_list_url;

    //private RecyclerViewAdater recyclerViewAdater;
    private DistrictAdoAdapter recyclerViewAdater;
    private String token;
    private String nextUrl;
    private ProgressBar progressBar;
    private LinearLayoutManager layoutManager;
    private GridLayoutManager gridlayout;
    private boolean isNextBusy = false;
    private RelativeLayout relativeLayout;
    private final String TAG = "district Ado";
    private RecyclerView Rview;
    private AlertDialog dialog;
    private TextView title;
    private ProgressBar spinner;
    boolean doubleBackToExitPressedOnce = false;

    ImageButton Ib,Ib1,Ib2,Ib3;
    TextView tv_edit;
    View v1,v2;
    Boolean is_settings_clicked = false;
    LinearLayout ll;

    //TextView title_top;
    String text;
    ///////////////////AdminActivity a;
//    DistrictAdo d;

    //for back button on action bar
    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_district_ado);
        Log.d(TAG,"in onCreate: ");

        Bundle bundle = getIntent().getExtras();
        curr_dist = bundle.getString("district");

        Toolbar toolbar = (Toolbar) findViewById(R.id.app__bar_district_ado);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ll = findViewById(R.id.for_edit_ado);
        ado_list="";
//        image="";
        district_list_url = Globals.districtUrl;                                        //"http://18.224.202.135/api/district/";
        username = new ArrayList<>();
        userinfo = new ArrayList<>();
        mUserId = new ArrayList<>();
        mPkList = new ArrayList<>();
        mDdoNames = new ArrayList<>();
        mDistrictNames = new ArrayList<>();
        image = new ArrayList<>();
        ///////////////a = new AdminActivity();
//        d = new DistrictAdo();

        tv_edit = findViewById(R.id.tv_edit);
        Ib = findViewById(R.id.ib_edit);
        Ib1 = findViewById(R.id.ib1_edit);
        Ib2 = findViewById(R.id.ib2_delete);
        Ib3 = findViewById(R.id.ib3_settings_fill);
        v1 = findViewById(R.id.view_edit);
        v2 = findViewById(R.id.vd1);

        Ib.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v1.setVisibility(View.GONE);
                Ib.setVisibility(View.GONE);

                Ib1.setVisibility(View.VISIBLE);
                Ib2.setVisibility(View.VISIBLE);
                Ib3.setVisibility(View.VISIBLE);
                v2.setVisibility(View.VISIBLE);
                is_settings_clicked = true;
                DistrictAdoAdapter adapt = new DistrictAdoAdapter(DistrictAdo_Activity.this, username, userinfo, mUserId, false, mPkList, mDdoNames, mDistrictNames,is_settings_clicked);
                Rview.setAdapter(adapt);

            }
        });

        Ib1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(DistrictAdo_Activity.this, "Edit clicked", Toast.LENGTH_SHORT).show();
            }
        });

        Ib2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(DistrictAdo_Activity.this, "Delete clicked", Toast.LENGTH_SHORT).show();
            }
        });

        Ib3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v1.setVisibility(View.VISIBLE);
                Ib.setVisibility(View.VISIBLE);

                Ib1.setVisibility(View.GONE);
                Ib2.setVisibility(View.GONE);
                Ib3.setVisibility(View.GONE);
                v2.setVisibility(View.GONE);
                is_settings_clicked = false;

                DistrictAdoAdapter adapt = new DistrictAdoAdapter(DistrictAdo_Activity.this, username, userinfo, mUserId, false, mPkList, mDdoNames, mDistrictNames,is_settings_clicked);
                Rview.setAdapter(adapt);
            }
        });

        String low_title = curr_dist.toLowerCase();;
        int len=low_title.length();
        char one = curr_dist.charAt(0);
        String rest = low_title.substring(1,len);
        String whole= one + rest;
        text = whole+"'s ADO";
//        getSupportActionBar().setTitle(text);

        TextView title_top = findViewById(R.id.topTitleName);
//        if (view.isEnabled()){
            title_top.setText(text);
//        }else {
//            title_top.setText("AFL Monitoring");
//        }


        spinner = findViewById(R.id.ado_progressbar);
        spinner.setVisibility(View.VISIBLE);

        progressBar = findViewById(R.id.ado_list_progressbar1);
        relativeLayout = findViewById(R.id.relativeLayout1);
        recyclerViewAdater = new DistrictAdoAdapter(DistrictAdo_Activity.this, username, userinfo, mUserId, false, mPkList, mDdoNames, mDistrictNames,image);//is_settings_clicked);
        Rview = findViewById(R.id.recyclerViewado1);
        Rview.setAdapter(recyclerViewAdater);
        SharedPreferences preferences = DistrictAdo_Activity.this.getSharedPreferences("tokenFile", Context.MODE_PRIVATE);
        token = preferences.getString("key", "");
        layoutManager = new LinearLayoutManager(DistrictAdo_Activity.this);
        Rview.setLayoutManager(layoutManager);
        DividerItemDecoration divider = new DividerItemDecoration(DistrictAdo_Activity.this, layoutManager.getOrientation());
        Rview.addItemDecoration(divider);


        ado_list= Globals.usersListADO + curr_dist;                                  //"http://18.224.202.135/api/users-list/ado/?search="+curr_dist;
        getData();

    }


    public void getData(){
        spinner.setVisibility(View.GONE);
        final RequestQueue requestQueue = Volley.newRequestQueue(DistrictAdo_Activity.this);
        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, ado_list, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject rootObject = new JSONObject(String.valueOf(response));
                    nextUrl = rootObject.getString("next");
                    Log.d(TAG, "onResponse: " + nextUrl);
                    JSONArray resultsArray = rootObject.getJSONArray("results");

                    if(resultsArray.length()== 0){
                        Log.d(TAG,"results array seems to be empty" + resultsArray);
                        ll.setVisibility(View.GONE);
                        recyclerViewAdater.notifyDataSetChanged();
                        //todo add image
                        setContentView(R.layout.fragment_nothing_toshow);
                    }
                    for (int i = 0; i < resultsArray.length(); i++)
                    {
                        JSONObject singleObject = resultsArray.getJSONObject(i);
                        JSONObject userobj = singleObject.getJSONObject("user");
                        username.add(userobj.getString("name").toUpperCase());
                        JSONArray villageArray = singleObject.getJSONArray("village_ado");
                        Log.d(TAG, "onResponse: LENGTH " + villageArray.length());
                        if (villageArray.length() == 0)
                            userinfo.add("NOT ASSIGNED");
                        for (int j = 0; j < 1; j++) {               //j<villageArray.length()
                            try {
                                JSONObject villageObject = villageArray.getJSONObject(i);
                                userinfo.add(villageObject.getString("village").toUpperCase());
                            } catch (JSONException e) {
                                userinfo.add("NOT ASSIGNED");
                            }
                        }
//                        JSONObject authObject = singleObject.getJSONObject("auth_user");
                        String pk = userobj.getString("id");
                        mPkList.add(pk);
                        String id = singleObject.getString("id");
                        mUserId.add(id);

                        String image_url ;                      //= " ";
                        try {
                            image_url = userobj.getString("image");
                            image.add(image_url);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        Log.d("Image URL ", "image url is:" + image);

                        try {
                            JSONObject ddaObject = singleObject.getJSONObject("dda");
                            String ddaName = ddaObject.getString("name");
                            mDdoNames.add(ddaName);
                            try {
                                JSONObject districtObject = ddaObject.getJSONObject("district");
                                String districtName = districtObject.getString("district");
                                mDistrictNames.add(districtName.toUpperCase());
                            } catch (JSONException e) {
                                mDistrictNames.add("NOT ASSIGNED");
                            }
                        } catch (JSONException e) {
                            mDdoNames.add("Not Assigned");
                        }
                    }
                    //recyclerViewAdater.mShowShimmer = false;
                    recyclerViewAdater.notifyDataSetChanged();
                    recyclerViewAdater.show_suggestions(username);
                    spinner.setVisibility(View.GONE);
                    //dialog.dismiss();



                } catch (JSONException e) {
                    spinner.setVisibility(View.GONE);
                    Toast.makeText(DistrictAdo_Activity.this,"An exception occured",Toast.LENGTH_LONG).show();
                    Log.e(TAG, "onResponse: JSON" + e);
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                spinner.setVisibility(View.GONE);
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    //This indicates that the reuest has either time out or there is no connection
                    Toast.makeText(DistrictAdo_Activity.this, "This error is case1", Toast.LENGTH_LONG).show();
                    /*
                    final BottomSheetDialog mBottomDialogNotificationAction = new BottomSheetDialog(DistrictAdo_Activity.this);
//                    View sheetView = getActivity().getLayoutInflater().inflate(R.layout.no_internet, null);
                    LayoutInflater li = LayoutInflater.from(DistrictAdo_Activity.this);
                    View sheetView = li.inflate(R.layout.no_internet, null);
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
                                Toast toast = Toast.makeText(com.theagriculture.app.Admin.DistrictAdo_Activity.this,"Tap on Close App again to exit app", Toast.LENGTH_LONG);
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
                    Toast.makeText(DistrictAdo_Activity.this, "This error is case2", Toast.LENGTH_LONG).show();
                } else if (error instanceof ServerError) {
                    //Indicates that the server responded with a error response
                    Toast.makeText(DistrictAdo_Activity.this, "This is a server error", Toast.LENGTH_LONG).show();
                } else if (error instanceof NetworkError) {
                    //Indicates that there was network error while performing the request
                    Toast.makeText(DistrictAdo_Activity.this, "This error is case4", Toast.LENGTH_LONG).show();
                } else if (error instanceof ParseError) {
                    // Indicates that the server response could not be parsed
                    Toast.makeText(DistrictAdo_Activity.this, "This error is case5", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(DistrictAdo_Activity.this, "An unknown error occurred.", Toast.LENGTH_SHORT).show();
                }
                Log.d(TAG, "onErrorResponse: " + error);
            }
        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> map = new HashMap<>();
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
        Rview.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {

                int totalCount, pastItemCount, visibleItemCount;
                if (dy > 0) {
                    totalCount = layoutManager.getItemCount();
                    pastItemCount = layoutManager.findFirstVisibleItemPosition();
                    visibleItemCount = layoutManager.getChildCount();
                    if ((pastItemCount + visibleItemCount) >= totalCount) {
                        Log.d(TAG, "onScrolled: " + totalCount + " " + pastItemCount + " " + visibleItemCount);
                        if (!nextUrl.equals("null") && !isNextBusy)
                            getNextAdos();
                    }
                }
                super.onScrolled(recyclerView, dx, dy);
            }
        });

    }

    private void getNextAdos() {
        RequestQueue requestQueue = Volley.newRequestQueue(DistrictAdo_Activity.this);
        isNextBusy = true;
        Log.d(TAG, "getNextAdos: count ");
        progressBar.setVisibility(View.VISIBLE);
        final JsonObjectRequest jsonArrayRequest = new JsonObjectRequest(Request.Method.GET, nextUrl, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject rootObject = new JSONObject(String.valueOf(response));
                    nextUrl = rootObject.getString("next");
                    JSONArray resultsArray = rootObject.getJSONArray("results");

                    for (int i = 0; i < resultsArray.length(); i++) {
                        JSONObject singleObject = resultsArray.getJSONObject(i);
                        JSONObject userobj = singleObject.getJSONObject("user");
                        username.add(userobj.getString("name").toUpperCase());
                        JSONArray villageArray = singleObject.getJSONArray("village_ado");
                        Log.d(TAG, "onResponse: LENGTH " + villageArray.length());
                        if (villageArray.length() == 0)
                            userinfo.add("NOT ASSIGNED");
                        for (int j = 0; j < 1; j++) {
                            try {
                                JSONObject villageObject = villageArray.getJSONObject(i);
                                userinfo.add(villageObject.getString("village").toUpperCase());
                            } catch (JSONException e) {
                                userinfo.add("NOT ASSIGNED");
                            }
                        }
//                        JSONObject authObject = singleObject.getJSONObject("auth_user");
                        String pk = userobj.getString("id");
                        mPkList.add(pk);
                        String id = singleObject.getString("id");
                        mUserId.add(id);

                        try {
                            JSONObject ddaObject = singleObject.getJSONObject("dda");
                            String ddaName = ddaObject.getString("name");
                            mDdoNames.add(ddaName);
                            try {
                                JSONObject districtObject = ddaObject.getJSONObject("district");
                                String districtName = districtObject.getString("district");
                                mDistrictNames.add(districtName.toUpperCase());
                            } catch (JSONException e) {
                                mDistrictNames.add("NOT ASSIGNED");
                            }
                        } catch (JSONException e) {
                            mDdoNames.add("Not Assigned");
                        }
                    }
                    Log.d(TAG, "onResponse: " + username);
                    recyclerViewAdater.notifyDataSetChanged();
                    isNextBusy = false;

                } catch (JSONException e) {
                    Log.e(TAG, "onResponse: " + e.getLocalizedMessage());
                    e.printStackTrace();
                }

            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error instanceof NoConnectionError)
                    Toast.makeText(DistrictAdo_Activity.this, "Check Your Internet Connection Please!", Toast.LENGTH_SHORT).show();
                isNextBusy = false;
            }
        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> map = new HashMap<>();
                map.put("Authorization", "Token " + token);
                return map;
            }
        };

        requestQueue.add(jsonArrayRequest);
        requestFinished(requestQueue);
        jsonArrayRequest.setRetryPolicy(new RetryPolicy() {
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
    }

    private void requestFinished(RequestQueue queue) {

        queue.addRequestFinishedListener(new RequestQueue.RequestFinishedListener<Object>() {

            @Override
            public void onRequestFinished(Request<Object> request) {
                progressBar.setVisibility(View.GONE);
            }
        });

    }
}
