package com.aironhight.vehicleassistant.Activity.main;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SlidingDrawer;
import android.widget.TextView;
import android.widget.Toast;

import com.aironhight.vehicleassistant.Activity.AddVehicleActivity;
import com.aironhight.vehicleassistant.Activity.LoginActivity;
import com.aironhight.vehicleassistant.Activity.SearchActivity;
import com.aironhight.vehicleassistant.Activity.repair.RepairActivity;
import com.aironhight.vehicleassistant.model.Vehicle;
import com.aironhight.vehicleassistant.R;
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
    private TextView userTextView, drawerSearchButton;
    private FirebaseUser user;
    private VehicleAdapter vehicleAdapter;
    private DatabaseReference databaseReference;
    private FloatingActionButton floatingActionButton;
    private Vehicle longclickedVehicle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //test
        firebaseAuth = FirebaseAuth.getInstance();
        user =  FirebaseAuth.getInstance().getCurrentUser();

        if(firebaseAuth.getInstance().getCurrentUser() == null) {
            finish();
            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
        } else {
            initialize();

            vehicleList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Vehicle selectedVehicle = (Vehicle) parent.getItemAtPosition(position);
                    Intent repairActivity = new Intent(getApplicationContext(), RepairActivity.class);
                    repairActivity.putExtra("vehicle", selectedVehicle);
                    startActivity(repairActivity);
                }
            });

        }

    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.longclick_item_mainactivity, menu);

        AdapterView.AdapterContextMenuInfo acmi = (AdapterView.AdapterContextMenuInfo) menuInfo;
        longclickedVehicle = (Vehicle) vehicleList.getItemAtPosition(acmi.position);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.deleteOptionMainActivity:
                new AlertDialog.Builder(this)
                        .setTitle("Really Delete?")
                        .setMessage("Are you sure you want to permanently remove the vehicle from your list?")
                        .setNegativeButton(android.R.string.no, null)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface arg0, int arg1) {
                                DatabaseReference dbNodeToRemove = FirebaseDatabase.getInstance().getReference().getRoot()
                                        .child("vehicles")
                                        .child(longclickedVehicle.getPushID());

                                dbNodeToRemove.setValue(null); //
                                updateVehicleList();
                            }
                        }).create().show();
                return true;

            case R.id.detailsOptionMainActivity:
                //TO BE IMPLEMENTED
                Toast.makeText(MainActivity.this, "NOT YET IMPLEMENTED", Toast.LENGTH_SHORT).show();
                return true;

            default:
                return super.onContextItemSelected(item);
        }

    }

    @Override
    public void onBackPressed() {
        if(floatingActionButton.getVisibility() == View.VISIBLE) {
            new AlertDialog.Builder(this)
                    .setTitle("Really Exit?")
                    .setMessage("Are you sure you want to exit?")
                    .setNegativeButton(android.R.string.no, null)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface arg0, int arg1) {
                            MainActivity.super.onBackPressed();
                        }
                    }).create().show();
        } else {
            drawer.animateClose();
        }
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

        if(view == drawerSearchButton) {
            startActivity(new Intent(getApplicationContext(), SearchActivity.class));
        }
    }

    private void initialize() {

        databaseReference = FirebaseDatabase.getInstance().getReference();

        drawer = (SlidingDrawer) findViewById(R.id.slidingDrawer);
        handle = (ImageButton) findViewById(R.id.handle);
        vehicleList = (ListView) findViewById(R.id.vehicleListView);
        logoutButton = (Button) findViewById(R.id.logOutButton);
        userTextView = (TextView) findViewById(R.id.userTextView);
        floatingActionButton = (FloatingActionButton) findViewById(R.id.addVehicleFloatingButton);
        drawerSearchButton = (TextView) findViewById(R.id.drawerSearchButton);

        vehicleAdapter = new VehicleAdapter(this, vehicles);
        vehicleList.setAdapter(null);
        vehicleList.setAdapter(vehicleAdapter);
        vehicleList.setLongClickable(true);
        logoutButton.setOnClickListener(this);
        floatingActionButton.setOnClickListener(this);
        drawerSearchButton.setOnClickListener(this);
        handle.setBackgroundResource(R.drawable.ic_drag_handle_black_140x40dp);

        registerForContextMenu(vehicleList);
        userTextView.setText("You are logged in as: " + user.getEmail());

        drawer.setOnDrawerOpenListener(new SlidingDrawer.OnDrawerOpenListener() {
            @Override
            public void onDrawerOpened() {
                drawerOpen();
            }
        });

        drawer.setOnDrawerCloseListener(new SlidingDrawer.OnDrawerCloseListener() {
            @Override
            public void onDrawerClosed() {
                drawerClose();
            }
        });

        updateVehicleList();

    }

    private void updateVehicleList() {
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
                        veh.setPushID(child.getKey());
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

    private void drawerClose() {
        vehicleList.setVisibility(View.VISIBLE);
        floatingActionButton.setVisibility(View.VISIBLE);
    }

    private void drawerOpen() {
        vehicleList.setVisibility(View.GONE);
        floatingActionButton.setVisibility(View.GONE);
    }
}
