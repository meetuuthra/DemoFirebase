package com.example.meetuuthra.socialnetworkingapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener, View.OnClickListener{

    GoogleApiClient mGoogleApiClient;
    TextView statusTextView;
    SignInButton signInButton;
    Button signOutButton;
    private static final int RC_SIGN_IN = 9001;
    private static final String TAG = "SignInActivity";
    EditText editUserName, editPassword;
    Button loginButton;
    TextView signUp;
    private FirebaseAuth firebaseauth;
    FirebaseUser muser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editUserName = (EditText) findViewById(R.id.eTUsername);
        editPassword = (EditText) findViewById(R.id.eTPassword);
        loginButton = (Button) findViewById(R.id.loginButton);
        firebaseauth = FirebaseAuth.getInstance();

        signUp = (TextView) findViewById(R.id.tVSignUp);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (IsNullorEmpty(editUserName.getText().toString()) || IsNullorEmpty(editPassword.getText().toString())) {
                    Toast.makeText(MainActivity.this, "Enter valid Email and Password", Toast.LENGTH_LONG).show();
                }
                else{
                    String email=editUserName.getText().toString();
                    String pass=editPassword.getText().toString();


                    firebaseauth.signInWithEmailAndPassword(email,pass)
                            .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (!task.isSuccessful()) {
                                        // there was an error
                                        Log.d("error","error");
                                        Toast.makeText(MainActivity.this, "Please Check your Credentials", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Log.d("Success","Success");
                                        Intent intent = new Intent(MainActivity.this, Contacts.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                }
                            });
                }

            }


        });

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,SignUpActivity.class);
                startActivity(intent);
            }
        });



        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this,this)
                .addApi(Auth.GOOGLE_SIGN_IN_API,gso)
                .build();

        statusTextView = (TextView) findViewById(R.id.status_textview);


        signInButton = (SignInButton)findViewById(R.id.sign_in_button);
        signInButton.setOnClickListener(this);

        signOutButton = (Button) findViewById(R.id.signOutButton);
        signOutButton.setOnClickListener(this);


    }

    

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.sign_in_button:
                signIn();
            case R.id.signOutButton:
                signOut();
        }
    }

    private void signIn(){
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent,RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode== RC_SIGN_IN){
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);


        }
    }

    private void handleSignInResult(GoogleSignInResult result){
        Log.d(TAG,"handleSignInResult:"+result.isSuccess());
        if(result.isSuccess()){
            GoogleSignInAccount acct = result.getSignInAccount();
            statusTextView.setText("Hello, "+ acct.getDisplayName());

            Intent intent = new Intent(MainActivity.this,Contacts.class);
            startActivity(intent);

        }else{
        }
    }
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

        Log.d(TAG,"onConnectionFailed:"+ connectionResult);

    }

    private void signOut(){
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(new ResultCallback<Status>() {
            @Override
            public void onResult(@NonNull Status status) {
                statusTextView.setText("Signed out");
            }
        });
    }
    private boolean IsNullorEmpty(String str) {
        {
            return (str == null || str.isEmpty());
        }
    }


}
