package com.example.habii.nbmreadingapp;

import android.content.Intent;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class ReadByActivity extends AppCompatActivity {


    FirebaseFirestore firebaseFirestore;


    List<ReadBy> readByList;

    ReadByAdapter readByAdapter;

    RecyclerView recyclerView;

    private FirebaseAuth mAuth;


    String classid,bookid;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_by);

        Bundle extras = getIntent().getExtras();
        if(extras != null){

            classid = extras.getString("classid");
            bookid = extras.getString("bookid");

        }

        firebaseFirestore = FirebaseFirestore.getInstance();
        recyclerView = (RecyclerView)findViewById(R.id.readbyList);


        readByList = new ArrayList<>();


        readByAdapter = new ReadByAdapter(readByList,getApplicationContext());

        mAuth = FirebaseAuth.getInstance();


        Query fireQuery = firebaseFirestore.collection("Teachers").document(mAuth.getCurrentUser().getUid())
                .collection("Class")
                .document(classid).collection("Books").document(bookid).collection("ReadBy")
                .orderBy("time",Query.Direction.DESCENDING);
        fireQuery.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {


                if(e != null){}


                for(DocumentChange documentChange : documentSnapshots.getDocumentChanges()){

                    if(documentChange.getType() == DocumentChange.Type.ADDED ){



                        String postid = documentChange.getDocument().getId();

                        ReadBy readBy = documentChange.getDocument().toObject(ReadBy.class).withID(postid,"","");

                        readByList.add(readBy);

                        readByAdapter.notifyDataSetChanged();

                    }


                }

            }
        });


        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                if(!(readByList.size()>=1)) {

                    Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "No student activiy", Snackbar.LENGTH_INDEFINITE);
                    snackbar.show();

                }
            }
        }, 3000);





        DividerItemDecoration divider = new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL);
        divider.setDrawable(ContextCompat.getDrawable(getBaseContext(), R.drawable.custom_divider));
        recyclerView.addItemDecoration(divider);


        recyclerView.hasFixedSize();
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setAdapter(readByAdapter);

    }
}
