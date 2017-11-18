package com.aironhight.vehicleassistant;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class RepairActivity extends AppCompatActivity {

    private final ArrayList<Repair> repairs = new ArrayList<Repair>();
    private DatabaseReference databaseReference;
    private ListView repairList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repair);

       // Vehicle current =

        databaseReference = FirebaseDatabase.getInstance().getReference();
        repairList = (ListView)findViewById(R.id.repairListView);

    }
}
