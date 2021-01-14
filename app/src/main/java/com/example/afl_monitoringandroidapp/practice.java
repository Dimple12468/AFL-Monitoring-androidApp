//package com.example.afl_monitoringandroidapp;
//
//
//
//import android.app.AlertDialog;
//import android.content.Context;
//import android.content.DialogInterface;
//import android.content.SharedPreferences;
//import android.content.pm.PackageInfo;
//import android.content.pm.PackageManager;
//import android.graphics.Bitmap;
//import android.graphics.Canvas;
//import android.graphics.drawable.Drawable;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//import androidx.core.content.ContextCompat;
//import androidx.fragment.app.Fragment;
//
//import com.android.volley.AuthFailureError;
//import com.android.volley.NoConnectionError;
//import com.android.volley.Request;
//import com.android.volley.RequestQueue;
//import com.android.volley.Response;
//import com.android.volley.RetryPolicy;
//import com.android.volley.VolleyError;
//import com.android.volley.toolbox.JsonObjectRequest;
//import com.android.volley.toolbox.Volley;
//import com.google.android.gms.maps.CameraUpdateFactory;
//import com.google.android.gms.maps.GoogleMap;
//import com.google.android.gms.maps.OnMapReadyCallback;
//import com.google.android.gms.maps.SupportMapFragment;
//import com.google.android.gms.maps.model.BitmapDescriptor;
//import com.google.android.gms.maps.model.BitmapDescriptorFactory;
//import com.google.android.gms.maps.model.LatLng;
//import com.google.android.gms.maps.model.LatLngBounds;
//import com.google.maps.android.clustering.ClusterManager;
//import com.theagriculture.app.R;
//
//import org.json.JSONArray;
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.Map;
//
//import dmax.dialog.SpotsDialog;
//
//public class practice  extends Fragment {
//        private final String TAG = "map fragment";
//
//        public GoogleMap map = null;
//        private String url_unassigned = "http://18.224.202.135/api/locations/unassigned";
//        private String url_assigned = "http://18.224.202.135/api/locations/assigned";
//        private String url_count="http://18.224.202.135/api/count-reports/";
//        private String token;
//        private String next;
//        private SupportMapFragment mapFragment;
//        private int count = 0 ;
//        //    private ProgressBar pbar;
//        private RequestQueue requestQueue;
//
//        private ArrayList<Double> latitude;
//        private ArrayList<Double> longitude;
//        private ArrayList<String> villname;
//        private AlertDialog dialog;
//
//
//        private TextView pendingView;
//        private TextView ongoingView;
//        private TextView completedView;
//
//
//        private ClusterManager<MyItem> mClusterManager;
//
//
//
//
//        @Nullable
//        @Override
//        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//            View view = inflater.inflate(R.layout.map_fragemnt, container, false);
//            mapFragment = ((SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map));
//            final SharedPreferences preferences = getActivity().getSharedPreferences("tokenFile", Context.MODE_PRIVATE);
//            token = preferences.getString("token", "");
//            latitude = new ArrayList<>();
//            longitude = new ArrayList<>();
//            villname = new ArrayList<>();
////        pbar = view.findViewById(R.id.pbar);
//
//            dialog = new SpotsDialog.Builder().setContext(getActivity()).setMessage("Loading locations...")
//                    .setTheme(R.style.CustomDialog)
//                    .setCancelable(false).build();
//            dialog.show();
//            next = url_assigned;
//      /*  final LinearLayout bottomsheetLayout = view.findViewById(R.id.map_bottom_sheet);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            bottomsheetLayout.setOutlineProvider(new ViewOutlineProvider() {
//                @Override
//                public void getOutline(View view, Outline outline) {
//                    outline.setRoundRect(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight() + 30, 40f);
//                }
//            });
//            bottomsheetLayout.setClipToOutline(true);
//        }   */
//            pendingView = view.findViewById(R.id.pending_count);
//            ongoingView = view.findViewById(R.id.ongoing_count);
//            completedView = view.findViewById(R.id.completed_count);
//
//
//            isUpdateAvail();
//
//
//
//
//            mapFragment.getMapAsync(new OnMapReadyCallback() {
//                @Override
//                public void onMapReady(GoogleMap googleMap) {
//                    map = googleMap;
//                    Log.d(TAG, "onMapReady:value if map is : " + map);
//
//                    LatLng one = new LatLng(7.798000, 68.14712);
//                    LatLng two = new LatLng(37.090000, 97.34466);
//
//                    LatLng shimala = new LatLng(31.104815,77.173401);
//                    LatLng jaipur = new LatLng(26.912434,75.787270);
//
//                    LatLngBounds.Builder builder = new LatLngBounds.Builder();
//                    LatLngBounds.Builder builder1 = new LatLngBounds.Builder();
//
//
//                    //add them to builder
//                    builder.include(one);
//                    builder.include(two);
//
//                    builder1.include(shimala);
//                    builder1.include(jaipur);
//
//                    LatLngBounds bounds = builder.build();
//                    LatLngBounds bounds1 = builder1.build();
//
//                    //get width and height to current display screen
//                    int width = getResources().getDisplayMetrics().widthPixels;
//                    int height = getResources().getDisplayMetrics().heightPixels;
//
//                    // 20% padding
//                    int padding = (int) (width * 0.20);
//
//                    //set latlong bounds
//                    map.setLatLngBoundsForCameraTarget(bounds);
//
//                    //move camera to fill the bound to screen
//                    map.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds1, width, height, padding));
//
//                    //set zoom to level to current so that you won't be able to zoom out viz. move outside bounds
//                    map.setMinZoomPreference(map.getCameraPosition().zoom);
//
//
//
//                }
//            });
//
//
//            Log.d(TAG, "onCreateView: look me here " + mapFragment);
//            isUpdateAvail();
//            return view;
//        }
//
//
//        void getCount(String urlcount){
//
//            RequestQueue requestQueue = Volley.newRequestQueue(getContext());
//
//            final JsonObjectRequest jsonObjectRequest2 = new JsonObjectRequest(Request.Method.GET, urlcount, null, new Response.Listener<JSONObject>() {
//                @Override
//                public void onResponse(JSONObject response) {
//
//                /*try {
//                    JSONObject jsonObject = new JSONObject(String.valueOf(response));
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }*/
///*
//                try {
//                    JSONArray result = response.getJSONArray("results");
//                    for (int i=0;i<result.length();i++){
//                        JSONObject singleObject = result.getJSONObject(i);
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }*/
//
//
//
//                    try {
//                        pendingView.setText(response.getString("pending_count"));
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//
//                    try {
//                        ongoingView.setText(response.getString("ongoing_count"));
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//
//                    try {
//                        completedView.setText(response.getString("completed_count"));
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//           /*   try {
//                  int pending=Integer.valueOf(response.getString("pending_count"));
//                  int ongoing=Integer.valueOf(response.getString("ongoing_count"));
//                  int completed=Integer.valueOf(response.getString("completed_count"));
//                 String total = String.valueOf(pending+ongoing+completed);
//                  counttotal.setText(total);
//              } catch (JSONException e)  {
//                  e.printStackTrace();
//              } */
//
//
//                }
//            }, new Response.ErrorListener() {
//                @Override
//                public void onErrorResponse(VolleyError error) {
//                    Log.e(TAG, "onErrorResponse: " + error);
////                pbar.setVisibility(View.GONE);
//                    dialog.dismiss();
//                    if(error instanceof NoConnectionError){
//                        Toast.makeText(getActivity(), "Check your internet connection", Toast.LENGTH_LONG).show();
//                    }
//                    else {
//                        Toast.makeText(getActivity(),"something went wrong",Toast.LENGTH_LONG).show();
//                    }
//                }
//            }) {
//
//                @Override
//                public Map<String, String> getHeaders() throws AuthFailureError{
//                    HashMap<String, String> map = new HashMap<>();
//                    map.put("Authorization", "Token " + token);
//                    return map;
//                }
//            };
//            jsonObjectRequest2.setTag("MAP REQUEEST");
//            requestQueue.add(jsonObjectRequest2);
//            jsonObjectRequest2.setRetryPolicy(new RetryPolicy() {
//                @Override
//                public int getCurrentTimeout() {
//                    return 50000;
//                }
//
//                @Override
//                public int getCurrentRetryCount() {
//                    return 50000;
//                }
//
//                @Override
//                public void retry(VolleyError error) throws VolleyError {
//
//                }
//            });
//
//
//        }
//
//        void getMarkers(String url) {
//
//            requestQueue = Volley.newRequestQueue(getContext());
//
//            final JsonObjectRequest jsonObjectRequest2 = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
//                @Override
//                public void onResponse(JSONObject response) {
//                    try {
//                        JSONObject jsonObject = new JSONObject(String.valueOf(response));
//                        JSONArray jsonArray = jsonObject.getJSONArray("results");
//
//                        for (int i = 0; i < jsonArray.length(); i++) {
//                            JSONObject c = jsonArray.getJSONObject(i);
//                            Double lat = Double.valueOf(c.getString("latitude"));
//                            Double lon = Double.valueOf(c.getString("longitude"));
//                            String vill = c.getString("village_name");
//                            latitude.add(lat);
//                            longitude.add(lon);
//                            villname.add(vill);
//                        }
//
//                    } catch (JSONException e) {
//                        Log.e(TAG, "onResponse: " + e.getLocalizedMessage());
//                        e.printStackTrace();
//                        dialog.dismiss();
//                    }
//                }
//            }, new Response.ErrorListener() {
//                @Override
//                public void onErrorResponse(VolleyError error) {
//                    Log.e(TAG, "onErrorResponse: " + error);
////                pbar.setVisibility(View.GONE);
//                    dialog.dismiss();
//                    if(error instanceof NoConnectionError){
//                        Toast.makeText(getActivity(), "Check your internet connection", Toast.LENGTH_LONG).show();
//                    }
//                    else {
//                        Toast.makeText(getActivity(),"something went wrong",Toast.LENGTH_LONG).show();
//                    }
//                }
//            }) {
//
//                @Override
//                public Map<String, String> getHeaders() throws AuthFailureError{
//                    HashMap<String, String> map = new HashMap<>();
//                    map.put("Authorization", "Token " + token);
//                    return map;
//                }
//            };
//            jsonObjectRequest2.setTag("MAP REQUEEST");
//            requestQueue.add(jsonObjectRequest2);
//            jsonObjectRequest2.setRetryPolicy(new RetryPolicy() {
//                @Override
//                public int getCurrentTimeout() {
//                    return 50000;
//                }
//
//                @Override
//                public int getCurrentRetryCount() {
//                    return 50000;
//                }
//
//                @Override
//                public void retry(VolleyError error) throws VolleyError {
//
//                }
//            });
//            requestFinished(requestQueue);
//        }
//        private void requestFinished(RequestQueue queue) {
//
//            queue.addRequestFinishedListener(new RequestQueue.RequestFinishedListener<Object>() {
//
//                @Override
//                public void onRequestFinished(Request<Object> request) {
//                    Log.d(TAG, "onRequestFinished: here too");
//                    if(count == 0)nextRequest();
//                    else if(count == 1) marklocation();
//
//                }
//            });
//
//        }
//
//        private void marklocation() {
//
////        pbar.setVisibility(View.GONE);
//
//        /*for(int i = 0 ; i < latitude.size();i++){
//            MarkerOptions Dlocation = new MarkerOptions().position(new LatLng(latitude.get(i), longitude.get(i))).title(villname.get(i)).icon(bitmapDescriptorFromVector(getActivity(),R.drawable.ic_action_name));
//            map.addMarker(Dlocation);
//            if(i == 0 ){
//                dialog.dismiss();
//            }
//        }*/
//            Log.d(TAG, "marklocation: SIZE" + latitude.size());
//            mClusterManager = new ClusterManager<MyItem>(getActivity(), map);
//
//            addmarkers();
//
//            map.setOnCameraIdleListener(mClusterManager);
//            map.setOnMarkerClickListener(mClusterManager);
//
//
//
//            dialog.dismiss();
//
//
//        }
//
//        private void addmarkers() {
//
//            for(int i = 0 ;i<latitude.size();i++){
//                double lat = latitude.get(i);
//                double lon = longitude.get(i);
//                String title = villname.get(i);
//
//                MyItem item = new MyItem(lat,lon,title);
//
//                mClusterManager.addItem(item);
//
//            }
//
//            mClusterManager.cluster();
//            Log.d(TAG, "addmarkers: CLUSTER SIZE" + mClusterManager.getRenderer());
//
//        }
//
//        void nextRequest(){
//            count = 1;
//            next= url_unassigned;
//            getMarkers(next);
//
//
//        }
//
//        BitmapDescriptor bitmapDescriptorFromVector(Context context, int vectorResId) {
//            Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorResId);
//            vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());
//            Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
//            Canvas canvas = new Canvas(bitmap);
//            vectorDrawable.draw(canvas);
//            return BitmapDescriptorFactory.fromBitmap(bitmap);
//        }
//
//        private void isUpdateAvail() {
//            String url = "http://18.224.202.135/api/checkVersion";
//            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
//                    new Response.Listener<JSONObject>() {
//                        @Override
//                        public void onResponse(JSONObject response) {
//                            try {
//                                JSONObject rootObject = new JSONObject(String.valueOf(response));
//                                double versionCode = Double.parseDouble(rootObject.getString("version"));
//                                String verName = "";
//                                try {
//                                    PackageInfo pInfo = getActivity().getPackageManager()
//                                            .getPackageInfo(getActivity().getPackageName(), 0);
//                                    verName = pInfo.versionName;
//                                } catch (PackageManager.NameNotFoundException e) {
//                                    e.printStackTrace();
//                                }
//                                double verCode = Double.parseDouble(verName);
//                                Log.d(TAG, "onResponse: versionCode " + versionCode + "verCode" + verCode);
//                                if (versionCode > verCode)
//                                {
//                                    showdialogbox("Update Available", "A new Update is available, " +
//                                                    "please update the app!", "Close",
//                                            new DialogInterface.OnClickListener() {
//                                                @Override
//                                                public void onClick(DialogInterface dialogInterface, int i) {
//                                                    getActivity().finish();
//                                                }
//                                            }, "", null, false);
//                                }else{
//
//                                    getCount(url_count);
//                                    getMarkers(next);
//                                }
//                            } catch (JSONException e) {
//                                e.printStackTrace();
//                            }
//                        }
//                    },
//                    new Response.ErrorListener() {
//                        @Override
//                        public void onErrorResponse(VolleyError error) {
//                            Log.d(TAG, "onErrorResponse: " + error);
//                        }
//                    });
//
//            RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
//            requestQueue.add(jsonObjectRequest);
//        }
//
//        private AlertDialog showdialogbox(String title, String msg, String positiveLabel, DialogInterface.OnClickListener positiveOnclick,
//                                          String negativeLabel, DialogInterface.OnClickListener negativeOnclick,
//                                          boolean isCancelable) {
//
//            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//            builder.setTitle(title);
//            builder.setMessage(msg);
//            builder.setPositiveButton(positiveLabel, positiveOnclick);
//            builder.setNegativeButton(negativeLabel, negativeOnclick);
//            builder.setCancelable(isCancelable);
//            AlertDialog alert = builder.create();
//            alert.show();
//            return alert;
//        }
//}
//
