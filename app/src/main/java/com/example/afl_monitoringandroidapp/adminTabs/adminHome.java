package com.example.afl_monitoringandroidapp.adminTabs;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.ui.AppBarConfiguration;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.afl_monitoringandroidapp.Globals;
import com.example.afl_monitoringandroidapp.InitialPage;
import com.example.afl_monitoringandroidapp.ProfilePage;
import com.example.afl_monitoringandroidapp.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.maps.android.clustering.ClusterManager;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static androidx.drawerlayout.widget.DrawerLayout.LOCK_MODE_LOCKED_CLOSED;
import static androidx.drawerlayout.widget.DrawerLayout.LOCK_MODE_UNLOCKED;

public class adminHome extends Fragment {

    private static adminHome mInstance;
    private adminHomeViewModel adminhomeViewModel;
    private SupportMapFragment mapFragment;
    private String token;
    private String TAG = "Map";
    private GoogleMap map = null;
    private String url_unassigned = null;
    private String url_assigned = null;
//    private String url_count = Globals.map_Count_Admin;
    private String next,next1;
    private RequestQueue requestQueue;
    private int count = 0 ;
    private ClusterManager<MyItem> mClusterManager;
    private ArrayList<Double> latitude;
    private ArrayList<Double> longitude;
    private ArrayList<String> villname;
    private String typeOfUser;
    private FloatingActionButton fab;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private AppBarConfiguration mAppBarConfiguration;
    private String position = "-";
    private String username, imagelink, newImageLink = " ";;


