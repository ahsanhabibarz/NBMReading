package com.example.habii.nbmreadingapp;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ReadByAdapter extends RecyclerView.Adapter<ReadByAdapter.ViewHolder> {

    public List<ReadBy> readByList;

    Context context;

    public ReadByAdapter(List<ReadBy> readByList, Context context) {
        this.readByList = readByList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.readby_model,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {

        final String post_id = readByList.get(position).PostID;
        final String classid = readByList.get(position).PostID2;
        final  String email = readByList.get(position).getEmail();

        final FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

        final FirebaseAuth mAuth = FirebaseAuth.getInstance();

        holder.name.setText(readByList.get(position).getName());
        holder.time.setText(readByList.get(position).getTime()+" min");

        String url = readByList.get(position).getPic();

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

    }

    @Override
    public int getItemCount() {

        return readByList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        View mView;

        TextView name,time;
        CircleImageView pic;
        ImageView email;


        public ViewHolder(View itemView) {
            super(itemView);

            mView = itemView;

            name = (TextView)mView.findViewById(R.id.name);
            pic = (CircleImageView) mView.findViewById(R.id.sprofilepic);
            email = (ImageView)mView.findViewById(R.id.email);
            time = (TextView)mView.findViewById(R.id.readtime);

        }
    }
}
