package com.example.habii.nbmreadingapp;

import android.app.ProgressDialog;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.github.barteksc.pdfviewer.PDFView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import org.apache.commons.io.IOUtils;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AllReadingActivity extends AppCompatActivity {

    PDFView pdfView;

    ProgressDialog progressDialog;

    String url,teacherid,classid,bookid;

    SimpleDateFormat format;

    Date date1,date2;

    FirebaseFirestore firebaseFirestore;

    private FirebaseAuth mAuth;

    boolean time =true;

    String type;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reading);


        Bundle extras = getIntent().getExtras();
        if(extras != null){

            url = extras.getString("booklink");
            teacherid = extras.getString("teacherid");
            classid = extras.getString("classid");
            bookid = extras.getString("bookid");
            type = extras.getString("type");
        }

        progressDialog = new ProgressDialog(this);

        progressDialog.setMessage("Loading");

        progressDialog.show();

        firebaseFirestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();


        //Toast.makeText(this, teacherid+"---------------"+classid+"---------------"+bookid, Toast.LENGTH_LONG).show();


        format = new SimpleDateFormat("hh:mm:ss aa");


        try {
            date1 = format.parse(format.format(Calendar.getInstance().getTime()));

        } catch (ParseException e) {
            e.printStackTrace();
        }








        pdfView = (PDFView)findViewById(R.id.pdfreader);

        new PDFStream().execute(url);


    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        try {
            date2 = format.parse(format.format(Calendar.getInstance().getTime()));

        } catch (ParseException e) {
            e.printStackTrace();
        }


        long mills = date2.getTime() - date1.getTime();
        final long diffSeconds = mills / 1000 % 60;
        //long diffMinutes = mills / (60 * 1000) % 60;
        //long diffHours = mills / (60 * 60 * 1000) % 24;`


        if(diffSeconds>10 && type.equals("Student")){


            firebaseFirestore.collection("Teachers").document(teacherid).collection("Class").document(classid)
                    .collection("Books").document(bookid).collection("ReadBy").document(mAuth.getCurrentUser().getUid())
                    .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                    if(task.getResult().exists() && time){

                        String stime = task.getResult().getString("time");

                        String finaltime = String.valueOf(diffSeconds + Long.parseLong(stime)) ;

                        Map<String,Object> post = new HashMap<>();
                        post.put("email",mAuth.getCurrentUser().getEmail().toString());
                        post.put("name", mAuth.getCurrentUser().getDisplayName().toString());
                        post.put("pic", mAuth.getCurrentUser().getPhotoUrl().toString());
                        post.put("time", finaltime);


                        firebaseFirestore.collection("Teachers").document(teacherid).collection("Class").document(classid)
                                .collection("Books").document(bookid).collection("ReadBy")
                                .document(mAuth.getCurrentUser().getUid()).set(post);


                        time = false;




                    }else if(!task.getResult().exists() && time){


                        Map<String,Object> post = new HashMap<>();
                        post.put("email",mAuth.getCurrentUser().getEmail().toString());
                        post.put("name", mAuth.getCurrentUser().getDisplayName().toString());
                        post.put("pic", mAuth.getCurrentUser().getPhotoUrl().toString());
                        post.put("time", String.valueOf(diffSeconds));


                        firebaseFirestore.collection("Teachers").document(teacherid).collection("Class").document(classid)
                                .collection("Books").document(bookid).collection("ReadBy")
                                .document(mAuth.getCurrentUser().getUid()).set(post);


                        time = false;

                    }


                }
            });

        }


        Toast.makeText(this, String.valueOf(diffSeconds), Toast.LENGTH_SHORT).show();

    }

    class PDFStream extends AsyncTask<String,Void,InputStream>{
        @Override
        protected InputStream doInBackground(String... strings) {
            InputStream inputStream = null;

            try {
                URL url = new URL(strings[0]);

                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();

                if(httpURLConnection.getResponseCode() == 200){

                    inputStream = new BufferedInputStream(httpURLConnection.getInputStream());

                }

            } catch (IOException e) {

                return null;
            }

            return inputStream;
        }


        @Override
        protected void onPostExecute(InputStream inputStream) {

            pdfView.fromStream(inputStream).load();
            progressDialog.dismiss();
        }
    }

}
