package com.example.habii.nbmreadingapp;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class TeachersBooksAdapter extends RecyclerView.Adapter<TeachersBooksAdapter.ViewHolder> {


    public List<Classes> bookList;

    Context context;

    public TeachersBooksAdapter(List<Classes> bookList, Context context) {
        this.bookList = bookList;
        this.context = context;
    }

    @NonNull
    @Override
    public TeachersBooksAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.teachers_book_model,parent,false);

        return new TeachersBooksAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TeachersBooksAdapter.ViewHolder holder, final int position) {

        final String bookid = bookList.get(position).PostID;
        final String classid = bookList.get(position).PostID2;

        holder.bookname.setText(bookList.get(position).getName());

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(context, AllReadingActivity.class);
                intent.putExtra("booklink", bookList.get(position).getId());
                intent.putExtra("type","Teacher");
                context.startActivity(intent);
            }
        });



        holder.readbybt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(context, ReadByActivity.class);
                intent.putExtra("bookid", bookid);
                intent.putExtra("classid",classid);
                context.startActivity(intent);
            }
        });




    }

    @Override
    public int getItemCount() {
        return bookList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        View mView;

        TextView bookname;

        ImageView readbybt;

        public ViewHolder(View itemView) {
            super(itemView);

            mView = itemView;


            bookname = (TextView)mView.findViewById(R.id.bookname);

            readbybt = (ImageView) mView.findViewById(R.id.readbybt);

        }
    }
}
