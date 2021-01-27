package com.example.habii.nbmreadingapp;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.List;

public class StudentsBookAdapter extends RecyclerView.Adapter<StudentsBookAdapter.ViewHolder> {


    public List<Classes> bookList;

    Context context;

    public StudentsBookAdapter(List<Classes> bookList, Context context) {
        this.bookList = bookList;
        this.context = context;
    }

    @NonNull
    @Override
    public StudentsBookAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.book_model,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final StudentsBookAdapter.ViewHolder holder, final int position) {

        final String teacherid = bookList.get(position).PostID;

        final String classid = bookList.get(position).PostID2;

        final String bookid = bookList.get(position).PostID3;

        final FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

        final FirebaseAuth mAuth = FirebaseAuth.getInstance();

        holder.bookname.setText(bookList.get(position).getName());

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(context, AllReadingActivity.class);
                intent.putExtra("booklink", bookList.get(position).getId());
                intent.putExtra("teacherid", teacherid);
                intent.putExtra("classid", classid);
                intent.putExtra("bookid", bookid);
                intent.putExtra("type","Student");
                context.startActivity(intent);



            }
        });


        firebaseFirestore.collection("Teachers").document(teacherid).collection("Class").document(classid)
                .collection("Books").document(bookid).collection("ReadBy").document(mAuth.getCurrentUser().getUid())
                .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(DocumentSnapshot documentSnapshot, FirebaseFirestoreException e) {

                        if(documentSnapshot.exists()){

                            String s = documentSnapshot.getString("time");
                            holder.time.setText(s+" min");

                        }else{

                            holder.time.setText("0 min");

                        }

                    }
                });






    }

    @Override
    public int getItemCount() {
        return bookList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        View mView;

        TextView bookname,time;

        public ViewHolder(View itemView) {
            super(itemView);

            mView = itemView;


            bookname = (TextView)mView.findViewById(R.id.bookname);
            time = (TextView)mView.findViewById(R.id.time);
        }
    }
}
