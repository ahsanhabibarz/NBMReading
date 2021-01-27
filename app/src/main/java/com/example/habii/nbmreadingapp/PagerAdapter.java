package com.example.habii.nbmreadingapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class PagerAdapter extends FragmentStatePagerAdapter{

    int mNoTabs;

    String post_id;

    public PagerAdapter(FragmentManager fm, int mNoTabs, String post_id) {
        super(fm);
        this.mNoTabs = mNoTabs;
        this.post_id = post_id;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position){

            case 0:

                Bundle bundle = new Bundle();
                bundle.putString("ClassID",post_id);

               BooksFrag booksFrag = new BooksFrag();
               booksFrag.setArguments(bundle);
               return booksFrag;
            case 1:

                Bundle bundle3 = new Bundle();
                bundle3.putString("ClassID",post_id);

                UploadFrag uploadFrag = new UploadFrag();
                uploadFrag.setArguments(bundle3);
                return uploadFrag;

            case 2:

                Bundle bundle2 = new Bundle();
                bundle2.putString("ClassID",post_id);

                StudentFrag studentFrag = new StudentFrag();
                studentFrag.setArguments(bundle2);
                return studentFrag;



            case 3:

                Bundle bundle4 = new Bundle();
                bundle4.putString("ClassID",post_id);

                ApprovalFrag approvalFrag = new ApprovalFrag();
                approvalFrag.setArguments(bundle4);
                return approvalFrag;

            default:
                return null;
        }



    }

    @Override
    public int getCount() {
        return mNoTabs;
    }
}
