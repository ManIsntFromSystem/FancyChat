package com.waytosuccess.fancychat;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

public class FancyMessageAdapter extends ArrayAdapter<FancyMessage> {
    private FancyMessage fancyMessage;
    public FancyMessageAdapter(Context context, int resource,
                               List<FancyMessage> messages) {
        super(context, resource, messages);
    }

    @Override
    public View getView(int position,View convertView,ViewGroup parent) {

        if (convertView == null){
            convertView = ((Activity) getContext()).getLayoutInflater()
                    .inflate(R.layout.message_item, parent, false);
        }

        ImageView photoImageView = convertView.findViewById(R.id.photoImgView);
        TextView textMsg = convertView.findViewById(R.id.textMsg);
        TextView textName = convertView.findViewById(R.id.textName);

        fancyMessage = getItem(position);
        Log.d("AdapterMy", "Adapter: " + fancyMessage.toString());


        boolean isText = fancyMessage.getImageURL() == null;

        if (isText){
            textMsg.setVisibility(View.VISIBLE);
            photoImageView.setVisibility(View.GONE);
            textMsg.setText(fancyMessage.getText());
        } else {
            textMsg.setVisibility(View.GONE);
            photoImageView.setVisibility(View.VISIBLE);
            Glide.with(photoImageView.getContext())
                    .load(fancyMessage.getImageURL())
                    .into(photoImageView);
        }

        textName.setText(fancyMessage.getName());

        return convertView;
    }
}
