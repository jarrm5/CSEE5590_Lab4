package com.jarrm5.signinsignupapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends FragmentActivity implements View.OnClickListener, SignInDialogFragment.SignInDialogListener {

    /* Controls */
    private ImageButton googleSignIn;
    private ImageButton firebaseSignIn;

    /* Google Auth objects */
    private GoogleSignInOptions googleSignInOptions;
    private GoogleSignInClient googleSignInClient;

    /* Firebase Auth objects */
    private FirebaseAuth firebaseAuth;

    /* static members for this activity */
    private static final int RC_SIGN_IN = 9001;
    private static final String TAG = "EmailPassword";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        googleSignIn = findViewById(R.id.google_sign_in_button);
        googleSignIn.setOnClickListener(this);

        firebaseSignIn = findViewById(R.id.firebase_sign_in_button);
        firebaseSignIn.setOnClickListener(this);

        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        // Build a GoogleSignInClient with the options specified by googleSignInOptions.
        googleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions);

        firebaseAuth = FirebaseAuth.getInstance();
    }

    public void showSignInDialog() {
        // Create an instance of the dialog fragment and show it
        DialogFragment dialog = new SignInDialogFragment();
        dialog.show(getSupportFragmentManager(), "SignInDialogFragment");
    }

    @Override
    public void onClick(View v){
        switch (v.getId()) {
            case R.id.google_sign_in_button:
                doGoogleSignIn();
                break;
            case R.id.firebase_sign_in_button:
                showSignInDialog();
                //signIn(mEmailField.getText().toString(), mPasswordField.getText().toString());
                break;
            default:
                break;
        }
    }

    // The dialog fragment receives a reference to this Activity through the
    // Fragment.onAttach() callback, which it uses to call the following methods
    // defined by the DialogFragment.DialogListener interface
    @Override
    public void onDialogSignIn(DialogFragment dialog){
        SignInDialogFragment signInDialogFragment = (SignInDialogFragment) dialog;
        String username = signInDialogFragment.username.getText().toString();
        String password = signInDialogFragment.password.getText().toString();

        Log.d("Fuck", "signIn:");
    }

    private void signIn(String email, String password) {
        Log.d(TAG, "signIn:" + email);
        /*if (!validateForm()) {
            return;
        }

        showProgressDialog();*/

        // [START sign_in_with_email]
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            //updateUI(user);
                            String email = user.getEmail();
                            String name = user.getDisplayName();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(MainActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            //updateUI(null);
                        }

                        // [START_EXCLUDE]
                        /*if (!task.isSuccessful()) {
                            mStatusTextView.setText(R.string.auth_failed);
                        }
                        hideProgressDialog();*/
                        // [END_EXCLUDE]
                    }
                });
        // [END sign_in_with_email]
    }


    private void doGoogleSignIn(){
        Intent signInIntent = googleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            String email = account.getEmail();
            String token = account.getIdToken();
            // Signed in successfully, show authenticated UI.
            //updateUI(account);
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
            //updateUI(null);
        }
    }



}
