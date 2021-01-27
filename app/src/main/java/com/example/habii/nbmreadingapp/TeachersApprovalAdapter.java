package com.example.habii.nbmreadingapp;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class TeachersApprovalAdapter extends RecyclerView.Adapter<TeachersApprovalAdapter.ViewHolder> {


    public List<TeachersModelClass> teachersModelClassList;

    Context context;


    public TeachersApprovalAdapter(List<TeachersModelClass> teachersModelClassList, Context context) {
        this.teachersModelClassList = teachersModelClassList;
        this.context = context;
    }



    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.teachers_req_students_model,parent,false);


        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {

        final String post_id = teachersModelClassList.get(position).PostID;
        final String classid = teachersModelClassList.get(position).PostID2;
        final  String email = teachersModelClassList.get(position).getEmail();

        final FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

        final FirebaseAuth mAuth = FirebaseAuth.getInstance();

        holder.name.setText(teachersModelClassList.get(position).getName());
        holder.depertment.setText(teachersModelClassList.get(position).getDepertment()+" - Depertment");
        holder.initail.setText(teachersModelClassList.get(position).getId());
        String url = teachersModelClassList.get(position).getPic();

        RequestOptions placeholderReq = new RequestOptions();
        placeholderReq.placeholder(R.drawable.user);


        Glide.with(context).setDefaultRequestOptions(placeholderReq).load(url).into(holder.pic);

        holder.email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent (Intent.ACTION_VIEW , Uri.parse("mailto:" + email));
                context.startActivity(intent);


            }
        });


        holder.approve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                firebaseFirestore.collection("Teachers").document(mAuth.getCurrentUser().getUid())
                        .collection("Class").document(classid).collection("StudentsReq").document(post_id)
                        .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                            @Override
                            public void onEvent(DocumentSnapshot documentSnapshot, FirebaseFirestoreException e) {

                                if(documentSnapshot.exists()){




                                    Map<String,Object> post = new HashMap<>();
                                    post.put("email",documentSnapshot.getString("email"));
                                    post.put("name", documentSnapshot.getString("name"));
                                    post.put("id",documentSnapshot.getString("id"));
                                    post.put("phone", documentSnapshot.getString("phone"));
                                    post.put("depertment", documentSnapshot.getString("depertment"));
                                    post.put("pic", documentSnapshot.getString("pic"));
                                    post.put("initial", "null");

                                    documentSnapshot.getReference().delete();


                                    firebaseFirestore.collection("Teachers").document(mAuth.getCurrentUser().getUid())
                                            .collection("Class")
                                            .document(classid).collection("Students").
                                            document(post_id).set(post);

                                }


                            }
                        });
            }
        });


    }

    @Override
    public int getItemCount() {
        return teachersModelClassList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {


        View mView;

        TextView name;
        TextView initail;
        TextView depertment;
        CircleImageView pic;
        ImageView email,approve;


        public ViewHolder(View itemView) {
            super(itemView);

            mView = itemView;

            name = (TextView)mView.findViewById(R.id.name);
            initail = (TextView)mView.findViewById(R.id.initial);
            depertment = (TextView)mView.findViewById(R.id.depertment);
            pic = (CircleImageView) mView.findViewById(R.id.sprofilepic);
            email = (ImageView)mView.findViewById(R.id.email);
            approve = (ImageView)mView.findViewById(R.id.call);


        }
    }
}
