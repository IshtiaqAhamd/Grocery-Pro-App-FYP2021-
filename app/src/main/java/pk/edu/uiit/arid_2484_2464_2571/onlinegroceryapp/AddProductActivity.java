package pk.edu.uiit.arid_2484_2464_2571.onlinegroceryapp;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;

public class AddProductActivity extends AppCompatActivity {
    //Declare Add Product Activity UI Views
    EditText productTitle, productDescription;
    TextView productCategory, productQuantity, productPrice, productDiscountPrice, productDiscountNote;
    ImageButton backButton;
    ImageView productIcon;
    Button addProduct;
    SwitchCompat productDiscountSwitch;

    //Permission Constants
    private static final int CAMERA_REQUEST_CODE = 200;
    private static final int STORAGE_REQUEST_CODE = 300;

    //Image Pick Constants
    private static final int IMAGE_PICK_GALLERY_CODE = 400;
    private static final int IMAGE_PICK_CAMERA_CODE = 500;

    //Permission Arrays
    String[] cameraPermissions;
    String[] storagePermissions;

    //Picked Image Uri
    private Uri imageUri;

    //Progress Dialog
    ProgressDialog progressDialog;

    //Firebase Auth
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);
        ViewsInitialization();
        ViewsPerformance();
    }

    //UI Views Initialization
    public void ViewsInitialization()
    {
        productTitle = findViewById(R.id.titleET);
        productDescription = findViewById(R.id.descriptionET);

        productCategory = findViewById(R.id.categoryTV);
        productQuantity = findViewById(R.id.quantityET);
        productPrice = findViewById(R.id.priceET);
        productDiscountPrice = findViewById(R.id.discountPriceET);
        productDiscountNote = findViewById(R.id.discountNoteET);

        backButton = findViewById(R.id.backBtn);
        productIcon = findViewById(R.id.productIconIV);
        addProduct = findViewById(R.id.addProductBtn);
        productDiscountSwitch = findViewById(R.id.discountSwitch);

        // UnChecked, Hide Product Discount Price, Product Discount Note
        productDiscountPrice.setVisibility(View.GONE);
        productDiscountNote.setVisibility(View.GONE);

        //Initialization of Permission Arrays
        cameraPermissions = new String[]{Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};

        //Setup Progress Dialog
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please Wait....");
        progressDialog.setCanceledOnTouchOutside(false);

        //
        firebaseAuth = FirebaseAuth.getInstance();
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
        productIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Pick Image
                showImagePickDialog();
            }
        });
        productCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                categoryDialog();
            }
        });
        productDiscountSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                {
                    // Checked, Show Product Discount Price, Product Discount Note
                    productDiscountPrice.setVisibility(View.VISIBLE);
                    productDiscountNote.setVisibility(View.VISIBLE);
                }
                else
                {
                    // UnChecked, Hide Product Discount Price, Product Discount Note
                    productDiscountPrice.setVisibility(View.GONE);
                    productDiscountNote.setVisibility(View.GONE);
                }
            }
        });
        addProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*  Flow:
                    1) Input Data
                    2) Validate Data
                    3) Add Data to DB
                */
                inputData();
            }
        });
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
    String Product_Title, Product_Description, Product_Category, Product_Quantity, Product_Price, Product_Discount_Price, Product_Discount_Note;
    boolean Discount_Available = false;

    private void inputData() {
        // 1) Input Data
        Product_Title = productTitle.getText().toString().trim();
        Product_Description = productDescription.getText().toString().trim();
        Product_Category = productCategory.getText().toString().trim();
        Product_Quantity = productQuantity.getText().toString().trim();
        Product_Price = productPrice.getText().toString().trim();
        Discount_Available = productDiscountSwitch.isChecked(); // true/false

        // 2) Validate Data
        if(TextUtils.isEmpty(Product_Title))
        {
            Toast.makeText(this, "Title is Required....", Toast.LENGTH_SHORT).show();
            return; // Don't Proceed Further
        }
        if(TextUtils.isEmpty(Product_Category))
        {
            Toast.makeText(this, "Category is Required....", Toast.LENGTH_SHORT).show();
            return; // Don't Proceed Further
        }
        if(TextUtils.isEmpty(Product_Price))
        {
            Toast.makeText(this, "Price is Required....", Toast.LENGTH_SHORT).show();
            return; // Don't Proceed Further
        }
        if(Discount_Available)
        //Product With Discount
        {
            Product_Discount_Price = productDiscountPrice.getText().toString().trim();
            Product_Discount_Note = productDiscountNote.getText().toString().trim();
            if(TextUtils.isEmpty(Product_Discount_Price))
            {
                Toast.makeText(this, "Discount Price is Required....", Toast.LENGTH_SHORT).show();
                return; // Don't Proceed Further
            }
            if(TextUtils.isEmpty(Product_Discount_Note))
            {
                Toast.makeText(this, "Discount Note is Required....", Toast.LENGTH_SHORT).show();
                return; // Don't Proceed Further
            }
        }
        else
        {
            //Product Without Discount
            Product_Discount_Price = "0";
            Product_Discount_Note = "";
            addProduct();
        }
    }

    private void addProduct() {
        // 3) Add Data to DB
        progressDialog.setMessage("Adding Product....");
        progressDialog.show();
        String timestamp = ""+System.currentTimeMillis();

        if (imageUri==null)
        {
            //Upload Without Image

            //Setup Data To Upload
            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("Product ID",""+timestamp);
            hashMap.put("Product Title",""+Product_Title);
            hashMap.put("Product Description", ""+Product_Description);
            hashMap.put("Product Category", ""+Product_Category);
            hashMap.put("Product Quantity", ""+Product_Quantity);
            hashMap.put("Product Icon", ""); // No Image, Set Empty
            hashMap.put("Product Price", ""+Product_Price);
            hashMap.put("Discount Price", ""+Product_Discount_Price);
            hashMap.put("Discount Note", ""+Product_Discount_Note);
            hashMap.put("Discount Available", ""+Discount_Available);
            hashMap.put("Time Stamp", ""+timestamp);
            hashMap.put("User ID", ""+firebaseAuth.getUid());

            //Add to DB
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
            reference.child(firebaseAuth.getUid())
                    .child("Products").child(timestamp).setValue(hashMap)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            //Added to  DB
                            progressDialog.dismiss();
                            Toast.makeText(AddProductActivity.this, "Product Added...", Toast.LENGTH_SHORT).show();
                            clearData();

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            //Failed adding to DB
                            progressDialog.dismiss();
                            Toast.makeText(AddProductActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        }
        else
        {
            // Upload With Image

            // First Upload Image To Storage

            // Name and Path of Image to be Upload
            String filePathAndName = "product_image/" + "" + timestamp;
            StorageReference storageReference = FirebaseStorage.getInstance().getReference(filePathAndName);
            storageReference.putFile(imageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            // Upload Image
                            Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                            while (!uriTask.isSuccessful());
                            Uri downloadImageUri = uriTask.getResult();
                            if (uriTask.isSuccessful())
                            {
                                // Url of image received, Upload to DB
                                //Setup Data To Upload
                                HashMap<String, Object> hashMap = new HashMap<>();
                                hashMap.put("Product ID",""+timestamp);
                                hashMap.put("Product Title",""+Product_Title);
                                hashMap.put("Product Description", ""+Product_Description);
                                hashMap.put("Product Category", ""+Product_Category);
                                hashMap.put("Product Quantity", ""+Product_Quantity);
                                hashMap.put("Product Icon", ""+downloadImageUri);
                                hashMap.put("Product Price", ""+Product_Price);
                                hashMap.put("Discount Price", ""+Product_Discount_Price);
                                hashMap.put("Discount Note", ""+Product_Discount_Note);
                                hashMap.put("Discount Available", ""+Discount_Available);
                                hashMap.put("Time Stamp", ""+timestamp);
                                hashMap.put("User ID", ""+firebaseAuth.getUid());

                                //Add to DB
                                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
                                reference.child(firebaseAuth.getUid())
                                        .child("Products").child(timestamp).setValue(hashMap)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                //Added to  DB
                                                progressDialog.dismiss();
                                                Toast.makeText(AddProductActivity.this, "Product Added...", Toast.LENGTH_SHORT).show();
                                                clearData();
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                //Failed adding to DB
                                                progressDialog.dismiss();
                                                Toast.makeText(AddProductActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();

                                            }
                                        });
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            // Failed Uploading Image
                            progressDialog.dismiss();
                            Toast.makeText(AddProductActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    private void clearData()
    {
        productTitle.setText("");
        productDescription.setText("");
        productCategory.setText("");
        productQuantity.setText("");
        productPrice.setText("");
        productDiscountPrice.setText("");
        productDiscountNote.setText("");
        productIcon.setImageResource(R.drawable.ic_add_shopping_primary);
        imageUri = null;
    }

    private void categoryDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Product Category")
                .setItems(Constants.ProductCategories, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Get Picked Category
                        String Categories = Constants.ProductCategories[which];
                        //Set Picked Category
                        productCategory.setText(Categories);
                    }
                }).show();
    }

    private void showImagePickDialog() {
        //Options To Display In Dialog
        String[] Options = {"Camera", "Gallery"};

        //Dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Pick Image").setItems(Options, new DialogInterface.OnClickListener() {
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
    private void pickFromGallery() {
        //Intent to pick image from Gallery
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, IMAGE_PICK_GALLERY_CODE);
    }
    private void pickFromCamera() {
        //Intent to pick image from Camera

        //Using media store to pick high/original quality image
        ContentValues contentValues = new ContentValues();
        contentValues.put(MediaStore.Images.Media.TITLE, "Temp_Image Title");
        contentValues.put(MediaStore.Images.Media.DESCRIPTION, "Temp_Image Description");

        imageUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(intent, IMAGE_PICK_CAMERA_CODE);
    }
    private boolean checkStoragePermission() {
        boolean Result = ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)==
                (PackageManager.PERMISSION_GRANTED);
        return Result;
    }
    private void requestStoragePermission() {
        ActivityCompat.requestPermissions(this, storagePermissions, STORAGE_REQUEST_CODE);
    }
    private boolean checkCameraPermission() {
        boolean Result1 = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)==
                (PackageManager.PERMISSION_GRANTED);
        boolean Result2 = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)==
                (PackageManager.PERMISSION_GRANTED);
        return Result1 && Result2;
    }
    private void requestCameraPermission() {
        ActivityCompat.requestPermissions(this, cameraPermissions, CAMERA_REQUEST_CODE);
    }

    //Handle Permission Results
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
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
                        Toast.makeText(AddProductActivity.this, "Camera Permissions are necessary...", Toast.LENGTH_SHORT).show();
                    }
                }
            }
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
                        Toast.makeText(AddProductActivity.this, "Storage Permission is necessary...", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
    //Handle Image Pick Result
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
                productIcon.setImageURI(imageUri);
            }
            else  if (requestCode == IMAGE_PICK_CAMERA_CODE)
            {
                //Picked From Camera and Set to ImageView
                productIcon.setImageURI(imageUri);
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }
}