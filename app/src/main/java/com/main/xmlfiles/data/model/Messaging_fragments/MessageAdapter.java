package com.main.xmlfiles.data.model.Messaging_fragments;

import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.main.xmlfiles.R;


import org.w3c.dom.Text;

import java.util.ArrayList;

public class MessageAdapter extends ArrayAdapter<Messages>  {
    private static final String TAG = "MessageAdapter";

    private Context mContext;
    private int mResource;
    private int lastPosition = -1;

    /**
     * Holds variables in a View
     */
    private static class ViewHolder {
        TextView sender;
        TextView message;
        View avatar;
        //boolean is_read;
    }

    /**
     * Default constructor for the PersonListAdapter
     * @param context
     * @param resource
     * @param objects
     */
    public MessageAdapter(Context context, int resource, ArrayList<Messages> objects) {
        super(context, resource, objects);
        mContext = context;
        mResource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        //get the persons information
        String sender = getItem(position).getSender();
        String receiver = getItem(position).getReceiver();
        String message = getItem(position).getMessage();
        Boolean is_read = getItem(position).isIs_read();

        //create the view result for showing the animation
        final View result;

        //ViewHolder object
        MessageAdapter.ViewHolder holder;


        if(convertView == null){
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(mResource, parent, false);
            holder= new MessageAdapter.ViewHolder();
            holder.sender = (TextView) convertView.findViewById(R.id.name);
            holder.message = (TextView) convertView.findViewById(R.id.message_body);
            holder.avatar = convertView.findViewById(R.id.avatar);
            if(is_read){
                if(Build.VERSION.SDK_INT >= 16){
                    holder.avatar.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.circle_read));
                }
            }

            result = convertView;

            convertView.setTag(holder);
        }
        else{
            holder = (MessageAdapter.ViewHolder) convertView.getTag();
            result = convertView;
        }


        Animation animation = AnimationUtils.loadAnimation(mContext,
                (position > lastPosition) ? R.anim.loading_down_anim : R.anim.loading_up_anim);
        result.startAnimation(animation);
        lastPosition = position;

        holder.sender.setText(sender);
        holder.message.setText(message);


        return convertView;
    }
}
