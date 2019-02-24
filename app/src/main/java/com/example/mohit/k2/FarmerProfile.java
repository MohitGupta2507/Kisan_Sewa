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
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class FarmerProfile extends AppCompatActivity {

    private DatabaseReference myRef2;
    private String Uid;
    private Uri uri;
    private Spinner Cityy,Statee;
    private EditText eName,eState,eCity,eTehsil,eVillage;
    private String Name,State,City,Tehsil,Village;
    private ImageView ProfileImage;
    private StorageReference mRef;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_farmer_profile);
        Window window = this.getWindow();
        // clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

// add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

// finally change the color
        window.setStatusBarColor(ContextCompat.getColor(FarmerProfile.this,R.color.colorPrimaryy));

        Uid= FirebaseAuth.getInstance().getCurrentUser().getUid().toString();
        myRef2= FirebaseDatabase.getInstance().getReference().child("User_Data").child("Farmer").child(Uid);
        eName=(EditText)findViewById(R.id.NameFarmer);
        Cityy=(Spinner)findViewById(R.id.CityFarmer);
        Statee=(Spinner)findViewById(R.id.StateFarmer);
        eTehsil=(EditText)findViewById(R.id.TehsilFarmer);
        eVillage=(EditText)findViewById(R.id.VillageFarmer);
        ProfileImage=(ImageView)findViewById(R.id.ProfileImageFarmer);
        Statee.setOnItemSelectedListener(StateItem);
        Cityy.setOnItemSelectedListener(CityItem);
        mRef= FirebaseStorage.getInstance().getReference().child("Profile_Images").child("Farmer");

    }

    private AdapterView.OnItemSelectedListener StateItem = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            State= adapterView.getItemAtPosition(i).toString();

            if (i == 0) {
                ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(FarmerProfile.this, R.array.AN, android.R.layout.simple_spinner_item);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                Cityy.setAdapter(adapter);
            }
            if (i == 1) {
                ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(FarmerProfile.this, R.array.AndhraPradesh, android.R.layout.simple_spinner_item);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                Cityy.setAdapter(adapter);
            }
            if (i == 2) {
                ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(FarmerProfile.this, R.array.ArunachalPradesh, android.R.layout.simple_spinner_item);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                Cityy.setAdapter(adapter);
            }
            if (i == 3) {
                ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(FarmerProfile.this, R.array.Assam, android.R.layout.simple_spinner_item);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                Cityy.setAdapter(adapter);
            }
            if (i == 4) {
                ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(FarmerProfile.this, R.array.Bihar, android.R.layout.simple_spinner_item);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                Cityy.setAdapter(adapter);
            }
            if (i == 5) {
                ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(FarmerProfile.this, R.array.Chandigarh, android.R.layout.simple_spinner_item);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                Cityy.setAdapter(adapter);
            }
            if (i == 6) {
                ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(FarmerProfile.this, R.array.Chhattisgarh, android.R.layout.simple_spinner_item);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                Cityy.setAdapter(adapter);
            }
            if (i == 7) {
                ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(FarmerProfile.this, R.array.DadraandNagarHaveli, android.R.layout.simple_spinner_item);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                Cityy.setAdapter(adapter);
            }
            if (i == 8) {
                ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(FarmerProfile.this, R.array.DamanandDiu, android.R.layout.simple_spinner_item);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                Cityy.setAdapter(adapter);
            }
            if (i == 9) {
                ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(FarmerProfile.this, R.array.Delhi, android.R.layout.simple_spinner_item);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                Cityy.setAdapter(adapter);
            }
            if (i == 10) {
                ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(FarmerProfile.this, R.array.Goa, android.R.layout.simple_spinner_item);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                Cityy.setAdapter(adapter);
            }
            if (i == 11) {
                ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(FarmerProfile.this, R.array.Gujrat, android.R.layout.simple_spinner_item);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                Cityy.setAdapter(adapter);
            }
            if (i == 12) {
                ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(FarmerProfile.this, R.array.Haryana, android.R.layout.simple_spinner_item);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                Cityy.setAdapter(adapter);
            }
            if (i == 13) {
                ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(FarmerProfile.this, R.array.HimachalPradesh, android.R.layout.simple_spinner_item);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                Cityy.setAdapter(adapter);
            }
            if (i == 14) {
                ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(FarmerProfile.this, R.array.Jk, android.R.layout.simple_spinner_item);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                Cityy.setAdapter(adapter);
            }
            if (i == 15) {
                ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(FarmerProfile.this, R.array.Jharkhand, android.R.layout.simple_spinner_item);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                Cityy.setAdapter(adapter);
            }
            if (i == 16) {
                ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(FarmerProfile.this, R.array.karnataka, android.R.layout.simple_spinner_item);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                Cityy.setAdapter(adapter);
            }
            if (i == 17) {
                ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(FarmerProfile.this, R.array.kerela, android.R.layout.simple_spinner_item);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                Cityy.setAdapter(adapter);
            }
            if (i == 18) {
                ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(FarmerProfile.this, R.array.Lakshadweep, android.R.layout.simple_spinner_item);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                Cityy.setAdapter(adapter);
            }
            if (i == 19) {
                ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(FarmerProfile.this, R.array.Mp, android.R.layout.simple_spinner_item);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                Cityy.setAdapter(adapter);
            }
            if (i == 20) {
                ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(FarmerProfile.this, R.array.Maharastra, android.R.layout.simple_spinner_item);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                Cityy.setAdapter(adapter);
            }
            if (i == 21) {
                ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(FarmerProfile.this, R.array.Mainipur, android.R.layout.simple_spinner_item);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                Cityy.setAdapter(adapter);
            }
            if (i == 22) {
                ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(FarmerProfile.this, R.array.Meghalaya, android.R.layout.simple_spinner_item);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                Cityy.setAdapter(adapter);
            }
            if (i == 23) {
                ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(FarmerProfile.this, R.array.Mizoram, android.R.layout.simple_spinner_item);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                Cityy.setAdapter(adapter);
            }
            if (i == 24) {
                ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(FarmerProfile.this, R.array.AN, android.R.layout.simple_spinner_item);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                Cityy.setAdapter(adapter);
            }
            if (i == 25) {
                ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(FarmerProfile.this, R.array.AN, android.R.layout.simple_spinner_item);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                Cityy.setAdapter(adapter);

            }
            if (i == 26) {
                ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(FarmerProfile.this, R.array.AN, android.R.layout.simple_spinner_item);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                Cityy.setAdapter(adapter);

            }
            if (i == 27) {
                ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(FarmerProfile.this, R.array.AN, android.R.layout.simple_spinner_item);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                Cityy.setAdapter(adapter);

            }
            if (i == 28) {
                ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(FarmerProfile.this, R.array.AN, android.R.layout.simple_spinner_item);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                Cityy.setAdapter(adapter);

            }
            if (i == 29) {
                ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(FarmerProfile.this, R.array.AN, android.R.layout.simple_spinner_item);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                Cityy.setAdapter(adapter);

            }
            if (i == 30) {
                ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(FarmerProfile.this, R.array.AN, android.R.layout.simple_spinner_item);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                Cityy.setAdapter(adapter);

            }
            if (i == 31) {
                ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(FarmerProfile.this, R.array.AN, android.R.layout.simple_spinner_item);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                Cityy.setAdapter(adapter);

            }
            if (i == 32) {
                ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(FarmerProfile.this, R.array.UP, android.R.layout.simple_spinner_item);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                Cityy.setAdapter(adapter);

            }
            if (i == 33) {
                ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(FarmerProfile.this, R.array.Uttrakhand, android.R.layout.simple_spinner_item);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                Cityy.setAdapter(adapter);

            }
            if (i == 34) {
                ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(FarmerProfile.this, R.array.AN, android.R.layout.simple_spinner_item);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                Cityy.setAdapter(adapter);

            }


        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }
    };

    private AdapterView.OnItemSelectedListener CityItem = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            City = adapterView.getItemAtPosition(i).toString();

        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }


    };







    public void ProfileButtonClicked(View v)
    {
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

    public void SaveButtonClicked( View view)
    {
        android.support.constraint.ConstraintLayout c=(android.support.constraint.ConstraintLayout)findViewById(R.id.FarmerLayout);
        Name=eName.getText().toString().trim();
//        City=eCity.getText().toString().trim();
        Tehsil=eTehsil.getText().toString().toLowerCase();
        Village=eVillage.getText().toString().trim();
        if(uri==null)
        {
            Snackbar.make(c,"Enter Profile Image",Snackbar.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(Name) || TextUtils.isEmpty(Village))
        {
            Snackbar.make(c,"Enter All Details",Snackbar.LENGTH_SHORT).show();
        }
        else
        {
            Button b=(Button)findViewById(R.id.SaveButton);
            b.setEnabled(false);
            Dialog mDialog=new Dialog(FarmerProfile.this);
            mDialog.setCancelable(false);
            mDialog.setContentView(R.layout.saving);
            mDialog.show();
            StorageReference mRef2=mRef.child(Uid).child(uri.getLastPathSegment());
            mRef2.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess( UploadTask.TaskSnapshot taskSnapshot ) {
                    myRef2.child("ProfileImage").setValue(taskSnapshot.getDownloadUrl().toString());
                    myRef2.child("Name").setValue(Name);
                    myRef2.child("City").setValue(City);
                    myRef2.child("Tehsil").setValue(Tehsil);
                    myRef2.child("State").setValue(State);
                    myRef2.child("Village").setValue(Village);
                    myRef2.child("Number").setValue(getIntent().getExtras().get("Num").toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete( @NonNull Task<Void> task ) {
                            Intent i=new Intent(FarmerProfile.this,MainDashboard.class);
                            i.putExtra("type","Farmer");
                            startActivity(i);
                            finish();
                        }
                    });

                }
            });
        }
    }


}
