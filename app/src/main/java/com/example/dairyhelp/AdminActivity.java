package com.example.dairyhelp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AdminActivity extends AppCompatActivity {

    Button AddDairy , AddWhole , AddRet , Logout , DelDairy , DelWhole , DelRet , AddShop , DelShop;
    private FirebaseAuth mAuth;

    DatabaseReference DairyRef , WholeRef , RetRef , ShopRef;

    private ArrayList<NameIdClass> DairyNames = new ArrayList<>() ;
    private ArrayList<String> DairyNamesString = new ArrayList<>() ;
    private ArrayList<NameIdClass> WholesalerNames = new ArrayList<>() ;
    private ArrayList<String> WholesalerNamesString = new ArrayList<>() ;
    private ArrayList<NameIdClass> RetailerNames = new ArrayList<>() ;
    private ArrayList<String> RetailerNamesString = new ArrayList<>() ;
    private ArrayList<NameIdClass> ShopNames = new ArrayList<>() ;
    private ArrayList<String> ShopNamesString = new ArrayList<>() ;

    private ArrayAdapter<String> adapterSpinnerDairy ;
    private ArrayAdapter<String> adapterSpinnerWholesaler ;
    private ArrayAdapter<String> adapterSpinnerRetailer ;
    private ArrayAdapter<String> adapterSpinnerShop ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);


        Utils.getDatabase();

        AddDairy = (Button)findViewById(R.id.AddDairyBtn);
        DelDairy = (Button)findViewById(R.id.DelDairyBtn);
        AddWhole = (Button)findViewById(R.id.AddWholeBtn);
        DelWhole = (Button)findViewById(R.id.DelWholeBtn);
        AddRet = (Button)findViewById(R.id.AddRetBtn);
        DelRet = (Button)findViewById(R.id.DelRetBtn);
        AddShop = (Button)findViewById(R.id.AddShopBtn);
        DelShop = (Button)findViewById(R.id.DelShopBtn);
        Logout = (Button)findViewById(R.id.LogOut);
        mAuth = FirebaseAuth.getInstance();

        adapterSpinnerDairy = new ArrayAdapter<String>(this , android.R.layout.simple_spinner_dropdown_item , DairyNamesString);
        adapterSpinnerWholesaler = new ArrayAdapter<String>(this , android.R.layout.simple_spinner_dropdown_item , WholesalerNamesString);
        adapterSpinnerRetailer = new ArrayAdapter<String>(this , android.R.layout.simple_spinner_dropdown_item , RetailerNamesString);
        adapterSpinnerShop = new ArrayAdapter<String>(this , android.R.layout.simple_spinner_dropdown_item , ShopNamesString);


        Logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(AdminActivity.this , "Sign Out" , Toast.LENGTH_SHORT ).show();
                mAuth.signOut();
                Intent mainIntent = new Intent(AdminActivity.this, MainActivity.class);
                startActivity(mainIntent);
                finish();
                overridePendingTransition(android.R.anim.fade_in , android.R.anim.fade_out);

            }
        });


        AddDairy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent mainIntent = new Intent(AdminActivity.this, Admin_Create_Dairy.class);
                mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(mainIntent);

            }
        });


        AddWhole.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent mainIntent = new Intent(AdminActivity.this, Admin_Create_Wholesaler.class);
                mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(mainIntent);

            }
        });


        AddRet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent mainIntent = new Intent(AdminActivity.this, Admin_Create_Retailer.class);
                mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(mainIntent);

            }
        });

        AddShop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent mainIntent = new Intent(AdminActivity.this, Admin_Create_Shop.class);
                mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(mainIntent);

            }
        });


        DairyRef = FirebaseDatabase.getInstance().getReference("Dairy/") ;
        DairyRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                DairyNames.clear();
                DairyNamesString.clear();
                adapterSpinnerDairy.notifyDataSetChanged();

                if(dataSnapshot.exists()){

                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){

                        String id = dataSnapshot1.getKey();
                        String Name = dataSnapshot1.child("Name").getValue().toString();
                        String Uid = dataSnapshot1.child("Uid").getValue().toString();
                        DairyNames.add(new NameIdClass(Uid , id));
                        DairyNamesString.add(Name);
                        adapterSpinnerDairy.notifyDataSetChanged();

                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        WholeRef = FirebaseDatabase.getInstance().getReference("Wholesaler/") ;
        WholeRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                WholesalerNames.clear();
                WholesalerNamesString.clear();
                adapterSpinnerWholesaler.notifyDataSetChanged();

                if(dataSnapshot.exists()){

                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){

                        String id = dataSnapshot1.getKey();
                        String Name = dataSnapshot1.child("Name").getValue().toString();
                        String Uid = dataSnapshot1.child("Uid").getValue().toString();
                        WholesalerNames.add(new NameIdClass(Uid , id));
                        WholesalerNamesString.add(Name);
                        adapterSpinnerWholesaler.notifyDataSetChanged();

                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        RetRef = FirebaseDatabase.getInstance().getReference("Retailer/") ;
        RetRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                RetailerNames.clear();
                RetailerNamesString.clear();
                adapterSpinnerRetailer.notifyDataSetChanged();

                if(dataSnapshot.exists()){

                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){

                        String id = dataSnapshot1.getKey();
                        String Name = dataSnapshot1.child("Name").getValue().toString();
                        String Uid = dataSnapshot1.child("Uid").getValue().toString();
                        RetailerNames.add(new NameIdClass(Uid , id));
                        RetailerNamesString.add(Name);
                        adapterSpinnerRetailer.notifyDataSetChanged();

                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        ShopRef = FirebaseDatabase.getInstance().getReference("Shop/") ;
        ShopRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                ShopNames.clear();
                ShopNamesString.clear();
                adapterSpinnerShop.notifyDataSetChanged();

                if(dataSnapshot.exists()){

                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){

                        String id = dataSnapshot1.getKey();
                        String Name = dataSnapshot1.child("Name").getValue().toString();
                        String Uid = dataSnapshot1.child("Uid").getValue().toString();
                        ShopNames.add(new NameIdClass(Uid , id));
                        ShopNamesString.add(Name);
                        adapterSpinnerShop.notifyDataSetChanged();

                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        DelDairy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                final AlertDialog.Builder mBuilder = new AlertDialog.Builder(AdminActivity.this)
                        .setCancelable(false);
                View mView = getLayoutInflater().inflate(R.layout.dialog_box_delete_type, null);

                final Spinner Sp2 = (Spinner) mView.findViewById(R.id.spinner2);
                final Button canc1 = (Button) mView.findViewById(R.id.button19);
                final Button ok1 = (Button) mView.findViewById(R.id.button20);


                Sp2.setAdapter(adapterSpinnerDairy);

                mBuilder.setView(mView);
                final AlertDialog dialog = mBuilder.create();
                dialog.show();


                ok1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        int ps = Sp2.getSelectedItemPosition();

                        if(ps != -1){

                            String Id = DairyNames.get(ps).getId();
                            DairyRef = FirebaseDatabase.getInstance().getReference("Dairy/" + Id ) ;
                            DairyRef.removeValue();
                            adapterSpinnerDairy.notifyDataSetChanged();

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




        DelWhole.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                final AlertDialog.Builder mBuilder = new AlertDialog.Builder(AdminActivity.this)
                        .setCancelable(false);
                View mView = getLayoutInflater().inflate(R.layout.dialog_box_delete_type, null);

                final Spinner Sp2 = (Spinner) mView.findViewById(R.id.spinner2);
                final Button canc1 = (Button) mView.findViewById(R.id.button19);
                final Button ok1 = (Button) mView.findViewById(R.id.button20);


                Sp2.setAdapter(adapterSpinnerWholesaler);

                mBuilder.setView(mView);
                final AlertDialog dialog = mBuilder.create();
                dialog.show();


                ok1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        int ps = Sp2.getSelectedItemPosition();

                        if(ps != -1){

                            String Id = WholesalerNames.get(ps).getId();
                            WholeRef = FirebaseDatabase.getInstance().getReference("Wholesaler/" + Id ) ;
                            WholeRef.removeValue();
                            adapterSpinnerWholesaler.notifyDataSetChanged();

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



        DelRet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                final AlertDialog.Builder mBuilder = new AlertDialog.Builder(AdminActivity.this)
                        .setCancelable(false);
                View mView = getLayoutInflater().inflate(R.layout.dialog_box_delete_type, null);

                final Spinner Sp2 = (Spinner) mView.findViewById(R.id.spinner2);
                final Button canc1 = (Button) mView.findViewById(R.id.button19);
                final Button ok1 = (Button) mView.findViewById(R.id.button20);
                Sp2.setAdapter(adapterSpinnerRetailer);

                mBuilder.setView(mView);
                final AlertDialog dialog = mBuilder.create();
                dialog.show();
                ok1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int ps = Sp2.getSelectedItemPosition();
                        if(ps != -1){
                            String Id = RetailerNames.get(ps).getId();
                            RetRef = FirebaseDatabase.getInstance().getReference("Retailer/" + Id ) ;
                            RetRef.removeValue();
                            adapterSpinnerRetailer.notifyDataSetChanged();
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



        DelShop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                final AlertDialog.Builder mBuilder = new AlertDialog.Builder(AdminActivity.this)
                        .setCancelable(false);
                View mView = getLayoutInflater().inflate(R.layout.dialog_box_delete_type, null);
                final Spinner Sp2 = (Spinner) mView.findViewById(R.id.spinner2);
                final Button canc1 = (Button) mView.findViewById(R.id.button19);
                final Button ok1 = (Button) mView.findViewById(R.id.button20);
                Sp2.setAdapter(adapterSpinnerShop);
                mBuilder.setView(mView);
                final AlertDialog dialog = mBuilder.create();
                dialog.show();


                ok1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int ps = Sp2.getSelectedItemPosition();
                        if(ps != -1){

                            String Id = ShopNames.get(ps).getId();
                            ShopRef = FirebaseDatabase.getInstance().getReference("Shop/" + Id ) ;
                            ShopRef.removeValue();
                            adapterSpinnerShop.notifyDataSetChanged();
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

















    }
}
