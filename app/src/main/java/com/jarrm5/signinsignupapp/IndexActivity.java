package com.jarrm5.signinsignupapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

public class IndexActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private GoogleSignInClient googleSignInClient;
    private GoogleSignInOptions googleSignInOptions;
    private GoogleSignInAccount account;

    private Toolbar toolbar;
    private ImageView accountLogo;
    private TextView accountName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index);

        accountLogo = findViewById(R.id.account_logo);
        accountName = findViewById(R.id.account_name);

        //Grab the toolbar and set it
        toolbar = findViewById(R.id.index_toolbar);
        setSupportActionBar(toolbar);

        googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        googleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions);

        account = GoogleSignIn.getLastSignedInAccount(this);
        if(account != null){
            //Picasso can take a raw URI string and set it to an Imageview
            Picasso.with(this).load(account.getPhotoUrl()).into(accountLogo);
            accountName.setText(account.getDisplayName());
        }

        firebaseAuth = FirebaseAuth.getInstance();
        if(firebaseAuth.getCurrentUser() != null){
            accountLogo.setImageResource(R.mipmap.firebase);
            accountName.setText(firebaseAuth.getCurrentUser().getEmail());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_index, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch(id){
            case R.id.menu_settings:
                Toast.makeText(IndexActivity.this, "Settings",
                        Toast.LENGTH_SHORT).show();
                break;
            case R.id.menu_help:
                Toast.makeText(IndexActivity.this, "help",
                        Toast.LENGTH_SHORT).show();
                break;
            case R.id.menu_signout:
                googleSignOut();
                firebaseSignOut();
                startActivity(new Intent(IndexActivity.this,MainActivity.class));
                break;
            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStop(){
        super.onStop();
        googleSignOut();
        firebaseSignOut();
    }

    private void googleSignOut() {
        if(account != null){
            final String displayName = account.getDisplayName();
            googleSignInClient.signOut()
                    .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Toast.makeText(IndexActivity.this, "Succesfully Signed out " + displayName,
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
        }


    }
    private void firebaseSignOut(){
        if(firebaseAuth.getCurrentUser() != null){
            Toast.makeText(IndexActivity.this, "Succesfully Signed out " + firebaseAuth.getCurrentUser().getDisplayName(),
                    Toast.LENGTH_SHORT).show();
            firebaseAuth.signOut();
        }
    }
}
