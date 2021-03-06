package com.example.appthesis;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ElectricianList extends AppCompatActivity {
    RecyclerView recyclerView;
    String url_info_electrician = "http://192.168.43.189/android_register_login/electrician_info.php";
    List<Electrician> electricians =  new ArrayList<>();
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_electrician_list);


        recyclerView = findViewById(R.id.recyclerView9);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);

        showDetails();

    }

    private void showDetails() {
        StringRequest request = new StringRequest(Request.Method.POST, url_info_electrician, new Response.Listener<String>() {
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject =  new JSONObject(response);
                    String message = jsonObject.getString("response");
                    JSONArray jsonArray = jsonObject.getJSONArray("values");
                    if(message.equals("success")){
                        for(int i=0; i<jsonArray.length(); i++) {
                            JSONObject object = jsonArray.getJSONObject(i);
                            String id = object.getString("id").trim();
                            String fname = object.getString("fname").trim();
                            String lname = object.getString("lname").trim();
                            String fullname = " " +fname + "  " + lname;

                            electricians.add(new Electrician(id, fullname));
                            initializeAdapter();

                        }
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }



            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT);
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return super.getParams();
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(request);

    }
    public void  initializeAdapter(){
        ElectricianAdapter adapter = new ElectricianAdapter(electricians,this);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }
}
