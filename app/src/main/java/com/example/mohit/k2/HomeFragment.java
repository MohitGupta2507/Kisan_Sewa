package com.example.mohit.k2;


import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment  {

    RecyclerView rView7;
    DatabaseReference r;
    ProgressBar p1;
    TextView v22;

     FirebaseRecyclerAdapter<RecyclerCropView,ViewHolder> Fbra = null;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_home, container, false);
        rView7 = (RecyclerView) v.findViewById(R.id.R_view7);
        v22=(TextView)v.findViewById(R.id.t);
        p1=(ProgressBar)v.findViewById(R.id.progressBar22);
        r = FirebaseDatabase.getInstance().getReference().child("HomeUploads");
        r.addValueEventListener(val);
        FirebaseRecyclerOptions<RecyclerCropView> options = new FirebaseRecyclerOptions.Builder<RecyclerCropView>().setQuery(r, RecyclerCropView.class).build();
        Fbra = new FirebaseRecyclerAdapter<RecyclerCropView, ViewHolder>(options) {

            @Override
            public int getItemCount() {
                return super.getItemCount();
            }

            @NonNull
            @Override
            public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View v;

                    v=LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclercardhome,parent,false);
                    return new ViewHolder(v);
                }

            @Override
            protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull final RecyclerCropView model) {

                holder.setProfile(model.getProfileImage());
                holder.setProductImage(model.getProduct_Image());
                holder.setQuantity(model.getQuantity());
                holder.setQuantityUnit(model.getQuantityUnit());
                holder.setName(model.getName());
                holder.setMax(model.getMaximumPrice());

                final String x=Fbra.getRef(position).getKey().toString();
                holder.setDate(x);

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getActivity(), Itemclicked.class);
                        intent.putExtra("Uid",model.getUid());
                        intent.putExtra("ProductName", model.getProduct());
                        intent.putExtra("State", model.getState());
                        intent.putExtra("City", model.getCity());
                        intent.putExtra("tehsil", model.getTehsil());
                        intent.putExtra("key", x);
                        startActivity(intent);

                    }
                });
            }
        };

            v22.setVisibility(View.GONE);
            Fbra.startListening();
            ///rView7.setHasFixedSize(true);
            rView7.setLayoutManager(new GridLayoutManager(getContext(),2));
            rView7.setAdapter(Fbra);
            rView7.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                @Override
                public void onGlobalLayout() {
                    p1.setVisibility(View.INVISIBLE);
                    rView7.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
            });



        return v;
    }

    private ValueEventListener val=new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {

            if(!(dataSnapshot.exists()))
            {
                v22.setVisibility(View.VISIBLE);
                rView7.setVisibility(View.GONE);
            }
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };


    @Override
    public void onStart() {
        super.onStart();
        Fbra.startListening();
    }

    @Override
    public void onResume() {
        super.onResume();
        Fbra.startListening();
    }


}