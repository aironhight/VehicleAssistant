package com.aironhight.vehicleassistant;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AddRepairActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText repair, cost, currentMileage;
    private Button saveButton;
    private DatabaseReference databaseReference;
    private Vehicle currentVehicle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_repair);

        databaseReference = FirebaseDatabase.getInstance().getReference();
        repair = (EditText) findViewById(R.id.repairEditText);
        cost = (EditText) findViewById(R.id.costEditText);
        currentMileage = (EditText) findViewById(R.id.currentMileageEditText);
        saveButton = (Button) findViewById(R.id.saveRepairButton);
        saveButton.setOnClickListener(this);

        currentVehicle = (Vehicle)getIntent().getSerializableExtra("vehicle");
    }

    @Override
    public void onClick(View view) {
        if(view == saveButton) {
            if(repair.getText().toString() == null
                    || cost.getText().toString() == null
                    || currentMileage.getText().toString() == null
                    || repair.getText().toString().length() < 2
                    || Integer.valueOf(cost.getText().toString()) < 0
                    || Long.valueOf(currentMileage.getText().toString()) < currentVehicle.getMileage()) {
                Toast.makeText(this, "Some of the fields were not filled correctly!", Toast.LENGTH_LONG).show();
                return;
            }


            final Repair rep = new Repair(repair.getText().toString().trim(), Integer.valueOf(cost.getText().toString()), Long.valueOf(currentMileage.getText().toString()), currentVehicle.getPushID());
            currentVehicle.addRepair(rep);
            databaseReference.child("repairs").push().setValue(rep);
            Intent intent = new Intent(getApplicationContext(), RepairActivity.class);
            intent.putExtra("vehicle", currentVehicle);
            startActivity(intent);
            finish();

            /*Query query = databaseReference.child("vehicles").orderByChild("pu").equalTo(currentVehicle.getVIN());

            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                        for(DataSnapshot child : children) {
                            Vehicle veh = child.getValue(Vehicle.class);

                            Iterable<DataSnapshot> children1 = child.getChildren();
                            for(DataSnapshot child1 : children1) {
                                ArrayList<Repair> repz = child1.child("repairs").getValue(ArrayList.class);
                                repz.add(rep);

                            }
                        }
                    }
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });*/

        }
    }
}