    public adminHome() {
        // Required empty public constructor
    }

//    @Override
//    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
//        inflater.inflate(R.menu.menu_top_bar,menu);
//        MenuItem searchItem = menu.findItem(R.id.search_in_title);
//        searchItem.setVisible(false);
//        super.onCreateOptionsMenu(menu, inflater);
//    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mInstance = this;
        requestQueue = Volley.newRequestQueue(getContext());
    }

    public static adminHome getInstance() {
        return mInstance;
    }

    public static adminHome newInstance(){//String URLassigned,String URLunassigned) {
        adminHome fragment = new adminHome();
//        Bundle bundle = new Bundle();
//        bundle.putString("Map URL assigned: ",URLassigned );
//        bundle.putString("Map URL Unassigned: ", URLunassigned);
//        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        adminhomeViewModel = new ViewModelProvider(this).get(adminHomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_admin_home, container, false);
        mapFragment = ((SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map));
        fab = (FloatingActionButton) root.findViewById(R.id.fab);

        drawerLayout = getActivity().findViewById(R.id.drawer_view);
        navigationView = getActivity().findViewById(R.id.navigation_view);
        drawerLayout.setDrawerLockMode(LOCK_MODE_LOCKED_CLOSED);

        View header = navigationView.getHeaderView(0);
        TextView textUsername = header.findViewById(R.id.name);
        TextView textUser = header.findViewById(R.id.type_of_user);
        ImageView userImage = header.findViewById(R.id.imageView);

        SharedPreferences preferences = getActivity().getSharedPreferences("tokenFile", Context.MODE_PRIVATE);
        typeOfUser = preferences.getString("role", "");
        token = preferences.getString("key", "");
        username = preferences.getString("Name","");
        imagelink  = preferences.getString("Image","");

//        if(typeOfUser.equals("5"))
//            position="Admin";
//        if(typeOfUser.equals("2"))
//            position="ADO";
//        if(typeOfUser.equals("4"))
//            position="DDA";


        TextView title_top = root.findViewById(R.id.topTitleName);
        if (root.isEnabled()){
            title_top.setText(R.string.home);
        }else {
            title_top.setText(R.string.app_name);
        }

        Toolbar toolbar = (Toolbar) root.findViewById(R.id.appTitleBar);
        AppCompatActivity appCompatActivity = (AppCompatActivity)getActivity();
        appCompatActivity.setSupportActionBar(toolbar);
        appCompatActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setHasOptionsMenu(true);
        appCompatActivity.getSupportActionBar().setDisplayShowTitleEnabled(false);

        latitude = new ArrayList<>();
        longitude = new ArrayList<>();
        villname = new ArrayList<>();

//        navigationView.inflateMenu(R.menu.admin_nav_drawer);
//        navigationView.inflateHeaderView(R.layout.header_layout);
//        mAppBarConfiguration = new AppBarConfiguration.Builder(
//                R.id.privacy, R.id.terms, R.id.help,R.id.advance_settings,R.id.logout_now)
//                .setDrawerLayout(drawerLayout)
//                .build();

        ActionBarDrawerToggle mDrawerToggle = new ActionBarDrawerToggle(
                getActivity(),drawerLayout,R.string.navigation_drawer_open,R.string.navigation_drawer_close)
        {
            @Override
            public void onDrawerClosed(View drawerView)
            {
                //super.onDrawerClosed(drawerView);
                drawerLayout.setDrawerLockMode(LOCK_MODE_LOCKED_CLOSED);
            }

            @Override
            public void onDrawerOpened(View drawerView)
            {
                //super.onDrawerOpened(drawerView);
                drawerLayout.setDrawerLockMode(LOCK_MODE_UNLOCKED);
            }
        };
        drawerLayout.addDrawerListener(mDrawerToggle);
        mDrawerToggle.setDrawerIndicatorEnabled(false);
        mDrawerToggle.setHomeAsUpIndicator(R.drawable.hamburger_icon);
        mDrawerToggle.syncState();

        switch (typeOfUser) {
            case "1":
                Toast.makeText(getActivity(), "login for farmer", Toast.LENGTH_SHORT).show();
                break;
            case "2":
                adminHome.newInstance();//url_assigned,url_unassigned);
                adminHome.getInstance();
                position="ADO";
                count =0;
                fab.setVisibility(View.GONE);
                url_assigned = Globals.map_Pending_ADO;
//                adminHome.newInstance();//url_assigned,url_unassigned);
                callForMap();
                break;
            case "3":
                Toast.makeText(getActivity(), "login for block admin", Toast.LENGTH_SHORT).show();
                break;
            case "4":
                adminHome.newInstance();//url_assigned,url_unassigned);
                adminHome.getInstance();
                position="DDA";
                count =0;
                fab.setVisibility(View.GONE);
                url_assigned = Globals.map_Assigned_DDA;
                url_unassigned = Globals.map_Unassigned_DDA;
//                adminHome.newInstance();//url_assigned,url_unassigned);
                callForMap();
                break;
            case "5":
                adminHome.newInstance();//url_assigned,url_unassigned);
                adminHome.getInstance();
                position="Admin";
                count =0;
                fab.setVisibility(View.VISIBLE);
                url_assigned = Globals.map_Assigned_Admin;
                url_unassigned = Globals.map_Unassigned_Admin;
//                callForMap(url_assigned,url_unassigned);
//                adminHome.newInstance();//url_assigned,url_unassigned);
                callForMap();
                break;
            case "6":
                Toast.makeText(getActivity(), "login for super admin", Toast.LENGTH_SHORT).show();
                break;
        }

        textUsername.setText(username);
        textUser.setText(position);

        //////////
        //for image click to profile
        userImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.closeDrawers();
                //Toast.makeText(getActivity(),"Synjnd",Toast.LENGTH_LONG).show();
                Intent intent= new Intent(getActivity(), ProfilePage.class);
                startActivity(intent);
            }
        });
        //////////////////

        char anc = imagelink.charAt(4);
        int comp = Character.compare(anc, 's');
        if(comp!=0){
            newImageLink = imagelink;
            newImageLink =  newImageLink.substring(4);
            newImageLink = "https" + newImageLink;
        }
        Picasso.get().load(newImageLink).error(R.drawable.user_image).into(userImage,new com.squareup.picasso.Callback() {
            @Override
            public void onSuccess() {
            }
            @Override
            public void onError(Exception e) {
                Log.d("error with image link: ",e.getMessage());
            }
        });

        navigationView.setBackgroundColor(getResources().getColor(R.color.white));
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()){

                    case R.id.logout_now:
                        SharedPreferences.Editor editor = getActivity().getSharedPreferences("tokenFile", Context.MODE_PRIVATE).edit();
                        editor.clear();
                        editor.commit();
                        drawerLayout.closeDrawers();
                        Intent intent = new Intent(getActivity(), InitialPage.class);
                        startActivity(intent);
                        getActivity().finish();
                        return true;

                    case R.id.privacy:
                        drawerLayout.closeDrawers();
                        Toast.makeText(getActivity(), "privacy clicked", Toast.LENGTH_SHORT).show();
                        return true;

                    case R.id.terms:
                        item.setChecked(true);
                        Toast.makeText(getActivity(), "terms clicked", Toast.LENGTH_SHORT).show();
                        drawerLayout.closeDrawers();
                        return true;

                    case R.id.help:
                        item.setChecked(true);
                        Toast.makeText(getActivity(), "help clicked", Toast.LENGTH_SHORT).show();
                        drawerLayout.closeDrawers();
                        return true;

                    case R.id.advance_settings:
                        item.setChecked(true);
                        Toast.makeText(getActivity(), "settings clicked", Toast.LENGTH_SHORT).show();
                        drawerLayout.closeDrawers();
                        return true;

                }
                return false;
            }
        });

