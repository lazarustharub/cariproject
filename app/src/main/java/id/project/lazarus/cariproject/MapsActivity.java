package id.project.lazarus.cariproject;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.nearby.messages.EddystoneUid;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.InputMismatchException;
import java.util.List;

import at.markushi.ui.CircleButton;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

import static android.R.id.button1;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback{

    Button button1;
    EditText searchview;
    TextView locationTv;
    private LocationManager locationManager;
    private LocationListener locationListener;
    private CircleButton my_location;
    Marker myLocationMarker;

    JSONObject json;

    private GoogleMap mMap;
    private Socket socket;
    Handler handler;
    Marker vehicleMarker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        handler = new Handler(Looper.getMainLooper());
        button1 = (Button)findViewById(R.id.button1);
        searchview = (EditText)findViewById(R.id.searchView1);
        locationTv = (TextView)findViewById(R.id.latlongLocation);
        my_location = (CircleButton) findViewById(R.id.my_location);
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                LatLng myLocation = new LatLng(location.getLatitude(),location.getLongitude());
                if(myLocationMarker!=null){
                    myLocationMarker.remove();
                }
                myLocationMarker = mMap.addMarker(new MarkerOptions().position(myLocation).title("Latitude:" + location.getLatitude() + ", Longitude:"
                        + location.getLongitude()));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(myLocation));
                mMap.animateCamera(CameraUpdateFactory.zoomTo(13f));
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };

        my_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getLocation();
            }
        });
        button1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                String g = searchview.getText().toString();

                Geocoder geocoder = new Geocoder(getBaseContext());
                List<Address> addresses = null;

                try {
                    // Getting a maximum of 3 Address that matches the input
                    // text
                    addresses = geocoder.getFromLocationName(g, 3);
                    if (addresses != null && !addresses.equals(""))
                        search(addresses);

                } catch (Exception e) {

                }

            }
        });
        try {
            socket = IO.socket("http://192.168.1.155:3000");
            socket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {

                @Override
                public void call(Object... args) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(MapsActivity.this, "Socket connected", Toast.LENGTH_SHORT).show();
                            socket.emit("gps-on");
                        }
                    });

                }

            }).on("event", new Emitter.Listener() {

                @Override
                public void call(Object... args) {
                }

            }).on(Socket.EVENT_DISCONNECT, new Emitter.Listener() {

                @Override
                public void call(Object... args) {
                }

            });
            socket.connect();

            // Receiving an object
            socket.on("gps-data", new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    try {
                        String obj = (String) args[0];

                        json = new JSONObject(obj);
                        Log.d("Maps","message : " + json);
                        handler.post(new Runnable() {
                        @Override
                        public void run() {

                        }
                    });

                        final Double lat = Double.valueOf(json.getString("lat"));
                        final Double lng = Double.valueOf(json.getString("lon"));
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                if(vehicleMarker != null){
                                    vehicleMarker.remove();
                                }
                                vehicleMarker = mMap.addMarker(new MarkerOptions().position(new LatLng(lat,lng)));
                                CameraUpdate cu = CameraUpdateFactory.newLatLng(new LatLng(lat,lng));
                                mMap.animateCamera(cu);
                            }
                        });


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    catch (InputMismatchException e){
                        e.printStackTrace();
                    }
                    catch (NumberFormatException e){
                        e.printStackTrace();
                    }
                }
            });
        }
        catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                startActivity(new Intent(MapsActivity.this, ProfileActivity.class));
            }
        });
        // Add a marker in Sydney and move the camera
        getLocation();

    }

    private void getLocation(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if(ActivityCompat.checkSelfPermission(MapsActivity.this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
                requestPermissions(new String[]{
                        android.Manifest.permission.ACCESS_COARSE_LOCATION,
                        android.Manifest.permission.INTERNET
                },10);
                return;
            }
        }

        Log.d("SMD","Locatin : getLocation");
        locationManager.requestSingleUpdate(LocationManager.NETWORK_PROVIDER,locationListener,null);
    }

    protected void search(List<Address> addresses) {

        Address address = (Address) addresses.get(0);
        Double home_long = address.getLongitude();
        Double home_lat = address.getLatitude();
        LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());

        String addressText = String.format(
                "%s, %s",
                address.getMaxAddressLineIndex() > 0 ? address
                        .getAddressLine(0) : "", address.getCountryName());

        MarkerOptions markerOptions = new MarkerOptions();

        markerOptions.position(latLng);
        markerOptions.title(addressText);
        markerOptions.snippet(String.valueOf(latLng));

        mMap.clear();
        mMap.addMarker(markerOptions);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(15));

        
//        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {   <PINDAH LAYOUT DENGAN KLIK MARKER SAJA>
//            @Override
//            public boolean onMarkerClick(Marker marker) {
//                startActivity(new Intent(MapsActivity.this, ProfileActivity.class));
//                return false;
//            }
//        }); <PINDAH LAYOUT DENGAN KLIK MARKER SAJA>

        locationTv.setText("Latitude:" + address.getLatitude() + ", Longitude:"
                + address.getLongitude());


    }


}
