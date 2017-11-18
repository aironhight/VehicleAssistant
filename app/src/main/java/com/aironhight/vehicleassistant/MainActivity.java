package com.aironhight.vehicleassistant;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SlidingDrawer;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private SlidingDrawer drawer;
    private ImageButton handle, addVehicleButton;
    private final ArrayList<Vehicle> vehicles = new ArrayList<Vehicle>();
    private ListView vehicleList;
    private Button logoutButton;
    private FirebaseAuth firebaseAuth;
    private TextView userTextView;
    private FirebaseUser user;
    private VehicleAdapter vehicleAdapter;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initialize();
    }

    @Override
    public void onClick(View view) {
        if(view == logoutButton) {
            firebaseAuth.signOut();
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }

        if(view == addVehicleButton) {
            /*Vehicle toSave = new Vehicle("Audi", "530", "M optic", 1997, 200000, "BMW128380901", user.getUid());
            Repair rep = new Repair("Spark plug", 70, 210000);
            toSave.addRepair(rep);
            toSave.setMileage(rep.getCurrentMileage());
            databaseReference.child("vehicles").push().setValue(toSave);*/
            startActivity(new Intent(getApplicationContext(), AddVehicleActivity.class));
        }
    }

    private void initialize() {
        firebaseAuth = FirebaseAuth.getInstance();

        if(firebaseAuth.getCurrentUser() == null) {
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }

        user = firebaseAuth.getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference();

        drawer = (SlidingDrawer) findViewById(R.id.slidingDrawer);
        handle = (ImageButton) findViewById(R.id.handle);
        vehicleList = (ListView) findViewById(R.id.vehicleListView);
        logoutButton = (Button) findViewById(R.id.logOutButton);
        addVehicleButton = (ImageButton) findViewById(R.id.addVehicleTestButton);
        userTextView = (TextView) findViewById(R.id.userTextView);

        //vehicles.add(new Vehicle("BMW", "530", "M optic", 1997, 200000, "BMW128380901", "Hristo"));
        //vehicles.add(new Vehicle("VW", "Bora","2.0", 2000, 235000, "VW128380901", "Hristo"));

        vehicleAdapter = new VehicleAdapter(this, vehicles);
        vehicleList.setAdapter(vehicleAdapter);
        addVehicleButton.setOnClickListener(this);
        logoutButton.setOnClickListener(this);

        handle.setBackgroundResource(R.drawable.ic_arrow_upward_black_24dp);

        userTextView.setText("You are logged in as: " + user.getEmail());

        //Query query = databaseReference.child("vehicles").orderByChild("make").equalTo("BMW");

        drawer.setOnDrawerOpenListener(new SlidingDrawer.OnDrawerOpenListener() {
            @Override
            public void onDrawerOpened() {
                handle.setBackgroundResource(R.drawable.ic_arrow_downward_black_24dp);
                vehicleList.setVisibility(View.GONE);
            }
        });

        drawer.setOnDrawerCloseListener(new SlidingDrawer.OnDrawerCloseListener() {
            @Override
            public void onDrawerClosed() {
                handle.setBackgroundResource(R.drawable.ic_arrow_upward_black_24dp);
                vehicleList.setVisibility(View.VISIBLE);
            }
        });

        databaseReference.child("vehicles").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                vehicles.clear();
                vehicleList.setAdapter(null);
                for(DataSnapshot child : children) {
                    Vehicle veh = child.getValue(Vehicle.class);
                    vehicles.add(veh);
                }
                vehicleList.setAdapter(vehicleAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
