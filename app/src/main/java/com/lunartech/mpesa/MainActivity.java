package com.lunartech.mpesa;

import androidx.appcompat.app.AppCompatActivity;
import androidx.collection.ArraySet;

import android.content.Context;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.apache.xerces.impl.dv.util.Base64;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {


Button stkbtn;

RequestQueue mRequestQue;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    mRequestQue = Volley.newRequestQueue(this);

stkbtn=findViewById(R.id.btn);
stkbtn.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {

  authTok(MainActivity.this);


    }
});




    }
    public void stkPushIssuer(String token,String PhoneNumber,int Amount)
    {


        if(PhoneNumber.startsWith("0"))
        {
            PhoneNumber=PhoneNumber.replaceFirst("0", "254");
        }
        if(PhoneNumber.startsWith("+254"))
        {

            PhoneNumber=PhoneNumber.substring(1);
        }
        Gson gson=new Gson();
        JSONObject json = new JSONObject();



        try {

            json.put("BusinessShortCode","7425313");
            json.put("Password","NzQyNTMxMzM0YTNmMTAwZTcyNDVmNzg3YmViYjI2ZGVlMTFmM2Q1ODAyNjJhMzg3ZmY0NTIzZGM2ZmYyNjE1ZDBmMDdiMmYyMDE5MDIxNjE2NTYyNw==");
            json.put("Timestamp","20190216165627");
            json.put("TransactionType","CustomerBuyGoodsOnline");
            json.put("Amount",Amount);
            json.put("PartyA","254707353225");
            json.put("PartyB","5426913");
            json.put("PhoneNumber",PhoneNumber);
            json.put("CallBackURL","https://api.lunar.cyou/api/lipacallback.php");
            json.put("AccountReference","Airtime");
            json.put("TransactionDesc","Purchase");

            String url="https://api.safaricom.co.ke/mpesa/stkpush/v1/processrequest";





            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url,
                    json,
                    new com.android.volley.Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            System.err.println(response);
                            try {
                                if(response.get("ResponseCode").equals("0"))
                                {

                                }
                                else {
                                    System.err.println("Error"+response.toString());

                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


                            Log.d("MUR", "onResponse: "+response);
                        }
                    }, new com.android.volley.Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    System.err.println(json);
                    // As of f605da3 the following should work
                    NetworkResponse response = error.networkResponse;
                    if (error instanceof ServerError && response != null) {
                        try {
                            String res = new String(response.data,
                                    HttpHeaderParser.parseCharset(response.headers, "utf-8"));
                            // Now you can use any deserializer to make sense of data
                            JSONObject obj = new JSONObject(res);
                            System.err.println(res);
                        } catch (UnsupportedEncodingException e1) {
                            // Couldn't properly decode data to string
                            e1.printStackTrace();
                        } catch (JSONException e2) {
                            // returned data is not JSONObject?
                            e2.printStackTrace();
                        }
                    }
                }
            }
            ){
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> header = new HashMap<>();
                    header.put("content-type","application/json");

                    header.put("authorization", "Bearer " + token);
                    return header;
                }
            };
            request.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 5, 1.0f));


            mRequestQue.add(request);
        }
        catch (Exception e)

        {
            e.printStackTrace();
        }










    }

    public void authTok(Context context)
    {

        Gson gson=new Gson();
        JSONObject json = new JSONObject();
        try {
            String app_key = "yBIf50DIGeUu2dRzZHWVOurNj0nAtA19";
            String app_secret = "oadlZaBFbXCaEkzZ";
            String appKeySecret = app_key + ":" + app_secret;
            byte[] bytes = appKeySecret.getBytes("ISO-8859-1");
            String auth = Base64.encode(bytes);
            String url="https://api.safaricom.co.ke/oauth/v1/generate?grant_type=client_credentials";
            JSONArray array=new JSONArray();


String payingPhone="254714947370";
int paymentValue=1;

            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url,
                    null,
                    new com.android.volley.Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {

                            try {
                                stkPushIssuer(response.getString("access_token"),payingPhone,paymentValue);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


                            Log.d("MUR", "onResponse: "+response);
                        }
                    }, new com.android.volley.Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    System.err.println(error);
                    Log.d("MUR", "onError............: "+error.networkResponse);
                }
            }
            ){
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> header = new HashMap<>();
                    header.put("content-type","application/json");
                    header.put("cache-control", "no-cache");
                    header.put("authorization", "Basic " + auth);
                    return header;
                }
            };

            mRequestQue.add(request);
        }
        catch (Exception e)

        {
            e.printStackTrace();
        }






    }


}