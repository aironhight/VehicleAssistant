package com.aironhight.vehicleassistant.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.aironhight.vehicleassistant.Activity.repair.RepairActivity;
import com.aironhight.vehicleassistant.model.Repair;
import com.aironhight.vehicleassistant.model.Vehicle;
import com.aironhight.vehicleassistant.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class AddRepairActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText repair, cost, currentMileage;
    private Button saveButton;
    private DatabaseReference databaseReference;
    private Vehicle currentVehicle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_repair);
        setTitle("Add Repair");

        databaseReference = FirebaseDatabase.getInstance().getReference();
        repair = (EditText) findViewById(R.id.repairEditText);
        cost = (EditText) findViewById(R.id.costEditText);
        currentMileage = (EditText) findViewById(R.id.currentMileageEditText);
        saveButton = (Button) findViewById(R.id.saveRepairButton);
        transferToCurrentVehicle((Vehicle)getIntent().getSerializableExtra("vehicle"));
        saveButton.setOnClickListener(this);


        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.ic_add_black_24dp);

    }

    @Override
    public void onClick(View view) {
        if(view == saveButton) {
            if(repair.getText().toString() == null
                    || cost.getText().toString() == null
                    || currentMileage.getText().toString() == null
                    || repair.getText().toString().length() < 2
                    || Integer.valueOf(cost.getText().toString()) < 0) {
                Toast.makeText(this, "Some of the fields were not filled correctly!", Toast.LENGTH_LONG).show();
                return;
            }


            Repair rep = new Repair(repair.getText().toString().trim(), Integer.valueOf(cost.getText().toString()), Long.valueOf(currentMileage.getText().toString()), currentVehicle.getPushID());
            currentVehicle.addRepair(rep);
            databaseReference.child("repairs").push().setValue(rep);
            databaseReference.child("vehicles").child(currentVehicle.getPushID()).child("mileage").setValue(rep.getCurrentMileage());
            Intent intent = new Intent(getApplicationContext(), RepairActivity.class);
            intent.putExtra("vehicle", currentVehicle);
            startActivity(intent);
            finish();
        }
    }

    private void transferToCurrentVehicle(Vehicle v){
        currentVehicle = new Vehicle(v.getMake(),
                v.getModel(),
                v.getSpecification(),
                v.getYear(),
                v.getMileage(),
                v.getVIN(),
                v.getOwner());
        currentVehicle.setPushID(v.getPushID());
    }
}
