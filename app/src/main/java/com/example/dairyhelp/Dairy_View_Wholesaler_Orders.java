package com.example.dairyhelp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Dairy_View_Wholesaler_Orders extends AppCompatActivity {


    ListView lst ;
    DatabaseReference DairyRef ;
    private ArrayList<OrderClass> Orders = new ArrayList<>() ;
    AppListAdapter adap;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dairy__view__wholesaler__orders);


        lst = (ListView)findViewById(R.id.ListOrders);
        SharedPreferences sharedPrefs = getSharedPreferences("userinfo" , Context.MODE_PRIVATE);
        String Uid = sharedPrefs.getString("Uid" , "" );
        String Id = sharedPrefs.getString("Id" , "" );
        adap = new AppListAdapter(getApplicationContext() , Orders);
        lst.setAdapter(adap);

        DairyRef = FirebaseDatabase.getInstance().getReference("DairyWholesalerOrders/"+ Id + "/Orders/") ;
        DairyRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                Orders.clear();
                adap.notifyDataSetChanged();

                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {

                    String Order = dataSnapshot1.child("Order").getValue().toString();
                    String DName = dataSnapshot1.child("Wholesaler").getValue().toString();
                    String OId = dataSnapshot1.child("Order Id").getValue().toString();

                    Orders.add(new OrderClass(Order , OId, "" , "Wholesaler Name : \n" + DName));
                    adap.notifyDataSetChanged();

                }

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });







    }
}
