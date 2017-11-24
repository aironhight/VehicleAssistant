package com.aironhight.vehicleassistant;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.Serializable;
import java.util.ArrayList;

public class RepairActivity extends AppCompatActivity implements Serializable {

    private  ArrayList<Repair> repairs;
    private DatabaseReference databaseReference;
    private ListView repairList;
    private RepairAdapter repairAdapter;
    private Vehicle currentVehicle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repair);

        //repairs = (ArrayList<Repair>)getIntent().getSerializableExtra("repairs");
        currentVehicle = (Vehicle)getIntent().getSerializableExtra("vehicle");
        Log.i("car", currentVehicle.toString());
        repairs = currentVehicle.getRepairs();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        repairList = (ListView)findViewById(R.id.repairListView);
        if(repairs != null && repairs.size() > 0) {
            repairAdapter = new RepairAdapter(this, repairs);
            repairList.setAdapter(repairAdapter);
        } else {
            Toast.makeText(this, "There are no repairs added to this vehicle.", Toast.LENGTH_LONG).show();
        }
    }
}
