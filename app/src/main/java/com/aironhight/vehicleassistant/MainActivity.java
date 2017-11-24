package com.aironhight.vehicleassistant;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
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
    private ImageButton handle;
    private final ArrayList<Vehicle> vehicles = new ArrayList<Vehicle>();
    private ListView vehicleList;
    private Button logoutButton;
    private FirebaseAuth firebaseAuth;
    private TextView userTextView;
    private FirebaseUser user;
    private VehicleAdapter vehicleAdapter;
    private DatabaseReference databaseReference;
    private FloatingActionButton floatingActionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        initialize();

        vehicleList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Vehicle selectedVehicle = (Vehicle) parent.getItemAtPosition(position);
                Intent repairActivity = new Intent(getApplicationContext(), RepairActivity.class);
                if(selectedVehicle.getRepairs() != null) {
                    repairActivity.putExtra("repairs", selectedVehicle.getRepairs());
                }
                startActivity(repairActivity);
            }
        });
    }

    @Override
    public void onClick(View view) {
        if(view == logoutButton) {
            firebaseAuth.signOut();
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }

        if(view == floatingActionButton) {
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
        userTextView = (TextView) findViewById(R.id.userTextView);
        floatingActionButton = (FloatingActionButton) findViewById(R.id.addVehicleFloatingButton);


        vehicleAdapter = new VehicleAdapter(this, vehicles);
        vehicleList.setAdapter(vehicleAdapter);
        logoutButton.setOnClickListener(this);
        floatingActionButton.setOnClickListener(this);
        handle.setBackgroundResource(R.drawable.ic_arrow_upward_black_24dp);

        userTextView.setText("You are logged in as: " + user.getEmail());

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

        Query query = databaseReference.child("vehicles").orderByChild("owner").equalTo(user.getUid());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                    vehicles.clear();
                    vehicleList.setAdapter(null);
                    for(DataSnapshot child : children) {
                        Vehicle veh = child.getValue(Vehicle.class);
                        vehicles.add(veh);
                    }
                    vehicleList.setAdapter(vehicleAdapter);
                    }
                }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
}
