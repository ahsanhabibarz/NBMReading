package com.example.habii.nbmreadingapp;

import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class StudentsActivity extends AppCompatActivity {

    FirebaseFirestore firebaseFirestore;


    List<TeachersModelClass> teachersModelClassList;

    StudentsTeachersAdapter studentsTeachersAdapter;

    RecyclerView recyclerView;

    private FirebaseAuth mAuth;

    private long doubleBackToExitPressedOnce;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_students);


        firebaseFirestore = FirebaseFirestore.getInstance();
        recyclerView = (RecyclerView)findViewById(R.id.teachers);

        teachersModelClassList = new ArrayList<>();

        studentsTeachersAdapter = new StudentsTeachersAdapter(teachersModelClassList,getApplicationContext(),StudentsActivity.this);

        mAuth = FirebaseAuth.getInstance();


        Query fireQuery = firebaseFirestore.collection("Teachers");

        fireQuery.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {


                if(e != null){}


                for(DocumentChange documentChange : documentSnapshots.getDocumentChanges()){

                    if(documentChange.getType() == DocumentChange.Type.ADDED ){


                        String postid = documentChange.getDocument().getId();

                        TeachersModelClass classess = documentChange.getDocument().toObject(TeachersModelClass.class).withID(postid,"","");

                        teachersModelClassList.add(classess);

                        studentsTeachersAdapter.notifyDataSetChanged();



                    }


                }

            }
        });


        DividerItemDecoration divider = new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL);
        divider.setDrawable(ContextCompat.getDrawable(getBaseContext(), R.drawable.custom_divider));
        recyclerView.addItemDecoration(divider);


        recyclerView.hasFixedSize();
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setAdapter(studentsTeachersAdapter);


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
