package com.example.habii.nbmreadingapp;

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

public class StudentsClassRoomActivity extends AppCompatActivity {


    FirebaseFirestore firebaseFirestore;


    List<Classes> classroomList;

    StudentsClassRoomAdapter sbooksAdapter;

    RecyclerView recyclerView;

    private FirebaseAuth mAuth;

    String post_id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_room);


        Bundle extras = getIntent().getExtras();
        if(extras != null){

            post_id = extras.getString("postid");
        }

        firebaseFirestore = FirebaseFirestore.getInstance();
        recyclerView = (RecyclerView)findViewById(R.id.classrooms);


        classroomList = new ArrayList<>();


        sbooksAdapter = new StudentsClassRoomAdapter(classroomList,getApplicationContext());

        mAuth = FirebaseAuth.getInstance();


        Query fireQuery = firebaseFirestore.collection("Teachers").document(post_id).collection("Class");

        fireQuery.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {


                if(e != null){}


                for(DocumentChange documentChange : documentSnapshots.getDocumentChanges()){

                    if(documentChange.getType() == DocumentChange.Type.ADDED ){



                        String postid = documentChange.getDocument().getId();

                        Classes classess = documentChange.getDocument().toObject(Classes.class).withID(postid,post_id,"");

                        classroomList.add(classess);

                        sbooksAdapter.notifyDataSetChanged();



                    }


                }

            }
        });


        DividerItemDecoration divider = new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL);
        divider.setDrawable(ContextCompat.getDrawable(getBaseContext(), R.drawable.custom_divider));
        recyclerView.addItemDecoration(divider);

        recyclerView.hasFixedSize();
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setAdapter(sbooksAdapter);

    }
}
