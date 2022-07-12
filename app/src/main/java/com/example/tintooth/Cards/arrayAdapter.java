package com.example.tintooth.Cards;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.tintooth.R;

import java.util.List;

public class arrayAdapter extends ArrayAdapter<cards> {
    Context context;
    public arrayAdapter(Context context, int resourceId, List<cards> items){
        super(context, resourceId, items);
    }
    public View getView(int position, View convertView, ViewGroup parent){
        cards card_item = getItem(position);
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item, parent, false);
        }
        TextView name = convertView.findViewById(R.id.name);
        name.setText(card_item.getName());
        TextView gender = convertView.findViewById(R.id.gender_display);
        gender.setText(card_item.getGender());
        TextView description = convertView.findViewById(R.id.description_display);
        description.setText(card_item.getDescription());
        TextView phone = convertView.findViewById(R.id.phone_display);
        phone.setText(card_item.getPhone());
        return convertView;
    }
}
