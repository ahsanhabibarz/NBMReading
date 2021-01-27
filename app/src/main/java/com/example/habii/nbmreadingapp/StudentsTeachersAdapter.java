package com.example.habii.nbmreadingapp;

import android.Manifest;
import android.app.Activity;
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

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class StudentsTeachersAdapter extends RecyclerView.Adapter<StudentsTeachersAdapter.ViewHolder> {

    public List<TeachersModelClass> teachersModelClassList;

    Context context;

    Activity activity;


    public StudentsTeachersAdapter(List<TeachersModelClass> teachersModelClassList, Context context, Activity activity) {
        this.teachersModelClassList = teachersModelClassList;
        this.context = context;
        this.activity = activity;
    }

    @NonNull
    @Override
    public StudentsTeachersAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.teachers_model, parent, false);


        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StudentsTeachersAdapter.ViewHolder holder, final int position) {

        final String post_id = teachersModelClassList.get(position).PostID;
        final String email = teachersModelClassList.get(position).getEmail();

        holder.name.setText(teachersModelClassList.get(position).getName());
        holder.depertment.setText(teachersModelClassList.get(position).getDepertment() + " - Depertment");
        holder.initail.setText(teachersModelClassList.get(position).getInitial());
        //holder.id.setText(teachersModelClassList.get(position).getId());
        String url = teachersModelClassList.get(position).getPic();

        RequestOptions placeholderReq = new RequestOptions();
        placeholderReq.placeholder(R.drawable.user);


        Glide.with(context).setDefaultRequestOptions(placeholderReq).load(url).into(holder.pic);

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(context, StudentsClassRoomActivity.class);
                intent.putExtra("postid", post_id);
                context.startActivity(intent);

            }
        });

        holder.email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("mailto:" + email));
                context.startActivity(intent);


            }
        });


        holder.call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent2 = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + teachersModelClassList.get(position).getPhone()));
                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling

                    ActivityCompat.requestPermissions(activity,new String[]{Manifest.permission.CALL_PHONE},19);

                    return;
                }
                context.startActivity(intent2);

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
        TextView initail,id;
        TextView depertment;
        CircleImageView pic;
        ImageView email,call;

        public ViewHolder(View itemView) {
            super(itemView);


            mView = itemView;

            name = (TextView)mView.findViewById(R.id.name);
            initail = (TextView)mView.findViewById(R.id.initial);
            call = (ImageView) mView.findViewById(R.id.call);
            depertment = (TextView)mView.findViewById(R.id.depertment);
            pic = (CircleImageView) mView.findViewById(R.id.profilepic);
            email = (ImageView)mView.findViewById(R.id.email);

        }
    }

}
