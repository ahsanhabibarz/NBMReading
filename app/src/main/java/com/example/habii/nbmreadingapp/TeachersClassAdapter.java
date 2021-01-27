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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;
import java.util.Random;

public class TeachersClassAdapter extends RecyclerView.Adapter<TeachersClassAdapter.ViewHolder> {

    public List<Classes> claasNameList;

    Context context;

    public TeachersClassAdapter(List<Classes> claasNameList, Context context) {
        this.claasNameList = claasNameList;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.class_model,parent,false);


        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final TeachersClassAdapter.ViewHolder holder, int position) {

        final String postid = claasNameList.get(position).PostID;

        holder.className.setText(claasNameList.get(position).getId());
        holder.courseName.setText(claasNameList.get(position).getName());


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




        firebaseFirestore.collection("Teachers").document(mAuth.getCurrentUser().getUid())
                .collection("Class").document(postid).collection("Students").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {


                if(documentSnapshots != null){

                    if(!documentSnapshots.isEmpty()){


                        if(documentSnapshots.size()== 1){

                            holder.studentCount.setText("1 Student");

                        }else{

                            holder.studentCount.setText(String.valueOf(documentSnapshots.size())+" Students");

                        }




                    }else{

                        holder.studentCount.setText("0 Student");

                    }

                }

            }
        });


        firebaseFirestore.collection("Teachers").document(mAuth.getCurrentUser().getUid())
                .collection("Class").document(postid).collection("Books").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {


                if(documentSnapshots != null){

                    if(!documentSnapshots.isEmpty()){

                        if(documentSnapshots.size() == 1){

                            holder.bookCount.setText("1 Book");
                        }else {

                            holder.bookCount.setText(String.valueOf(documentSnapshots.size())+" Books");

                        }

                    }else{

                        holder.bookCount.setText("0 Book");

                    }

                }

            }
        });



        firebaseFirestore.collection("Teachers").document(mAuth.getCurrentUser().getUid())
                .collection("Class").document(postid).collection("StudentsReq").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {


                if(documentSnapshots != null){

                    if(!documentSnapshots.isEmpty()){

                        holder.reqcount.setVisibility(View.VISIBLE);
                        holder.reqcount.setText(String.valueOf(documentSnapshots.size()));

                    }else{

                        holder.reqcount.setVisibility(View.INVISIBLE);

                    }

                }

            }
        });








        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(context, TeachersClassActivityConFrag.class);
                intent.putExtra("postid", postid);
                context.startActivity(intent);


            }
        });

    }

    @Override
    public int getItemCount() {
        return claasNameList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        View mView;

        TextView className,courseName,studentCount,bookCount, reqcount;

        ImageView unknown;

        public ViewHolder(View itemView) {
            super(itemView);

            mView = itemView;
            className = (TextView)mView.findViewById(R.id.Classname);
            courseName = (TextView)mView.findViewById(R.id.coursename);
            studentCount = (TextView)mView.findViewById(R.id.sCount);
            bookCount = (TextView)mView.findViewById(R.id.bCount);
            reqcount = (TextView)mView.findViewById(R.id.reqCount);
            unknown = (ImageView)mView.findViewById(R.id.unknown);


        }
    }
}
