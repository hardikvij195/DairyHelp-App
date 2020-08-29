package com.example.dairyhelp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DairySide extends AppCompatActivity {


    DatabaseReference DairyRef;
    Button b1 , LogOut ;
    private FirebaseAuth mAuth;
    String DUid , DId , DName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dairy_side);

        mAuth = FirebaseAuth.getInstance();

        b1 = (Button)findViewById(R.id.DairyViewOrdersBtn);
        LogOut = (Button)findViewById(R.id.LogOut);

        DUid = mAuth.getUid();



        DairyRef = FirebaseDatabase.getInstance().getReference("Users/Dairy/"+ DUid ) ;
        DairyRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                DId = dataSnapshot.child("Id").getValue().toString();
                DName = dataSnapshot.child("Name").getValue().toString();
                SharedPreferences sharedPrefs = getSharedPreferences("userinfo" , Context.MODE_PRIVATE);
                SharedPreferences.Editor edit = sharedPrefs.edit();
                edit.putString("Uid" , DUid );
                edit.putString("Id" , DId );
                edit.putString("Name" , DName );

                edit.apply();



            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent mainIntent = new Intent(DairySide.this, Dairy_View_Wholesaler_Orders.class);
                mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(mainIntent);

            }
        });





        LogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(DairySide.this , "Sign Out" , Toast.LENGTH_SHORT ).show();
                mAuth.signOut();
                Intent mainIntent = new Intent(DairySide.this, MainActivity.class);
                startActivity(mainIntent);
                finish();
                overridePendingTransition(android.R.anim.fade_in , android.R.anim.fade_out);

            }
        });


    }
}
