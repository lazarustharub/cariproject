package id.project.lazarus.cariproject;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.Socket;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.Map;
import java.util.logging.Handler;

import io.socket.client.IO;
import io.socket.emitter.Emitter;

public class ProfileActivity extends AppCompatActivity {


    TextView name_car;
    TextView year_car;
    TextView color_car;
    TextView fuel_car;
    TextView bought_car;
    TextView service_car;
    TextView license_car;
    String id_car;
    TextView status_car;
    Button button_camera;
    ImageView picture_sec;

    private ImageView car_pp;
    private io.socket.client.Socket socket;
    android.os.Handler handler;

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
        handler = new android.os.Handler();
        initSocket();

        picture_sec = (ImageView) findViewById(R.id.picture_sec);
        button_camera = (Button) findViewById(R.id.button_camera);

        button_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(socket!=null){
                    socket.emit("capture-cam");
                }
            }
        });

        car_pp = (ImageView) findViewById(R.id.car_pp);

        Picasso.with(this)
                .load("http://115.85.70.168:1234/cariadmin_db/car_pp/"+id_car+".jpg")
                .into(car_pp);

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
                                    status_car.setText("Online");
                                }
                                else {
                                    status_car.setText("Offline");
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
        status_car = (TextView) findViewById(R.id.status_car);
    }

    private void initSocket(){
        try {
            socket = IO.socket("http://192.168.1.115:3000");

        socket.on(io.socket.client.Socket.EVENT_CONNECT, new Emitter.Listener() {

            @Override
            public void call(Object... args) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(ProfileActivity.this, "Socket connected", Toast.LENGTH_SHORT).show();
                        socket.emit("gps-on");
                    }
                });

            }

        }).on("event", new Emitter.Listener() {

            @Override
            public void call(Object... args) {
            }

        }).on(io.socket.client.Socket.EVENT_DISCONNECT, new Emitter.Listener() {

            @Override
            public void call(Object... args) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(ProfileActivity.this, "Socket disconnected", Toast.LENGTH_SHORT).show();
                    }
                });
            }

        }).on(io.socket.client.Socket.EVENT_CONNECT_ERROR, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(ProfileActivity.this, "Socket error : ", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }).on(io.socket.client.Socket.EVENT_CONNECT_TIMEOUT, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(ProfileActivity.this, "Socket timeout", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }).on("result-capture-cam", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                final String base64 = (String)args[0];
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        byte[] decodedString = Base64.decode(base64, Base64.DEFAULT);
                        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                        picture_sec.setImageBitmap(decodedByte);

//                        Toast.makeText(ProfileActivity.this, ""+base64, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        socket.connect();
    }
     catch (URISyntaxException e) {


        e.printStackTrace();
    }
    }


}
