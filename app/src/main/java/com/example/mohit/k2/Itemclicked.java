package com.example.mohit.k2;

import android.app.Dialog;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class Itemclicked extends AppCompatActivity {

    private String State,City,Tehsil,Uid,Key,ProductName,CropMin,CropM;
    private TextView CropName,CropMax,FarmersName,q,qunit,M,D;
    private DatabaseReference dbref,dbref2,dbref3;
    private ImageView img;
    private Button b;
    private Dialog d;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_itemclicked);
        State=getIntent().getExtras().getString("State").toString();
        City=getIntent().getExtras().getString("City").toString();
        Tehsil=getIntent().getExtras().getString("tehsil").toString();
        Key=getIntent().getExtras().getString("key").toString();
        ProductName=getIntent().getExtras().getString("ProductName").toString();
        Uid=getIntent().getExtras().getString("Uid").toString();
        /***/

        CropName=(TextView)findViewById(R.id.CropName);
        CropMax=(TextView)findViewById(R.id.CropMax);
        FarmersName=(TextView)findViewById(R.id.FarmersName);
        q=(TextView)findViewById(R.id.q);
        qunit=(TextView)findViewById(R.id.qunit);
        M=(TextView)findViewById(R.id.M);
        D=(TextView)findViewById(R.id.D);
        dbref= FirebaseDatabase.getInstance().getReference().child("Uploads2").child(ProductName).child(State).child(City).child(Tehsil).child(Key);
       // Toast.makeText(Itemclicked.this,dbref.toString(),Toast.LENGTH_LONG).show();

        dbref.addListenerForSingleValueEvent(Val);

        dbref3=FirebaseDatabase.getInstance().getReference().child("User_Data").child(MainDashboard.type.toString()).child(FirebaseAuth.getInstance().getUid());
        img=(ImageView)findViewById(R.id.imageView);
        b=(Button)findViewById(R.id.buttonBid);
        b.setVisibility(View.INVISIBLE);
        d=new Dialog(Itemclicked.this);
        d.setContentView(R.layout.card_bid);

    }

    private ValueEventListener Val=new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            FarmersName.setText(dataSnapshot.child("Name").getValue().toString());
            q.setText(dataSnapshot.child("Quantity").getValue().toString());
            M.setText(dataSnapshot.child("Moisture").getValue().toString());
            D.setText(dataSnapshot.child("Damage").getValue().toString());
            qunit.setText(dataSnapshot.child("QuantityUnit").getValue().toString());;
            Picasso.with(Itemclicked.this).load(dataSnapshot.child("Product_Image").getValue().toString()).into(img);
            CropMax.setText(dataSnapshot.child("MaximumPrice").getValue().toString());
            CropName.setText(ProductName);
            CropM=dataSnapshot.child("MaximumPrice").getValue().toString();
            CropMin=dataSnapshot.child("MinimumPrice").getValue().toString();
            String Uid=dataSnapshot.child("uid").getValue().toString();
            if(MainDashboard.type.equals("Buyer"))
            {
                b.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };



    public void BidButtonClicked(View v)
    {

        d.show();
        final EditText e=(EditText)d.findViewById(R.id.editText66);
        Button bb=(Button)d.findViewById(R.id.buttonnn);
        bb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String Bid=e.getText().toString();
                final int Max,min,bidd;
                Max=Integer.parseInt(CropM);
                min=Integer.parseInt(CropMin);
                bidd=Integer.parseInt(Bid);
                if(bidd>Max)
                {
                    Toast.makeText(Itemclicked.this,"Value cannot be greater than "+Max,Toast.LENGTH_SHORT).show();

                }
                else if(bidd<min)
                {
                    Toast.makeText(Itemclicked.this,"Value cannot be less than "+min,Toast.LENGTH_SHORT).show();

                }
                else
                {
                    dbref3.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            final Dialog m=new Dialog(Itemclicked.this);
                            m.setContentView(R.layout.saving);
                            m.show();
                            dbref2=FirebaseDatabase.getInstance().getReference().child("Uploads").child(ProductName).child(State).child(City).child(Tehsil).child(Uid).child(Key);
                            dbref2.child("Bid").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("BuyerImage").setValue(dataSnapshot.child("ProfileImage").getValue());
                            dbref2.child("Bid").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("BuyerName").setValue(dataSnapshot.child("Name").getValue());
                            dbref2.child("Bid").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("BuyerNumber").setValue(dataSnapshot.child("Number").getValue());
                            dbref2.child("Bid").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Bid").setValue(bidd).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    m.cancel();
                                    d.cancel();
                                    Toast.makeText(Itemclicked.this,"Bid Success",Toast.LENGTH_SHORT).show();
                                }
                            });
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                                   }
            }
        });

    }
}
