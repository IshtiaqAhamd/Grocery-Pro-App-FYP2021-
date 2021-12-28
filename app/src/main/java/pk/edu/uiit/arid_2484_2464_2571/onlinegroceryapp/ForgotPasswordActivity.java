package pk.edu.uiit.arid_2484_2464_2571.onlinegroceryapp;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

public class ForgotPasswordActivity extends AppCompatActivity {
    //Declare Forgot Password Activity UI Views
    EditText email;
    ImageButton backButton;
    Button recoverButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        ViewsInitialization();
        ViewsPerformance();
    }

    //UI Views Initialization
    public void ViewsInitialization()
    {
        backButton = findViewById(R.id.backBtn);
        email = findViewById(R.id.sellerEmailET);
        recoverButton = findViewById(R.id.recoverBtn);
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
    }
}