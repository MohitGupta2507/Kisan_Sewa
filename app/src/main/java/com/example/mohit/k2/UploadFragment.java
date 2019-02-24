package com.example.mohit.k2;


import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.lang.reflect.Array;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import static android.app.Activity.RESULT_OK;


/**
 * A simple {@link Fragment} subclass.
 */
public class UploadFragment extends Fragment {

    private EditText Quantity, Tehsil, MaximumPrice, MinimumPrice, Moisture, Damage;
    private Spinner State, City, SellTime, ProductName, QuantityUnit;
    private Button UploadSave;
    private ImageView ProductImage;
    private Uri uri1;
    private String uid;
    private DatabaseReference myRef, myRef3;
    private StorageReference StorageRef;
    private FirebaseAuth mAuth;
    private String Profile, Name, url, QuantityUnitEntered, SellingTime, StateEntered, CityEntered, ProductEntered;
    java.lang.String AN[];
    private int n = 0;

    public UploadFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_upload, container, false);
        ProductName = (Spinner) view.findViewById(R.id.ProductName);
        ProductName.setSelection(0,false);
        Quantity = (EditText) view.findViewById(R.id.Product_Quantity);
        MaximumPrice = (EditText) view.findViewById(R.id.Maximum_Price);
        MinimumPrice = (EditText) view.findViewById(R.id.Maximum_Price);
        Moisture = (EditText) view.findViewById(R.id.Moisture);
        Damage = (EditText) view.findViewById(R.id.Damage);
        SellTime = (Spinner) view.findViewById(R.id.Sell);
        State = (Spinner) view.findViewById(R.id.State_spinner);
        City = (Spinner) view.findViewById(R.id.City_spinner);
        ProductImage = (ImageView) view.findViewById(R.id.Product_Image);
        Tehsil = (EditText) view.findViewById(R.id.Tehsil_spinner);
        UploadSave = (Button) view.findViewById(R.id.UploadSaveButton);
        QuantityUnit = (Spinner) view.findViewById(R.id.Unit);
        State.setPrompt("State");
        City.setPrompt("City");
        SellTime.setPrompt("Selling Time(Days)");
        ProductName.setOnItemSelectedListener(Product);
        QuantityUnit.setOnItemSelectedListener(QuantityUnit1);
        State.setOnItemSelectedListener(StateItem);
        City.setOnItemSelectedListener(CityItem);
        SellTime.setOnItemSelectedListener(SellItem);
        UploadSave.setOnClickListener(SaveButton);
        ProductImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                i.setType("image/*");
                startActivityForResult(i, 1);
            }
        });
        return view;
    }


    private View.OnClickListener SaveButton = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            if (TextUtils.isEmpty(Quantity.getText().toString())
                    || TextUtils.isEmpty(MaximumPrice.getText().toString())
                    || TextUtils.isEmpty(MinimumPrice.getText().toString())
                    || TextUtils.isEmpty(Moisture.getText().toString())
                    || TextUtils.isEmpty(Damage.getText().toString())) {
                Toast.makeText(getActivity(), "Please Enter Each Field", Toast.LENGTH_SHORT).show();
            }
            if (n == 1) {
                Toast.makeText(getActivity(), "Please Select Each Field", Toast.LENGTH_SHORT).show();

            }
            if (uri1 == null) {
                Toast.makeText(getActivity(), "Please Select Product Image", Toast.LENGTH_SHORT).show();

            } else {
                final String dates, time;
                Date c = new Date();
                SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm a");
                timeFormat.setTimeZone(TimeZone.getTimeZone("GMT+5:30"));
                time = timeFormat.format(c);
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM");
                dates = dateFormat.format(c);
                mAuth = FirebaseAuth.getInstance();
                uid = mAuth.getCurrentUser().getUid().toString();
                myRef = FirebaseDatabase.getInstance().getReference("Uploads").child(ProductEntered).child(StateEntered).child(CityEntered).child(Tehsil.getText().toString().toLowerCase()).child(uid).child(dates + time);
                myRef3 = FirebaseDatabase.getInstance().getReference("Uploads2").child(ProductEntered).child(StateEntered).child(CityEntered).child(Tehsil.getText().toString()).child(dates + time);
                StorageRef = FirebaseStorage.getInstance().getReference().child("Product").child(dates+time);
                final Dialog mDialog = new Dialog(getActivity());
                mDialog.setCancelable(false);
                mDialog.setContentView(R.layout.saving);
                mDialog.show();
                StorageReference S2 = StorageRef;
                S2.putFile(uri1).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        url = taskSnapshot.getDownloadUrl().toString();
                        myRef.child("Bids").setValue(null);
                        myRef.child("Product_Image").setValue(taskSnapshot.getDownloadUrl().toString());
                        myRef.child("QuantityUnit").setValue(QuantityUnitEntered);
                        myRef.child("Quantity").setValue(Quantity.getText().toString());
                        myRef.child("MaximumPrice").setValue(MaximumPrice.getText().toString());
                        myRef.child("MinimumPrice").setValue(MinimumPrice.getText().toString());
                        myRef.child("Moisture").setValue(Moisture.getText().toString());
                        myRef.child("Uid").setValue(uid.toString());
                        myRef.child("Product").setValue(ProductEntered);
                        myRef.child("Damage").setValue(Damage.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                final DatabaseReference myRef2;
                                myRef2 = FirebaseDatabase.getInstance().getReference().child("User_Data").child(MainDashboard.type.toString()).child(uid);
                                myRef2.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        Profile = dataSnapshot.child("ProfileImage").getValue().toString();
                                        Name = dataSnapshot.child("Name").getValue().toString();
                                        myRef.child("Post").child("ProfileImage").setValue(Profile);
                                        myRef.child("Post").child("Name").setValue(Name);

                                            /*
                                            Upload2
                                             */

                                        myRef3.child("ProfileImage").setValue(Profile);
                                        myRef3.child("Name").setValue(Name);
                                        myRef3.child("Bids").setValue(null);
                                        myRef3.child("Product_Image").setValue(url);
                                        myRef3.child("uid").setValue(uid);
                                        myRef3.child("QuantityUnit").setValue(QuantityUnitEntered);
                                        myRef3.child("Quantity").setValue(Quantity.getText().toString());
                                        myRef3.child("MaximumPrice").setValue(MaximumPrice.getText().toString());
                                        myRef3.child("MinimumPrice").setValue(MinimumPrice.getText().toString());
                                        myRef3.child("Moisture").setValue(Moisture.getText().toString());
                                        myRef3.child("Damage").setValue(Damage.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {

                                                DatabaseReference myRef4 = FirebaseDatabase.getInstance().getReference().child("HomeUploads").child(dates + time);
                                                myRef4.child("Uid").setValue(FirebaseAuth.getInstance().getCurrentUser().getUid().toString());
                                                myRef4.child("ProfileImage").setValue(Profile);
                                                myRef4.child("Name").setValue(Name);
                                                myRef4.child("State").setValue(StateEntered);
                                                myRef4.child("City").setValue(CityEntered);
                                                myRef4.child("Tehsil").setValue(Tehsil.getText().toString());
                                                myRef4.child("Product").setValue(ProductEntered);
                                                myRef4.child("Product_Image").setValue(url);
                                                myRef4.child("QuantityUnit").setValue(QuantityUnitEntered);
                                                myRef4.child("Quantity").setValue(Quantity.getText().toString());
                                                myRef4.child("MaximumPrice").setValue(MaximumPrice.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        mDialog.cancel();
                                                        Toast.makeText(getActivity(), "Upload Success", Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                            }
                                        });

                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });

                            }
                        });


                    }
                });


            }

        }
    };
    private AdapterView.OnItemSelectedListener QuantityUnit1 = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            QuantityUnitEntered = adapterView.getItemAtPosition(i).toString();

        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }
    };

    private AdapterView.OnItemSelectedListener Product = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            ProductEntered = adapterView.getItemAtPosition(i).toString();

        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }


    };

    private AdapterView.OnItemSelectedListener StateItem = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            StateEntered = adapterView.getItemAtPosition(i).toString();

            if (i == 0) {
                ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(), R.array.AN, android.R.layout.simple_spinner_item);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                City.setAdapter(adapter);
            }
            if (i == 1) {
                ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(), R.array.AndhraPradesh, android.R.layout.simple_spinner_item);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                City.setAdapter(adapter);
            }
            if (i == 2) {
                ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(), R.array.ArunachalPradesh, android.R.layout.simple_spinner_item);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                City.setAdapter(adapter);
            }
            if (i == 3) {
                ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(), R.array.Assam, android.R.layout.simple_spinner_item);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                City.setAdapter(adapter);
            }
            if (i == 4) {
                ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(), R.array.Bihar, android.R.layout.simple_spinner_item);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                City.setAdapter(adapter);
            }
            if (i == 5) {
                ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(), R.array.Chandigarh, android.R.layout.simple_spinner_item);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                City.setAdapter(adapter);
            }
            if (i == 6) {
                ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(), R.array.Chhattisgarh, android.R.layout.simple_spinner_item);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                City.setAdapter(adapter);
            }
            if (i == 7) {
                ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(), R.array.DadraandNagarHaveli, android.R.layout.simple_spinner_item);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                City.setAdapter(adapter);
            }
            if (i == 8) {
                ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(), R.array.DamanandDiu, android.R.layout.simple_spinner_item);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                City.setAdapter(adapter);
            }
            if (i == 9) {
                ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(), R.array.Delhi, android.R.layout.simple_spinner_item);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                City.setAdapter(adapter);
            }
            if (i == 10) {
                ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(), R.array.Goa, android.R.layout.simple_spinner_item);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                City.setAdapter(adapter);
            }
            if (i == 11) {
                ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(), R.array.Gujrat, android.R.layout.simple_spinner_item);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                City.setAdapter(adapter);
            }
            if (i == 12) {
                ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(), R.array.Haryana, android.R.layout.simple_spinner_item);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                City.setAdapter(adapter);
            }
            if (i == 13) {
                ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(), R.array.HimachalPradesh, android.R.layout.simple_spinner_item);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                City.setAdapter(adapter);
            }
            if (i == 14) {
                ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(), R.array.Jk, android.R.layout.simple_spinner_item);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                City.setAdapter(adapter);
            }
            if (i == 15) {
                ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(), R.array.Jharkhand, android.R.layout.simple_spinner_item);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                City.setAdapter(adapter);
            }
            if (i == 16) {
                ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(), R.array.karnataka, android.R.layout.simple_spinner_item);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                City.setAdapter(adapter);
            }
            if (i == 17) {
                ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(), R.array.kerela, android.R.layout.simple_spinner_item);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                City.setAdapter(adapter);
            }
            if (i == 18) {
                ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(), R.array.Lakshadweep, android.R.layout.simple_spinner_item);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                City.setAdapter(adapter);
            }
            if (i == 19) {
                ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(), R.array.Mp, android.R.layout.simple_spinner_item);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                City.setAdapter(adapter);
            }
            if (i == 20) {
                ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(), R.array.Maharastra, android.R.layout.simple_spinner_item);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                City.setAdapter(adapter);
            }
            if (i == 21) {
                ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(), R.array.Mainipur, android.R.layout.simple_spinner_item);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                City.setAdapter(adapter);
            }
            if (i == 22) {
                ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(), R.array.Meghalaya, android.R.layout.simple_spinner_item);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                City.setAdapter(adapter);
            }
            if (i == 23) {
                ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(), R.array.Mizoram, android.R.layout.simple_spinner_item);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                City.setAdapter(adapter);
            }
            if (i == 24) {
                ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(), R.array.AN, android.R.layout.simple_spinner_item);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                City.setAdapter(adapter);
            }
            if (i == 25) {
                ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(), R.array.AN, android.R.layout.simple_spinner_item);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                City.setAdapter(adapter);

            }
            if (i == 26) {
                ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(), R.array.AN, android.R.layout.simple_spinner_item);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                City.setAdapter(adapter);

            }
            if (i == 27) {
                ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(), R.array.AN, android.R.layout.simple_spinner_item);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                City.setAdapter(adapter);

            }
            if (i == 28) {
                ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(), R.array.AN, android.R.layout.simple_spinner_item);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                City.setAdapter(adapter);

            }
            if (i == 29) {
                ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(), R.array.AN, android.R.layout.simple_spinner_item);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                City.setAdapter(adapter);

            }
            if (i == 30) {
                ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(), R.array.AN, android.R.layout.simple_spinner_item);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                City.setAdapter(adapter);

            }
            if (i == 31) {
                ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(), R.array.AN, android.R.layout.simple_spinner_item);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                City.setAdapter(adapter);

            }
            if (i == 32) {
                ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(), R.array.UP, android.R.layout.simple_spinner_item);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                City.setAdapter(adapter);

            }
            if (i == 33) {
                ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(), R.array.Uttrakhand, android.R.layout.simple_spinner_item);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                City.setAdapter(adapter);

            }
            if (i == 34) {
                ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(), R.array.AN, android.R.layout.simple_spinner_item);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                City.setAdapter(adapter);

            }


        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {
            n = 1;
        }
    };

    private AdapterView.OnItemSelectedListener CityItem = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            CityEntered = adapterView.getItemAtPosition(i).toString();

        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {
            n = 1;
        }


    };

    private String TehsilEntered;
/*    private AdapterView.OnItemSelectedListener TehsilItem = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            TehsilEntered = adapterView.getItemAtPosition(i).toString();

        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }


    };*/

    private AdapterView.OnItemSelectedListener SellItem = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            if (i == 0) {
                SellingTime = adapterView.getItemAtPosition(0).toString();
            }
            if (i == 1) {
                SellingTime = adapterView.getItemAtPosition(1).toString();
            }
            if (i == 2) {
                SellingTime = adapterView.getItemAtPosition(2).toString();
            }

        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {
            n = 1;
        }

    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {
                uri1 = data.getData();
                ProductImage.setImageURI(uri1);
            }
        }
    }
}



