package com.example.afl_monitoringandroidapp;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.ClientError;
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
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.androidnetworking.interfaces.UploadProgressListener;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import dmax.dialog.SpotsDialog;
import id.zelory.compressor.Compressor;
import io.nlopez.smartlocation.OnLocationUpdatedListener;
import io.nlopez.smartlocation.SmartLocation;

public class ReportFire extends AppCompatActivity {
    //String mytoken = "f7b0f292e11552bcb60459576fedc754064ed663";
    String mytoken;
    String district_list_url = "https://api.aflmonitoring.com/api/district/";
    String newUrl = "https://api.aflmonitoring.com/rest-auth/login/";
    //String district_list_url = "http://api.theagriculture.tk/api/district/";
    String reportSubmitUrl = "https://api.aflmonitoring.com/api/report-user/add/";
    String imageUploadUrl = "https://api.aflmonitoring.com/api/upload/images/";

    TextView textDummyHintName, textDummyHintPhoneNumber, textDummyHintVillage,textDummyHintAddress1,textDummyHintAddress2;
    EditText editName,editPhoneNumber,editVillage, editAddress1, editAddress2;
    View view1,view2,view3,view4,view5;
    TextInputLayout ti1,ti2,ti3, ti4,ti5;
    Spinner district_spinner;
    Button submit;
    String token;
    List DistrictName = new ArrayList<>();
    FloatingActionButton photo;
    AlertDialog reportSubmitLoading;
    FusedLocationProviderClient fusedLocationProviderClient;
    final Location AdoCurrentLocation = new Location(" ");

    Location userLocation;
    int PhotosUploadedCount =0;
    String reportId;

    //for imagespage
    Button  done,save;
    FloatingActionButton clickPhoto;
    boolean isFirstPic = true;
    private static String TAG = "CheckInActivity2";
    private static int IMAGE_CAPTURE_RC = 123;
    private ArrayList<File> mImages;
    private String imageFilePath;
    RecyclerView recyclerView;
    public ReportImageRecyAdapter adapter;
    private ArrayList<String> mImagesPath;

    TextView title_top;

