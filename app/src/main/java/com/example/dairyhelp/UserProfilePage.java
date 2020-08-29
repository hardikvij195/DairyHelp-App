package com.example.dairyhelp;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class UserProfilePage extends AppCompatActivity {


    Button nxt  , ok ;
    EditText name , age , sex , num ;


    private DatabaseReference ClassRef , UserRef ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile_page);


        Utils.getDatabase();

        nxt = (Button)findViewById(R.id.NextBtn);
        ok = (Button)findViewById(R.id.OkBtn);

        name = (EditText)findViewById(R.id.NameTxt);
        age = (EditText)findViewById(R.id.editTextAgeUser);
        sex = (EditText)findViewById(R.id.editTextSexUser);
        num = (EditText)findViewById(R.id.editTextWhatsAppUser);

        SharedPreferences sharedPrefs = getSharedPreferences("userinfo" , Context.MODE_PRIVATE);
        final String Uid = sharedPrefs.getString("Uid" , "");
        final String Id = sharedPrefs.getString("Id" , "");



        UserRef = FirebaseDatabase.getInstance().getReference("Users/User/" + Uid + "/Name/") ;
        UserRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                if(dataSnapshot.exists()){


                    UserRef = FirebaseDatabase.getInstance().getReference("Users/User/" + Uid + "/") ;
                    UserRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                            if(dataSnapshot.exists()) {

                                String Name = dataSnapshot.child("Name").getValue().toString();
                                String Age = dataSnapshot.child("Age").getValue().toString();
                                String Sex = dataSnapshot.child("Sex").getValue().toString();
                                String Num = dataSnapshot.child("Phone Number").getValue().toString();

                                name.setText(Name);
                                age.setText(Age);
                                sex.setText(Sex);
                                num.setText(Num);

                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });



                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



        nxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!name.getText().toString().isEmpty() && !age.getText().toString().isEmpty()
                        && !sex.getText().toString().isEmpty() && !num.getText().toString().isEmpty()) {

                    Intent m2 = new Intent(UserProfilePage.this, User_Main_Page.class);
                    startActivity(m2);
                } else {

                    Toast.makeText(UserProfilePage.this , "Fill All The Details" , Toast.LENGTH_LONG).show();

                }
            }
        });


        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!name.getText().toString().isEmpty() && !age.getText().toString().isEmpty()
                        && !sex.getText().toString().isEmpty() && !num.getText().toString().isEmpty()){

                    String Name = name.getText().toString();
                    String Age = age.getText().toString();
                    String Sex = sex.getText().toString();
                    String Num = num.getText().toString();

                    SharedPreferences sharedPrefs = getSharedPreferences("userinfo" , Context.MODE_PRIVATE);
                    String uid = sharedPrefs.getString("Uid" , "");
                    String ph = sharedPrefs.getString("Ph" , "");
                    String Id = sharedPrefs.getString("Id" , "");


                    ClassRef = FirebaseDatabase.getInstance().getReference("Users/User/"+ uid) ;
                    HashMap<String,String> dataMap2 = new HashMap<String, String>();
                    dataMap2.put("Phone Number" , ph);
                    dataMap2.put("Uid" , uid);
                    dataMap2.put("Name" , Name);
                    dataMap2.put("Age" , Age);
                    dataMap2.put("Sex" , Sex);
                    dataMap2.put("Num" , Num);
                    dataMap2.put("Id" , Id);

                    ClassRef.setValue(dataMap2);

                    Intent m2 = new Intent(UserProfilePage.this , User_Main_Page.class);
                    startActivity(m2);
                    finish();



                }   else {

                    Toast.makeText(UserProfilePage.this , "Fill All The Details" , Toast.LENGTH_LONG).show();

                }

            }
        });


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();




    }
}
