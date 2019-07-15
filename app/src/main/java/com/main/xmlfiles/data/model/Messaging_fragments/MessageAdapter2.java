package com.main.xmlfiles.data.model.Messaging_fragments;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.main.xmlfiles.R;

import java.util.ArrayList;

public class MessageAdapter2 extends ArrayAdapter<Messages> {
    private static final String TAG = "MessageAdapter2";

    private Context mContext;
    private int mResource;
    private int lastPosition = -1;

    /**
     * Holds variables in a View
     */
    private static class ViewHolder {
        TextView receiver;
        TextView message;
    }

    /**
     * Default constructor for the PersonListAdapter
     * @param context
     * @param resource
     * @param objects
     */
    public MessageAdapter2(Context context, int resource, ArrayList<Messages> objects) {
        super(context, resource, objects);
        mContext = context;
        mResource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        //get the persons information
        String receiver = getItem(position).getReceiver();
        //Logg.d(TAG, receiver);
        String message = getItem(position).getMessage();

        //create the view result for showing the animation
        final View result;

        //ViewHolder object
        MessageAdapter2.ViewHolder holder;


        if(convertView == null){
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(mResource, parent, false);
            holder= new MessageAdapter2.ViewHolder();
            holder.receiver = (TextView) convertView.findViewById(R.id.name_my);
            holder.message = (TextView) convertView.findViewById(R.id.message_body_my);
            //holder.is_read =

            result = convertView;

            convertView.setTag(holder);
        }
        else{
            holder = (MessageAdapter2.ViewHolder) convertView.getTag();
            result = convertView;
        }


        Animation animation = AnimationUtils.loadAnimation(mContext,
                (position > lastPosition) ? R.anim.loading_down_anim : R.anim.loading_up_anim);
        result.startAnimation(animation);
        lastPosition = position;

        holder.receiver.setText(receiver);
        holder.message.setText(message);


        return convertView;
    }
}

