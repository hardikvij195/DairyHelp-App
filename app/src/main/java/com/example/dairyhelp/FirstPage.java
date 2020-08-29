package com.example.dairyhelp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class FirstPage extends AppCompatActivity {

    Button login , userlogin;
    EditText Nametxt, Passtxt;

    TextView forgotPass ;
    String user_id = "";
    private FirebaseAuth mAuth;
    private FirebaseDatabase database;
    private ProgressDialog mLoginProgress;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_page);

        Utils.getDatabase();

        login = (Button) findViewById(R.id.LoginActBtn);
        userlogin = (Button) findViewById(R.id.UserActBtn);
        forgotPass = (TextView)findViewById(R.id.ForgotPasswordTextView);
        mLoginProgress = new ProgressDialog(this);
        database = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
        Nametxt = (EditText) findViewById(R.id.NameText);
        Passtxt = (EditText) findViewById(R.id.PassText);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        if (currentUser != null) {

            mLoginProgress.setTitle("Logging In");
            mLoginProgress.setMessage("Please Wait While We Log Into Your Account");
            mLoginProgress.setCanceledOnTouchOutside(false);
            mLoginProgress.show();
            user_id = currentUser.getUid();

            DatabaseReference AlreadySignedIn1 = database.getReference("Users");
            AlreadySignedIn1.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    System.out.println("Working  ---  " + user_id);

                    if(dataSnapshot.child("Admin").child(user_id).exists())
                    {
                        Intent mainIntent = new Intent(FirstPage.this, AdminActivity.class);
                        mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(mainIntent);
                        finish();
                        mLoginProgress.dismiss();

                    }
                    else if(dataSnapshot.child("Dairy").child(user_id).exists())
                    {

                        Intent mainIntent = new Intent(FirstPage.this, DairySide.class);
                        mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(mainIntent);
                        finish();
                        mLoginProgress.dismiss();



                    } else if(dataSnapshot.child("Wholesaler").child(user_id).exists())
                    {
                        Intent mainIntent = new Intent(FirstPage.this, WholesalerSide.class);
                        mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(mainIntent);
                        finish();
                        mLoginProgress.dismiss();

                    }
                    else if(dataSnapshot.child("Retailer").child(user_id).exists())
                    {
                        Intent mainIntent = new Intent(FirstPage.this, RetailersSide.class);
                        mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(mainIntent);
                        finish();
                        mLoginProgress.dismiss();

                    }
                    else if(dataSnapshot.child("Shop").child(user_id).exists())
                    {
                        Intent mainIntent = new Intent(FirstPage.this, ShopkeeperSide.class);
                        mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(mainIntent);
                        finish();
                        mLoginProgress.dismiss();

                    }
                    else if(dataSnapshot.child("User").child(user_id).exists())
                    {
                        Intent mainIntent = new Intent(FirstPage.this, UserProfilePage.class);
                        mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(mainIntent);
                        finish();
                        mLoginProgress.dismiss();

                    }
                    else {
                        mAuth.signOut();
                        Toast.makeText(FirstPage.this, "User Not Found -  " + user_id , Toast.LENGTH_SHORT).show();
                        mLoginProgress.dismiss();


                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

            Toast.makeText(FirstPage.this, "User Already Logged In :  " + user_id, Toast.LENGTH_SHORT).show();

        } else {

            Toast.makeText(FirstPage.this, "No User Logged In ", Toast.LENGTH_SHORT).show();

        }


        userlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent mainIntent = new Intent(FirstPage.this, UserLoginAct.class);
                mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(mainIntent);

            }
        });

        //FORGOT PASSWORD -----------------------------------------------------------------------

        forgotPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final AlertDialog.Builder mBuilder = new AlertDialog.Builder( FirstPage.this)
                        .setCancelable(false);
                View mView = getLayoutInflater().inflate(R.layout.dialog_box_forgot_password, null);
                final EditText EditTextForgotPass = (EditText) mView.findViewById(R.id.EditTextForgotPassword);
                AlertDialog.Builder builder = mBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialog, int which) {

                        if (!EditTextForgotPass.getText().toString().isEmpty()) {

                            mLoginProgress.setMessage("Sending Mail");
                            mLoginProgress.setCanceledOnTouchOutside(false);
                            mLoginProgress.show();
                            mAuth.sendPasswordResetEmail(EditTextForgotPass.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    if (task.isSuccessful()) {

                                        Toast.makeText(FirstPage.this, "Email Sent", Toast.LENGTH_SHORT).show();
                                        mLoginProgress.dismiss();
                                        dialog.dismiss();
                                    } else {

                                        Toast.makeText(FirstPage.this, "Email Does Not Exist", Toast.LENGTH_SHORT).show();
                                        mLoginProgress.dismiss();
                                        dialog.dismiss();
                                    }

                                }
                            });


                        }


                    }
                });

                mBuilder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();
                    }
                });


                mBuilder.setView(mView);
                AlertDialog dialog = mBuilder.create();
                dialog.show();


            }
        });




        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String email = Nametxt.getText().toString();
                final String Pass = Passtxt.getText().toString();

                if (!TextUtils.isEmpty(email) || !TextUtils.isEmpty(Pass)) {

                    mLoginProgress.setTitle("Logging In");
                    mLoginProgress.setMessage("Please wait while we check your credentials");
                    mLoginProgress.setCanceledOnTouchOutside(false);
                    mLoginProgress.show();

                    mAuth.signInWithEmailAndPassword(email, Pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if (task.isSuccessful()) {

                                Toast.makeText(FirstPage.this, "Successfully Logged In", Toast.LENGTH_SHORT).show();
                                FirebaseUser user = Objects.requireNonNull(task.getResult()).getUser();
                                assert user != null;
                                user_id = user.getUid();

                                SharedPreferences sharedPrefs = getSharedPreferences("userinfo" , Context.MODE_PRIVATE);
                                SharedPreferences.Editor edit = sharedPrefs.edit();
                                edit.putString("USERID" , user_id );
                                edit.putString("EMAIL" , Nametxt.getText().toString().trim());
                                edit.putString("PASSWORD" , Passtxt.getText().toString().trim() );
                                edit.apply();

                                System.out.println("1  -- Working Email ---  " + Nametxt);



                                //DatabaseReference myRef = database.getReference("Users/" + user_id).child("Type");
                                System.out.println("1  -- Working  ---  " + user_id);

                                if(!user_id.equals("")){

                                    DatabaseReference AlreadySignedIn1 = database.getReference("Users");
                                    AlreadySignedIn1.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {

                                            System.out.println("Working  ---  " + user_id);

                                            if(Nametxt.getText().toString().trim().equals("admin@gmail.com") && user_id.equals("xPytIihNr0W0uLAT9DBWrbYELkB3"))
                                            {
                                                Intent mainIntent = new Intent(FirstPage.this, AdminActivity.class);
                                                mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                                startActivity(mainIntent);
                                                finish();
                                                mLoginProgress.dismiss();

                                            }
                                            else if(dataSnapshot.child("Dairy").child(user_id).exists())
                                            {

                                                Intent mainIntent = new Intent(FirstPage.this, DairySide.class);
                                                mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                                startActivity(mainIntent);
                                                finish();
                                                mLoginProgress.dismiss();



                                            } else if(dataSnapshot.child("Wholesaler").child(user_id).exists())
                                            {
                                                Intent mainIntent = new Intent(FirstPage.this, WholesalerSide.class);
                                                mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                                startActivity(mainIntent);
                                                finish();
                                                mLoginProgress.dismiss();

                                            }
                                            else if(dataSnapshot.child("Retailer").child(user_id).exists())
                                            {
                                                Intent mainIntent = new Intent(FirstPage.this, RetailersSide.class);
                                                mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                                startActivity(mainIntent);
                                                finish();
                                                mLoginProgress.dismiss();

                                            }
                                            else if(dataSnapshot.child("Shop").child(user_id).exists())
                                            {
                                                Intent mainIntent = new Intent(FirstPage.this, ShopkeeperSide.class);
                                                mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                                startActivity(mainIntent);
                                                finish();
                                                mLoginProgress.dismiss();

                                            }    else if(dataSnapshot.child("User").child(user_id).exists())
                                            {
                                                Intent mainIntent = new Intent(FirstPage.this, UserProfilePage.class);
                                                mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                                startActivity(mainIntent);
                                                finish();
                                                mLoginProgress.dismiss();

                                            }
                                            else {
                                                mAuth.signOut();
                                                Toast.makeText(FirstPage.this, "User Not Found -- " + user_id, Toast.LENGTH_SHORT).show();
                                                mLoginProgress.dismiss();


                                            }

                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });



                                }



                            } else {

                                try
                                {
                                    throw task.getException();
                                }
                                // if user enters wrong email.
                                catch (FirebaseAuthInvalidUserException invalidEmail)
                                {
                                    mLoginProgress.hide();
                                    Toast.makeText(FirstPage.this, "Invalid Email" , Toast.LENGTH_SHORT).show();


                                }
                                // if user enters wrong password.
                                catch (FirebaseAuthInvalidCredentialsException wrongPassword)
                                {
                                    mLoginProgress.hide();
                                    Toast.makeText(FirstPage.this, "Wrong Password", Toast.LENGTH_SHORT).show();

                                }
                                catch (Exception e)
                                {
                                    Toast.makeText(FirstPage.this,  e.getMessage() ,
                                            Toast.LENGTH_SHORT).show();
                                    mLoginProgress.dismiss();
                                }
                            }
                        }
                    });
                } else {
                    Toast.makeText(FirstPage.this, "FIELDS CANNOT BE EMPTY", Toast.LENGTH_SHORT).show();

                }
            }
        });

    }
}
