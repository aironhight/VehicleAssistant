package com.aironhight.vehicleassistant;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddVehicleActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText makeEditText, modelEditText, specificationEditText, yearEditText, mileageEditText, vinEditText;
    private Button addVehicleButton;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_vehicle);

        initialize();

    }

    @Override
    public void onClick(View view) {
        if(view == addVehicleButton) {
            if(makeEditText.getText().toString().length() >= 2 &&
                    modelEditText .getText().toString().length() >= 2 &&
                    yearEditText.getText().toString().length() >= 4 &&
                    mileageEditText.getText().toString().length() >= 2 &&
                    vinEditText.getText().toString().length() >= 11 //that's the shortest VIN number according to the internet :)
             ) {
                addVehicle();
            } else {
                Toast.makeText(this, "Some of the fields were not filled correctly.", Toast.LENGTH_SHORT);
            }


        }
    }

    private void addVehicle() {
        Vehicle toSave = new Vehicle(
                makeEditText.getText().toString().trim(),
                modelEditText.getText().toString().trim(),
                specificationEditText.getText().toString().trim(),
                Integer.getInteger(yearEditText.getText().toString().trim()),
                Long.getLong(mileageEditText.getText().toString().trim()),
                vinEditText.getText().toString().trim()
                , user.getUid());
        databaseReference.child("vehicles").push().setValue(toSave);
    }

    private void initialize(){
        makeEditText = (EditText) findViewById(R.id.vehicleMakeEditText);
        modelEditText = (EditText) findViewById(R.id.vehicleModelEditText);
        specificationEditText = (EditText) findViewById(R.id.vehicleSpeciEditText);
        yearEditText = (EditText) findViewById(R.id.vehicleYearEditText);
        mileageEditText = (EditText) findViewById(R.id.vehicleMileageEditText);
        vinEditText = (EditText) findViewById(R.id.vehicleVINEditText);

        addVehicleButton = (Button) findViewById(R.id.addVehicleButton);

        databaseReference = FirebaseDatabase.getInstance().getReference();

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        if(firebaseAuth.getCurrentUser() == null) {
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }
        user = firebaseAuth.getCurrentUser();
    }
}
