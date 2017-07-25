package id.project.lazarus.cariproject;

import android.app.Activity;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CarListActivity extends AppCompatActivity {

    List <Car> list_car;
    String id_car;
    ListView list_view;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_list);
        list_car = new ArrayList<>();
        initView();
        if(Util.InternetConnectionStatus(this)){
            initData();
        }else{
            Toast.makeText(this,"Failed Connecting Internet",Toast.LENGTH_LONG).show();
        }


    }
    private void initData() {
        StringRequest request = new StringRequest(Request.Method.GET, Api.GET_ALL_CAR,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Response SIAP", "This is cari => response " +response);
                        try {
                            Log.d("try masuk", "This is cari => response " +response);
                            JSONObject jsonresponse = new JSONObject(response);
                            if(Util.RequestChecker(jsonresponse)){
                                JSONObject jsoncar = jsonresponse.getJSONArray(Api.RESPONSE_DATA).getJSONObject(0);
                                String text_namecar = jsoncar.getString(Api.RESPONSE_NAMECAR);

                                String text_licensecar = jsoncar.getString(Api.RESPONSE_LICENSECAR);
                                progressBar.setVisibility(View.GONE);
                                list_view.setVisibility(View.VISIBLE);

                                Car car = new Car(jsoncar);

                                list_car.add(car);

                                Toast.makeText(CarListActivity.this, ""+ list_car.size(), Toast.LENGTH_SHORT).show();

                                ListAdapter adapter = new ListAdapter(list_car, CarListActivity.this);

                                list_view.setAdapter(adapter);


                            }
                            else{
                                Toast.makeText(CarListActivity.this,"Request Failed",Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            Log.d("catch masuk", "This is cari => response " +e.getMessage());
                            e.printStackTrace();
                        }
                    }
    }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Response SIAP", "This is cari => error " +error.getMessage());
                Toast.makeText(CarListActivity.this,"WebService Connection Failed "+error,Toast.LENGTH_LONG).show();
            }
        });
        Volley.newRequestQueue(this).add(request);
    }


    private void initView() {
        list_view = (ListView) findViewById(R.id.list_car);
        progressBar = (ProgressBar)findViewById(R.id.proCollageList);
    }



}
