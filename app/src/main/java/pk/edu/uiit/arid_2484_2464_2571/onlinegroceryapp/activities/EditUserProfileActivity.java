package pk.edu.uiit.arid_2484_2464_2571.onlinegroceryapp.activities;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnFailureListener;
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

import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import pk.edu.uiit.arid_2484_2464_2571.onlinegroceryapp.R;

public class EditUserProfileActivity extends AppCompatActivity implements LocationListener {
    //Declare Forgot Password Activity UI Views
    EditText userName, userPhone, userCountry, userState, userCity, userAddress;
    ImageButton backButton, gpsButton;
    ImageView userProfile;
    Button userUpdate;

    //Permission Constants
    private static final int LOCATION_REQUEST_CODE = 100;
    private static final int CAMERA_REQUEST_CODE = 200;
    private static final int STORAGE_REQUEST_CODE = 300;

    //Image Pick Constants
    private static final int IMAGE_PICK_GALLERY_CODE = 400;
    private static final int IMAGE_PICK_CAMERA_CODE = 500;

    //Permission Arrays
    String[] locationPermissions;
    String[] cameraPermissions;
    String[] storagePermissions;

    //Picked Image Uri
    private Uri imageUri;

    //Set Initial value of Latitude and Longitude
    double latitude = 0.0;
    double longitude = 0.0;

    //Progress Dialog
    ProgressDialog progressDialog;

    //Firebase Auth
    FirebaseAuth firebaseAuth;

