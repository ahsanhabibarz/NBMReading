package com.example.habii.nbmreadingapp;

import com.google.firebase.firestore.Exclude;

public class PostID {


    @Exclude
    public String PostID;
    public String PostID2;
    public String PostID3;

    public <T extends  PostID> T withID(final String ID, final String ID2, final String ID3){


        this.PostID = ID;
        this.PostID2 = ID2;
        this.PostID3 = ID3;

        return (T) this;


    }
}
