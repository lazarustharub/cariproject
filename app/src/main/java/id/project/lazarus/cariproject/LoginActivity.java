package id.project.lazarus.cariproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;

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

public class LoginActivity extends AppCompatActivity {

    EditText et_email;
    EditText et_password;

    Button button_login;
    String email;
    String password;

    SharedPreferenceManager sharedPreferenceManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        sharedPreferenceManager = SharedPreferenceManager.getInstance(this);

        if(sharedPreferenceManager.isLogin()){
            startActivity(new Intent(LoginActivity.this, MapsActivity.class) );
            finish();
        }

        et_email = (EditText)findViewById(R.id.input_email);
        et_password = (EditText)findViewById(R.id.input_password);

        button_login = (Button)findViewById(R.id.button_log_in);
        button_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = et_email.getText().toString();
                password = et_password.getText().toString();
                Log.d("Car-i","email : "+email);
                Log.d("Car-i","password : "+password);

//                StringRequest str_req = new StringRequest(Request.Method.POST, "http://192.168.1.136/cariadmin_db/web/Api/users/",
                StringRequest str_req = new StringRequest(Request.Method.POST, "http://115.85.70.168:1234/cariadmin_db/web/Api/users/",

                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Log.d("Car-i response gan", response);
                                try {
                                    JSONObject json_response = new JSONObject(response);
                                    boolean isTrue = json_response.getBoolean("status");

                                    if(isTrue){
                                        JSONObject data = json_response.getJSONObject("data");
                                        String email = data.getString("email");
                                        String nomor_induk = data.getString("nomer_induk");
                                        sharedPreferenceManager.login(email,nomor_induk);
                                        startActivity(new Intent(LoginActivity.this, MapsActivity.class) );
                                        finish();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Car-i error gan", ""+error.getMessage());
                    }
                }){
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String>Param = new HashMap<String, String>();
                        Param.put("email", email);
                        Param.put("password", password);
                        return Param;
                    }
                };
                Volley.newRequestQueue(LoginActivity.this).add(str_req);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
