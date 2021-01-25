package com.example.afl_monitoringandroidapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.android.volley.VolleyLog.TAG;

public class ProfilePage extends AppCompatActivity {
    String userId,completedCount,pendingCount;
    TextView userName,pCount,cCount,position,userAddress,userNumber,userMail;

    ////ImageView image;
    CircularImageView image;
    private ImageLoader imageLoader;

    RecyclerView recyclerView;
    ProfilePageAdapter adapter;
    ArrayList<String> location, district;
    LinearLayoutManager layoutManager;
    Spinner spin;
    private String nextUrl;
    private boolean isNextBusy = false;
    ProgressBar loading;

    String typeOfUser;

    //String adminPendingUrl = Globals.pendingList,adminOngoingUrl = Globals.ongoingList,adminCompletedUrl = Globals.completedList;
    String adminPendingUrl = Globals.pendingDatewiseList,adminOngoingUrl = Globals.ongoingDatewiseList,adminCompletedUrl = Globals.completedDatewiseList;
    String adoPendingUrl = Globals.adoPending,adoCompletedUrl = Globals.adoCompleted;
    String ddaAssignedUrl = Globals.assignedLocationsDDA, ddaUnassignedUrl = Globals.unassignedLocationsDDA,
            ddaOngoingUrl = Globals.ddaOngoing,ddaCompletedUrl = Globals.ddaCompleted;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_page);


        userName = findViewById(R.id.user_name);
        position = findViewById(R.id.position);
        userNumber = findViewById(R.id.user_phonenumber);
        userAddress = findViewById(R.id.user_address);
        userMail = findViewById(R.id.user_email);
        recyclerView = findViewById(R.id.status_recycler);
        spin = findViewById(R.id.set_status);
        ///// image = (ImageView) findViewById(R.id.imageView8);
        image = (CircularImageView) findViewById(R.id.imageView8);
        loading = findViewById(R.id.loading);

        pCount = findViewById(R.id.pending_count);
        cCount = findViewById(R.id.completed_count);

        //////
        /*
        Toolbar toolbar = (Toolbar) findViewById(R.id.app__bar_profile);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.arrow_back_24);
        //getSupportActionBar().setDisplayShowTitleEnabled(false);

        ///////
        TextView title_top = findViewById(R.id.topTitleName);
        title_top.setText("Profile");

         */
        ///////

        Toolbar toolbar = (Toolbar) findViewById(R.id.app__bar_profile);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ///
        TextView title_top = findViewById(R.id.topTitleName);
                title_top.setText("Profile");



        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getApplicationContext(),"you clicked",Toast.LENGTH_LONG).show();
                ProfilePage.super.onBackPressed();
                //startActivity(new Intent(getApplicationContext(),MainActivity.class));
                //finish();
            }
        });

        location = new ArrayList<>();
        district = new ArrayList<>();



        /*
        location.add("wee");
        location.add("wee");
        location.add("wee");
        location.add("wee");
        location.add("wee");
        location.add("wee");
        district.add("abc");
        district.add("abc");
        district.add("abc");
        district.add("abc");
        district.add("abc");

         */

        //for text from shared prefernces file
        SharedPreferences preferences = getSharedPreferences("tokenFile", Context.MODE_PRIVATE);

        typeOfUser = preferences.getString("role","");

        userName.setText(preferences.getString("Name",""));
        //position.setText(preferences.getString("role","").toUpperCase());
        userNumber.setText(preferences.getString("PhoneNumber",""));
        userAddress.setText(preferences.getString("Address",""));
        userMail.setText(preferences.getString("Email",""));

        if(typeOfUser.equals("5")) {
            position.setText("Admin");
            getCount(adminPendingUrl,false);
            getCount(adminCompletedUrl,true);

        }
        if(typeOfUser.equals("2")) {
            position.setText("ADO");
            getCount(adoPendingUrl,false);
            getCount(adoCompletedUrl,true);
        }
        if(typeOfUser.equals("4")) {
            position.setText("DDA");
            getCount(ddaAssignedUrl,false);
            getCount(ddaCompletedUrl,true);
        }

        ///
        String imagelink=preferences.getString("Image","");

        Log.d("imagelink1",imagelink);
        String newImageLink = " ";

        char anc = imagelink.charAt(4);
        int comp = Character.compare(anc, 's');
        if(comp!=0){
            newImageLink = imagelink;
            newImageLink =  newImageLink.substring(4);
            newImageLink = "https" + newImageLink;
        }

        Log.d("imagelink2",newImageLink);

        //WORKS FINE


        final ProgressBar progressBar= findViewById(R.id.load_image);
        progressBar.setVisibility(View.VISIBLE);

        Picasso.get().load(newImageLink).error(R.drawable.user_image).into(image,new com.squareup.picasso.Callback() {
            @Override
            public void onSuccess() {
                progressBar.setVisibility(View.GONE);
                //Toast.makeText(getApplicationContext(),"Success",Toast.LENGTH_LONG).show();
                //progressBar.setVisibility(View.GONE);
            }
            @Override
            public void onError(Exception e) {
                progressBar.setVisibility(View.GONE);
                //Toast.makeText(getApplicationContext(),"Exception occured "+e.getMessage(),Toast.LENGTH_LONG).show();
                Log.d("imagelink3",e.getMessage());
            }
        });




        //Loading Image from URL
        /*
        Picasso.with(this)
                .load("https://www.simplifiedcoding.net/wp-content/uploads/2015/10/advertise.png")
                .placeholder(R.drawable.placeholder)   // optional
                .error(R.drawable.error)      // optional
                .resize(400,400)                        // optional
                .into(imageView);

        Picasso.with(this)
                .load("YOUR IMAGE URL HERE")
                .into(image);

         */

        /*
        //image.setImageResource(Integer.parseInt(imagelink));
        // Retrieves an image specified by the URL, displays it in the UI.
        ImageRequest request = new ImageRequest(imagelink,
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap bitmap) {
                        image.setImageBitmap(bitmap);
                    }
                }, 0, 0, null,
                new Response.ErrorListener() {
                    public void onErrorResponse(VolleyError error) {
                        image.setImageResource(R.drawable.user_image);
                    }
                });

        getInstance(getApplicationContext())

        getInstance(this).addToRequestQueue(request);

         {
            imageLoader = CustomVolleyRequest.getInstance(this.getApplicationContext())
                    .getImageLoader();
        }
        imageLoader.get(newImageLink, ImageLoader.getImageListener(image,
                R.drawable.user_image, android.R.drawable
                        .ic_dialog_alert));
        image.setImageUrl(newImageLink, imageLoader);

         */


        ///

        String[] adminSpinner={"Pending","Ongoing","Completed"};
        String[] adoSpinner={"Pending","Completed"};
        String[] ddaSpinner={"Assigned Pending","Unassigned Pending","Ongoing","Completed"};
        //Creating the ArrayAdapter instance having the list

        ArrayAdapter aa;
        if(typeOfUser.equals("5")) {//admin
            aa = new ArrayAdapter(this, android.R.layout.simple_spinner_item, adminSpinner);
            aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            //Setting the ArrayAdapter data on the Spinner
            spin.setAdapter(aa);
        }
        else if(typeOfUser.equals("2")) {//ado
            aa = new ArrayAdapter(this, android.R.layout.simple_spinner_item, adoSpinner);
            aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            //Setting the ArrayAdapter data on the Spinner
            spin.setAdapter(aa);
        }
        else if(typeOfUser.equals("4"))  {//dda
            aa = new ArrayAdapter(this, android.R.layout.simple_spinner_item, ddaSpinner);
            aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            //Setting the ArrayAdapter data on the Spinner
            spin.setAdapter(aa);
        }

        spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String selected_status=   spin.getItemAtPosition(spin.getSelectedItemPosition()).toString();
                location.clear();
                district.clear();
                //Toast.makeText(getApplicationContext(),selected_status + "has been selected" ,Toast.LENGTH_LONG).show();
                if(typeOfUser.equals("5")){
                    if(spin.getSelectedItemPosition()==0)
                        getData(adminPendingUrl);
                    if(spin.getSelectedItemPosition()==1)
                        getData(adminOngoingUrl);
                    if(spin.getSelectedItemPosition()==2)
                        getData(adminCompletedUrl);
                }
                if(typeOfUser.equals("2")){
                    //Toast.makeText(getApplicationContext(),"ado entry with "+ spin.getSelectedItemPosition(),Toast.LENGTH_LONG).show();
                    if(spin.getSelectedItemPosition()==0)
                        getData(adoPendingUrl);
                    if(spin.getSelectedItemPosition()==1)
                        getData(adoCompletedUrl);
                }
                if(typeOfUser.equals("4")){
                    if(spin.getSelectedItemPosition()==0)
                        getData(ddaAssignedUrl);
                    if(spin.getSelectedItemPosition()==1)
                        getData(ddaUnassignedUrl);
                    if(spin.getSelectedItemPosition()==2)
                        getData(ddaOngoingUrl);
                    if(spin.getSelectedItemPosition()==3)
                        getData(ddaCompletedUrl);
                }

                //Toast.makeText(getApplicationContext(),country,Toast.LENGTH_LONG).show();
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // DO Nothing here
            }
        });





        layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);

        ////////getData(adoPendingUrl);

        adapter = new ProfilePageAdapter(getApplicationContext(),location,district,false,false,false);
        recyclerView.setAdapter(adapter);




        adapter.notifyDataSetChanged();
        //DividerItemDecoration divider = new DividerItemDecoration(getActivity(), layoutManager.getOrientation());
        //recyclerView.addItemDecoration(divider);

    }
    private void getData(String url) {
        //location = new ArrayList<>();
        //district = new ArrayList<>();
        loading.setVisibility(View.VISIBLE);
        Log.d("get","enterd function");
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        isNextBusy = true;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("response",response.toString());
                        //Toast.makeText(getApplicationContext(),"response is "+response.toString(),Toast.LENGTH_LONG).show();

                        try {
                            JSONObject rootObject = new JSONObject(String.valueOf(response));
                            JSONArray resultsArray = rootObject.getJSONArray("results");
                           // Toast.makeText(getApplicationContext(),resultsArray.toString(),Toast.LENGTH_LONG).show();
                            /*
                            location.add("wee");
                            location.add("wee");
                            location.add("wee");
                            location.add("wee");
                            location.add("wee");
                            location.add("wee");
                            district.add("abc");
                            district.add("abc");
                            district.add("abc");
                            district.add("abc");
                            district.add("abc");

                             */
                            nextUrl = rootObject.getString("next");

                            if(resultsArray.length()== 0){
                                adapter.notifyDataSetChanged();
                                Toast.makeText(getApplicationContext(),"Array is empty",Toast.LENGTH_LONG).show();

                            }
                            for (int i = 0; i < resultsArray.length(); i++) {
                                JSONObject singleObject = resultsArray.getJSONObject(i);
                                String state=" ",districtname=" ",blockname=" ",villagename = " ";
                                try {
                                    state= singleObject.getString("state");
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                try {
                                    JSONObject districtObject = singleObject.getJSONObject("district");
                                    districtname= districtObject.getString("district");
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                try {
                                    blockname = singleObject.getString("block");
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                try {
                                    JSONObject villageObject = singleObject.getJSONObject("village_name");
                                    villagename= villageObject.getString("village");
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                                String location_name = villagename + ", " + blockname;
                                String district_name = districtname + ", " + state;
                                //String location_name = singleObject.getString("village_name")+", "+singleObject.getString("block");
                                //String district_name = singleObject.getString("district")+", "+singleObject.getString("state") ;

                                //Toast.makeText(getApplicationContext(),location_name1+location_name2+district_name,Toast.LENGTH_LONG).show();
                                location.add(location_name);
                                district.add(district_name);

                            }

                            isNextBusy = false;
                            adapter.notifyDataSetChanged();
                            loading.setVisibility(View.GONE);

                        } catch (JSONException e) {
                            e.printStackTrace();
                            loading.setVisibility(View.GONE);
                            Toast.makeText(getApplicationContext(),"An exception occurred",Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        loading.setVisibility(View.GONE);
                        Toast.makeText(getApplicationContext(),error.getMessage(),Toast.LENGTH_LONG).show();
                        if (error instanceof NoConnectionError || error instanceof TimeoutError)
                            Toast.makeText(getApplicationContext(), "Please Check your internet connection", Toast.LENGTH_LONG).show();
                        else if (error instanceof AuthFailureError) {
                            // Error indicating that there was an Authentication Failure while performing the request
                            Toast.makeText(getApplicationContext(), "This error is case2", Toast.LENGTH_LONG).show();
                        } else if (error instanceof ServerError) {
                            //Indicates that the server responded with a error response
                            Toast.makeText(getApplicationContext(), "This error is server error", Toast.LENGTH_LONG).show();
                        } else if (error instanceof NetworkError) {
                            //Indicates that there was network error while performing the request
                            Toast.makeText(getApplicationContext(), "This error is case4", Toast.LENGTH_LONG).show();
                        } else if (error instanceof ParseError) {
                            // Indicates that the server response could not be parsed
                            Toast.makeText(getApplicationContext(), "This error is case5", Toast.LENGTH_LONG).show();
                        }
                        else
                            Toast.makeText(getApplicationContext(), "Something went wrong, please try again",
                                    Toast.LENGTH_LONG).show();

                        isNextBusy = false;
                        Log.d(TAG, "onErrorResponse: " + error);
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> map = new HashMap<>();
                SharedPreferences prefs = getSharedPreferences("tokenFile", Context.MODE_PRIVATE);
                String token = prefs.getString("key", "");
                map.put("Authorization", "Token " + token);
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
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                int totalCount, pastItemCount, visibleItemCount;
                if (dy > 0) {
                    totalCount = layoutManager.getItemCount();
                    pastItemCount = layoutManager.findFirstVisibleItemPosition();
                    visibleItemCount = layoutManager.getChildCount();
                    if ((pastItemCount + visibleItemCount) >= totalCount) {
                        Log.d(TAG, "onScrolled: " + nextUrl);
                        if (!nextUrl.equals("null") && !isNextBusy)
                            getData(nextUrl);
                    }
                }
                super.onScrolled(recyclerView, dx, dy);
            }
        });

    }

    public  void getCount(String url, final boolean flag){
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        isNextBusy = true;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("response",response.toString());
                        try {
                            JSONObject rootObject = new JSONObject(String.valueOf(response));
                            String acount = rootObject.getString("count");
                            if(flag == false)
                                pCount.setText(acount);
                            if(flag == true)
                                cCount.setText(acount);

                            }
                        catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(),"An exception occurred",Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(),error.getMessage(),Toast.LENGTH_LONG).show();
                        if (error instanceof NoConnectionError || error instanceof TimeoutError)
                            Toast.makeText(getApplicationContext(), "Please Check your internet connection", Toast.LENGTH_LONG).show();
                        else if (error instanceof AuthFailureError) {
                            // Error indicating that there was an Authentication Failure while performing the request
                            Toast.makeText(getApplicationContext(), "This error is case2", Toast.LENGTH_LONG).show();
                        } else if (error instanceof ServerError) {
                            //Indicates that the server responded with a error response
                            Toast.makeText(getApplicationContext(), "This error is server error", Toast.LENGTH_LONG).show();
                        } else if (error instanceof NetworkError) {
                            //Indicates that there was network error while performing the request
                            Toast.makeText(getApplicationContext(), "This error is case4", Toast.LENGTH_LONG).show();
                        } else if (error instanceof ParseError) {
                            // Indicates that the server response could not be parsed
                            Toast.makeText(getApplicationContext(), "This error is case5", Toast.LENGTH_LONG).show();
                        }
                        else
                            Toast.makeText(getApplicationContext(), "Something went wrong, please try again", Toast.LENGTH_LONG).show();

                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> map = new HashMap<>();
                SharedPreferences prefs = getSharedPreferences("tokenFile", Context.MODE_PRIVATE);
                String token = prefs.getString("key", "");
                map.put("Authorization", "Token " + token);
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
}
