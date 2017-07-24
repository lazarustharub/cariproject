package id.project.lazarus.cariproject;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ProfileActivity extends AppCompatActivity {


    TextView name_car;
    TextView year_car;
    TextView color_car;
    TextView fuel_car;
    TextView bought_car;
    TextView service_car;
    TextView license_car;
    String id_car;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        id_car = getIntent().getStringExtra("serial_id");
        initView();
        if(Util.InternetConnectionStatus(this)){
            initData();
        }else{
            Toast.makeText(this,"Failed Connecting Internet",Toast.LENGTH_LONG).show();
        }

    }

    private void initData() {
        StringRequest request = new StringRequest(Request.Method.POST, Api.GET_CAR,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonresponse = new JSONObject(response);
                            if(Util.RequestChecker(jsonresponse)){
                                JSONObject jsoncar = jsonresponse.getJSONObject(Api.RESPONSE_DATA);
                                String text_namecar = jsoncar.getString(Api.RESPONSE_NAMECAR);
                                String text_yearcar = jsoncar.getString(Api.RESPONSE_YEARCAR);
                                String text_fuelcar = jsoncar.getString(Api.RESPONSE_FUELCAR);
                                String text_boughtcar = jsoncar.getString(Api.RESPONSE_BOUGHTCAR);
                                String text_servicecar = jsoncar.getString(Api.RESPONSE_SERVICECAR);
                                String text_licensecar = jsoncar.getString(Api.RESPONSE_LICENSECAR);
                                String text_pictureprofil = jsoncar.getString(Api.RESPONSE_PICTUREPROFIL);
                                String text_picturesec = jsoncar.getString(Api.RESPONSE_PICTURESEC);
                                String text_colorcar = jsoncar.getString(Api.RESPONSE_COLORCAR);

                                boolean status = jsoncar.getInt(Api.RESPONSE_STATUS) == 1 ? true:false;

                                if(status){
                                    // Do Something;
                                }
                                else {
                                    // Do something;
                                }

                                name_car.setText(text_namecar);
                                year_car.setText(text_yearcar);
                                fuel_car.setText(text_fuelcar);
                                bought_car.setText(text_boughtcar);
                                service_car.setText(text_servicecar);
                                license_car.setText(text_licensecar);
                                color_car.setText(text_colorcar);

                            }
                            else{
                                Toast.makeText(ProfileActivity.this,"Request Failed",Toast.LENGTH_LONG).show();

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ProfileActivity.this,"WebService Connection Failed",Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put(Api.PARAM_IDCAR, id_car);
                return params;
            }
        };
        Volley.newRequestQueue(this).add(request);
    }

    private void initView() {
        name_car = (TextView) findViewById(R.id.name_car);
        year_car = (TextView) findViewById(R.id.year_car);
        color_car = (TextView) findViewById(R.id.color_car);
        fuel_car = (TextView) findViewById(R.id.fuel_car);
        bought_car = (TextView) findViewById(R.id.bought_car);
        service_car = (TextView) findViewById(R.id.service_car);
        license_car = (TextView) findViewById(R.id.license_car);

    }
}
