package com.aironhight.vehicleassistant;

import android.content.Context;
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
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private SlidingDrawer drawer;
    private ImageButton handle;
    private ListView vehicleList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawer = (SlidingDrawer) findViewById(R.id.slidingDrawer);
        handle = (ImageButton) findViewById(R.id.handle);
        vehicleList = (ListView) findViewById(R.id.vehicleListView);

        ArrayList<Vehicle> vehicles = new ArrayList<Vehicle>();
        vehicles.add(new Vehicle("BMW", "530", "M optic", 1997, 200000, "BMW128380901", "Hristo"));
        vehicles.add(new Vehicle("VW", "Bora","2.0", 2000, 235000, "VW128380901", "Hristo"));

        VehicleAdapter vehicleAdapter = new VehicleAdapter(this, vehicles);
        vehicleList.setAdapter(vehicleAdapter);


        handle.setBackgroundResource(R.drawable.ic_arrow_upward_black_24dp);

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
    }

    @Override
    public void onClick(View view) {

    }

}
