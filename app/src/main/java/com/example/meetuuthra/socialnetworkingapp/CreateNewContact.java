package com.example.meetuuthra.socialnetworkingapp;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class CreateNewContact extends AppCompatActivity {

    EditText editCName,editCPhone,editCemail;
    Button buttonCSave;
    FirebaseAuth mAuth;
    ContactsModel contactsModel;
    ArrayList<ContactsModel> contactArraylist = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_contact);

        editCName = (EditText)findViewById(R.id.editCName);
        editCPhone = (EditText)findViewById(R.id.editCPhone);
        editCemail = (EditText)findViewById(R.id.editCemail);

        buttonCSave = (Button) findViewById(R.id.buttonCSave);

        buttonCSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (IsNullorEmpty(editCName.getText().toString()) || IsNullorEmpty(editCPhone.getText().toString())
                        || IsNullorEmpty(editCemail.getText().toString())) {
                    Toast.makeText(CreateNewContact.this, "All fields are Mandatory", Toast.LENGTH_LONG).show();
                }

                else{
                    contactaddtodb(editCName.getText().toString(),editCPhone.getText().toString(),editCemail.getText().toString());
                    Intent intent = new Intent(CreateNewContact.this,Contacts.class);
                    intent.putExtra("get",contactArraylist);
                    Log.d("size",contactArraylist.size()+"");
                    startActivity(intent);


                }
            }
        });

    }

    void contactaddtodb(String name,String phone,String email){
        DatabaseReference Ref,insref;
        DatabaseReference dbref = FirebaseDatabase.getInstance().getReference();
        Uri file;
        mAuth = FirebaseAuth.getInstance();

        StorageReference mStorageRef;
        mStorageRef = FirebaseStorage.getInstance().getReference();

        /*file =imageURI;
        Log.d("imageuri",imageURI.toString());
        StorageReference riversRef = mStorageRef.child(name+" Contact");
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



        ContactsModel contactsModel = new ContactsModel();
        contactsModel.setContactName(name);
        contactsModel.setContactPhone(phone);
        contactsModel.setContactEmail(email);

        //contactsModel.setCimage(cimage);

        insref = dbref.child("contacts");
        Ref = insref.push();

        Ref.setValue(contactsModel);


    }


    private boolean IsNullorEmpty(String str) {
        return (str == null || str.isEmpty());
    }
}
