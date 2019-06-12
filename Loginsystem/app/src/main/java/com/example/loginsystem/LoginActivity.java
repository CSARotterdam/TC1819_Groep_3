package com.example.loginsystem;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

public class LoginActivity extends AppCompatActivity {
    int RC_SIGN_IN = 0;
    SignInButton signInButton;
    GoogleSignInClient mGoogleSignInClient;
    MyTechlab myDb;
    boolean HrEmail;
    View FindEmail;
    static String Email;
    View FindPassword;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        /* Initializing Views*/
        signInButton = findViewById(R.id.sign_in_button);

        /* Configure sign-in to request the user's ID, email address, and basic*/
        /* profile. ID and basic profile are included in DEFAULT_SIGN_IN.*/
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        // Build a GoogleSignInClient with the options specified by gso.*/
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });
        myDb = new MyTechlab(this);
        myDb.insertData("0960882@hr.nl", "test123", "Gebruiker");
        myDb.insertData("0961065@hr.nl", "test123", "Gebruiker");
        myDb.insertData("0961988@hr.nl", "test123", "Gebruiker");
        myDb.insertData("0965662@hr.nl", "test123", "Gebruiker");
        myDb.insertData("0971084@hr.nl", "test123", "Gebruiker");
        myDb.insertData("techlabapp00@gmail.com", "test123", "Admin");

        myDb.getWritableDatabase();
    }

   public void inloggen(View view) {
       myDb = new MyTechlab(this);
       FindEmail = (EditText) findViewById(R.id.Gebruiksnaam);
       Email = ((EditText) FindEmail).getText().toString();
       FindPassword = (EditText) findViewById(R.id.Password);
       String Password = ((EditText) FindPassword).getText().toString();
       String Admin = "Admin";
       Boolean Email_Password = myDb.Email_Password(Email, Password);
       Boolean AdminTrue = myDb.Rol(Email ,Admin);
       if (Email_Password == true) {

           if (Email.endsWith("@hr.nl")){
               Toast.makeText(LoginActivity.this, "Kies uw Hogeschool email", Toast.LENGTH_SHORT).show();
               signIn();
           }else if(AdminTrue){
               startActivity(new Intent(LoginActivity.this, AdminActivity.class));
           }
       }
       else if (Email_Password == false) {
           Toast.makeText(LoginActivity.this, "Fout wachtwoord of email", Toast.LENGTH_SHORT).show();

       }

    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        /* Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);*/
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {

        try {//check Hr account or not
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            String personEmail = account.getEmail();
            if (personEmail != null && personEmail.endsWith("@hr.nl")) {

                HrEmail = true;
                /* Signed in successfully, show authenticated UI. */
                startActivity(new Intent(LoginActivity.this, UserActivity.class));
                finish();
            } else {
                Toast.makeText(LoginActivity.this, "U moet met uw Hr account inloggen", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(LoginActivity.this, LoginActivity.class));
                mGoogleSignInClient.signOut();
            }

        } catch (ApiException e) {
            /* The ApiException status code indicates the detailed failure reason. */
            /* Please refer to the GoogleSignInStatusCodes class reference for more information.*/
            Log.w("Google Sign In Error", "signInResult:failed code=" + e.getStatusCode());
            Toast.makeText(LoginActivity.this, "Failed", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onStart() {
        /* Check for existing Google Google Sign In account, if the user is already signed in
        the GoogleSignInAccount will be non-null
         */
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        if (account != null) {
            startActivity(new Intent(LoginActivity.this, UserActivity.class));
        }
        super.onStart();
    }
}
