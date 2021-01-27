package com.example.habii.nbmreadingapp;

import android.content.DialogInterface;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TeachersActivity extends AppCompatActivity {


    FirebaseFirestore firebaseFirestore;


    List<Classes> classNameList;

    TeachersClassAdapter teachersClassAdapter;

    RecyclerView recyclerView;

    private FirebaseAuth mAuth;

    FloatingActionButton floatingActionButton;

    AlertDialog b;
    EditText className,semesterName,courseid;

    String initial;

    int year;

    private long doubleBackToExitPressedOnce;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teachers);


        firebaseFirestore = FirebaseFirestore.getInstance();
        recyclerView = (RecyclerView)findViewById(R.id.classes);


        classNameList = new ArrayList<>();

        final AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
        final LayoutInflater inflater = LayoutInflater.from(this);

        floatingActionButton = (FloatingActionButton)findViewById(R.id.addButton);
        teachersClassAdapter = new TeachersClassAdapter(classNameList,getApplicationContext());

        mAuth = FirebaseAuth.getInstance();

        alertBuilder.setPositiveButton("Apply", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {


                if(!className.getText().toString().isEmpty()&&!courseid.getText().toString().isEmpty()&& !semesterName.getText().toString().isEmpty()){


                    Map<String,Object> post = new HashMap<>();
                    post.put("name",className.getText().toString()+"-"+courseid.getText().toString());
                    post.put("id", initial+"-"+className.getText().toString()+"-"+courseid.getText().toString()+"-"+semesterName.getText().toString()+"-"+String.valueOf(year));
                    firebaseFirestore.collection("Teachers").document(mAuth.getCurrentUser().getUid()).collection("Class")
                            .document().set(post);

                }else{

                    Toast.makeText(TeachersActivity.this, "Please Fill Up Correctly", Toast.LENGTH_SHORT).show();

                }




            }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });


        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final View dialogView = inflater.inflate(R.layout.new_class, null);

                alertBuilder.setView(dialogView);
                b = alertBuilder.create();
                b.show();

                className = b.findViewById(R.id.className);
                semesterName = b.findViewById(R.id.SemesterName);
                courseid = b.findViewById(R.id.courseID);

                Calendar calendar = Calendar.getInstance();
                year = calendar.get(Calendar.YEAR);


                getInitial();

            }
        });


        Query fireQuery = firebaseFirestore.collection("Teachers").document(mAuth.getCurrentUser().getUid()).collection("Class")
                .orderBy("name",Query.Direction.ASCENDING);

        fireQuery.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {


                if(e != null){}


                for(DocumentChange documentChange : documentSnapshots.getDocumentChanges()){

                    if(documentChange.getType() == DocumentChange.Type.ADDED ){


                        String postid = documentChange.getDocument().getId();

                        Classes classess = documentChange.getDocument().toObject(Classes.class).withID(postid,"","");

                        classNameList.add(classess);

                        teachersClassAdapter.notifyDataSetChanged();

                    }else if(documentChange.getType() == DocumentChange.Type.REMOVED){

                        classNameList.remove(documentChange.getOldIndex());
                        teachersClassAdapter.notifyItemRemoved(documentChange.getOldIndex());

                    }

                }

            }
        });



        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                if(!(classNameList.size()>=1)) {

                    Toast.makeText(TeachersActivity.this, "Please add classrooms", Toast.LENGTH_LONG).show();

                }
            }
        }, 3000);



        DividerItemDecoration divider = new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL);
        divider.setDrawable(ContextCompat.getDrawable(getBaseContext(), R.drawable.custom_divider));
        recyclerView.addItemDecoration(divider);
        recyclerView.hasFixedSize();
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setAdapter(teachersClassAdapter);
    }


    void getInitial(){

        firebaseFirestore.collection("Teachers").document(mAuth.getCurrentUser().getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                if(task.isSuccessful()){

                    DocumentSnapshot documentSnapshot = task.getResult();

                    if(documentSnapshot.exists()&& documentSnapshot != null){

                        initial = documentSnapshot.getString("initial");

                    }

                }

            }
        });


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

}
