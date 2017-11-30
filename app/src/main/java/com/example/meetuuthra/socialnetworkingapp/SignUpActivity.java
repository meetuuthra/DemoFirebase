package com.example.meetuuthra.socialnetworkingapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SignUpActivity extends AppCompatActivity {

    EditText firstName,lastName,userName,password,editRPasswordRepeat;
    Button rregister,buttonCancel;
    FirebaseAuth mAuth;
    ArrayList<ContactsModel> clist = new ArrayList<>();
    ContactsModel C = new ContactsModel();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        clist.add(C);

        firstName = (EditText) findViewById(R.id.editRFirstName);
        lastName = (EditText) findViewById(R.id.editRLastName);
        userName = (EditText) findViewById(R.id.editRUserName);
        password = (EditText) findViewById(R.id.editRPassword);
        rregister = (Button) findViewById(R.id.buttonRRegister);
        buttonCancel=(Button) findViewById(R.id.buttonCancel);
        editRPasswordRepeat = (EditText) findViewById(R.id.editRPasswordRepeat);
        mAuth = FirebaseAuth.getInstance();

        rregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (IsNullorEmpty(firstName.getText().toString()) || IsNullorEmpty(lastName.getText().toString())
                        || IsNullorEmpty(userName.getText().toString()) || IsNullorEmpty(password.getText().toString())|| IsNullorEmpty(editRPasswordRepeat.getText().toString())) {
                    Toast.makeText(SignUpActivity.this, "All fields are Mandatory", Toast.LENGTH_LONG).show();
                } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(userName.getText().toString()).matches()) {
                    Toast.makeText(SignUpActivity.this, "Invalid Email", Toast.LENGTH_LONG).show();

                } else if (!(password.getText().toString().equals(editRPasswordRepeat.getText().toString()))) {
                    Toast.makeText(SignUpActivity.this, "Password and Repeat password not matches", Toast.LENGTH_LONG).show();}
                else{
                    Log.d("testwo","testtwo");
                    useraddtodb(firstName.getText().toString(),lastName.getText().toString(),userName.getText().toString(),password.getText().toString(),mAuth.getCurrentUser().getUid());
                }
            }

        });

    }

    private void useraddtodb(final String firstname, final String lastname, final String username, final String pass,final String id) {

        mAuth.createUserWithEmailAndPassword(username, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (!task.isSuccessful()) {
                    Toast.makeText(SignUpActivity.this, "Failed SignUp",
                            Toast.LENGTH_SHORT).show();
                }
                else {
                    Log.d("successSignup","success");
                    DatabaseReference dbref = FirebaseDatabase.getInstance().getReference();
                    DatabaseReference insref;
                    DatabaseReference Ref;
                  //  Uri file = Uri.fromFile(new File(imageURI.toString()));
                    StorageReference mStorageRef;
                    mStorageRef = FirebaseStorage.getInstance().getReference();

                    //file =imageURI;
                    /*Log.d("imageuri",imageURI.toString());
                    StorageReference riversRef = mStorageRef.child(username+" profile");
                    riversRef.putFile(file)
                            .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    // Get a URL to the uploaded content
                                    Uri downloadUrl = taskSnapshot.getDownloadUrl();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception exception) {
                                    // Handle unsuccessful uploads
                                    // ...
                                }
                            });
                    File localFile = null;
                    try {
                        localFile = File.createTempFile("images", "jpg");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    riversRef.getFile(localFile)
                            .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                    // Successfully downloaded data to local file
                                    // ...
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            // Handle failed download
                            // ...
                        }
                    });*/

                    mAuth = FirebaseAuth.getInstance();
                    Users user = new Users();
                    user.setFirstname(firstname);
                    user.setLastname(lastname);
                    user.setUsername(username);
                    user.setPassword(pass);
                    user.setContacts(clist);
                    //user.setProfilepic(profilepic.toString());


                    final DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Users").child(id);
                    Map newpost = new HashMap();
                    newpost.put("fname", firstname);
                    newpost.put("lname", lastname);
                    newpost.put("username", username);
                    newpost.put("Contacts",clist);
                    newpost.put("id",id);


                    ref.setValue(newpost);




                    Intent intent = new Intent(SignUpActivity.this,Contacts.class);
                    startActivity(intent);
                }
            }
        });

    }

    private boolean IsNullorEmpty(String str) {
        return (str == null || str.isEmpty());
    }
}
