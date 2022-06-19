package com.example.collagemanagementsystem.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.collagemanagementsystem.R;

import java.util.List;


public class SpinnerListAdapter extends BaseAdapter {
    private Context context;
    private List<String> values;

    public SpinnerListAdapter(Context context, List<String> values) {
        this.context = context;
        this.values=values;
    }

    @Override
    public int getCount() {
            return values.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        if(convertView==null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.spinner_item_layout, parent, false);
            TextView textView=convertView.findViewById(R.id.spinnerHeaderTExt);
            textView.setText(""+values.get(position));
        }
        return convertView;
    }
}
