package com.jarrm5.signinsignupapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    //Regex pattern for email validation
    public static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    private Button registerButton;
    private Button cancelButton;
    private EditText emailEditText;
    private EditText passwordEditText;
    private EditText passwordAgainEditText;
    private TextView registrationErrorTextview;

    private FirebaseAuth auth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        registerButton = findViewById(R.id.register_account);
        cancelButton = findViewById(R.id.cancel_button);
        emailEditText = findViewById(R.id.register_email);
        passwordEditText = findViewById(R.id.register_password);
        passwordAgainEditText = findViewById(R.id.register_password_again);
        registrationErrorTextview = findViewById(R.id.registration_error);

        registerButton.setOnClickListener(this);
        cancelButton.setOnClickListener(this);

        auth = FirebaseAuth.getInstance();
    }

    @Override
    public void onClick(View v){
        switch (v.getId()) {
            case R.id.register_account:
                RegisterToFirebase();
                break;
            case R.id.cancel_button:
                startActivity(new Intent(RegisterActivity.this,MainActivity.class));
                break;
            default:
                break;
        }
    }

    public void RegisterToFirebase(){

        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        String passwordAgain = passwordAgainEditText.getText().toString();

        if(validateForm(email,password,passwordAgain)){
            auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            Toast.makeText(RegisterActivity.this, "createUserWithEmail:onComplete:" + task.isSuccessful(), Toast.LENGTH_SHORT).show();
                            // If sign in fails, display a message to the user. If sign in succeeds
                            // the auth state listener will be notified and logic to handle the
                            // signed in user can be handled in the listener.
                            if (!task.isSuccessful()) {
                                Toast.makeText(RegisterActivity.this, "Authentication failed." + task.getException(),
                                        Toast.LENGTH_SHORT).show();
                            } else {
                                ClearAllFields();
                                startActivity(new Intent(RegisterActivity.this,IndexActivity.class));
                            }
                        }
                    });
        }
    }





    //Check if Registration form has a valid email string
    //Check if passwords match
    public boolean validateForm(String email, String password, String passwordAgain){

        String error = "";
        boolean isValid = true;

        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX .matcher(email);
        if(!matcher.find()){
            error += "You must enter a valid email (e.g. abc@xyz.com)";
            isValid = false;
        }

        if(!password.equals(passwordAgain)){
            if(!isValid){
                error += "\n";
            }
            error += "Passwords must match. Try again.";
            isValid = false;
        }
        registrationErrorTextview.setText(error);
        registrationErrorTextview.setVisibility(View.VISIBLE);
        return isValid;
    }

    private void ClearAllFields(){

        emailEditText.setText("");
        passwordEditText.setText("");
        passwordAgainEditText.setText("");

        registrationErrorTextview.setText("");
        registrationErrorTextview.setVisibility(View.INVISIBLE);
    }
}
