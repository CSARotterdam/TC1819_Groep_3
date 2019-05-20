package com.example.borrow.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.borrow.R;
import com.example.borrow.Row_list;

import java.util.List;


public class Adpter extends ArrayAdapter<Row_list> {
    public Adpter(@NonNull Context context, List<Row_list> objects) {
        super(context, 0, objects);
    }

    @Override
    public View getView(int position, @Nullable View convertView, @Nullable ViewGroup parent) {
        if (convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.row_layout, parent,false);

        }
        TextView item_name = convertView.findViewById(R.id.item_name);
        TextView description = convertView.findViewById(R.id.description);
        Row_list p = getItem(position);
        item_name.setText(p.getItem_name());
        description.setText(p.getDescription());
        return convertView;
    }
}
