package pk.edu.uiit.arid_2484_2464_2571.onlinegroceryapp;

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
import androidx.appcompat.widget.SwitchCompat;
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

public class EditSellerProfileActivity extends AppCompatActivity implements LocationListener{
    //Declare Forgot Password Activity UI Views
    EditText sellerName, sellerShop, sellerPhone, deliveryFee, sellerCountry, sellerState, sellerCity, sellerAddress;
    ImageButton backButton, gpsButton;
    ImageView sellerProfile;
    SwitchCompat shopOpenSwitch;
    Button sellerUpdate;

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
        setContentView(R.layout.activity_edit_seller_profile);
        ViewsInitialization();
        ViewsPerformance();
    }

    //UI Views Initialization
    public void ViewsInitialization()
    {
        backButton = findViewById(R.id.backBtn);
        gpsButton = findViewById(R.id.gpsBtn);
        sellerProfile = findViewById(R.id.sellerProfileIv);
        sellerName = findViewById(R.id.sellerNameET);
        sellerShop = findViewById(R.id.shopNameET);
        sellerPhone = findViewById(R.id.sellerPhoneET);
        deliveryFee = findViewById(R.id.deliveryFeeET);
        sellerCountry = findViewById(R.id.sellerCountryET);
        sellerState = findViewById(R.id.sellerStateET);
        sellerCity = findViewById(R.id.sellerCityET);
        sellerAddress = findViewById(R.id.sellerAddressET);
        shopOpenSwitch = findViewById(R.id.shopOpenSwitch);
        sellerUpdate = findViewById(R.id.sellerUpdateBtn);

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
                            String SHOP_NAME = ""+ds.child("Shop Name").getValue();
                            String PHONE_NUMBER = ""+ds.child("Phone Number").getValue();
                            String DELIVERY_FEE = ""+ds.child("Deliver Fee").getValue();
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
                            String SHOP_OPEN = ""+ds.child("Shop Open").getValue();
                            String PROFILE_IMAGE = ""+ds.child("Profile Image").getValue();
                            String TIME_STAMP = ""+ds.child("Time Stamp").getValue();

                            sellerName.setText(SELLER_NAME);
                            sellerShop.setText(SHOP_NAME);
                            sellerPhone.setText(PHONE_NUMBER);
                            deliveryFee.setText(DELIVERY_FEE);
                            sellerCountry.setText(COUNTRY_NAME);
                            sellerState.setText(STATE_NAME);
                            sellerCity.setText(CITY_NAME);
                            sellerAddress.setText(ADDRESS);

                            if (SHOP_OPEN.equals("true")){
                                shopOpenSwitch.setChecked(true);
                            }
                            else {
                                shopOpenSwitch.setChecked(false);
                            }
                            try {

                            }catch (Exception exp){
                                sellerProfile.setImageResource(R.drawable.ic_person_gray);
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
        sellerProfile.setOnClickListener(new View.OnClickListener() {
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
        sellerUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Begin Update Profile
                inputData();
            }
        });
    }

    String Seller_Name, Seller_Shop, Seller_Phone, Delivery_Fee, Seller_Country, Seller_State, Seller_City, Seller_Address;
    Boolean Shop_Open;
    private void inputData() {
        Seller_Name = sellerName.getText().toString().trim();
        Seller_Shop = sellerShop.getText().toString().trim();
        Seller_Phone= sellerPhone.getText().toString().trim();
        Delivery_Fee = deliveryFee.getText().toString().trim();
        Seller_Country = sellerCountry.getText().toString().trim();
        Seller_State = sellerState.getText().toString().trim();
        Seller_City = sellerState.getText().toString().trim();
        Seller_Address = sellerAddress.getText().toString().trim();
        Shop_Open = shopOpenSwitch.isChecked(); //true or false
        updateProfile();
    }

    private void updateProfile() {
        progressDialog.setMessage("Updating Profile....");
        progressDialog.show();

        if (imageUri==null){
            //Update Without Image
            //Setup Data to Update
            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("Full Name", "" + Seller_Name);
            hashMap.put("Shop Name", "" + Seller_Shop);
            hashMap.put("Phone Number", "" + Seller_Phone);
            hashMap.put("Deliver Fee", "" + Delivery_Fee);
            hashMap.put("Country Name", "" + Seller_Country);
            hashMap.put("State Name", "" + Seller_State);
            hashMap.put("City Name", "" + Seller_City);
            hashMap.put("Latitude", "" + latitude);
            hashMap.put("Longitude", "" + longitude);
            hashMap.put("Address", "" + Seller_Address);
            hashMap.put("Account Type", "" + "Seller");
            hashMap.put("Shop Open", "" + Shop_Open);

            //Update to DB
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
            reference.child(firebaseAuth.getUid()).updateChildren(hashMap)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            //Updated
                            progressDialog.dismiss();
                            Toast.makeText(EditSellerProfileActivity.this, "Updated Seller Profile", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            //Failed to  Update
                            progressDialog.dismiss();
                            Toast.makeText(EditSellerProfileActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
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
                                hashMap.put("Full Name", "" + Seller_Name);
                                hashMap.put("Shop Name", "" + Seller_Shop);
                                hashMap.put("Phone Number", "" + Seller_Phone);
                                hashMap.put("Deliver Fee", "" + Delivery_Fee);
                                hashMap.put("Country Name", "" + Seller_Country);
                                hashMap.put("State Name", "" + Seller_State);
                                hashMap.put("City Name", "" + Seller_City);
                                hashMap.put("Latitude", "" + latitude);
                                hashMap.put("Longitude", "" + longitude);
                                hashMap.put("Address", "" + Seller_Address);
                                hashMap.put("Account Type", "" + "Seller");
                                hashMap.put("Shop Open", "" + Shop_Open);
                                hashMap.put("Profile Image", "" + downloadImageUri);

                                //Update to DB
                                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
                                reference.child(firebaseAuth.getUid()).updateChildren(hashMap)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                //Updated
                                                progressDialog.dismiss();
                                                Toast.makeText(EditSellerProfileActivity.this, "Updated Seller Profile", Toast.LENGTH_SHORT).show();
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                //Failed to  Update
                                                progressDialog.dismiss();
                                                Toast.makeText(EditSellerProfileActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
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
                            Toast.makeText(EditSellerProfileActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
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
            sellerCountry.setText(Country);
            sellerState.setText(State);
            sellerCity.setText(City);
            sellerAddress.setText(Address);
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
        Toast.makeText(EditSellerProfileActivity.this, "Location is Disabled", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(EditSellerProfileActivity.this, "Location Permission necessary...", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(EditSellerProfileActivity.this, "Camera Permissions are necessary...", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(EditSellerProfileActivity.this, "Storage Permission is necessary...", Toast.LENGTH_SHORT).show();
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
                sellerProfile.setImageURI(imageUri);
            }
            else  if (requestCode == IMAGE_PICK_CAMERA_CODE)
            {
                //Picked From Camera and Set to ImageView
                sellerProfile.setImageURI(imageUri);
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }
}