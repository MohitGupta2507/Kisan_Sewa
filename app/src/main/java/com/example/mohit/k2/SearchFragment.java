package com.example.mohit.k2;


import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


/**
 * A simple {@link Fragment} subclass.
 */
public class SearchFragment extends Fragment {


    Toolbar tt;
    RecyclerView rView;
    Button b;
    TextView v2;
    ProgressBar p1;
    private Spinner State, City, Product;
    private String StateEntered, CityEntered, TehsilEntered, ProductEntered;
    private EditText Tehsil;
    DatabaseReference r2;
    public static FirebaseRecyclerAdapter<RecyclerCropView, ViewHolder> FBRA;

    public SearchFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_search, container, false);

        v2 = (TextView) v.findViewById(R.id.tt);
        p1 = (ProgressBar) v.findViewById(R.id.progressBar3);
        Product = (Spinner) v.findViewById(R.id.P_Name);
        Product.setSelection(0, false);
        Tehsil = (EditText) v.findViewById(R.id.TehsilSearch);
        State = (Spinner) v.findViewById(R.id.StateSearch);
        City = (Spinner) v.findViewById(R.id.DistrictSearch);
        State.setOnItemSelectedListener(ss);
        Product.setOnItemSelectedListener(p);
        City.setOnItemSelectedListener(cc);
        rView = (RecyclerView) v.findViewById(R.id.R_view);
        b = (Button) v.findViewById(R.id.button4);
        b.setOnClickListener(bclick);
        return v;
    }


    private View.OnClickListener bclick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            final DatabaseReference mRef;
            TehsilEntered = Tehsil.getText().toString();
           // Toast.makeText(getContext(),ProductEntered+"\n"+StateEntered+"\n"+CityEntered+"\n"+Tehsil.getText().toString(),Toast.LENGTH_LONG).show();

            mRef = FirebaseDatabase.getInstance().getReference().child("Uploads2").child(ProductEntered).child(StateEntered).child(CityEntered).child(TehsilEntered);
            p1.setVisibility(View.VISIBLE);

            mRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (!(dataSnapshot.exists())) {
                        v2.setVisibility(View.VISIBLE);
                        rView.setVisibility(View.GONE);
                        p1.setVisibility(View.GONE);
                    } else {

                        /*mRef.addChildEventListener(new ChildEventListener() {
                            @Override
                            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                                for (DataSnapshot d:dataSnapshot.getChildren()) {
                                    String key=d.getKey();
                                    Log.d("key",key);

                                }

                            }

                            @Override
                            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                            }

                            @Override
                            public void onChildRemoved(DataSnapshot dataSnapshot) {

                            }

                            @Override
                            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });*/

                        rView.setVisibility(View.VISIBLE);
                        //Toast.makeText(getActivity(),TehsilEntered,Toast.LENGTH_LONG).show();
                        DatabaseReference mReff= FirebaseDatabase.getInstance().getReference().child("Uploads2").child(ProductEntered).child(StateEntered).child(CityEntered).child(TehsilEntered);
                        FirebaseRecyclerOptions<RecyclerCropView> options = new FirebaseRecyclerOptions.Builder<RecyclerCropView>().setQuery(mReff, RecyclerCropView.class).build();
                        FBRA = new FirebaseRecyclerAdapter<RecyclerCropView, ViewHolder>(options) {

                            @Override
                            public int getItemCount() {
                                return super.getItemCount();
                            }

                            @Override
                            protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull RecyclerCropView model) {
                                holder.setProfile(model.getProfileImage());
                                holder.setProductImage(model.getProduct_Image());
                                holder.setQuantity(model.getQuantity());
                                holder.setQuantityUnit(model.getQuantityUnit());
                                holder.setName(model.getName());
                                holder.setMax(model.getMaximumPrice());

                                final String x = FBRA.getRef(position).getKey().toString();
                                holder.setDate(x);
                                //     Toast.makeText(getActivity(),x.toString(),Toast.LENGTH_LONG).show();
                                holder.itemView.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Intent intent = new Intent(getActivity(), Itemclicked.class);
                                        intent.putExtra("ProductName", ProductEntered);
                                        intent.putExtra("State", StateEntered);
                                        intent.putExtra("City", CityEntered);
                                        intent.putExtra("tehsil", Tehsil.getText().toString());
                                        intent.putExtra("key", x);
                                        startActivity(intent);
                                    }
                                });
                            }

                            @NonNull
                            @Override
                            public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                                View v;
                                if (FBRA.getItemCount() == 0) {
                                    v = LayoutInflater.from((parent.getContext())).inflate(R.layout.secondhome, parent, false);
                                    return new ViewHolder(v);
                                } else {
                                    v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclercard, parent, false);
                                    return new ViewHolder(v);
                                }
                            }


                        };
                        FBRA.notifyDataSetChanged();
                        FBRA.startListening();
                        v2.setVisibility(View.GONE);
                        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
                        llm.setOrientation(LinearLayoutManager.VERTICAL);
                        rView.setLayoutManager(llm);
                        rView.setHasFixedSize(true);
                        rView.setAdapter(FBRA);
                        rView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                            @Override
                            public void onGlobalLayout() {
                                p1.setVisibility(View.GONE);

                                rView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                            }
                        });

                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }

    };


    private AdapterView.OnItemSelectedListener cc = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            CityEntered = adapterView.getItemAtPosition(i).toString();
            Log.d("City",CityEntered.toString());
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }
    };

    private AdapterView.OnItemSelectedListener p = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            if(i==0)
            {
                ProductEntered="Product Type";
            }
            else {
                ProductEntered = adapterView.getItemAtPosition(i).toString();
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }
    };

    private AdapterView.OnItemSelectedListener ss = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

            StateEntered = adapterView.getItemAtPosition(i).toString();
            Log.d("State",StateEntered.toString());
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

        }
    };


}


