package id.project.lazarus.cariproject;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by lazar on 7/24/2017.
 */

public class ListAdapter extends BaseAdapter {
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
        if(convertView == null)
        {
//            viewItem = new ViewItem();
//            LayoutInflater layoutInfiater = (LayoutInflater)this.context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            //LayoutInflater layoutInfiater = LayoutInflater.from(context);
            convertView = LayoutInflater.from(context).inflate(R.layout.list_adapter_view, parent, false);
//            convertView = layoutInfiater.inflate(R.layout.activity_car_list, null);
        }

        TextView txtTitle = (TextView)convertView.findViewById(R.id.adapter_text_title);
        TextView txtDescription = (TextView)convertView.findViewById(R.id.adapter_text_description);
        txtTitle.setText(valueList.get(position).getName_car());
        txtDescription.setText(valueList.get(position).getId_car());

        return convertView;
    }
}
