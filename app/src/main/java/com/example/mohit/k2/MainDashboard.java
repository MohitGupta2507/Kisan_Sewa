package com.example.mohit.k2;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class MainDashboard extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private String uidd;
    public static  String type,t;
    private DatabaseReference mReff;
    private android.support.v7.widget.Toolbar Toolbar;
    private android.support.design.widget.BottomNavigationView bnm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_dashboard);

        Toolbar=(android.support.v7.widget.Toolbar)findViewById(R.id.toolbar);
        Toolbar.setTitle("KisanSewa");
        setSupportActionBar(Toolbar);
        bnm=(android.support.design.widget.BottomNavigationView)findViewById(R.id.bottom);
        BottomNavigationViewHelper.removeShiftMode(bnm);
        getSupportFragmentManager().beginTransaction().replace(R.id.container1,new HomeFragment()).commit();
        mAuth=FirebaseAuth.getInstance();

    }






    private BottomNavigationView.OnNavigationItemSelectedListener navListner=new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            Fragment selectedFragment=null;
            //FrameLayout frameLayout=(FrameLayout)findViewById(R.id.container1);
            switch(item.getItemId())
            {
                case R.id.home:
                    selectedFragment=new HomeFragment();
                    break;
                case R.id.Search:
                    selectedFragment=new SearchFragment();
                    break;
                case R.id.Upload:
                    if(type.equals("Buyer"))
                    {
                        selectedFragment=new upload2();
                        break;
                    }
                    else {
                        selectedFragment = new UploadFragment();
                        break;
                    }
                case R.id.Profile:
                    selectedFragment=new ProfileFragment();
                    break;

            }
            getSupportFragmentManager().beginTransaction().replace(R.id.container1,selectedFragment).commit();
            return true;
        }

    };

    @Override
    public boolean onCreateOptionsMenu( Menu menu ) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_dashboard, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected( MenuItem item ) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

        if(id == R.id.logout)
        {
            mAuth.signOut();
            Intent i=new Intent(MainDashboard.this,SignIn.class);
            startActivity(i);
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    static class BottomNavigationViewHelper {

        @SuppressLint("RestrictedApi")
        static void removeShiftMode(BottomNavigationView view) {
            BottomNavigationMenuView menuView = (BottomNavigationMenuView) view.getChildAt(0);
            try {
                Field shiftingMode = menuView.getClass().getDeclaredField("mShiftingMode");
                shiftingMode.setAccessible(true);
                shiftingMode.setBoolean(menuView, false);
                shiftingMode.setAccessible(false);
                for (int i = 0; i < menuView.getChildCount(); i++) {
                    BottomNavigationItemView item = (BottomNavigationItemView) menuView.getChildAt(i);
                    item.setShiftingMode(false);
                    // set once again checked value, so view will be updated
                    item.setChecked(item.getItemData().isChecked());
                }
            } catch (NoSuchFieldException e) {
                Log.e("ERROR NO SUCH FIELD", "Unable to get shift mode field");
            } catch (IllegalAccessException e) {
                Log.e("ERROR ILLEGAL ALG", "Unable to change value of shift mode");
            }
        }
    }




    @Override
    protected void onStart() {
        super.onStart();
        MobileDataCheck(MainDashboard.this);
        checkPermissionInternet(MainDashboard.this);
        checkPermissionStorage(MainDashboard.this);
        checkPermissionReadStorage(MainDashboard.this);
        if(mAuth.getCurrentUser()==null)
        {
            Intent i=new Intent(MainDashboard.this,SignIn.class);
            startActivity(i);
            finish();
        }
        else
        {
            uidd=mAuth.getCurrentUser().getUid().toString();
            mReff=FirebaseDatabase.getInstance().getReference("Type").child(uidd).child("Category");
            mReff.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    type=dataSnapshot.getValue().toString();
                    t=type;
                   // Toast.makeText(MainDashboard.this,type,Toast.LENGTH_LONG).show();
                    bnm.setOnNavigationItemSelectedListener(navListner);

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        MainDashboard.this.finish();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        type=t;
    }

    @Override
    protected void onResume() {
        super.onResume();
        type=t;
    }

    public static void MobileDataCheck(Context c)
    {
        boolean mobileDataEnabled = false; // Assume disabled
        ConnectivityManager cm = (ConnectivityManager) c.getSystemService(Context.CONNECTIVITY_SERVICE);
        try {
            Class cmClass = Class.forName(cm.getClass().getName());
            Method method = cmClass.getDeclaredMethod("getMobileDataEnabled");
            method.setAccessible(true); // Make the method callable
            // get the setting for "mobile data"
            mobileDataEnabled = (Boolean)method.invoke(cm);
        } catch (Exception e) {
            // Some problem accessible private API
            // TODO do whatever error handling you want here
        }

        if(mobileDataEnabled==false)
        {
            final Dialog dialog=new Dialog(c);
            dialog.setContentView(R.layout.internet);
            dialog.setCancelable(false);
            Button b=(Button)dialog.findViewById(R.id.buttonn);
            b.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick( View v ) {
                    dialog.cancel();
                }
            });
            dialog.show();
        }
    }

    public static boolean checkPermissionStorage( final Context context)
    {
        int currentAPIVersion = Build.VERSION.SDK_INT;
        if(currentAPIVersion>=android.os.Build.VERSION_CODES.M)
        {
            if (ContextCompat.checkSelfPermission(context, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale((Activity)context, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
                    alertBuilder.setCancelable(true);
                    alertBuilder.setTitle("Permission necessary");
                    alertBuilder.setMessage("Write Storage permission is necessary to write event!!!");
                    alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public static final int MY_PERMISSIONS_REQUEST_CAMERA = 1;

                        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions((Activity)context, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_CAMERA);
                        }
                    });
                    AlertDialog alert = alertBuilder.create();
                    alert.show();
                } else {
                    ActivityCompat.requestPermissions((Activity)context, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                }
                return false;
            } else {
                return true;
            }
        } else {
            return true;
        }
    }


    public static boolean checkPermissionReadStorage( final Context context)
    {
        int currentAPIVersion = Build.VERSION.SDK_INT;
        if(currentAPIVersion>=android.os.Build.VERSION_CODES.M)
        {
            if (ContextCompat.checkSelfPermission(context, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, android.Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
                    alertBuilder.setCancelable(true);
                    alertBuilder.setTitle("Permission necessary");
                    alertBuilder.setMessage("Read Storage permission is necessary to write event!!!");
                    alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public static final int MY_PERMISSIONS_REQUEST_CAMERA = 1;

                        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions((Activity)context, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_CAMERA);
                        }
                    });
                    AlertDialog alert = alertBuilder.create();
                    alert.show();
                } else {
                    ActivityCompat.requestPermissions((Activity)context, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                }
                return false;
            } else {
                return true;
            }
        } else {
            return true;
        }
    }


    public static boolean checkPermissionInternet( final Context context)
    {
        int currentAPIVersion = Build.VERSION.SDK_INT;
        if(currentAPIVersion>=android.os.Build.VERSION_CODES.M)
        {
            if (ContextCompat.checkSelfPermission(context, android.Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED) {

                if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, android.Manifest.permission.INTERNET)) {
                    AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
                    alertBuilder.setCancelable(true);
                    alertBuilder.setTitle("Permission necessary");
                    alertBuilder.setMessage("Internet permission is necessary to write event!!!");
                    alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public static final int MY_PERMISSIONS_REQUEST_CAMERA = 1;

                        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                        public void onClick( DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions((Activity)context, new String[]{android.Manifest.permission.INTERNET}, MY_PERMISSIONS_REQUEST_CAMERA);
                        }
                    });
                    AlertDialog alert = alertBuilder.create();
                    alert.show();
                } else {
                    ActivityCompat.requestPermissions((Activity)context, new String[]{android.Manifest.permission.INTERNET}, 1);
                }
                return false;
            } else {
                return true;
            }
        } else {
            return true;
        }
    }

}

