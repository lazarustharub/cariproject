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
import java.util.List;
import java.util.Map;

public class CarListActivity extends AppCompatActivity {

    String id_car;
    ListView list_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_list);
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
                        Log.d("Response SIAP", "RESPONSE OK");
                        try {
                            JSONObject jsonresponse = new JSONObject(response);
                            if(Util.RequestChecker(jsonresponse)){
                                JSONObject jsoncar = jsonresponse.getJSONObject(Api.RESPONSE_DATA);
                                String text_namecar = jsoncar.getString(Api.RESPONSE_NAMECAR);
                                String text_licensecar = jsoncar.getString(Api.RESPONSE_LICENSECAR);



                            }
                            else{
                                Toast.makeText(CarListActivity.this,"Request Failed",Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
    }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("ERROR RESPONSE","ERROR BRO");
                Toast.makeText(CarListActivity.this,"WebService Connection Failed "+error,Toast.LENGTH_LONG).show();
            }
        });
        Volley.newRequestQueue(this).add(request);
    }


    private void initView() {
        list_view = (ListView) findViewById(R.id.list_car);

    }

    private class ListAdapter extends BaseAdapter{
        Context context;
        List<Car> valueList;
        public ListAdapter(List<Car> listValue, Context context)
        {
            this.context = context;
            this.valueList = listValue;
        }

        @Override
        public int getCount() {
            return this.valueList.size();
        }

        @Override
        public Object getItem(int position) {
            return this.valueList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewItem viewItem = null;
            if(convertView == null)
            {
                viewItem = new ViewItem();
                LayoutInflater layoutInfiater = (LayoutInflater)this.context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
                //LayoutInflater layoutInfiater = LayoutInflater.from(context);
                convertView = layoutInfiater.inflate(R.layout.activity_car_list, null);

                viewItem.txtTitle = (TextView)convertView.findViewById(R.id.adapter_text_title);
                viewItem.txtDescription = (TextView)convertView.findViewById(R.id.adapter_text_description);
                convertView.setTag(viewItem);
            }else
            {
                viewItem = (ViewItem) convertView.getTag();
            }
            viewItem.txtTitle.setText(valueList.get(position).car_name);
            viewItem.txtDescription.setText(valueList.get(position).car_description);

            return convertView;
        }
    }

    class ViewItem
    {
        TextView txtTitle;
        TextView txtDescription;
    }
}
