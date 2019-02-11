package com.webcafeappdev.immapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;

/**
 * Created by Urgent Nkala
 */

public class IncidentListAdapter extends BaseAdapter {
    private Context context;
    private int layout;
    private ArrayList<Incident> incidentList;

    public IncidentListAdapter(Context context, int layout, ArrayList<Incident> incidentList) {
        this.context = context;
        this.layout = layout;
        this.incidentList = incidentList;
    }

    @Override
    public int getCount() {
        return incidentList.size();
    }

    @Override
    public Object getItem(int position) {

        return incidentList.get (position);
    }

    @Override
    public long getItemId(int position) {

        return position;
    }
    private class ViewHolder{
        ImageView imageView;
        TextView txtName,txtDescription,txtIncidentClass,txtDate,txtLocations;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        View row = view;
        ViewHolder holder = new ViewHolder();
        if(row == null) {
            LayoutInflater inflater = (LayoutInflater)
                    context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(layout, null);

            holder.txtName = (TextView) row.findViewById(R.id.txtName);
            holder.txtDate = (TextView) row.findViewById(R.id.txtDate);
            holder.txtDescription = (TextView) row.findViewById(R.id.txtDescription);
            holder.txtIncidentClass = (TextView) row.findViewById(R.id.txtIncidentClass);
            holder.txtLocations = (TextView) row.findViewById(R.id.txtLocations);
            holder.imageView = (ImageView) row.findViewById(R.id.imageView);
            row.setTag(holder);

        }
        else{
            holder=(ViewHolder) row.getTag();
        }

        Incident image =incidentList.get(position);
        holder.txtName.setText(image.getName());
        holder.txtDate.setText(image.getDate());
        holder.txtDescription.setText(image.getDescription());
        holder.txtIncidentClass.setText(image.getIncidentClass());
        holder.txtLocations.setText(image.getLocations());
        byte[] imageView = image.getImageView();
        Bitmap bitmap = BitmapFactory.decodeByteArray(imageView, 0,imageView.length
        );

        holder.imageView.setImageBitmap(bitmap);

        return row;
    }

}

