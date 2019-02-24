package com.example.mohit.k2;

import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MyPost extends AppCompatActivity {

    private RecyclerView recyclerView;
    private DatabaseReference r, r2, r3;
    private String S, D, T,Crop;
    private ProgressBar p1;
    private TextView v;

    public static FirebaseRecyclerAdapter<RecyclerCropView, ViewHolder2> Fbra1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_post);
        Crop=getIntent().getExtras().getString("Crop").toString();
        r = FirebaseDatabase.getInstance().getReference().child("User_Data").child(MainDashboard.type).child(FirebaseAuth.getInstance().getUid());
        v=(TextView)findViewById(R.id.ttt);
        p1=(ProgressBar)findViewById(R.id.progressBar5);
        p1.setVisibility(View.VISIBLE);
    }


    private ValueEventListener rr = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {

            S = dataSnapshot.child("State").getValue().toString();
            D = dataSnapshot.child("City").getValue().toString();
            T = dataSnapshot.child("Tehsil").getValue().toString();
            //   Toast.makeText(MyPost.this,S+D+T.toString(),Toast.LENGTH_LONG).show();
            r2 = FirebaseDatabase.getInstance().getReference().child("Uploads").child(Crop).child(S).child(D).child(T).child(FirebaseAuth.getInstance().getUid());
            r2.addValueEventListener(val);
            //   Toast.makeText(MyPost.this,r2.toString(),Toast.LENGTH_LONG).show();

            //Toast.makeText(MyPost.this, r2.toString(), Toast.LENGTH_LONG).show();
            FirebaseRecyclerOptions<RecyclerCropView> options = new FirebaseRecyclerOptions.Builder<RecyclerCropView>().setQuery(r2, RecyclerCropView.class).build();
            Fbra1 = new FirebaseRecyclerAdapter<RecyclerCropView, ViewHolder2>(options) {


                @Override
                protected void onBindViewHolder(@NonNull ViewHolder2 holder, final int position, @NonNull RecyclerCropView model) {
                    holder.setProductImage(model.getProduct_Image());
                    holder.setProduct(model.getProduct());
                    holder.setMax(model.getMaximumPrice());
                    holder.setQuantityUnit(model.getQuantityUnit());
                    holder.setQuantity(model.getQuantity());
                    holder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent i=new Intent(MyPost.this,BidShow.class);
                            String p=Fbra1.getRef(position).getKey().toString();
                            i.putExtra("Crop",Crop);
                            i.putExtra("keyPostion",p);
                            i.putExtra("State",S);
                            i.putExtra("D",D);
                            i.putExtra("T",T);
                            startActivity(i);
                        }
                    });
                }

                @NonNull
                @Override
                public ViewHolder2 onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                    View v;
                    if(Fbra1.getItemCount()==0)
                    {
                        v = LayoutInflater.from(parent.getContext()).inflate(R.layout.secondhome, parent, false);
                        return new ViewHolder2(v);
                    }
                    else{
                        v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_post, parent, false);
                        return new ViewHolder2(v);
                    }
                }
            };

            recyclerView = (RecyclerView) findViewById(R.id.R_view3);
            recyclerView.setLayoutManager(new LinearLayoutManager(MyPost.this));
            Fbra1.startListening();

            recyclerView.setAdapter(Fbra1);
            recyclerView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                @Override
                public void onGlobalLayout() {
                    p1.setVisibility(View.GONE);
                    recyclerView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
            });

        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };

    @Override
    protected void onStart() {
        super.onStart();
        r.addListenerForSingleValueEvent(rr);

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        finish();
    }


    private ValueEventListener val=new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {

            if(!(dataSnapshot.exists()))
            {
                v.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
            }
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };
}
