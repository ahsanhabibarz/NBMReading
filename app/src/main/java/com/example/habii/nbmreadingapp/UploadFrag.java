package com.example.habii.nbmreadingapp;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;

import static android.app.Activity.RESULT_OK;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link UploadFrag.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link UploadFrag#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UploadFrag extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    String post_id;

    View Root;

    Button select,upload;

    EditText booksname;

    ViewGroup viewGroup;

    Uri pdfUri;

    FirebaseFirestore firebaseFirestore;

    FirebaseStorage firebaseStorage;

    private FirebaseAuth mAuth;

    ProgressDialog progressDialog;

    private OnFragmentInteractionListener mListener;

    public UploadFrag() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment UploadFrag.
     */
    // TODO: Rename and change types and number of parameters
    public static UploadFrag newInstance(String param1, String param2) {
        UploadFrag fragment = new UploadFrag();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             final Bundle savedInstanceState) {

        Root = inflater.inflate(R.layout.fragment_upload, container, false);

        Bundle bundle = getArguments();

        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        mAuth = FirebaseAuth.getInstance();


        viewGroup = container;

        post_id = bundle.getString("ClassID");

        upload = (Button)Root.findViewById(R.id.upload);
        select = (Button)Root.findViewById(R.id.select);

        booksname = (EditText)Root.findViewById(R.id.booksname);

        booksname.setCursorVisible(false);

        select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(ContextCompat.checkSelfPermission(container.getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){

                    selectFile();

                }else{

                    ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},9);

                }

            }
        });

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(pdfUri != null){

                    progressDialog = new ProgressDialog(container.getContext());
                    progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                    progressDialog.setTitle("Uploading");
                    progressDialog.setProgress(5);
                    progressDialog.show();

                    StorageReference storageReference = firebaseStorage.getReference();
                    storageReference.child("Uploads").child(mAuth.getCurrentUser().getUid()+"/"+booksname.getText().toString()).putFile(pdfUri)
                            .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                                    String url = taskSnapshot.getDownloadUrl().toString();

                                    Map<String,Object> post = new HashMap<>();
                                    post.put("id",url);
                                    post.put("name",booksname.getText().toString());


                                    firebaseFirestore.collection("Teachers").document(mAuth.getCurrentUser().getUid())
                                            .collection("Class").document(post_id).collection("Books").document().set(post).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {

                                            if(task.isSuccessful()){

                                                Toast.makeText(viewGroup.getContext(), "Uploaded", Toast.LENGTH_SHORT).show();

                                            }

                                        }
                                    });
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                        }
                    }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {

                            int progress = (int)(100*taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount());
                            progressDialog.setProgress(progress);

                            if(progress == 100){

                                progressDialog.dismiss();

                            }
                        }
                    });
                }else{

                    Toast.makeText(viewGroup.getContext(), "Null", Toast.LENGTH_SHORT).show();

                }

            }
        });


        return Root;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if(requestCode == 9 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            
            selectFile();
            
        }else{

            Toast.makeText(viewGroup.getContext(), "Please Select a File", Toast.LENGTH_SHORT).show();
        }
    }

    private void selectFile() {

        Intent intent = new Intent();
        intent.setType("application/pdf");
        intent.setAction(Intent.ACTION_GET_CONTENT);

        startActivityForResult(intent,23);

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(requestCode == 23 && resultCode==RESULT_OK && data != null){

            pdfUri = data.getData();

            Cursor returnCursor =
                    getActivity().getContentResolver().query(pdfUri, null, null, null, null);

            int nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
            int sizeIndex = returnCursor.getColumnIndex(OpenableColumns.SIZE);

            returnCursor.moveToFirst();
            booksname.setText(returnCursor.getString(nameIndex));
            //sizeView.setText(Long.toString(returnCursor.getLong(sizeIndex)));


        }else{

            Toast.makeText(viewGroup.getContext(), "Please Select a File", Toast.LENGTH_SHORT).show();

        }


    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