    ConstraintLayout reportPage, imagesPage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.report_fire);

        getUserLocation();

        getToken(newUrl,"haryana_admin","haryana_admin");

        reportPage= findViewById(R.id.report_page);
        imagesPage = findViewById(R.id.images_page);

        textDummyHintName = findViewById(R.id.text_dummy_hint_name);
        editName = (EditText) findViewById(R.id.edit_name);
        ti1 = (TextInputLayout) findViewById(R.id.ti1);
        view1 = findViewById(R.id.view1);

        textDummyHintPhoneNumber = findViewById(R.id.text_dummy_hint_pnumber);
        editPhoneNumber = (EditText) findViewById(R.id.edit_pnumber);
        ti2 = (TextInputLayout) findViewById(R.id.ti2);
        view2 = findViewById(R.id.view2);

        textDummyHintVillage = findViewById(R.id.text_dummy_hint_villagename);
        editVillage = (EditText) findViewById(R.id.edit_villagename);
        ti3 = (TextInputLayout) findViewById(R.id.ti3);
        view3 = findViewById(R.id.view3);

        district_spinner = findViewById(R.id.spinner_district);

        textDummyHintAddress1 = findViewById(R.id.text_dummy_hint_address1);
        editAddress1 = (EditText) findViewById(R.id.edit_address1);
        ti4 = (TextInputLayout) findViewById(R.id.ti4);
        view4 = findViewById(R.id.view4);

        textDummyHintAddress2 = findViewById(R.id.text_dummy_hint_address2);
        editAddress2 = (EditText) findViewById(R.id.edit_address2);
        ti5 = (TextInputLayout) findViewById(R.id.ti5);
        view5 = findViewById(R.id.view5);

        photo = findViewById(R.id.fab_camera);
        submit = findViewById(R.id.submit_fire_report);


        title_top = findViewById(R.id.topTitleName);
        if (title_top.isEnabled()){
            title_top.setText("Report");
        }else {
            title_top.setText("AFL Monitoring");
        }



        editName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                if (hasFocus) {
                    new Handler().postDelayed(new Runnable() {

                        @Override
                        public void run() {
                            // Show white background behind floating label
                            textDummyHintName.setVisibility(View.VISIBLE);
                        }
                    }, 100);
                    view1.setBackgroundResource(R.drawable.blackbg);
                } else {
                    // Required to show/hide white background behind floating label during focus change
                    if (editName.getText().length() > 0)
                        textDummyHintName.setVisibility(View.VISIBLE);
                    else
                        textDummyHintName.setVisibility(View.INVISIBLE);
                    view1.setBackgroundResource(R.drawable.rectanglebg);
                }

                //ti1.setError("You need to enter a name");
            }
        });

        editPhoneNumber.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                if (hasFocus) {
                    new Handler().postDelayed(new Runnable() {

                        @Override
                        public void run() {
                            // Show white background behind floating label
                            textDummyHintPhoneNumber.setVisibility(View.VISIBLE);
                        }
                    }, 100);
                    view2.setBackgroundResource(R.drawable.blackbg);
                } else {
                    // Required to show/hide white background behind floating label during focus change
                    if (editPhoneNumber.getText().length() > 0)
                        textDummyHintPhoneNumber.setVisibility(View.VISIBLE);
                    else
                        textDummyHintPhoneNumber.setVisibility(View.INVISIBLE);
                    view2.setBackgroundResource(R.drawable.rectanglebg);
                }

                //ti1.setError("You need to enter a name");
            }
        });

        editVillage.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                if (hasFocus) {
                    new Handler().postDelayed(new Runnable() {

                        @Override
                        public void run() {
                            // Show white background behind floating label
                            textDummyHintVillage.setVisibility(View.VISIBLE);
                        }
                    }, 100);
                    view3.setBackgroundResource(R.drawable.blackbg);
                } else {
                    // Required to show/hide white background behind floating label during focus change
                    if (editVillage.getText().length() > 0)
                        textDummyHintVillage.setVisibility(View.VISIBLE);
                    else
                        textDummyHintVillage.setVisibility(View.INVISIBLE);
                    view3.setBackgroundResource(R.drawable.rectanglebg);
                }

                //ti1.setError("You need to enter a name");
            }
        });


        district_spinner = findViewById(R.id.spinner_district);
        district_spinner.setPrompt("District");
        final SharedPreferences sp = getSharedPreferences("tokenFire", Context.MODE_PRIVATE);
        if (sp.contains("token")) {
            mytoken = sp.getString("token","");
            getDistrictData();
        }

        district_spinner.setSelection(0,false);
        district_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                //String selected_district=   district_spinner.getItemAtPosition(district_spinner.getSelectedItemPosition()).toString();
                //Toast.makeText(ReportFire.this,selected_district + "has been selected" ,Toast.LENGTH_LONG).show();
                //Toast.makeText(getApplicationContext(),country,Toast.LENGTH_LONG).show();
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // DO Nothing here
            }
        });


        editAddress1.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                if (hasFocus) {
                    new Handler().postDelayed(new Runnable() {

                        @Override
                        public void run() {
                            // Show white background behind floating label
                            textDummyHintAddress1.setVisibility(View.VISIBLE);
                        }
                    }, 100);
                    view4.setBackgroundResource(R.drawable.blackbg);
                } else {
                    // Required to show/hide white background behind floating label during focus change
                    if (editAddress1.getText().length() > 0)
                        textDummyHintAddress1.setVisibility(View.VISIBLE);
                    else
                        textDummyHintAddress1.setVisibility(View.INVISIBLE);
                    view4.setBackgroundResource(R.drawable.rectanglebg);
                }

                //ti1.setError("You need to enter a name");
            }
        });

        editAddress2.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                if (hasFocus) {
                    new Handler().postDelayed(new Runnable() {

                        @Override
                        public void run() {
                            // Show white background behind floating label
                            textDummyHintAddress2.setVisibility(View.VISIBLE);
                        }
                    }, 100);
                    view5.setBackgroundResource(R.drawable.blackbg);
                } else {
                    // Required to show/hide white background behind floating label during focus change
                    if (editAddress2.getText().length() > 0)
                        textDummyHintAddress2.setVisibility(View.VISIBLE);
                    else
                        textDummyHintAddress2.setVisibility(View.INVISIBLE);
                    view5.setBackgroundResource(R.drawable.rectanglebg);
                }

                //ti1.setError("You need to enter a name");
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(ReportFire.this,"Synjnd",Toast.LENGTH_LONG).show();
                checkReport();
            }
        });

        photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reportPage.setVisibility(View.GONE);
                imagesPage.setVisibility(View.VISIBLE);
                title_top.setText("Image Upload");
                //Intent intent = new Intent(ReportFire.this,ImageUpload.class);
                //startActivity(intent);
                //Toast.makeText(ReportFire.this,"To upload Image screen",Toast.LENGTH_LONG).show();
            }
        });


        recyclerView = findViewById(R.id.rvimages);
        mImagesPath = new ArrayList<>();
        mImages = new ArrayList<>();
        adapter = new ReportImageRecyAdapter(this, mImagesPath);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        recyclerView.setAdapter(adapter);

        clickPhoto = findViewById(R.id.camera);
        clickPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //uploadingPhotos();
                if (isFirstPic) {
                    openCameraIntent();
                    isFirstPic = false;
                } else if (mImages.size() < 4) {
                    //Toast.makeText(ReportFire.this, "images are " + mImagesPath, Toast.LENGTH_SHORT).show();
                    openCameraIntent();
                }
                else
                    Toast.makeText(ReportFire.this, "Max Photos Reached...images are " + mImagesPath, Toast.LENGTH_SHORT).show();

            }
        });

        save = findViewById(R.id.save_images);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //imagesPage.setVisibility(View.GONE);
                //reportPage.setVisibility(View.VISIBLE);
                //title_top.setText("Report");
                if(mImagesPath.size()==0)
                    Toast.makeText(getApplicationContext(),"You have not uploaded any images",Toast.LENGTH_LONG).show();
                else
                    uploadingPhotos();


            }
        });


        editName.addTextChangedListener(entryTextWatcher);//entryTextWatcher function is defined at last
        editPhoneNumber.addTextChangedListener(entryTextWatcher);
        //// editVillage.addTextChangedListener(entryTextWatcher);
        ///////editAddress1.addTextChangedListener(entryTextWatcher);

    }

    //for district spinner
    public void getDistrictData(){
        DistrictName.add("District(Optional)");
        RequestQueue district_requestQueue = Volley.newRequestQueue(ReportFire.this);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, district_list_url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try{
                    for (int i = 0; i < response.length(); i++) {
                        JSONObject singleObject = response.getJSONObject(i);
                        DistrictName.add(singleObject.getString("district"));
                    }
                    Log.d("spinner",DistrictName.toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                district_spinner.setAdapter(new ArrayAdapter<String>(ReportFire.this, android.R.layout.simple_spinner_dropdown_item, DistrictName));
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ReportFire.this,"error in loading district is "+error.getMessage(),Toast.LENGTH_LONG).show();
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    //This indicates that the reuest has either time out or there is no connection
                    Toast.makeText(ReportFire.this, "This error is case1", Toast.LENGTH_LONG).show();
                } else if (error instanceof AuthFailureError) {
                    // Error indicating that there was an Authentication Failure while performing the request
                    Toast.makeText(ReportFire.this, "This error is case2", Toast.LENGTH_LONG).show();
                } else if (error instanceof ServerError) {
                    //Indicates that the server responded with a error response
                    Toast.makeText(ReportFire.this, "This error is server error", Toast.LENGTH_LONG).show();
                } else if (error instanceof NetworkError) {
                    //Indicates that there was network error while performing the request
                    Toast.makeText(ReportFire.this, "This error is case4", Toast.LENGTH_LONG).show();
                } else if (error instanceof ParseError) {
                    // Indicates that the server response could not be parsed
                    Toast.makeText(ReportFire.this, "This error is case5", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(ReportFire.this, "An unknown error occurred.", Toast.LENGTH_SHORT).show();
                }

            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {

                HashMap<String, String> map = new HashMap<>();
                // map.put("Authorization", "token " + "dbf8a13129cc5369437f1631939c19b45cdbcbb5");//token for api.aflmonitoring.com
                //map.put("Authorization", "token " + "6c00f8bf80284fbbe1a5153e234465c45eb2bc75");//token for api.theagriculture.tk
                map.put("Authorization", "Token " + mytoken);
                return map;
            }
        };

        district_requestQueue.add(jsonArrayRequest);
    }

    //function to change colour of login button when text is entered
    public TextWatcher entryTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            //btnLogin.setBackgroundResource(R.drawable.buttons_before_text);
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String name = editName.getText().toString().trim();
            String pnumber = editPhoneNumber.getText().toString().trim();
            String village = editVillage.getText().toString().trim();
            String address1 = editAddress1.getText().toString().trim();
            if(!name.isEmpty() && !pnumber.isEmpty())// && !village.isEmpty() && !address1.isEmpty() && !(district_spinner.getSelectedItemPosition()==0))
            {
                submit.setBackgroundResource(R.color.theme_color);
            }
            else{
                submit.setBackgroundResource(R.color.btn_before_text);
            }
        }
        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    public void checkReport(){
        String name = editName.getText().toString().trim();
        String pnumber = editPhoneNumber.getText().toString().trim();
        String village = editVillage.getText().toString().trim();
        String address1 = editAddress1.getText().toString().trim();

        if(name.isEmpty())
            Toast.makeText(ReportFire.this,"Enter your name",Toast.LENGTH_LONG).show();
        else if(pnumber.isEmpty())
            Toast.makeText(ReportFire.this,"Enter phone number",Toast.LENGTH_LONG).show();
        /*
        else if(village.isEmpty())
            Toast.makeText(ReportFire.this,"Enter village name",Toast.LENGTH_LONG).show();
        else if(district_spinner.getSelectedItemPosition()==0)
            Toast.makeText(ReportFire.this,"Select a district",Toast.LENGTH_LONG).show();
        else if(address1.isEmpty())
            Toast.makeText(ReportFire.this,"Enter your address",Toast.LENGTH_LONG).show();
            //else if(!checkIfLocationEnabled())
            //  Toast.makeText(ReportFire.this,"Enable locations to send report",Toast.LENGTH_LONG).show();

         */
        else{
            //Toast.makeText(ReportFire.this,"enterd else loop",Toast.LENGTH_LONG).show();
            //Location abc = get();
            //Toast.makeText(ReportFire.this,"location is check is "+abc.toString(),Toast.LENGTH_LONG).show();
            submitReport();
        }
    }


    public void submitReport(){
        //Toast.makeText(ReportFire.this,"enterd submit report",Toast.LENGTH_LONG).show();
        String sname = editName.getText().toString().trim();
        String spnumber = editPhoneNumber.getText().toString().trim();
        //Location abc = get();
        //Toast.makeText(ReportFire.this,"location in submit is "+abc.toString(),Toast.LENGTH_LONG).show();


        reportSubmitLoading = new SpotsDialog.Builder().setContext(ReportFire.this).setMessage("Submitting Report")
                .setTheme(R.style.CustomDialog)
                .setCancelable(false)
                .build();
        reportSubmitLoading.show();
        RequestQueue requestQueue = Volley.newRequestQueue(ReportFire.this);

        JSONObject postParams = new JSONObject();
        try {
            postParams.put("longitude", "76.8498");
            postParams.put("latitude", "28.2314");
//            postParams.put("longitude", String.valueOf(userLocation.getLongitude()));
//            postParams.put("latitude", String.valueOf(userLocation.getLatitude()));
            postParams.put("name", sname);
            postParams.put("phone_number", spnumber);
            //Toast.makeText(getApplicationContext(),postParams.toString(),Toast.LENGTH_LONG).show();


        } catch (JSONException e) {
            Toast.makeText(ReportFire.this, "An exception occured", Toast.LENGTH_LONG).show();
            reportSubmitLoading.dismiss();
            Log.d(TAG, "submitReport: " + e);
        }
        //Toast.makeText(getApplicationContext(),"posting values "+postParams.toString(),Toast.LENGTH_LONG).show();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(reportSubmitUrl, postParams,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            //Toast.makeText(ReportFire.this,"response is "+response.toString(),Toast.LENGTH_LONG).show();
                            JSONObject singleObject = new JSONObject(String.valueOf(response));
                            reportId = singleObject.getString("NormalUserReport_id");
                            Toast.makeText(getApplicationContext(),"Your report id is "+reportId,Toast.LENGTH_LONG).show();
                            Log.d(TAG, "onResponse: " + singleObject);
                            reportSubmitLoading.dismiss();
                            ///////////
                            reportPage.setVisibility(View.GONE);
                            imagesPage.setVisibility(View.VISIBLE);
                            title_top.setText("Image Upload");
                            //isReportSubmitted = true;
                            ////uploadingPhotos();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            reportSubmitLoading.dismiss();
                            Log.d(TAG, "jsonexception: reportSubmitRequest " + e);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(ReportFire.this,"error is "+error.getMessage(),Toast.LENGTH_LONG).show();
                        if (error instanceof NoConnectionError || error instanceof TimeoutError){
                            Toast.makeText(ReportFire.this, "Check your internet connection!", Toast.LENGTH_LONG).show();
                        }else if (error instanceof AuthFailureError) {
                            // Error indicating that there was an Authentication Failure while performing the request
                            Toast.makeText(ReportFire.this, "This error is case2", Toast.LENGTH_LONG).show();
                        } else if (error instanceof ServerError) {
                            //Indicates that the server responded with a error response
                            Toast.makeText(ReportFire.this, "This error is case3", Toast.LENGTH_LONG).show();
                        } else if (error instanceof NetworkError) {
                            //Indicates that there was network error while performing the request
                            Toast.makeText(ReportFire.this, "This error is case4", Toast.LENGTH_LONG).show();
                        } else if (error instanceof ParseError) {
                            // Indicates that the server response could not be parsed
                            Toast.makeText(ReportFire.this, "This error is case5", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(ReportFire.this, "An unknown error occurred.", Toast.LENGTH_SHORT).show();
                        }
                        reportSubmitLoading.dismiss();
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> map = new HashMap<>();
                //map.put("Authorization", "Token " + "4a4ae26d6df615268d641f8efc7dd27dd27f2c308b29");
                // map.put("Authorization", "Token " + "dbf8a13129cc5369437f1631939c19b45cdbcbb5");
                map.put("Authorization", "Token " +mytoken);
                return map;
            }
        };
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

    }


    public boolean checkIfLocationEnabled(){
        LocationManager lm = (LocationManager)  ReportFire.this.getSystemService(Context.LOCATION_SERVICE);
        boolean gps_enabled = false;
        boolean network_enabled = false;

        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        }
        catch(Exception ex) {
            Toast.makeText(ReportFire.this,"An exception occured while checking GPS Location ",Toast.LENGTH_SHORT);
        }

        try {
            network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        }
        catch(Exception ex) {
            Toast.makeText(ReportFire.this,"An exception occured while checking Network Location ",Toast.LENGTH_SHORT);
        }

        if(!gps_enabled && !network_enabled) {
            //Toast.makeText(context,"Enable your location",Toast.LENGTH_SHORT).show();
            return false;
        }
        else{
            return true;
            // Toast.makeText(context,"Location is enabled",Toast.LENGTH_SHORT).show();
        }
    }


    public Location get() {
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(ReportFire.this);
        if (ActivityCompat.checkSelfPermission(ReportFire.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if ((ActivityCompat.checkSelfPermission(ReportFire.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) && (ActivityCompat.checkSelfPermission(ReportFire.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)) {
                Toast.makeText(ReportFire.this,"Returned from if",Toast.LENGTH_LONG).show();
            }
            fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                @Override
                public void onComplete(@NonNull Task<Location> task) {
                    Location location = task.getResult();
                    AdoCurrentLocation.setLatitude(location.getLatitude());
                    AdoCurrentLocation.setLongitude(location.getLongitude());
                }
            });
        } else {
            Toast.makeText(ReportFire.this, "You denied permission to access location", Toast.LENGTH_LONG).show();
            ActivityCompat.requestPermissions((Activity) ReportFire.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
        }
        //Toast.makeText(context,AdoCurrentLocation.toString(),Toast.LENGTH_SHORT).show();
        return AdoCurrentLocation;

    }

    ////////////////////////////////
    //functions for images page
    private void openCameraIntent() {
        Intent pictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (pictureIntent.resolveActivity(getPackageManager()) != null) {

            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException e) {
                Log.d(TAG, "openCameraIntent: IOEXCEPTION PHOTOFILE: " + e.getMessage());
                e.printStackTrace();
                return;
            }
            Uri photoUri = FileProvider.getUriForFile(this, getPackageName() + ".provider", photoFile);
            pictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
            pictureIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            startActivityForResult(pictureIntent, IMAGE_CAPTURE_RC);
        }
    }

    private File createImageFile() throws IOException {

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = "IMG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName, ".jpg", storageDir);
        imageFilePath = image.getAbsolutePath();
        return image;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == IMAGE_CAPTURE_RC) {
            if (resultCode == RESULT_OK) {
                File file = new File(imageFilePath);
                try {
                    mImagesPath.add(imageFilePath);
                    File compressedFile = new Compressor(getApplicationContext()).compressToFile(file);
                    mImages.add(compressedFile);
                    adapter.notifyDataSetChanged();
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Unable to load Image, please try again!",
                            Toast.LENGTH_SHORT).show();
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }



    public void uploadingPhotos(){
        // Toast.makeText(getApplicationContext(),"Eneterd uploading photos function",Toast.LENGTH_LONG).show();
        reportSubmitLoading = new SpotsDialog.Builder().setContext(ReportFire.this).setMessage("Uploading Images")
                .setTheme(R.style.CustomDialog)
                .setCancelable(false)
                .build();
        reportSubmitLoading.show();

        AndroidNetworking.upload(imageUploadUrl)
                .addHeaders("Authorization", "Token " + token)
                //.addHeaders("Authorization", "detail" + token)
                .addMultipartParameter("report",reportId)
                .addMultipartFile("image", mImages.get(PhotosUploadedCount))
                .setTag("Upload Images")
                .setPriority(Priority.HIGH)
                .build()
                .setUploadProgressListener(new UploadProgressListener() {
                    @Override
                    public void onProgress(long bytesUploaded, long totalBytes) {
                        //if (bytesUploaded == totalBytes) {
                        //PhotosUploadedCount++;
                        //}
                        // Toast.makeText(getApplicationContext(),"uploading image "+PhotosUploadedCount+"bytesUploaded are "+String.valueOf(bytesUploaded)+"total bytes are "+String.valueOf(totalBytes),Toast.LENGTH_LONG).show();
                    }
                })
                .getAsJSONObject(new JSONObjectRequestListener() {
                                     @Override
                                     public void onResponse(JSONObject response) {
                                         Log.d(TAG, "onResponse: " + response);
                                         PhotosUploadedCount++;
                                         Toast.makeText(getApplicationContext(),"response is "+response.toString(),Toast.LENGTH_LONG).show();
                                         if(PhotosUploadedCount==mImages.size()) {
                                             reportSubmitLoading.dismiss();
                                             //submit_btn.setText("Submitted");
                                         }
                                     }

                                     @Override
                                     public void onError(ANError anError) {
                                         Log.d(TAG, "onError: " + anError.getErrorBody());
                                         Toast.makeText(getApplicationContext(), "Photos Upload failed, please try again ", Toast.LENGTH_SHORT).show();
                                         reportSubmitLoading.dismiss();
                                     }

                                 }
                );
    }

    public void getToken(String url,String username,String password){
        final RequestQueue MyRequestQueue = Volley.newRequestQueue(this);
        JSONObject postparams = null;
        try {
            postparams = new JSONObject();
            postparams.put("username", username);
            postparams.put("password", password);
        } catch (JSONException e) {
            //Log.d(TAG, "Login: Error:" + e);
            Toast.makeText(getApplicationContext(), "Please try again", Toast.LENGTH_LONG).show();
        }
        // Toast.makeText(CheckToken.this,"posted "+postparams.toString(),Toast.LENGTH_LONG).show();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest( url, postparams,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        //Toast.makeText(CheckToken.this,"response from token url is "+response.toString(),Toast.LENGTH_LONG).show();
                        //token.setText(response.toString());
                        //retrieve the token from server

                        try {
                            JSONObject jsonObject = new JSONObject(String.valueOf(response));
                            String tokenfire = jsonObject.getString("key");
                            SharedPreferences.Editor editor = getSharedPreferences("tokenFire", Context.MODE_PRIVATE).edit();
                            editor.putString("token", tokenfire);
                            editor.apply();
                            // Toast.makeText(getApplicationContext(),"got token "+tokenfire,Toast.LENGTH_LONG).show();

                        } catch (JSONException e) {
                            Toast.makeText(getApplicationContext(),"exception occured",Toast.LENGTH_LONG).show();
                            //Log.d(TAG, "onResponse: error in post catch block: " + e);
                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(),"error from token url is "+error.getMessage(),Toast.LENGTH_LONG).show();
                        if (error instanceof NoConnectionError)
                            Toast.makeText(getApplicationContext(), "Check your internet connection", Toast.LENGTH_LONG).show();
                        else if (error instanceof ClientError)
                            Toast.makeText(getApplicationContext(), "Invalid User!", Toast.LENGTH_SHORT).show();
                        else if (error instanceof TimeoutError)
                            Toast.makeText(getApplicationContext(), "Time our!", Toast.LENGTH_SHORT).show();
                        else if (error instanceof AuthFailureError) {
                            // Error indicating that there was an Authentication Failure while performing the request
                            Toast.makeText(getApplicationContext(), "This error is case2", Toast.LENGTH_LONG).show();
                        } else if (error instanceof ServerError) {
                            //Indicates that the server responded with a error response
                            Toast.makeText(getApplicationContext(), "This error is case3", Toast.LENGTH_LONG).show();
                        } else if (error instanceof NetworkError) {
                            //Indicates that there was network error while performing the request
                            Toast.makeText(getApplicationContext(), "This error is case4", Toast.LENGTH_LONG).show();
                        } else if (error instanceof ParseError) {
                            // Indicates that the server response could not be parsed
                            Toast.makeText(getApplicationContext(), "This error is case5", Toast.LENGTH_LONG).show();
                        }
                        else
                            Toast.makeText(getApplicationContext(), "Something went wrong, please try again!", Toast.LENGTH_LONG).show();
                    }
                });

        MyRequestQueue.add(jsonObjectRequest);

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
    }

    public void getUserLocation() {
        SmartLocation.with(getApplicationContext()).location().start(new OnLocationUpdatedListener() {
            @Override
            public void onLocationUpdated(Location location) {
                userLocation = location;
            }
        });

    }
}
