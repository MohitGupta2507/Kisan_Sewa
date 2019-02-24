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
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class LoginActivity extends AppCompatActivity {

    EditText Namee,PhoneNumber;
    String namee,phonenumber,VerificationId;
    Dialog myDialog,myDialog2;
    FirebaseAuth mAuth;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth=FirebaseAuth.getInstance();
        myDialog=new Dialog(LoginActivity.this);
        myDialog.setContentView(R.layout.card_verification);
        myDialog.setCancelable(false);
        myDialog2=new Dialog(LoginActivity.this);
        myDialog2.setContentView(R.layout.signing);
        myDialog2.setCancelable(true);
        Namee=(EditText)findViewById(R.id.NameLogin);
        PhoneNumber=(EditText)findViewById(R.id.Numberlogin);
        namee=Namee.getText().toString();
        phonenumber=PhoneNumber.getText().toString();
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

    public void LoginButtonClicked(View view) {

        if((TextUtils.isEmpty(Namee.getText().toString())) || (TextUtils.isEmpty(PhoneNumber.getText().toString())))
        {
            Toast.makeText(LoginActivity.this,"Please Enter Both Fields",Toast.LENGTH_SHORT).show();

        }
        else
        {
            PhoneAuthProvider.getInstance().verifyPhoneNumber(
                    PhoneNumber.getText().toString(),
                    60,                 // Timeout duration
                    TimeUnit.SECONDS,   // Unit of timeout
                    LoginActivity.this,  // Activity (for callback binding)
                    mCallbacks);

            myDialog.show();
            Button ss=(Button)myDialog.findViewById(R.id.buttonn);
            ss.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    EditText VerificationCode;
                    VerificationCode=(EditText)myDialog.findViewById(R.id.editText5);
                    if(TextUtils.isEmpty(VerificationCode.getText().toString()))
                    {
                        Toast.makeText(LoginActivity.this,"Please Enter the Verification Code",Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        myDialog.cancel();
                        myDialog2.show();
                        PhoneAuthCredential credential=PhoneAuthProvider.getCredential(VerificationId,VerificationCode.getText().toString());
                        signInWithPhoneAuthCredentiall(credential);

                    }
                }
            });



        }
    }

    public void signInWithPhoneAuthCredentiall(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful())
                        {
                            Intent i=new Intent(LoginActivity.this,MainDashboard.class);
                            startActivity(i);
                            finish();
                        }

                     else {
                        // Sign in failed, display a message and update the UI
                        Log.w("TAG", "signInWithCredential:failure", task.getException());
                        if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                            // The verification code entered was invalid
                            Toast.makeText(LoginActivity.this,"Invalid Verification Code Entered",Toast.LENGTH_SHORT).show();
                            myDialog2.cancel();
                        }
                    }
                }
    });
}

}
