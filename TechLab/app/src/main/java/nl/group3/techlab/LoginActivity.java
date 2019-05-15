package nl.group3.techlab;

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

import nl.group3.techlab.databases.MyTechlab;

public class LoginActivity extends AppCompatActivity {
    int RC_SIGN_IN = 0;
    SignInButton signInButton;
    GoogleSignInClient mGoogleSignInClient;
    MyTechlab myDb;
    boolean HrEmail;
    View FindEmail;
    static String Email;
    View FindPassword;
    View FindText;
    String personEmail;
    static boolean logged_in;
    boolean AdminClicked;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        logged_in = account != null;

        findViewById(R.id.Password).setVisibility(View.GONE);
        findViewById(R.id.textView4).setVisibility(View.GONE);
        findViewById(R.id.button).setVisibility(View.GONE);
        findViewById(R.id.email).setVisibility(View.GONE);
        findViewById(R.id.textView3).setVisibility(View.GONE);
        findViewById(R.id.back).setVisibility(View.GONE);


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
        myDb.insertData("techlabapp00@gmail.com", "test123", "Admin");

        myDb.getWritableDatabase();
    }

    public void LogInAdmin(View view) {
        findViewById(R.id.inloggen_als_student).setVisibility(View.GONE);
        findViewById(R.id.sign_in_button).setVisibility(View.GONE);
        findViewById(R.id.inloggen_als_admin).setVisibility(View.GONE);
        findViewById(R.id.admin_button).setVisibility(View.GONE);
        findViewById(R.id.Password).setVisibility(View.VISIBLE);
        findViewById(R.id.textView4).setVisibility(View.VISIBLE);
        findViewById(R.id.button).setVisibility(View.VISIBLE);
        findViewById(R.id.email).setVisibility(View.VISIBLE);
        findViewById(R.id.textView3).setVisibility(View.VISIBLE);
        findViewById(R.id.back).setVisibility(View.VISIBLE);
        AdminClicked = true;

    }

    public void AdminTerug(View view) {
//        findViewById(R.id.inloggen_als_student).setVisibility(View.VISIBLE);
//        findViewById(R.id.sign_in_button).setVisibility(View.VISIBLE);
//        findViewById(R.id.inloggen_als_admin).setVisibility(View.VISIBLE);
//        findViewById(R.id.admin_button).setVisibility(View.VISIBLE);
//        findViewById(R.id.Password).setVisibility(View.GONE);
//        findViewById(R.id.textView4).setVisibility(View.GONE);
//        findViewById(R.id.button).setVisibility(View.GONE);
//        findViewById(R.id.email).setVisibility(View.GONE);
//        findViewById(R.id.textView3).setVisibility(View.GONE);
//        findViewById(R.id.back).setVisibility(View.GONE);
//        AdminClicked = false;
        onBackPressed();
    }

    public void onBackPressed() {
        if (AdminClicked) {
            findViewById(R.id.inloggen_als_student).setVisibility(View.VISIBLE);
            findViewById(R.id.sign_in_button).setVisibility(View.VISIBLE);
            findViewById(R.id.inloggen_als_admin).setVisibility(View.VISIBLE);
            findViewById(R.id.admin_button).setVisibility(View.VISIBLE);
            findViewById(R.id.Password).setVisibility(View.GONE);
            findViewById(R.id.textView4).setVisibility(View.GONE);
            findViewById(R.id.button).setVisibility(View.GONE);
            findViewById(R.id.email).setVisibility(View.GONE);
            findViewById(R.id.textView3).setVisibility(View.GONE);
            findViewById(R.id.back).setVisibility(View.GONE);
            AdminClicked = false;
        } else {
//            finish();
            super.onBackPressed();
        }
    }
    public void loggingIn(View view) {
        myDb = new MyTechlab(this);
        FindEmail = findViewById(R.id.email);
        Email = ((EditText) FindEmail).getText().toString();
        FindPassword = findViewById(R.id.Password);
        String Password = ((EditText) FindPassword).getText().toString();
        String Admin = "Admin";
        Boolean Email_Password = myDb.Email_Password(Email, Password);
        Boolean AdminTrue = myDb.Rol(Email ,Admin);
        if (AdminTrue) {
            if (Email_Password){
                startActivity(new Intent(LoginActivity.this, ItemsAndMenuActivity.class));
            } else if (Password.equals("")){
                Toast.makeText(LoginActivity.this, "Voer een wachtwoord in", Toast.LENGTH_SHORT).show();
            } else if (!Password.equals("")){
                Toast.makeText(LoginActivity.this, "Fout wachtwoord", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(LoginActivity.this, "Log in met de admin email", Toast.LENGTH_SHORT).show();
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

            if (account != null){
                personEmail = account.getEmail();
            }
            if (personEmail != null && personEmail.endsWith("@hr.nl")) {
                HrEmail = true;
                /* Signed in successfully, show authenticated UI. */
                startActivity(new Intent(LoginActivity.this, ItemsAndMenuActivity.class));
                finish();
            } else {
                Toast.makeText(LoginActivity.this, "Je moet met jouw HR account inloggen", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(LoginActivity.this, LoginActivity.class));
                mGoogleSignInClient.signOut();
                finish();
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
        if (account != null && logged_in) {
//        if (account != null) {
            startActivity(new Intent(LoginActivity.this, ItemsAndMenuActivity.class));
            finish();
        } else {
            logged_in = false;
        }
        super.onStart();
    }
}
