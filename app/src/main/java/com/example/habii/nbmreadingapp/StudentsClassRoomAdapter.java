package com.example.habii.nbmreadingapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class StudentsClassRoomAdapter extends RecyclerView.Adapter<StudentsClassRoomAdapter.ViewHolder> {


    public List<Classes> classesList;

    Context context;

    public StudentsClassRoomAdapter(List<Classes> classesList, Context context) {
        this.classesList = classesList;
        this.context = context;
    }

    @NonNull
    @Override
    public StudentsClassRoomAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.student_class_model,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final StudentsClassRoomAdapter.ViewHolder holder, int position) {


        final String classid = classesList.get(position).PostID;

        final String teacherid = classesList.get(position).PostID2;




        holder.className.setText(classesList.get(position).getId());
        holder.coursename.setText(classesList.get(position).getName());


        final FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

        final FirebaseAuth mAuth = FirebaseAuth.getInstance();
        Random rnd = new Random();
        String[] col = {

                "#B71C1C",
                "#880E4F",
                "#4A148C",
                "#311B92",
                "#1A237E",
                "#0D47A1",
                "#01579B",
                "#006064",
                "#004D40",
                "#1B5E20",
                "#33691E",
                "#1B5E20",
                "#827717",
                "#F57F17",
                "#E65100",
                "#BF360C",
                "#3E2723",
                "#212121",
                "#000000",
                "#3E2723"
        };

        holder.unknown.setBackgroundColor(Color.parseColor(col[rnd.nextInt(20)]));

        firebaseFirestore.collection("Teachers").document(teacherid).collection("Class")
                .document(classid).collection("Students").document(mAuth.getCurrentUser().getUid().toString()).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(DocumentSnapshot documentSnapshot, FirebaseFirestoreException e) {


                if(documentSnapshot.exists()){

                    holder.join.setVisibility(View.GONE);
                    holder.visit.setVisibility(View.VISIBLE);
                    holder.pending.setVisibility(View.GONE);

                }else{

                    firebaseFirestore.collection("Teachers").document(teacherid).collection("Class")
                            .document(classid).collection("StudentsReq").document(mAuth.getCurrentUser().getUid().toString()).addSnapshotListener(new EventListener<DocumentSnapshot>() {
                        @Override
                        public void onEvent(DocumentSnapshot documentSnapshot, FirebaseFirestoreException e) {

                            if(documentSnapshot != null && documentSnapshot.exists()){


                                holder.join.setVisibility(View.GONE);
                                holder.visit.setVisibility(View.GONE);
                                holder.pending.setVisibility(View.VISIBLE);

                            }else{

                                holder.join.setVisibility(View.VISIBLE);
                                holder.visit.setVisibility(View.GONE);
                                holder.pending.setVisibility(View.GONE);

                            }

                        }
                    });


                }


            }
        });


        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(holder.visit.getVisibility() == View.VISIBLE){

                    Intent intent = new Intent(context, StudentsBookActivity.class);
                    intent.putExtra("postid", classid);
                    intent.putExtra("teachersid", teacherid);
                    context.startActivity(intent);

                }else{

                    Toast.makeText(context, "Please Join to view classroom", Toast.LENGTH_SHORT).show();

                }


            }
        });


        holder.visit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(context, StudentsBookActivity.class);
                intent.putExtra("postid", classid);
                intent.putExtra("teachersid", teacherid);
                context.startActivity(intent);

            }
        });


        holder.join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                String uid = mAuth.getCurrentUser().getUid();

                firebaseFirestore.collection("Students").document(uid).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                        DocumentSnapshot documentSnapshot = task.getResult();

                        Map<String,Object> post = new HashMap<>();
                        post.put("email",mAuth.getCurrentUser().getEmail());
                        post.put("name", mAuth.getCurrentUser().getDisplayName());
                        post.put("id",documentSnapshot.getString("id"));
                        post.put("phone", documentSnapshot.getString("phone"));
                        post.put("depertment", documentSnapshot.getString("depertment"));
                        post.put("pic", documentSnapshot.getString("pic"));
                        post.put("initial", "null");

                        firebaseFirestore.collection("Teachers").document(teacherid).collection("Class")
                                .document(classid).collection("StudentsReq").document(mAuth.getCurrentUser().getUid()).set(post);


                    }
                });

            }
        });

        holder.pending.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Toast.makeText(context, "Request Pendding", Toast.LENGTH_SHORT).show();
            }
        });


    }

    @Override
    public int getItemCount() {
        return classesList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        View mView;

        TextView className,coursename;

        ImageView join,visit,unknown,pending;

        public ViewHolder(View itemView) {
            super(itemView);

            mView = itemView;
            className = (TextView)mView.findViewById(R.id.Classname);
            coursename = (TextView)mView.findViewById(R.id.coursename);
            join = (ImageView)mView.findViewById(R.id.join);
            pending = (ImageView)mView.findViewById(R.id.pending);

            visit = (ImageView)mView.findViewById(R.id.visit);

            unknown = (ImageView)mView.findViewById(R.id.unknown);

        }
    }
}
