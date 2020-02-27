package com.example.thoughtful;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    Button loginnormal, register;
    EditText email, password;

    private LoginButton loginButton;
    private FirebaseAuth mAuth;
    private CallbackManager callbackManager;
    private static final String EMAIL = "email";

    GoogleSignInClient mGoogleSignInClient;
    int RC_SIGN_IN = 420;

    @Override
    protected void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser!=null){
            Intent i = new Intent(MainActivity.this, AfterFBLogin.class);
            i.putExtra("name", currentUser.getDisplayName());
            startActivity(i);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.i("TRUTH", "STARTED");

        mAuth = FirebaseAuth.getInstance();

        //---------------------------------------Match HashCode - Just In Case---------------------------------------
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.example.thoughtful",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.i("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {
            Log.i("KeyHash:", "Catch : "+e.getMessage());

        } catch (NoSuchAlgorithmException e) {
            Log.i("KeyHash:", "Catch : "+e.getMessage());
        }

        //---------------------------------------Facebook Login--------------------------------------------
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        if((accessToken == null) || (accessToken.isExpired())){
            FirebaseUser firebaseUser = mAuth.getCurrentUser();
            if(firebaseUser==null){
                email = findViewById(R.id.email);
                password = findViewById(R.id.pass);
                loginnormal = findViewById(R.id.loginnormal);

                loginnormal.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mAuth.signInWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                    @Override
                                    public void onSuccess(AuthResult authResult) {
                                        Log.i("TRUTH", "Signed In");
                                        Toast.makeText(MainActivity.this, "Signed In Successfully!", Toast.LENGTH_LONG).show();
                                        //Intent i = new Intent(MainActivity.this,);
                                    }
                                })
                                .addOnCanceledListener(new OnCanceledListener() {
                                    @Override
                                    public void onCanceled() {
                                        Log.i("TRUTH", "Signed In Cancelled");
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.i("TRUTH", "Error");
                                        Toast.makeText(MainActivity.this, "Email/Password Incorrect.", Toast.LENGTH_LONG).show();
                                    }
                                });
                    }
                });

                register = findViewById(R.id.register);
                register.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent i = new Intent(MainActivity.this, RegisterActivity.class);
                        /*i.putExtra("email", email.getText().toString());
                        i.putExtra("password", password.getText().toString());*/
                        startActivity(i);
                    }
                });
            }
            else{
                String name = firebaseUser.getDisplayName();
                Intent i = new Intent(MainActivity.this, AfterFBLogin.class);
                i.putExtra("name", name);
                Log.i("TRUTH", "Logged in : "+name);
                startActivity(i);
            }
            mAuth = FirebaseAuth.getInstance();
            callbackManager = CallbackManager.Factory.create();
            loginButton = (LoginButton) findViewById(R.id.login_button);
            //LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile"));
            String a[] = new String[] { "email"};
            loginButton.setPermissions(Arrays.asList(EMAIL));
            loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
                @Override
                public void onSuccess(LoginResult loginResult) {
                    int x = loginResult.hashCode();
                    Log.i("TRUTH", "hashcode2: "+x);
                    // App code
                    Log.i("TRUTH", "facebook:onSuccess:" + loginResult);
                    handleFacebookAccessToken(loginResult.getAccessToken());
                }

                @Override
                public void onCancel() {
                    // App code
                    Log.i("TRUTH", "facebook:onCancel:");
                }

                @Override
                public void onError(FacebookException exception) {

                    // App code
                    Log.i("TRUTH", "facebook:onError: "+exception.getMessage());
                }
            });
        }
        else{
            handleFacebookAccessToken(accessToken);
        }

        //---------------------------------------Google Sign In--------------------------------------------
        /*SignInButton signInButton = findViewById(R.id.sign_in_button);
        signInButton.setSize(SignInButton.SIZE_STANDARD);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.sign_in_button:
                        signIn();
                        break;
                    // ...
                }
            }
        });
        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestProfile()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        // Check for existing Google Sign In account, if the user is already signed in
        // the GoogleSignInAccount will be non-null.
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        updateUI();*/
    }

    public void updateUI(){
        Log.i("TRUTH", "updateUI");
    }

    private void handleFacebookAccessToken(AccessToken token) {
        Log.i("TRUTH", "handleFacebookAccessToken:" + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information

                            Bitmap bmp;

                            Log.i("TRUTH", "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            Bundle params = new Bundle();
                            params.putString("fields", "id,name,email,gender,cover,picture.type(large)");
                            new GraphRequest((AccessToken.getCurrentAccessToken()), "me", params, HttpMethod.GET,
                                    new GraphRequest.Callback() {
                                        @Override
                                        public void onCompleted(GraphResponse response) {
                                            if(response!=null){
                                                try {
                                                    JSONObject data = response.getJSONObject();
                                                    Log.i("TRUTH", "JSON Data : "+data);
                                                    String name = data.getString("name");
                                                    String email = data.getString("email");
                                                    Log.i("TRUTH", "name, email : "+name+" "+email);
                                                    if (data.has("picture")) {
                                                        Log.i("TRUTH", "PP Accessed.");
                                                        final String profilePicUrl = data.getJSONObject("picture").getJSONObject("data").getString("url");
                                                        Intent i = new Intent(MainActivity.this, AfterFBLogin.class);
                                                        i.putExtra("url", profilePicUrl);
                                                        i.putExtra("name", name);
                                                        i.putExtra("email", email);
                                                        i.putExtra("gender", "--ACCESS_DENIED--");
                                                        startActivity(i);
                                                    }

                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        }
                                    }).executeAsync();
                            updateUI();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.i("TRUTH", "signInWithCredential:failure", task.getException());
                            //Toast.makeText(FacebookLoginActivity.this, "Authentication failed.",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    /*private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }*/

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        /*if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }*/
    }

    /*private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            Log.i("TRUTH", "SignedInSuccessfully");
            String url = account.getId();
            Log.i("TRUTH", "URL : "+url);
            /*try{
                URL url = new URL(account.getPhotoUrl().toString());
                Log.i("TRUTH", "URL : "+url);
            }catch (MalformedURLException e){
                Log.i("TRUTH", "Exception : "+e.getMessage());
            }
            // Signed in successfully, show authenticated UI.
            //updateUI();
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.i("TRUTH", "signInResult:failed code=" + e.getStatusCode());
            updateUI();
        }
    }*/
}