    //Location Manager
    LocationManager locationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user_profile);
        ViewsInitialization();
        ViewsPerformance();
    }
    //UI Views Initialization
    public void ViewsInitialization()
    {
        backButton = findViewById(R.id.backBtn);
        gpsButton = findViewById(R.id.gpsBtn);

        userProfile = findViewById(R.id.userProfileIV);
        userName = findViewById(R.id.userNameET);
        userPhone = findViewById(R.id.userPhoneET);
        userCountry = findViewById(R.id.userCountryET);
        userState = findViewById(R.id.userStateET);
        userCity = findViewById(R.id.userCityET);
        userAddress = findViewById(R.id.userAddressET);
        userUpdate = findViewById(R.id.userUpdateBtn);

        //Initialization of Permission Arrays
        locationPermissions = new String[]{Manifest.permission.ACCESS_FINE_LOCATION};
        cameraPermissions = new String[]{Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};

        //Setup Progress Dialog
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please Wait....");
        progressDialog.setCanceledOnTouchOutside(false);

        //
        firebaseAuth = FirebaseAuth.getInstance();
        checkUser();

    }
    private void checkUser() {
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user==null)
        {
            startActivity(new Intent(getApplicationContext(),LoginActivity.class));
        }
        else
        {
            loadMyInfo();
        }
    }
    private void loadMyInfo() {
        //Load user information, and Set to the Views
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
        reference.orderByChild("uid").equalTo(firebaseAuth.getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot ds: snapshot.getChildren()){
                            String UID = ""+ds.child("uid").getValue();
                            String SELLER_NAME = ""+ds.child("Full Name").getValue();
                            String PHONE_NUMBER = ""+ds.child("Phone Number").getValue();
                            String COUNTRY_NAME = ""+ds.child("Country Name").getValue();
                            String STATE_NAME = ""+ds.child("State Name").getValue();
                            String CITY_NAME = ""+ds.child("City Name").getValue();
                            latitude = Double.parseDouble(""+ds.child("Latitude").getValue());
                            longitude = Double.parseDouble(""+ds.child("Longitude").getValue());
                            String ADDRESS = ""+ds.child("Address").getValue();
                            String EMAIL_ADDRESS = ""+ds.child("Email Address").getValue();
                            String PASSWORD = ""+ds.child("Password").getValue();
                            String CONFIRM_PASSWORD = ""+ds.child("Confirm Password").getValue();
                            String ACCOUNT_TYPE = ""+ds.child("Account Type").getValue();
                            String ONLINE = ""+ds.child("Online").getValue();
                            String PROFILE_IMAGE = ""+ds.child("Profile Image").getValue();
                            String TIME_STAMP = ""+ds.child("Time Stamp").getValue();

                            userName.setText(SELLER_NAME);
                            userPhone.setText(PHONE_NUMBER);
                            userCountry.setText(COUNTRY_NAME);
                            userState.setText(STATE_NAME);
                            userCity.setText(CITY_NAME);
                            userAddress.setText(ADDRESS);
                            try {

                            }catch (Exception exp){
                                userProfile.setImageResource(R.drawable.ic_person_gray);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }
    // UI Views Performance Actions
    public void ViewsPerformance() {
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Go to back previous activity
                onBackPressed();
            }
        });
        userProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Pick Image
                showImagePickDialog();
            }
        });
        gpsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Detect Current Location
                if (checkLocationPermission())
                {
                    // Already Allowed
                    detectLocation();
                }
                else
                {
                    // Not Allowed, Request
                    requestLocationPermission();
                }
            }
        });
        userUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Begin Update Profile
                inputData();
            }
        });
    }

    String User_Name, User_Phone, User_Country, User_State, User_City, User_Address;
    private void inputData() {
        User_Name = userName.getText().toString().trim();
        User_Phone= userPhone.getText().toString().trim();
        User_Country = userCountry.getText().toString().trim();
        User_State = userState.getText().toString().trim();
        User_City = userState.getText().toString().trim();
        User_Address = userAddress.getText().toString().trim();
        updateProfile();
    }

    private void updateProfile() {
        progressDialog.setMessage("Updating Profile....");
        progressDialog.show();

        if (imageUri==null){
            //Update Without Image
            //Setup Data to Update
            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("Full Name", "" + User_Name);
            hashMap.put("Phone Number", "" + User_Phone);
            hashMap.put("Country Name", "" + User_Country);
            hashMap.put("State Name", "" + User_State);
            hashMap.put("City Name", "" + User_City);
            hashMap.put("Latitude", "" + latitude);
            hashMap.put("Longitude", "" + longitude);
            hashMap.put("Address", "" + User_Address);
            hashMap.put("Account Type", "" + "User");
            //Update to DB
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
            reference.child(firebaseAuth.getUid()).updateChildren(hashMap)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            //Updated
                            progressDialog.dismiss();
                            Toast.makeText(EditUserProfileActivity.this, "Updated Seller Profile", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            //Failed to  Update
                            progressDialog.dismiss();
                            Toast.makeText(EditUserProfileActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        }
        else {
            //Update With Image
            /*----------UpLoad Image First----------*/
            String filePathAndName = "profile_images/" + ""+ firebaseAuth.getUid();
            //Get Storage reference
            StorageReference storageReference = FirebaseStorage.getInstance().getReference(filePathAndName);
            storageReference.putFile(imageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            //Image Upload, get url of uploaded image
                            Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                            while(!uriTask.isSuccessful());
                            Uri downloadImageUri = uriTask.getResult();

                            if(uriTask.isSuccessful())
                            {
                                //Image url received, now update DB
                                //Setup Data to Update
                                HashMap<String, Object> hashMap = new HashMap<>();
                                hashMap.put("Full Name", "" + User_Name);
                                hashMap.put("Phone Number", "" + User_Phone);
                                hashMap.put("Country Name", "" + User_Country);
                                hashMap.put("State Name", "" + User_State);
                                hashMap.put("City Name", "" + User_City);
                                hashMap.put("Latitude", "" + latitude);
                                hashMap.put("Longitude", "" + longitude);
                                hashMap.put("Address", "" + User_Address);
                                hashMap.put("Account Type", "" + "User");
                                hashMap.put("Profile Image", "" + downloadImageUri);
                                //Update to DB
                                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
                                reference.child(firebaseAuth.getUid()).updateChildren(hashMap)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                //Updated
                                                progressDialog.dismiss();
                                                Toast.makeText(EditUserProfileActivity.this, "Updated Seller Profile", Toast.LENGTH_SHORT).show();
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                //Failed to  Update
                                                progressDialog.dismiss();
                                                Toast.makeText(EditUserProfileActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        });
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            //Failed to update
                            progressDialog.dismiss();
                            Toast.makeText(EditUserProfileActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    private void showImagePickDialog() {
        //Options To Display In Dialog
        String[] Options = {"Camera", "Gallery"};

        //Dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Pick Image:").setItems(Options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Handle Item Clicks
                if (which==0)
                {
                    //Camera Clicked
                    if (checkCameraPermission()){
                        //Allowed Open Camera
                        pickFromCamera();
                    }
                    else
                    {
                        //Not Allowed, Request
                        requestCameraPermission();
                    }
                }
                else
                {
                    //Gallery Clicked
                    if(checkStoragePermission()){
                        //Allowed Open Gallery
                        pickFromGallery();
                    }
                    else
                    {
                        //Not Allowed, Request
                        requestStoragePermission();
                    }
                }
            }
        }).show();
    }
    private void requestStoragePermission() {
        ActivityCompat.requestPermissions(this, storagePermissions, STORAGE_REQUEST_CODE);
    }
    private void requestCameraPermission() {
        ActivityCompat.requestPermissions(this, cameraPermissions, CAMERA_REQUEST_CODE);
    }
    private void requestLocationPermission() {
        ActivityCompat.requestPermissions(this, locationPermissions, LOCATION_REQUEST_CODE);
    }
    private boolean checkStoragePermission() {
        boolean Result = ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)==
                (PackageManager.PERMISSION_GRANTED);
        return Result;
    }
    private boolean checkCameraPermission() {
        boolean Result1 = ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA)==
                (PackageManager.PERMISSION_GRANTED);
        boolean Result2 = ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)==
                (PackageManager.PERMISSION_GRANTED);
        return Result1 && Result2;
    }
    private boolean checkLocationPermission() {
        boolean Result = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)==
                (PackageManager.PERMISSION_GRANTED);
        return Result;
    }
    private void pickFromGallery() {
        //Intent to pick image from Gallery
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, IMAGE_PICK_GALLERY_CODE);
    }
    private void pickFromCamera() {
        ContentValues contentValues = new ContentValues();
        contentValues.put(MediaStore.Images.Media.TITLE, "Temp_Image Title");
        contentValues.put(MediaStore.Images.Media.DESCRIPTION, "Temp_Image Description");

        imageUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(intent, IMAGE_PICK_CAMERA_CODE);
    }
    private void detectLocation() {
        Toast.makeText(this, "Please Wait.....", Toast.LENGTH_SHORT).show();

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
    }
    private void findAddress() {
        //Find Country, State, City and Address
        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(this, Locale.getDefault());
        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 1);

            String Country = addresses.get(0).getCountryName();
            String State = addresses.get(0).getAdminArea();
            String City = addresses.get(0).getLocality();
            String Address = addresses.get(0).getAddressLine(0);

            //Set Addresses
            userCountry.setText(Country);
            userState.setText(State);
            userCity.setText(City);
            userAddress.setText(Address);
        }catch (Exception exp)
        {
            Toast.makeText(this, ""+exp.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        latitude = location.getLatitude();
        longitude = location.getLongitude();
        findAddress();
    }
    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }
    @Override
    public void onProviderEnabled(@NonNull String provider) {

    }
    @Override
    public void onProviderDisabled(@NonNull String provider) {
        // GPS/Location Disabled
        Toast.makeText(EditUserProfileActivity.this, "Location is Disabled", Toast.LENGTH_SHORT).show();
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case LOCATION_REQUEST_CODE:{
                if(grantResults.length>0){
                    boolean locationAccepted = grantResults[0]==PackageManager.PERMISSION_GRANTED;
                    if(locationAccepted)
                    {
                        // Permission Allowed
                        detectLocation();
                    }
                    else
                    {
                        // Permission Denied
                        Toast.makeText(EditUserProfileActivity.this, "Location Permission necessary...", Toast.LENGTH_SHORT).show();
                    }
                }
            }
            break;
            case CAMERA_REQUEST_CODE:{
                if(grantResults.length>0){
                    boolean cameraAccepted = grantResults[0]==PackageManager.PERMISSION_GRANTED;
                    boolean storageAccepted = grantResults[1]==PackageManager.PERMISSION_GRANTED;
                    if(cameraAccepted && storageAccepted)
                    {
                        // Permission Allowed
                        pickFromCamera();
                    }
                    else
                    {
                        // Permission Denied
                        Toast.makeText(EditUserProfileActivity.this, "Camera Permissions are necessary...", Toast.LENGTH_SHORT).show();
                    }
                }
            }
            break;
            case STORAGE_REQUEST_CODE:{
                if(grantResults.length>0){
                    boolean storageAccepted = grantResults[0]==PackageManager.PERMISSION_GRANTED;
                    if(storageAccepted)
                    {
                        // Permission Allowed
                        pickFromGallery();
                    }
                    else
                    {
                        // Permission Denied
                        Toast.makeText(EditUserProfileActivity.this, "Storage Permission is necessary...", Toast.LENGTH_SHORT).show();
                    }
                }
            }
            break;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        //Handle Image Pick Result
        if (resultCode== RESULT_OK)
        {
            if (requestCode == IMAGE_PICK_GALLERY_CODE)
            {
                //Picked From Gallery
                imageUri = data.getData();
                //Set to ImageView
                userProfile.setImageURI(imageUri);
            }
            else  if (requestCode == IMAGE_PICK_CAMERA_CODE)
            {
                //Picked From Camera and Set to ImageView
                userProfile.setImageURI(imageUri);
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }
}