//        final TextView textView = root.findViewById(R.id.text_home);
//        adminhomeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
//            }
//        });

//        Button button = root.findViewById(R.id.logOut);
//        button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                SharedPreferences.Editor editor = getActivity().getSharedPreferences("tokenFile", Context.MODE_PRIVATE).edit();
//                editor.clear();
//                editor.commit();
//                Intent intent = new Intent(getActivity(), InitialPage.class);
//                startActivity(intent);
//                getActivity().finish();
//            }
//        });
        return root;
    }

    void callForMap(/*String url_assigned, String url_unassigned*/){
        next = url_assigned;
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                map = googleMap;
                Log.d(TAG, "onMapReady:value if map is : " + map);

                LatLng one = new LatLng(7.798000, 68.14712);
                LatLng two = new LatLng(37.090000, 97.34466);

                LatLng shimala = new LatLng(31.104815,77.173401);
                LatLng jaipur = new LatLng(26.912434,75.787270);

                LatLngBounds.Builder builder = new LatLngBounds.Builder();
                LatLngBounds.Builder builder1 = new LatLngBounds.Builder();

                //add them to builder
                builder.include(one);
                builder.include(two);

                builder1.include(shimala);
                builder1.include(jaipur);

                LatLngBounds bounds = builder.build();
                LatLngBounds bounds1 = builder1.build();

                //get width and height to current display screen
                int width = getResources().getDisplayMetrics().widthPixels;
                int height = getResources().getDisplayMetrics().heightPixels;

                // 20% padding
                int padding = (int) (width * 0.20);

                //set latlong bounds
                map.setLatLngBoundsForCameraTarget(bounds);

                //move camera to fill the bound to screen
                map.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds1, width, height, padding));

                //set zoom to level to current so that you won't be able to zoom out viz. move outside bounds
                map.setMinZoomPreference(map.getCameraPosition().zoom);

                // mMapView = (MapView) mView.findViewById(R.id.map_admin);
                View toolbar = ((View) mapFragment.getView().findViewById(Integer.parseInt("1")).
                        getParent()).findViewById(Integer.parseInt("4"));
                RelativeLayout.LayoutParams rlp = (RelativeLayout.LayoutParams) toolbar.getLayoutParams();
                // position on top right
                rlp.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
                rlp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, 0);
                rlp.setMargins(0, 30, 30, 0);

            }
        });

