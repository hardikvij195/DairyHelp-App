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

public class User_Main_Page extends AppCompatActivity {

    Button b1 , b2 , LogOut;
    DatabaseReference TypeRef ,  RetRef;
    private FirebaseAuth mAuth;

    private ArrayList<NameIdClass> ShopNames = new ArrayList<>() ;
    private ArrayList<String> ShopNamesString  = new ArrayList<>() ;

    ArrayAdapter adapterSpinnerShopNames ;
    String PName , uid ,Id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user__main__page);





        Utils.getDatabase();
        mAuth = FirebaseAuth.getInstance();

        b1 = (Button)findViewById(R.id.AddOrderBtn);
        b2 = (Button)findViewById(R.id.ViewOrderBtn);
        LogOut = (Button)findViewById(R.id.LogOut);

        SharedPreferences sharedPrefs = getSharedPreferences("userinfo" , Context.MODE_PRIVATE);
        uid = sharedPrefs.getString("Uid" , "");
        Id = sharedPrefs.getString("Id" , "");


        adapterSpinnerShopNames = new ArrayAdapter<String>(this , android.R.layout.simple_spinner_dropdown_item , ShopNamesString);

        RetRef = FirebaseDatabase.getInstance().getReference("Users/User/"+ uid ) ;
        RetRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                PName = dataSnapshot.child("Name").getValue().toString();
                SharedPreferences sharedPrefs = getSharedPreferences("userinfo" , Context.MODE_PRIVATE);
                SharedPreferences.Editor edit = sharedPrefs.edit();
                edit.putString("Name" , PName );
                edit.apply();



            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        RetRef = FirebaseDatabase.getInstance().getReference("Shop/" ) ;
        RetRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                ShopNamesString.clear();
                ShopNames.clear();
                adapterSpinnerShopNames.notifyDataSetChanged();

                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){

                    String Id = dataSnapshot1.getKey();
                    String Name = dataSnapshot1.child("Name").getValue().toString();

                    ShopNames.add(new NameIdClass( Name , Id ));
                    ShopNamesString.add(Name);
                    adapterSpinnerShopNames.notifyDataSetChanged();

                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        LogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(User_Main_Page.this , "Sign Out" , Toast.LENGTH_SHORT ).show();
                mAuth.signOut();
                Intent mainIntent = new Intent(User_Main_Page.this, MainActivity.class);
                startActivity(mainIntent);
                finish();
                overridePendingTransition(android.R.anim.fade_in , android.R.anim.fade_out);

            }
        });

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                //DIALOG BOX
                final AlertDialog.Builder mBuilder = new AlertDialog.Builder(User_Main_Page.this)
                        .setCancelable(false);
                View mView = getLayoutInflater().inflate(R.layout.dialog_box_add_order, null);

                final EditText Name = (EditText) mView.findViewById(R.id.ItemNameEditTxt);
                final Button canc1 = (Button) mView.findViewById(R.id.button19);
                final Button ok1 = (Button) mView.findViewById(R.id.button20);
                final Spinner Spin = (Spinner) mView.findViewById(R.id.spinnerNames);

                Spin.setAdapter(adapterSpinnerShopNames);

                mBuilder.setView(mView);
                final AlertDialog dialog = mBuilder.create();
                dialog.show();

                ok1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        int pos = Spin.getSelectedItemPosition();

                        if( !ShopNames.isEmpty() && !ShopNamesString.isEmpty() &&  !Name.getText().toString().isEmpty()  && !uid.isEmpty() ){

                            Date date = new Date();  // to get the date
                            SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss"); // getting date in this format
                            final String formattedDate = df.format(date.getTime());

                            String DId = ShopNames.get(pos).getId();
                            String DName = ShopNames.get(pos).getName();

                            TypeRef = FirebaseDatabase.getInstance().getReference("ShopUserOrders/"+ DId+ "/Orders/" + formattedDate + "/" ) ;
                            HashMap<String,String> dataMap = new HashMap<String, String>();

                            dataMap.put("Order" , Name.getText().toString().trim().toUpperCase());
                            dataMap.put("DName" , DName);
                            dataMap.put("PName" , PName);
                            dataMap.put("Order Id" , formattedDate);
                            TypeRef.setValue(dataMap);


                            TypeRef = FirebaseDatabase.getInstance().getReference("UserShopOrders/"+ Id + "/Orders/" + formattedDate + "/" ) ;
                            TypeRef.setValue(dataMap);

                            Toast.makeText(User_Main_Page.this , "Order : " + Name.getText().toString() + " Added" , Toast.LENGTH_SHORT).show();

                        }else{

                            Toast.makeText(User_Main_Page.this , "No Order Added" , Toast.LENGTH_SHORT).show();
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

                Intent mainIntent = new Intent(User_Main_Page.this, User_View_Orders.class);
                mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(mainIntent);

            }
        });





    }
}
