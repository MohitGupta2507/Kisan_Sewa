package com.example.mohit.k2;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class BuyerProfile extends AppCompatActivity {

    private DatabaseReference myRef2;
    private String Uid;
    private Uri uri;
    private EditText eName,eState,eCity,eAdhaar;
    private String Name,State,City,Adhaar;
    private ImageView ProfileImage;
    private StorageReference mRef;


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buyer_profile);
        Window window = this.getWindow();
        // clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

// add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

// finally change the color
        window.setStatusBarColor(ContextCompat.getColor(BuyerProfile.this,R.color.colorPrimaryy));

        Uid= FirebaseAuth.getInstance().getCurrentUser().getUid().toString();
        myRef2= FirebaseDatabase.getInstance().getReference().child("User_Data").child("Buyer").child(Uid);
        eName=(EditText)findViewById(R.id.NameBuyer);
        eCity=(EditText)findViewById(R.id.CityBuyer);
        eState=(EditText)findViewById(R.id.StateBuyer);
        eAdhaar=(EditText)findViewById(R.id.AdhaarBuyer);
        ProfileImage=(ImageView)findViewById(R.id.ProfileImageBuyer);
        mRef= FirebaseStorage.getInstance().getReference().child("Profile_Images").child("Buyer");
    }


    public void BuyerSave( View view ) {

        android.support.constraint.ConstraintLayout c = (android.support.constraint.ConstraintLayout) findViewById(R.id.Buyer);
        Name=eName.getText().toString();
        City=eCity.getText().toString();
        State=eState.getText().toString();
        Adhaar=eAdhaar.getText().toString();
        if (uri == null) {
            Snackbar.make(c, "Enter Profile Image", Snackbar.LENGTH_SHORT).show();
        }
        else if (Name.isEmpty() || City.isEmpty() || State.isEmpty() || Adhaar.isEmpty()) {
            Snackbar.make(c, "Enter All Details", Snackbar.LENGTH_SHORT).show();
        } else {
            Button b=(Button)findViewById(R.id.button3);
            b.setEnabled(false);
            Dialog mDialog=new Dialog(BuyerProfile.this);
            mDialog.setCancelable(false);
            mDialog.setContentView(R.layout.saving);
            mDialog.show();
            StorageReference mRef2 = mRef.child(Uid).child(uri.getLastPathSegment());
            mRef2.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess( UploadTask.TaskSnapshot taskSnapshot ) {
                    myRef2.child("ProfileImage").setValue(taskSnapshot.getDownloadUrl().toString());
                    myRef2.child("Name").setValue(Name);
                    myRef2.child("City").setValue(City);
                    myRef2.child("State").setValue(State);
                    myRef2.child("Adhaar").setValue(Adhaar);
                    myRef2.child("Number").setValue(getIntent().getExtras().get("Num").toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete( @NonNull Task<Void> task ) {
                            Intent i=new Intent(BuyerProfile.this,MainDashboard.class);
                            i.putExtra("type","Buyer");
                            startActivity(i);
                            finish();
                        }
                    });

                }
            });
        }
    }
    public void BuyerProfile( View view ) {
        Intent i=new Intent(Intent.ACTION_GET_CONTENT);
        i.setType("image/*");
        startActivityForResult(i,1);
    }

    @Override
    protected void onActivityResult( int requestCode, int resultCode, Intent data ) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK)
        {
            if(requestCode==1)
            {
                uri=data.getData();
                ProfileImage.setImageURI(uri);
            }
        }
    }
}

