package com.example.habii.nbmreadingapp;

import android.content.Intent;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;

public class MainActivity extends AppCompatActivity {


    boolean authState = false;

    private FirebaseAuth.AuthStateListener mAuthStateListener;

    private FirebaseAuth mAuth;

    boolean tfound ;

    boolean sfound ;

    FirebaseFirestore firebaseFirestore;

    private long doubleBackToExitPressedOnce;

    //private ProgressDialog progressDialog;

    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);


        mAuth = FirebaseAuth.getInstance();

        firebaseFirestore = FirebaseFirestore.getInstance();

        imageView = (ImageView)findViewById(R.id.loading);


        //progressDialog = new ProgressDialog(this);

        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(true)
                .build();


        Glide.with(getApplicationContext()).load(R.drawable.loading_gif).into(imageView);


        //constraintLayout.setVisibility(View.GONE);

        firebaseFirestore.setFirestoreSettings(settings);


        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                if(firebaseAuth.getCurrentUser() == null){

                    startActivity(new Intent(MainActivity.this,LoginActivity.class));

                    authState = false;

                }

                else if(firebaseAuth.getCurrentUser() != null){

                    authState = true;

                    setUpUser(firebaseAuth.getCurrentUser().getUid());

                }

            }
        };

    }


    @Override
    protected void onStart() {
        super.onStart();

        mAuth.addAuthStateListener(mAuthStateListener);
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce + 2000 > System.currentTimeMillis()){

            finishAffinity();

            super.onBackPressed();
        }
        else{

            Toast.makeText(getBaseContext(),
                    "Press once again to exit!", Toast.LENGTH_SHORT)
                    .show();
        }
        doubleBackToExitPressedOnce = System.currentTimeMillis();
    }


    void setUpUser(String userid){


        final  String uid = userid;


//        progressDialog.setMessage("Getting Info...");
//        progressDialog.show();


        firebaseFirestore.collection("Teachers").document(uid).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {


                if(task.getResult().exists()){


                    final Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {

                                //progressDialog.dismiss();
                                finish();
                                startActivity(new Intent(MainActivity.this,TeachersActivity.class));
                                overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                            }
                        }, 500);


                }else {

                    firebaseFirestore.collection("Students").document(uid).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                            if(task.getResult().exists()){


                                final Handler handler2 = new Handler();
                                handler2.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {

                                        //progressDialog.dismiss();
                                        finish();
                                        startActivity(new Intent(MainActivity.this,StudentsActivity.class));
                                        overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                                    }
                                }, 500);


                            }else{

                                finish();
                                startActivity(new Intent(MainActivity.this,AllProfileActivity.class));
                                overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);

                            }


                        }
                    });

                }



            }
        });

    }
}



