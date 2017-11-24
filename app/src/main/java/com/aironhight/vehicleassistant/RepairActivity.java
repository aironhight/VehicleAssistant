package com.aironhight.vehicleassistant;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.ArrayList;

public class RepairActivity extends AppCompatActivity implements Serializable, View.OnClickListener {

    private ArrayList<Repair> repairs;
    private DatabaseReference databaseReference;
    private ListView repairList;
    private RepairAdapter repairAdapter;
    private Vehicle currentVehicle;
    private FloatingActionButton addRepairButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repair);

        //repairs = (ArrayList<Repair>)getIntent().getSerializableExtra("repairs");
        currentVehicle = (Vehicle)getIntent().getSerializableExtra("vehicle");
        repairs = currentVehicle.getRepairsWithoutDummy();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        repairList = (ListView)findViewById(R.id.repairListView);
        addRepairButton = (FloatingActionButton) findViewById(R.id.addRepairFloatingButton);
        addRepairButton.setOnClickListener(this);
        repairAdapter = new RepairAdapter(this, repairs);


        Query query = databaseReference.child("repairs").orderByChild("vehicleUID").equalTo(currentVehicle.getPushID());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                    repairs.clear();
                    repairList.setAdapter(null);
                    for(DataSnapshot child : children) {
                        Repair rep = child.getValue(Repair.class);
                        repairs.add(rep);
                    }
                    repairList.setAdapter(repairAdapter);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        if(repairs != null && repairs.size() > 0) {

            repairList.setAdapter(repairAdapter);
        } else {
            Toast.makeText(this, "There are no repairs added to this vehicle.", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onClick(View view) {
        if(view == addRepairButton) {
            Intent addRepair = new Intent(getApplicationContext(), AddRepairActivity.class);
            addRepair.putExtra("vehicle", currentVehicle);
            startActivity(addRepair);
        }
    }
}
