package com.example.mohit.k2;


import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {


    private ImageView Profile;
    private TextView Username;
    private DatabaseReference r1;
    private Button myPost;
    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v;
        v=inflater.inflate(R.layout.fragment_profile, container, false);
        Profile=(ImageView)v.findViewById(R.id.imageView2);
        Username=(TextView)v.findViewById(R.id.textView6);
        myPost=(Button)v.findViewById(R.id.button5);
        if(MainDashboard.type.equals("Buyer"))
        {
            myPost.setVisibility(View.GONE);
        }
        myPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog m=new Dialog(getContext());
                m.setContentView(R.layout.dialogpost);
                m.show();
                TextView t=(TextView)m.findViewById(R.id.textView12);
                TextView t2=(TextView)m.findViewById(R.id.textView13);
                t.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent i=new Intent(getActivity(),MyPost.class);
                        i.putExtra("Crop","Wheat");
                        startActivity(i);


                    }
                });
                t2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent i=new Intent(getActivity(),MyPost.class);
                        i.putExtra("Crop","Paddy");
                        startActivity(i);


                    }
                });

            }
        });
        r1= FirebaseDatabase.getInstance().getReference().child("User_Data").child(MainDashboard.type).child(FirebaseAuth.getInstance().getUid());
        r1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Username.setText(dataSnapshot.child("Name").getValue().toString());
                Picasso.with(getActivity()).load(dataSnapshot.child("ProfileImage").getValue().toString()).into(Profile);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return v;
    }

}
