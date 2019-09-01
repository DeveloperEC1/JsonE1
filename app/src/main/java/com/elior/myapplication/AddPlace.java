package com.elior.myapplication;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

public class AddPlace extends AppCompatActivity {

    private EditText editText;
    private static String HI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_place);

        initUI();
        getData();
    }

    private void initUI() {
        editText = findViewById(R.id.editText8);

        HI = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=31.4,35.1&radius=50000&sensor=true&rankby=prominence&types=&keyword=&key=" + getString(R.string.my_key);
    }

    private void getData() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, HI, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject mainObj = new JSONObject(response);
                    JSONArray list = mainObj.getJSONArray("results");
                    for (int i = 0; i < list.length(); i++) {
                        JSONObject elem = list.getJSONObject(i);
                        JSONObject elem2 = elem.getJSONObject("geometry");
                        JSONObject elem3 = elem2.getJSONObject("location");
                        double lat = elem3.getDouble("lat");
                        editText.setText(String.valueOf(lat));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

}
