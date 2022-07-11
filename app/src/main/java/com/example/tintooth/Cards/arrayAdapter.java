package com.example.tintooth.Cards;

import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.example.tintooth.R;

import org.w3c.dom.Text;

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
        TextView name = (TextView) convertView.findViewById(R.id.name);
        name.setText(card_item.getName());
        TextView gender = (TextView) convertView.findViewById(R.id.gender_display);
        gender.setText(card_item.getGender());
        TextView description = (TextView) convertView.findViewById(R.id.description_display);
        description.setText(card_item.getDescription());
        TextView phone = (TextView) convertView.findViewById(R.id.phone_display);
        phone.setText(card_item.getPhone());
        return convertView;
    }
}
