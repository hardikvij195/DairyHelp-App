package com.example.dairyhelp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class RetailersSide extends AppCompatActivity {


    Button b1 , b2 , b3 , LogOut;
    DatabaseReference TypeRef ,  WholeRef , RetRef;
    private FirebaseAuth mAuth;

    private ArrayList<NameIdClass> WholesalerNames = new ArrayList<>() ;
    private ArrayList<String> WholesalerNamesString  = new ArrayList<>() ;

    ArrayAdapter adapterSpinnerWholesalerNames ;
    String WUid , WId , WName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retailers_side);

        Utils.getDatabase();
        mAuth = FirebaseAuth.getInstance();

        b1 = (Button)findViewById(R.id.NewWholesalerOrderBtn);
        b2 = (Button)findViewById(R.id.RetailerViewWholesalerOrders);
        b3 = (Button)findViewById(R.id.RetailerViewShopOrders);
        LogOut = (Button)findViewById(R.id.LogOut);

        WUid = mAuth.getUid();
        adapterSpinnerWholesalerNames = new ArrayAdapter<String>(this , android.R.layout.simple_spinner_dropdown_item , WholesalerNamesString);

        RetRef = FirebaseDatabase.getInstance().getReference("Users/Retailer/"+ WUid ) ;
        RetRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                WId = dataSnapshot.child("Id").getValue().toString();
                WName = dataSnapshot.child("Name").getValue().toString();
                SharedPreferences sharedPrefs = getSharedPreferences("userinfo" , Context.MODE_PRIVATE);
                SharedPreferences.Editor edit = sharedPrefs.edit();
                edit.putString("Uid" , WUid );
                edit.putString("Id" , WId );
                edit.putString("Name" , WName );
                edit.apply();



            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        WholeRef = FirebaseDatabase.getInstance().getReference("Wholesaler/" ) ;
        WholeRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                WholesalerNamesString.clear();
                WholesalerNames.clear();
                adapterSpinnerWholesalerNames.notifyDataSetChanged();

                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){

                    String Id = dataSnapshot1.getKey();
                    String Name = dataSnapshot1.child("Name").getValue().toString();

                    WholesalerNames.add(new NameIdClass( Name , Id ));
                    WholesalerNamesString.add(Name);
                    adapterSpinnerWholesalerNames.notifyDataSetChanged();

                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        LogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(RetailersSide.this , "Sign Out" , Toast.LENGTH_SHORT ).show();
                mAuth.signOut();
                Intent mainIntent = new Intent(RetailersSide.this, MainActivity.class);
                startActivity(mainIntent);
                finish();
                overridePendingTransition(android.R.anim.fade_in , android.R.anim.fade_out);

            }
        });


        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                //DIALOG BOX
                final AlertDialog.Builder mBuilder = new AlertDialog.Builder(RetailersSide.this)
                        .setCancelable(false);
                View mView = getLayoutInflater().inflate(R.layout.dialog_box_add_order, null);

                final EditText Name = (EditText) mView.findViewById(R.id.ItemNameEditTxt);
                final Button canc1 = (Button) mView.findViewById(R.id.button19);
                final Button ok1 = (Button) mView.findViewById(R.id.button20);
                final Spinner Spin = (Spinner) mView.findViewById(R.id.spinnerNames);

                Spin.setAdapter(adapterSpinnerWholesalerNames);

                mBuilder.setView(mView);
                final AlertDialog dialog = mBuilder.create();
                dialog.show();

                ok1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        int pos = Spin.getSelectedItemPosition();

                        if( !WholesalerNames.isEmpty() && !WholesalerNamesString.isEmpty() &&  !Name.getText().toString().isEmpty()  && !WUid.isEmpty() ){

                            Date date = new Date();  // to get the date
                            SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss"); // getting date in this format
                            final String formattedDate = df.format(date.getTime());

                            String DId = WholesalerNames.get(pos).getId();
                            String DName = WholesalerNames.get(pos).getName();

                            TypeRef = FirebaseDatabase.getInstance().getReference("WholesalerRetailerOrders/"+ DId+ "/Orders/" + formattedDate + "/" ) ;
                            HashMap<String,String> dataMap = new HashMap<String, String>();

                            dataMap.put("Order" , Name.getText().toString().trim().toUpperCase());
                            dataMap.put("DName" , DName);
                            dataMap.put("Retailer" , WName);
                            dataMap.put("Order Id" , formattedDate);
                            TypeRef.setValue(dataMap);


                            TypeRef = FirebaseDatabase.getInstance().getReference("RetailerWholesalerOrders/"+ WId+ "/Orders/" + formattedDate + "/" ) ;
                            TypeRef.setValue(dataMap);

                            Toast.makeText(RetailersSide.this , "Order : " + Name.getText().toString() + " Added" , Toast.LENGTH_SHORT).show();

                        }else{

                            Toast.makeText(RetailersSide.this , "No Order Added" , Toast.LENGTH_SHORT).show();
                        }

                    }
                });

                canc1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        dialog.dismiss();

                    }
                });

            }
        });


        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent mainIntent = new Intent(RetailersSide.this, Retailer_View_Wholesaler_Orders.class);
                mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(mainIntent);

            }
        });


        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent mainIntent = new Intent(RetailersSide.this, Retailer_View_Shop_Orders.class);
                mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(mainIntent);

            }
        });





    }
}
