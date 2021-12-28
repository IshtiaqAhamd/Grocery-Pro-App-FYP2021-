package pk.edu.uiit.arid_2484_2464_2571.onlinegroceryapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class LoginActivity extends AppCompatActivity {
    //Declare Login Activity UI Views
    EditText loginEmail, loginPassword;
    TextView loginNoAccount, loginForgotPassword;
    Button loginButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ViewsInitialization();
        ViewsPerformance();
    }
    //UI Views Initialization
    public void ViewsInitialization()
    {
        loginEmail = findViewById(R.id.sellerEmailET);
        loginPassword = findViewById(R.id.sellerPasswordET);
        loginNoAccount = findViewById(R.id.noAccountTv);
        loginForgotPassword = findViewById(R.id.forgotTv);
        loginButton = findViewById(R.id.loginBtn);
    }
    // UI Views Performance Actions
    public void ViewsPerformance()
    {
        loginNoAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegisterUserActivity.class));
            }
        });

        loginForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, ForgotPasswordActivity.class));

            }
        });
    }
}