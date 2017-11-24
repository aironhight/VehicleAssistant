package com.aironhight.vehicleassistant;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by airon on 15-Nov-17.
 */

public class VehicleAdapter extends ArrayAdapter<Vehicle> {
    public VehicleAdapter(@NonNull Context context, ArrayList<Vehicle> vehicles) {
        super(context, 0, vehicles);
    }

    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItemView = convertView;

        if(listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.vehicle_list_layout, parent, false);
        }

        Vehicle currentVehicle = getItem(position);

        TextView carInfo = (TextView)listItemView.findViewById(R.id.vehicleInfoTextView);
        carInfo.setText(currentVehicle.toString());

        return listItemView;
    }
}
