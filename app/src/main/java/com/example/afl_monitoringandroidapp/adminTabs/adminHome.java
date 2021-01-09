package com.example.afl_monitoringandroidapp.adminTabs;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import com.example.afl_monitoringandroidapp.Home;
import com.example.afl_monitoringandroidapp.InitialPage;
import com.example.afl_monitoringandroidapp.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.maps.android.clustering.ClusterManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class
adminHome extends Fragment {

    private adminHomeViewModel adminhomeViewModel;
    private SupportMapFragment mapFragment;
    private String token;
    private String TAG = "Map";
    private GoogleMap map = null;
    private String url_unassigned = Globals.map_Unassigned_Admin;
    private String url_assigned = Globals.map_Assigned_Admin;
    private String url_count = Globals.map_Count_Admin;
    private String next;
    private RequestQueue requestQueue;
    private int count = 0 ;
    private ClusterManager<MyItem> mClusterManager;
    private ArrayList<Double> latitude;
    private ArrayList<Double> longitude;
    private ArrayList<String> villname;

    public adminHome() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        adminhomeViewModel = new ViewModelProvider(this).get(adminHomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_admin_home, container, false);
        mapFragment = ((SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map));

        SharedPreferences preferences = getActivity().getSharedPreferences("tokenFile", Context.MODE_PRIVATE);
        String typeOfUser = preferences.getString("role", "");
        token = preferences.getString("key", "");

        latitude = new ArrayList<>();
        longitude = new ArrayList<>();
        villname = new ArrayList<>();

        switch (typeOfUser) {
            case "1":
                Toast.makeText(getActivity(), "login for farmer", Toast.LENGTH_SHORT).show();
                break;
            case "2":
                Toast.makeText(getActivity(), "login for ado", Toast.LENGTH_SHORT).show();
                break;
            case "3":
                Toast.makeText(getActivity(), "login for block admin", Toast.LENGTH_SHORT).show();
                break;
            case "4":
                Toast.makeText(getActivity(), "login for dda", Toast.LENGTH_SHORT).show();
                break;
            case "5":
//                Toast.makeText(getActivity(), "login for admin", Toast.LENGTH_SHORT).show();
                callForMap();
                break;
            case "6":
                Toast.makeText(getActivity(), "login for super admin", Toast.LENGTH_SHORT).show();
                break;
        }

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

    void callForMap(){
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

    void getMarkers(String url) {
        requestQueue = Volley.newRequestQueue(getContext());
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
                        String vill = c.getString("village_name");
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
                if(count == 0)nextRequest();
                else if(count == 1) marklocation();

            }
        });

    }

    void nextRequest(){
        count = 1;
        next= url_unassigned;
        getMarkers(next);
    }

    private void marklocation() {
        Log.d(TAG, "mark location: SIZE" + latitude.size());
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
        Log.d(TAG, "add markers: CLUSTER SIZE" + mClusterManager.getRenderer());

    }
}