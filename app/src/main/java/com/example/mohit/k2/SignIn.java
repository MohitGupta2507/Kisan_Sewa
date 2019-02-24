package com.example.mohit.k2;

import android.app.Dialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.concurrent.TimeUnit;

public class SignIn extends AppCompatActivity {

    Spinner spinner;
    EditText name,number,VerificationCode;
    android.support.constraint.ConstraintLayout c;
    PhoneAuthCredential credential;
    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    String VerificationId,code,UserCode;
    private Dialog myDialog,myDialog2;
    private FirebaseAuth mAuth;
    private DatabaseReference myRef,myRef2;
    String Category,uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        c=(android.support.constraint.ConstraintLayout)findViewById(R.id.cc);
        spinner=(Spinner)findViewById(R.id.spinner);
        spinner.setOnItemSelectedListener(spinnerSelection);
        name=(EditText)findViewById(R.id.Name);
        number=(EditText)findViewById(R.id.Number22);
        myDialog=new Dialog(SignIn.this);
        myDialog.setContentView(R.layout.card_verification);
        myDialog.setCancelable(false);
        myDialog2=new Dialog(SignIn.this);
        myDialog2.setContentView(R.layout.signing);
        myDialog2.setCancelable(false);

        //  UserCode=number.getText().toString();
        mAuth=FirebaseAuth.getInstance();
        myRef= FirebaseDatabase.getInstance().getReference().child("Mobile_Number");
        myRef2=FirebaseDatabase.getInstance().getReference().child("Type");
        mCallbacks=new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {

            }

            @Override
            public void onVerificationFailed(FirebaseException e) {

            }

            @Override
            public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);
                VerificationId=s;
            }
        };
    }

    private AdapterView.OnItemSelectedListener spinnerSelection=new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Category=adapterView.getItemAtPosition(i).toString();
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }
    };

    public void SignInClicked(View view) {

        String Username=name.getText().toString().trim();
        String  Number= number.getText().toString().trim();
        if(Username.isEmpty() || Number.isEmpty())
        {
            Snackbar.make(c,"Enter Each Field",Snackbar.LENGTH_SHORT).show();
        }
        else if(Number.length()>10 || Number.length()<10)
        {

            Snackbar.make(c,"Invalid Number",Snackbar.LENGTH_SHORT).show();
        }
        else {
            UserCode = number.getText().toString();
            PhoneAuthProvider.getInstance().verifyPhoneNumber(
                    Number,               // Phone number to verify
                    60,                 // Timeout duration
                    TimeUnit.SECONDS,   // Unit of timeout
                    SignIn.this,  // Activity (for callback binding)
                    mCallbacks);        // OnVerificationStateChangedCallbacks

            myDialog.show();
            Button ss=(Button)myDialog.findViewById(R.id.buttonn);
            ss.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    EditText VerificationCode;
                    VerificationCode=(EditText)myDialog.findViewById(R.id.editText5);
                    if(TextUtils.isEmpty(VerificationCode.getText().toString()))
                    {
                        Snackbar.make(c,"Please Enter the Verification Code",Snackbar.LENGTH_SHORT).show();
                    }
                    else
                    {
                        myDialog.cancel();
                        myDialog2.show();
                        PhoneAuthCredential credential=PhoneAuthProvider.getCredential(VerificationId,VerificationCode.getText().toString());
                        signInWithPhoneAuthCredential(credential);

                    }
                }
            });
        }

    }

    public void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful())
                        {
                            uid=mAuth.getCurrentUser().getUid();
                            myRef.child(number.getText().toString()).child("Name").setValue(name.getText().toString());
                            myRef.child(number.getText().toString()).child("VerificationId").setValue(VerificationId);
                            myRef.child(number.getText().toString()).child("Code").setValue(code);
                            myRef.child(number.getText().toString()).child("Category").setValue(Category);
                            myRef2.child(uid).child("Category").setValue(Category);
                            if(Category.equals("Farmer"))
                            {
                                Intent intent=new Intent(SignIn.this,FarmerProfile.class);
                                intent.putExtra("Num", number.getText().toString());
                                startActivity(intent);
                                finish();
                            }
                            else if(Category.equals("Buyer"))
                            {
                                Intent intent=new Intent(SignIn.this,BuyerProfile.class);
                                intent.putExtra("Num", number.getText().toString());
                                startActivity(intent);
                                finish();
                            }

                        } else {
                            // Sign in failed, display a message and update the UI
                            Log.w("TAG", "signInWithCredential:failure", task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                                Snackbar.make(c,"Invalid Verification Code Entered",Snackbar.LENGTH_SHORT).show();

                            }
                        }
                    }
                });
    }

    public void Login(View view)
    {
        Intent i= new Intent(SignIn.this,LoginActivity.class);
        startActivity(i);
        finish();
    }

    @Override
    protected void onStart() {
        super.onStart();
        MainDashboard.MobileDataCheck(SignIn.this);
        MainDashboard.checkPermissionInternet(SignIn.this);
        MainDashboard.checkPermissionReadStorage(SignIn.this);
        MainDashboard.checkPermissionStorage(SignIn.this);
    }
}