//        getCount(url_count);
        getMarkers(next);

    }

    void getMarkers(String url) {
//        requestQueue = Volley.newRequestQueue(getContext());
        final JsonObjectRequest jsonObjectRequest2 = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject jsonObject = new JSONObject(String.valueOf(response));
                    JSONArray jsonArray = jsonObject.getJSONArray("results");

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject c = jsonArray.getJSONObject(i);
                        Double lat = Double.valueOf(c.getString("latitude"));
                        Double lon = Double.valueOf(c.getString("longitude"));
                        String vill;
                        try {
                            JSONObject villOBJ = c.getJSONObject("village_name");
                            vill = villOBJ.getString("village");
                        } catch (JSONException e) {
                            vill = "NULL";
                        }

                        latitude.add(lat);
                        longitude.add(lon);
                        villname.add(vill);
                    }

                } catch (JSONException e) {
                    Log.e(TAG, "onResponse: " + e.getLocalizedMessage());
                    e.printStackTrace();
//                    dialog.dismiss();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "onErrorResponse: " + error);
//                pbar.setVisibility(View.GONE);
//                dialog.dismiss();
                if(error instanceof NoConnectionError){
                    Toast.makeText(getActivity(), "Check your internet connection", Toast.LENGTH_LONG).show();
                }
                else {
                    Toast.makeText(getActivity(),"something went wrong",Toast.LENGTH_LONG).show();
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
        jsonObjectRequest2.setTag("MAP REQUEST");
        requestQueue.add(jsonObjectRequest2);
        jsonObjectRequest2.setRetryPolicy(new RetryPolicy() {
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
        requestFinished(requestQueue);
    }

    private void requestFinished(RequestQueue queue) {

        queue.addRequestFinishedListener(new RequestQueue.RequestFinishedListener<Object>() {

            @Override
            public void onRequestFinished(Request<Object> request) {
                Log.d(TAG, "onRequestFinished: here too");
                if (typeOfUser.equals("2")) marklocation();
                if (typeOfUser.equals("4") || typeOfUser.equals("5")) {
                    if (count == 0) nextRequest();
                    else if (count == 1) marklocation();
                }

            }
        });

    }

    void nextRequest(){
        count = 1;
        next= url_unassigned;
        getMarkers(next);
    }

    private void marklocation() {
        Log.d(TAG, "mark location: SIZE " + latitude.size());
        mClusterManager = new ClusterManager<MyItem>(getActivity(), map);
        addmarkers();
        map.setOnCameraIdleListener(mClusterManager);
        map.setOnMarkerClickListener(mClusterManager);
//        dialog.dismiss();
    }

    private void addmarkers() {

        for(int i = 0 ;i<latitude.size();i++){
            double lat = latitude.get(i);
            double lon = longitude.get(i);
            String title = villname.get(i);
            MyItem item = new MyItem(lat,lon,title);
            mClusterManager.addItem(item);
        }
        mClusterManager.cluster();
        Log.d(TAG, "add markers: CLUSTER SIZE " + mClusterManager.getRenderer());

    }


//    void getCount(String urlcount){
//
//        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
//
//        final JsonObjectRequest jsonObjectRequest2 = new JsonObjectRequest(Request.Method.GET, urlcount, null, new Response.Listener<JSONObject>() {
//            @Override
//            public void onResponse(JSONObject response) {
//                try {
//                    pendingView.setText(response.getString("pending_count"));
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//
//                try {
//                    ongoingView.setText(response.getString("ongoing_count"));
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//
//                try {
//                    completedView.setText(response.getString("completed_count"));
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Log.e(TAG, "onErrorResponse: " + error);
////                pbar.setVisibility(View.GONE);
//                dialog.dismiss();
//                if(error instanceof NoConnectionError){
//                    Toast.makeText(getActivity(), "Check your internet connection", Toast.LENGTH_LONG).show();
//                }
//                else {
//                    Toast.makeText(getActivity(),"something went wrong",Toast.LENGTH_LONG).show();
//                }
//            }
//        }) {
//
//            @Override
//            public Map<String, String> getHeaders() throws AuthFailureError{
//                HashMap<String, String> map = new HashMap<>();
//                map.put("Authorization", "Token " + token);
//                return map;
//            }
//        };
//        jsonObjectRequest2.setTag("MAP REQUEEST");
//        requestQueue.add(jsonObjectRequest2);
//        jsonObjectRequest2.setRetryPolicy(new RetryPolicy() {
//            @Override
//            public int getCurrentTimeout() {
//                return 50000;
//            }
//
//            @Override
//            public int getCurrentRetryCount() {
//                return 50000;
//            }
//
//            @Override
//            public void retry(VolleyError error) throws VolleyError {
//
//            }
//        });
//
//
//    }

}