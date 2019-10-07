package com.aironhight.vehicleassistant.Activity.repair;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.aironhight.vehicleassistant.model.Repair;
import com.aironhight.vehicleassistant.R;

import java.util.ArrayList;

/**
 * Created by airon on 18-Nov-17.
 */

public class RepairAdapter extends ArrayAdapter<Repair> {
    public RepairAdapter(@NonNull Context context, ArrayList<Repair> repairs) {
        super(context, 0, repairs);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItemView = convertView;

        if(listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.repair_list_layout, parent, false);
        }

        Repair currentRepair = getItem(position);

        TextView repairInfo = (TextView)listItemView.findViewById(R.id.repairInfoTextView);
        repairInfo.setText(currentRepair.toString());

        return listItemView;
    }
}
