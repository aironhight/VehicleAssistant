package com.aironhight.vehicleassistant;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity implements View.OnClickListener {

    private DatabaseReference databaseReference;
    private Vehicle currentVehicle;
    private EditText searchText;
    private Button searchButton;
    private TextView carInfoTextView;
    private ListView repairList;
    private RepairAdapter repairAdapter;
    private ArrayList<Repair> repairs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        setTitle("Search");

        databaseReference = FirebaseDatabase.getInstance().getReference();
        searchText = (EditText) findViewById(R.id.searchVINEditText);
        searchButton = (Button) findViewById(R.id.vehicleSearchButton);
        carInfoTextView = (TextView) findViewById(R.id.vehicleInfoSearchTextView);
        repairList = (ListView) findViewById(R.id.searchRepairsList);
        currentVehicle = null;
        repairs = new ArrayList<Repair>();
        searchButton.setOnClickListener(this);
        repairAdapter = new RepairAdapter(this, repairs);
    }

    @Override
    public void onClick(View view) {
        if (view == searchButton) {
            query();

        }
    }

    private void query(){
        Query query = databaseReference.child("vehicles").orderByChild("vin").equalTo(searchText.getText().toString().trim().toUpperCase());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                    for (DataSnapshot child : children) {
                        currentVehicle = child.getValue(Vehicle.class);
                        carInfoTextView.setText(currentVehicle.toString()  + " (" + currentVehicle.getMileage() + "km) \n" + "Repairs done to the vehicle:" + "\n");
                    }
                } else {
                    Toast.makeText(SearchActivity.this, "Unknown VIN number.", Toast.LENGTH_SHORT).show();
                }
                queryRepairs();
            }
            public void onCancelled (DatabaseError databaseError){

            }
        });
    }

    private void queryRepairs() {
            Query query = databaseReference.child("repairs").orderByChild("vehicleUID").equalTo(currentVehicle.getPushID());
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                        repairs.clear();
                        repairList.setAdapter(null);
                        for (DataSnapshot child : children) {
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
        }
    }


