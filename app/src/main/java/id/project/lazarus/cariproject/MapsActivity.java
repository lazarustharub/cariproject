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
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;

import at.markushi.ui.CircleButton;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener,
        NavigationView.OnNavigationItemSelectedListener{

    Button button1;
    Button burger_button;
//    Button skip;
    EditText searchview;
    TextView locationTv;
    private LocationManager locationManager;
    private LocationListener locationListener;
    private CircleButton my_location;
    Marker myLocationMarker = null;
    private TextView textLongLat;
    JSONObject jsonGPS;
    JSONObject json;

    private GoogleMap mMap;
    private Socket socket;
    Handler handler;
    Marker vehicleMarker;
    private DrawerLayout drawerLayout;
    private List<Marker> markers;
    SharedPreferenceManager sharedPreferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deletable);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        sharedPreferenceManager = SharedPreferenceManager.getInstance(this);
        handler = new Handler(Looper.getMainLooper());
        markers = new ArrayList<>();
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

//        textLongLat = (TextView) findViewById(R.id.textview_longlat);
//        skip = (Button) findViewById(R.id.skip);
//        skip.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(MapsActivity.this, ProfileActivity.class));
//            }
//        });
        button1 = (Button)findViewById(R.id.button1);
        burger_button = (Button)findViewById(R.id.burger_button);
        burger_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    drawerLayout.openDrawer(Gravity.LEFT);
            }
        });

        searchview = (EditText)findViewById(R.id.searchView1);
      //  locationTv = (TextView)findViewById(R.id.latlongLocation);
        my_location = (CircleButton) findViewById(R.id.my_location);
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                LatLng myLocation = new LatLng(location.getLatitude(),location.getLongitude());
                if(myLocationMarker==null) {
                    myLocationMarker = mMap.addMarker(new MarkerOptions().position(myLocation).title("Latitude:" + location.getLatitude() + ", Longitude:"
                            + location.getLongitude()));
//                    markers.add(myLocationMarker);
                    Log.d("Map", "marker created : "+myLocationMarker.getTitle());
                }
                else myLocationMarker.setPosition(myLocation);
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
//            socket = IO.socket("http://192.168.1.136:3000");
            socket = IO.socket("http://192.168.1.136:3000");

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
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(MapsActivity.this, "Socket disconnected", Toast.LENGTH_SHORT).show();
                        }
                    });
                }

            }).on(Socket.EVENT_CONNECT_ERROR, new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(MapsActivity.this, "Socket error : ", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }).on(Socket.EVENT_CONNECT_TIMEOUT, new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(MapsActivity.this, "Socket timeout", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });

            // Receiving an object
            socket.on("gps-data", new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    try {
                        String obj = (String) args[0];
                        json = new JSONObject(obj);
                        jsonGPS = new JSONObject(json.getString("message"));
                        Log.d("Map","GPS data : " +jsonGPS);
                        final String serial_id =json.getString("serial_id");
                        final String speed_car =jsonGPS.getString("speed");
                        final String alti = jsonGPS.getString("alti");
                        Log.d("Maps","message : " + jsonGPS);
                        handler.post(new Runnable() {
                        @Override
                        public void run() {

                        }
                    });

                        final Double lat = Double.valueOf(jsonGPS.getString("lat"));
                        final Double lng = Double.valueOf(jsonGPS.getString("lon"));
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                if(vehicleMarker == null){
                                    vehicleMarker = mMap.addMarker(new MarkerOptions().position(new LatLng(lat,lng)).title(serial_id));
                                    CameraUpdate cu = CameraUpdateFactory.newLatLngZoom(new LatLng(lat,lng),13);
                                    mMap.animateCamera(cu);
                                }
                                else vehicleMarker.setPosition(new LatLng(lat,lng));
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
            socket.connect();
        }
        catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        socket.emit("gps-off");
        socket.disconnect();
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


        mMap.setOnMarkerClickListener(this);
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_carlist) {
            // Handle the camera action
            startActivity(new Intent(MapsActivity.this, CarListActivity.class));
//        } else if (id == R.id.nav_userprofile) {

//        } else if (id == R.id.nav_slideshow) {

//        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_logout) {
            sharedPreferenceManager.logout();
            startActivity(new Intent(MapsActivity.this, LoginActivity.class));
            finish();
            Toast.makeText(this, "Logout berhasil", Toast.LENGTH_SHORT).show();
//        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
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


    @Override
    public boolean onMarkerClick(Marker marker) {
        Log.d("Map","markerclicked: "+markers.size());
        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                Intent intent = new Intent(MapsActivity.this,ProfileActivity.class);
                intent.putExtra("serial_id",marker.getTitle());
                startActivity(intent);
            }
        });
//        for(int i=0;i<markers.size();i++){
////            if(marker.getTitle().equalsIgnoreCase(markers.get(i).getTitle())){
//                Log.d("Map","markerFound");
//                textLongLat.setText(marker.getTitle());
////            }
//        }
        return false;
    }
}
