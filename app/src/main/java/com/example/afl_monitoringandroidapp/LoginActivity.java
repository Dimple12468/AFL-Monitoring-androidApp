package com.example.afl_monitoringandroidapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.ClientError;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "Login Screen";
    private TextInputLayout _editEmail, _editPassword;
    private EditText editEmail, editPassword;
    private String urlGet, urlPost;
    private Button onClickLogin;
    private String token, typeOfUser, Name;
    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

        urlGet = Globals.userTypeURL;
        urlPost = Globals.urlPost_user;

        Log.d(TAG,urlGet);
        Log.d(TAG,urlPost);

        onClickLogin = findViewById(R.id.button);
        _editEmail = findViewById(R.id.editEmail);
        _editPassword = findViewById(R.id.editPass);

        editEmail = _editEmail.getEditText();
        editPassword = _editPassword.getEditText();

        final SharedPreferences sp = getSharedPreferences("tokenFile", Context.MODE_PRIVATE);
        String Usertype = sp.getString("role", "");
        if (sp.contains("key")) {
            Intent intent = null;
            if (Usertype.equals("1"))
                Toast.makeText(LoginActivity.this, "login for farmer", Toast.LENGTH_SHORT).show();
            if (Usertype.equals("2"))
                intent = new Intent(this, Home.class);
            if (Usertype.equals("3"))
                Toast.makeText(this, "login for block admin", Toast.LENGTH_SHORT).show();
            if (Usertype.equals("4"))
                intent = new Intent(this, Home.class);
            if (Usertype.equals("5"))
                intent = new Intent(this, Home.class);
            if (Usertype.equals("6"))
                Toast.makeText(this, "login for super admin", Toast.LENGTH_SHORT).show();
            if (intent != null) {
                startActivity(intent);
                finish();
            }
        }

            onClickLogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String mEmail=editEmail.getText().toString().trim();
                    String mPass = editPassword.getText().toString().trim();

                    if (!mEmail.isEmpty() && !mPass.isEmpty()) {
                        Login(mEmail, mPass);
                    } else if (!mEmail.isEmpty() && mPass.isEmpty()) {
                        displayDialog("Please insert password");
                    } else if (mEmail.isEmpty() && !mPass.isEmpty()) {
                        displayDialog("Please insert Email");
                    } else {
                        displayDialog("Please insert Email and password");
                    }
                }
            });
        }

    private void Login(final String email, final String password) {

        dialog = new ProgressDialog(LoginActivity.this,R.style.AlertDialog);

        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setMessage("Logging In....");
        dialog.show();


        Log.d(TAG, "onResponse: login clicked");
        final RequestQueue MyRequestQueue = Volley.newRequestQueue(this);
        JSONObject postparams = null;
        try {
            postparams = new JSONObject();
            postparams.put("username", email);
            postparams.put("password", password);
        } catch (JSONException e) {
            Log.d(TAG, "Login: Error:" + e);
            Toast.makeText(getApplicationContext(), "Please try again", Toast.LENGTH_LONG).show();
            dialog.dismiss();
            onClickLogin.setEnabled(true);
        }

        final JsonObjectRequest jsonObjectRequest1 = new JsonObjectRequest(Request.Method.GET, urlGet, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        String pk;
                        try {
                            JSONObject c = new JSONObject(String.valueOf(response));
                            Log.d(TAG, "onResponse: " + c);
                            JSONObject a = c.getJSONObject("user");
                            Log.d(TAG, "onResponse: user:" + a);
                            typeOfUser = a.getString("role");
                            Name = a.getString("name");
                            String number="Not Available",email="Not Available",address="Not Available",image=null;
                            try{
                                number = a.getString("phone_number");
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            try{
                                email = a.getString("email");
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            try{
                                image = a.getString("image");
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            try{
                                JSONObject state= a.getJSONObject("state");
                                address = state.getString("state");
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            pk = a.getString("id");
                            Log.d(TAG, "onResponse: valuepk"+pk);
                            SharedPreferences.Editor editor = getSharedPreferences("tokenFile", Context.MODE_PRIVATE).edit();
                            editor.putString("role", typeOfUser);
                            editor.putString("Name", Name);
                            editor.putString("id", pk);
                            editor.putString("PhoneNumber",number);
                            editor.putString("Email",email);
                            editor.putString("Address",address);
                            editor.putString("Image",image);
                            editor.apply();

                            Intent intent = null;

                            if (typeOfUser.equals("1")) {
                                Toast.makeText(LoginActivity.this, "login for farmer", Toast.LENGTH_SHORT).show();
                            }else if (typeOfUser.equals("2")) {
                                intent = new Intent(LoginActivity.this, Home.class);
//                                new Home(typeOfUser);
                                startActivity(intent);
                                finish();
                            }else if (typeOfUser.equals("3")) {
                                Toast.makeText(LoginActivity.this, "login for block admin", Toast.LENGTH_SHORT).show();
                            }else if (typeOfUser.equals("4")) {
                                onClickLogin.setEnabled(false);
                                onClickLogin.setClickable(false);
                                intent = new Intent(LoginActivity.this, Home.class);
                                startActivity(intent);
                                finish();
                            }else if (typeOfUser.equals("5")) {
                                intent = new Intent(LoginActivity.this,Home.class);
                                startActivity(intent);
//                                finish();
                            }else if (typeOfUser.equals("6")) {
                                Toast.makeText(LoginActivity.this, "login for super admin", Toast.LENGTH_SHORT).show();
                            } else {
                                dialog.dismiss();
                                //Toast.makeText(getApplicationContext(), "Invalid User", Toast.LENGTH_SHORT).show();
                                displayDialog("Incorrect Password or Email");
                            }


                        } catch (JSONException e) {
                            Log.d(TAG, "onResponse: error in get catch block :" + e.getMessage());
                            //Toast.makeText(getApplicationContext(), "Please try again", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                            displayDialog("Please try again");
                            onClickLogin.setEnabled(true);
//
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error instanceof NoConnectionError)
                            noInternetDialog();
                        else if (error instanceof ClientError)
                            displayDialog("Invalid User!");
                        else {
                            Log.d(TAG,"error.networkResponse.toString()" + error.networkResponse.toString());
                            displayDialog("Something went wrong,please try again");
                        }
                        onClickLogin.setEnabled(true);
                        dialog.dismiss();
                        Log.d(TAG, "onErrorResponse: some error in get: " + error.getLocalizedMessage());
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
                Log.d(TAG, "getHeaders: ");
                headers.put("Authorization", "Token " + token);
                Log.d(TAG,token);
                return headers;
            }
        };

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, urlPost, postparams,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        //retrieve the token from server
                        try {
                            JSONObject jsonObject = new JSONObject(String.valueOf(response));
                            token = jsonObject.getString("key");
                            SharedPreferences.Editor editor = getSharedPreferences("tokenFile", Context.MODE_PRIVATE).edit();
                            editor.putString("key", token);
                            editor.apply();
                            MyRequestQueue.add(jsonObjectRequest1);
                            Log.d(TAG, "onResponse: key:" + token);
                        } catch (JSONException e) {
                            dialog.dismiss();
                            onClickLogin.setEnabled(true);
                            Log.d(TAG, "onResponse: error in post catch block: " + e);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error instanceof NoConnectionError)
                            noInternetDialog();
                        else if (error instanceof ClientError)
                            displayDialog("Incorrect Password or Email");
                        else
                            displayDialog("Something went wrong,please try again");
                        Log.d(TAG, "onErrorResponse: invalid user : " + error);
                        dialog.dismiss();
                        onClickLogin.setEnabled(true);
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

    public final void displayDialog(String str){

        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(LoginActivity.this,R.style.AlertDialog);
        builder.setMessage(str);
        androidx.appcompat.app.AlertDialog alertDialog=builder.create();
        alertDialog.show();
        alertDialog.getWindow().getWindowStyle();
    }

    public void noInternetDialog() {
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(LoginActivity.this, R.style.noInternetDialog);
        builder.setMessage("Not connected to Internet");
        androidx.appcompat.app.AlertDialog alertDialog = builder.create();
        WindowManager.LayoutParams wmlp = alertDialog.getWindow().getAttributes();
        wmlp.gravity = Gravity.TOP | Gravity.CENTER;
        alertDialog.show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (dialog != null) {
            dialog.dismiss();
            dialog = null;
        }
    }

}



