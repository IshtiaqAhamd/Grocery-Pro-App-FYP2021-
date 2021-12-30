package pk.edu.uiit.arid_2484_2464_2571.onlinegroceryapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class MainUserActivity extends AppCompatActivity {
    TextView userName;
    ImageButton userLogout;

    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_user);
        ViewsInitialization();
        ViewsPerformance();
        checkUser();
    }
    //UI Views Initialization
    public void ViewsInitialization()
    {
        userName = findViewById(R.id.userNameTV);
        userLogout = findViewById(R.id.userLogoutBtn);

        firebaseAuth = FirebaseAuth.getInstance();
        //
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please Wait...");
        progressDialog.setCanceledOnTouchOutside(false);
    }
    public void ViewsPerformance()
    {
        userLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Make offline, sign out and go to Login Activity
                makeMeOffline();
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
                            String name = ""+ds.child("Full Name").getValue();
                            String accountType = ""+ds.child("Account Type").getValue();
                            userName.setText(name+ "("+accountType+")");
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }
}