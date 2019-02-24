package com.example.mohit.k2;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

public class BidShow extends AppCompatActivity {

    RecyclerView recyclerView;
    DatabaseReference r;
    ProgressBar p1;
    TextView v;
    FirebaseRecyclerAdapter<Bidd, ViewH> Fbra;
    String r2, r3, r4,crop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bid_show);
        recyclerView = (RecyclerView) findViewById(R.id.R_view33);
        v=(TextView)findViewById(R.id.tttt);
        String Date = getIntent().getExtras().get("keyPostion").toString();
        r2 = getIntent().getExtras().get("State").toString();
        r3 = getIntent().getExtras().get("D").toString();
        r4 = getIntent().getExtras().get("T").toString();
        crop=getIntent().getExtras().getString("Crop").toString();
        p1 = (ProgressBar) findViewById(R.id.progressBar4);
        p1.setVisibility(View.VISIBLE);
       // Toast.makeText(BidShow.this, Date.toString(), Toast.LENGTH_LONG).show();
        r = FirebaseDatabase.getInstance().getReference().child("Uploads").child(crop).child(r2).child(r3).child(r4).child(FirebaseAuth.getInstance().getUid()).child(Date).child("Bid");
        r.addValueEventListener(val);
        //Toast.makeText(BidShow.this, r.toString(), Toast.LENGTH_LONG).show();
        FirebaseRecyclerOptions<Bidd> options = new FirebaseRecyclerOptions.Builder<Bidd>().setQuery(r, Bidd.class).build();
        Fbra = new FirebaseRecyclerAdapter<Bidd, ViewH>(options) {
            @Override
            public int getItemCount() {
                return super.getItemCount();
            }

            @Override
            protected void onBindViewHolder(@NonNull ViewH holder, int position, @NonNull final Bidd model) {

                holder.setImage(model.getBuyerImage());
                holder.setName(model.getBuyerName());
                holder.setBid(String.valueOf(model.getBid()));
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String Number = model.getBuyerNumber().toString();
                        Intent i = new Intent(Intent.ACTION_CALL);
                        i.setData(Uri.parse("tel:" + model.getBuyerNumber()));
                        checkPermissionCall(BidShow.this);
                        if (ActivityCompat.checkSelfPermission(BidShow.this, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                            // TODO: Consider calling
                            //    ActivityCompat#requestPermissions
                            // here to request the missing permissions, and then overriding
                            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                            //                                          int[] grantResults)
                            // to handle the case where the user grants the permission. See the documentation
                            // for ActivityCompat#requestPermissions for more details.
                            return;
                        }
                        startActivity(i);
                    }
                });

            }

            @NonNull
            @Override
            public ViewH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View v;
                if(Fbra.getItemCount()==0)
                {
                    v=LayoutInflater.from((parent.getContext())).inflate(R.layout.secondhome,parent,false);
                    return new ViewH(v);
                }
                else {
                    v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_bid2, parent, false);
                    return new ViewH(v);
                }}
        };
        Fbra.startListening();
        recyclerView.setLayoutManager(new LinearLayoutManager(BidShow.this));
        recyclerView.setAdapter(Fbra);
        recyclerView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onGlobalLayout() {
                p1.setVisibility(View.GONE);
                recyclerView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });
        }

    public static boolean checkPermissionCall( final Context context)
    {
        int currentAPIVersion = Build.VERSION.SDK_INT;
        if(currentAPIVersion>=android.os.Build.VERSION_CODES.M)
        {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission. CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale((Activity)context, android.Manifest.permission.CALL_PHONE)) {
                    AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
                    alertBuilder.setCancelable(true);
                    alertBuilder.setTitle("Permission necessary");
                    alertBuilder.setMessage("Write Storage permission is necessary to write event!!!");
                    alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public static final int MY_PERMISSIONS_REQUEST_CAMERA = 1;

                        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions((Activity)context, new String[]{android.Manifest.permission.CALL_PHONE}, MY_PERMISSIONS_REQUEST_CAMERA);
                        }
                    });
                    AlertDialog alert = alertBuilder.create();
                    alert.show();
                } else {
                    ActivityCompat.requestPermissions((Activity)context, new String[]{android.Manifest.permission.CALL_PHONE}, 1);
                }
                return false;
            } else {
                return true;
            }
        } else {
            return true;
        }
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



    class ViewH extends RecyclerView.ViewHolder
    {

        View v;
        public ViewH(View itemView) {
            super(itemView);
            v=itemView;
        }

        public void setImage(String Pro)
        {
            ImageView i=(ImageView)v.findViewById(R.id.BuyerImage);
            Picasso.with(BidShow.this).load(Pro).into(i);
        }

        public void setName(String name)
        {

            TextView t=(TextView)v.findViewById(R.id.BuyName);
            t.setText(""+name);
        }

        public void setBid(String bid)
        {
            TextView b=(TextView)v.findViewById(R.id.Bid);
            b.setText(bid);
        }

    }


    public static class  Bidd {

        private long Bid;
        private String BuyerImage;
        private String BuyerName;
        private String BuyerNumber;



        public Bidd(long bid, String biuyerImage, String buyerName, String buyerNumber) {
            Bid = bid;
            BuyerImage = biuyerImage;
            BuyerName = buyerName;
            BuyerNumber = buyerNumber;
        }

        public  Bidd() {
        }

        public long getBid() {
            return Bid;
        }

        public void setBid(long bid) {
            Bid = bid;
        }

        public String getBuyerImage() {
            return BuyerImage;
        }

        public void setBuyerImage(String buyerImage) {
            BuyerImage = buyerImage;
        }

        public String getBuyerName() {
            return BuyerName;
        }

        public void setBuyerName(String buyerName) {
            BuyerName = buyerName;
        }

        public String getBuyerNumber() {
            return BuyerNumber;
        }

        public void setBuyerNumber(String buyerNumber) {
            BuyerNumber = buyerNumber;
        }
    }
}
