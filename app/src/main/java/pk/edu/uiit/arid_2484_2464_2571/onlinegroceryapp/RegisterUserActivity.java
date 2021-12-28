package pk.edu.uiit.arid_2484_2464_2571.onlinegroceryapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
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
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import java.util.Locale;

public class RegisterUserActivity extends AppCompatActivity implements LocationListener {
    //Declare Forgot Password Activity UI Views
    EditText userName, userPhone, userCountry, userState, userCity, userAddress, userEmail, userPassword, userConfirmPassword;
    ImageButton backButton, gpsButton;
    TextView registerSeller;
    ImageView userProfile;
    Button userRegister;
    //Permission Constant
    private static final int LOCATION_REQUEST_CODE = 100;
    private static final int CAMERA_REQUEST_CODE = 200;
    private static final int STORAGE_REQUEST_CODE = 300;
    //Permission Constant
    private static final int IMAGE_PICK_GALLERY_CODE = 400;
    private static final int IMAGE_PICK_CAMERA_CODE = 500;

    //Permission Arrays
    private String[] locationPermissions;
    private String[] cameraPermissions;
    private String[] storagePermissions;
    //Picked Image Uri
    private Uri imageUri;
    private  double latitude, longitude;

    private LocationManager locationManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);
        ViewsInitialization();
        ViewsPerformance();
    }
    //UI Views Initialization
    public void ViewsInitialization()
    {
        backButton = findViewById(R.id.backBtn);
        gpsButton = findViewById(R.id.gpsBtn);
        userProfile = findViewById(R.id.sellerProfileIv);
        userName = findViewById(R.id.userNameET);
        userPhone = findViewById(R.id.userPhoneET);
        userCountry = findViewById(R.id.sellerCountryET);
        userState = findViewById(R.id.sellerStateET);
        userCity = findViewById(R.id.sellerCityET);
        userAddress = findViewById(R.id.sellerAddressET);
        userEmail = findViewById(R.id.sellerEmailET);
        userPassword = findViewById(R.id.sellerPasswordET);
        userConfirmPassword = findViewById(R.id.sellerConfPassET);
        userRegister = findViewById(R.id.sellerRegisterBtn);
        registerSeller = findViewById(R.id.registerSellerTV);


        locationPermissions = new String[]{Manifest.permission.ACCESS_FINE_LOCATION};
        cameraPermissions = new String[]{Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};

    }
    // UI Views Performance Actions
    public void ViewsPerformance()
    {
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
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
        userProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Pick Image
                showImagePickDialog();
            }
        });
        userRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Register User

            }
        });
        registerSeller.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Open Register Seller Activity
                startActivity(new Intent(RegisterUserActivity.this, RegisterSellerActivity.class));
            }
        });
    }
    private void showImagePickDialog() {
        //Options To Display  in Dialog
        String[] options = {"Camera","Gallery"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Pick Image").setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Handle Clicks
                if(which==0)
                {
                    //Camera Clicked
                    if(checkCameraPermission())
                    {
                        //Camera Permission allowed
                        pickFromCamera();
                    }
                    else
                    {
                        //Not allowed, request
                        requestCameraPermission();
                    }
                }
                else
                {
                    //Gallery Clicked
                    if(checkStoragePermission())
                    {
                        //Storage Permission allowed
                        pickFromGallery();
                    }
                    else
                    {
                        //Not allowed, request
                        requestStoragePermission();
                    }
                }
            }
        }).show();
    }

    private void pickFromGallery()
    {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, IMAGE_PICK_GALLERY_CODE);
    }

    private void pickFromCamera()
    {
        ContentValues contentValues = new ContentValues();
        contentValues.put(MediaStore.Images.Media.TITLE, "Temp_Image Title");
        contentValues.put(MediaStore.Images.Media.DESCRIPTION, "Temp_Image Description");

        imageUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(intent, IMAGE_PICK_CAMERA_CODE);
    }

    private void detectLocation() {
        Toast.makeText(RegisterUserActivity.this, "Please Wait....", Toast.LENGTH_LONG).show();
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0, 0, this);
    }

    private void findAddress() {
        // Find Address, Country, State and City
        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(this, Locale.getDefault());
        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 1);
            String address = addresses.get(0).getAddressLine(0); // Complete Address
            String city = addresses.get(0).getLocality();
            String state = addresses.get(0).getAdminArea();
            String country = addresses.get(0).getCountryName();

            // Set Addresses
            userAddress.setText(address);
            userCity.setText(city);
            userState.setText(state);
            userCountry.setText(country);
        }catch (Exception exp)
        {
            Toast.makeText(RegisterUserActivity.this, ""+exp.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private boolean checkLocationPermission()
    {
        boolean result = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)==(PackageManager.PERMISSION_GRANTED);
        return result;
    }
    private void requestLocationPermission()
    {
        ActivityCompat.requestPermissions(this,locationPermissions, LOCATION_REQUEST_CODE);
    }

    private boolean checkStoragePermission()
    {
        boolean result = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)==(PackageManager.PERMISSION_GRANTED);
        return result;
    }

    private void requestStoragePermission()
    {
        ActivityCompat.requestPermissions(this, storagePermissions, STORAGE_REQUEST_CODE);
    }

    private boolean checkCameraPermission()
    {
        boolean result1 = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)==(PackageManager.PERMISSION_GRANTED);
        boolean result2 = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)==(PackageManager.PERMISSION_GRANTED);

        return result1 && result2;
    }

    private void requestCameraPermission()
    {
        ActivityCompat.requestPermissions(this, cameraPermissions, CAMERA_REQUEST_CODE);
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        // Location Detected
        latitude = location.getLatitude();
        longitude = location.getLongitude();

        findAddress();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(@NonNull String provider) {
        //
    }

    @Override
    public void onProviderDisabled(@NonNull String provider) {
        // GPS/Location Disabled
        Toast.makeText(RegisterUserActivity.this, "Please Turn On Location...", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(RegisterUserActivity.this, "Location Permission necessary...", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(RegisterUserActivity.this, "Camera Permissions are necessary...", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(RegisterUserActivity.this, "Storage Permission is necessary...", Toast.LENGTH_SHORT).show();
                    }
                }
            }
            break;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode==RESULT_OK)
        {
            if (requestCode == IMAGE_PICK_GALLERY_CODE)
            {
                //Get Picked Image
                imageUri = data.getData();
                //Set to ImageView
                userProfile.setImageURI(imageUri);
            }
            else  if (requestCode == IMAGE_PICK_CAMERA_CODE)
            {
                //Set to ImageView
                userProfile.setImageURI(imageUri);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}