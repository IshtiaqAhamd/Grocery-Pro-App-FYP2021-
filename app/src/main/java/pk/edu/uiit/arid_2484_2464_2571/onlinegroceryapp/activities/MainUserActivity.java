package pk.edu.uiit.arid_2484_2464_2571.onlinegroceryapp.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

import pk.edu.uiit.arid_2484_2464_2571.onlinegroceryapp.R;
import pk.edu.uiit.arid_2484_2464_2571.onlinegroceryapp.adapters.AdapterShop;
import pk.edu.uiit.arid_2484_2464_2571.onlinegroceryapp.models.ModelShop;

public class MainUserActivity extends AppCompatActivity {
    TextView userNameTV, userEmailTV, userPhoneTV, tabShopsTV, tabOrdersTV;
    ImageButton userLogoutBtn, editProfileBtn;
    ImageView userProfileIV;
    RelativeLayout shopsRL, ordersRL;
    RecyclerView shopRV;

    ArrayList<ModelShop> shopsList;
    AdapterShop adapterShop;

    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_user);
        ViewsInitialization();
        ViewsPerformance();
        checkUser();

        // At Start Show Shops UI
        showShopsUI();
    }

    private void showShopsUI() {
        // Show Shops UI and hide Orders UI
        shopsRL.setVisibility(View.VISIBLE);
        ordersRL.setVisibility(View.GONE);

        tabShopsTV.setTextColor(getResources().getColor(R.color.colorBlack));
        tabShopsTV.setBackgroundResource(R.drawable.shape_rect04);

        tabOrdersTV.setTextColor(getResources().getColor(R.color.colorWhite));
        tabOrdersTV.setBackgroundColor(getResources().getColor(android.R.color.transparent));
    }

    private void showOrdersUI() {
        // Show Orders UI and hide Products UI
        shopsRL.setVisibility(View.GONE);
        ordersRL.setVisibility(View.VISIBLE);

        tabShopsTV.setTextColor(getResources().getColor(R.color.colorWhite));
        tabShopsTV.setBackgroundColor(getResources().getColor(android.R.color.transparent));

        tabOrdersTV.setTextColor(getResources().getColor(R.color.colorBlack));
        tabOrdersTV.setBackgroundResource(R.drawable.shape_rect04);
    }

    //UI Views Initialization
    public void ViewsInitialization()
    {
        userProfileIV = findViewById(R.id.userProfileIV);
        userNameTV = findViewById(R.id.userNameTV);
        userEmailTV = findViewById(R.id.userEmailTV);
        userPhoneTV = findViewById(R.id.userPhoneTV);
        tabShopsTV = findViewById(R.id.tabShopsTV);
        tabOrdersTV = findViewById(R.id.tabOrdersTV);
        shopsRL = findViewById(R.id.shopsRL);
        ordersRL = findViewById(R.id.ordersRL);
        userLogoutBtn = findViewById(R.id.userLogoutBtn);
        editProfileBtn = findViewById(R.id.editProfileBtn);
        shopRV = findViewById(R.id.shopRV);
        firebaseAuth = FirebaseAuth.getInstance();
        //
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please Wait...");
        progressDialog.setCanceledOnTouchOutside(false);
    }
    public void ViewsPerformance()
    {
        userLogoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Make offline, sign out and go to Login Activity
                makeMeOffline();
            }
        });
        editProfileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //open edit profile activity
                startActivity(new Intent(MainUserActivity.this,EditUserProfileActivity.class));
            }
        });
        tabOrdersTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Show Shops
                showShopsUI();
            }
        });
        tabOrdersTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Show Orders
                showOrdersUI();
            }
        });
    }
    private void makeMeOffline() {
        //After Logout, make user offline
        progressDialog.setMessage("Logging out....");
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("Online","false");

        //Update value to db
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
        reference.child(firebaseAuth.getUid()).updateChildren(hashMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        //Update successfully
                        firebaseAuth.signOut();
                        checkUser();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //Failed Updating
                        progressDialog.dismiss();
                        Toast.makeText(MainUserActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
    private void checkUser() {
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user==null)
        {
            startActivity(new Intent(MainUserActivity.this,LoginActivity.class));
            finish();
        }
        else
        {
            loadMyInfo();
        }
    }
    private void loadMyInfo() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
        reference.orderByChild("uid").equalTo(firebaseAuth.getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot ds: snapshot.getChildren()){
                            // Get User Data
                            String name = ""+ds.child("Name").getValue();
                            String email = ""+ds.child("Email").getValue();
                            String phone = ""+ds.child("Phone").getValue();
                            String profileImage = ""+ds.child("ProfileImage").getValue();
                            String accountType = ""+ds.child("AccountType").getValue();
                            String city = ""+ds.child("City").getValue();
                            // Set User Data
                            userNameTV.setText(name);
                            userEmailTV.setText(email);
                            userPhoneTV.setText(phone);
                            try {
                                userProfileIV.setImageResource(R.drawable.ic_person_gray);
                            }
                            catch (Exception exp){
                                userProfileIV.setImageResource(R.drawable.ic_store_gray);
                            }

                            // Load only those shops that are in the city of user
                            loadShops(city);

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    private void loadShops(String myCity) {
        // Initialize List
        shopsList = new ArrayList<>();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
        reference.orderByChild("AccountType").equalTo("Seller")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        // Clear List Before Adding
                        shopsList.clear();
                        for (DataSnapshot ds: snapshot.getChildren()){
                            ModelShop modelShop = ds.getValue(ModelShop.class);

                            String shopCity = ""+ds.child("City").getValue();

                            // Show only user city shops
                            if (shopCity.equals(myCity)) {
                                shopsList.add(modelShop);
                            }
                            // Display all shops and skip above if statement
                            // shopsList.add(modelShop);
                        }
                        // Setup Adapter
                        adapterShop = new AdapterShop(MainUserActivity.this, shopsList);
                        // Set Adapter to Recyclerview
                        shopRV.setAdapter(adapterShop);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }
}