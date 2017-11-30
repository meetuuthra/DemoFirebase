package com.example.meetuuthra.socialnetworkingapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Contacts extends AppCompatActivity {

    ImageButton imageAddContact;
    RecyclerView listContact;
    FirebaseAuth mAuth;
    DatabaseReference dbref = FirebaseDatabase.getInstance().getReference();
    //ArrayList<ContactsModel> contactList = new ArrayList<ContactsModel>();
    ArrayList<ContactsModel> contactArrayList;
    DatabaseReference insref;
    FirebaseUser mUser;
    ContactsListAdapter adapter;
    TextView currentUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);

        imageAddContact = (ImageButton) findViewById(R.id.imageAddContact);
        listContact = (RecyclerView) findViewById(R.id.listContact);
        currentUser = (TextView) findViewById(R.id.currentUSER);

        imageAddContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Contacts.this,CreateNewContact.class);
                startActivityForResult(intent,9);
            }
        });

       /* Name = (TextView) findViewById(R.id.tVName);
        Phone = (TextView) findViewById(R.id.tVPhone);
        Email = (TextView) findViewById(R.id.tVEmail);*/

       // ContactsModel c = new ContactsModel("Meetu","muthra","asdf");


       // contactList.add(c);

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getBaseContext());
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        listContact.setLayoutManager(mLayoutManager);



        mAuth = FirebaseAuth.getInstance();
       mUser=  mAuth.getCurrentUser();

        if(mUser!=null){

           String name=  mUser.getDisplayName();
        }




        insref = dbref.child("Users");


        insref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<Users> users = new ArrayList<Users>();
                for(DataSnapshot ds: dataSnapshot.getChildren()){
                    Users user = ds.getValue(Users.class);
                    users.add(user);
                    //String user_id = mAuth.getCurrentUser().getUid();
                    //String firstname = (String)(dataSnapshot.child("Users").child(user_id).child("fname").getValue());
                   //Log.d("fname", (String) dataSnapshot.child("fname").getValue());
                   // Log.d("firstname",firstname);
                   // Log.d("fname", String.valueOf(ds.child("fname").getValue()));
                    String fname = String.valueOf(ds.child("fname").getValue());
                    String lname = String.valueOf(ds.child("lname").getValue());
                    currentUser.setText(fname+" "+lname);



                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



        insref = dbref.child("contacts");
        insref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<ContactsModel> contactList1 = new ArrayList<ContactsModel>();
                for(DataSnapshot ds: dataSnapshot.getChildren()){
                    ContactsModel post = ds.getValue(ContactsModel.class);
                    contactList1.add(post);
                }
                Log.d("otherList",contactList1.size()+"");
                contactArrayList = contactList1;
                FillAppsList(contactArrayList);
                Log.d("postModelSize",contactArrayList.size()+"");





            }





            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK){
            ContactsModel contact = (ContactsModel) data.getExtras().getSerializable("get");
            contactArrayList.add(contact);
            String user_id = mAuth.getCurrentUser().getUid();
            DatabaseReference update = FirebaseDatabase.getInstance().getReference().child("Users").child(user_id).child("Contacts");
            update.setValue(contactArrayList);
            adapter.notifyDataSetChanged();


        }
    }

    public void FillAppsList(final List<ContactsModel> contactList){

        mUser=mAuth.getCurrentUser();

        if(mUser !=null) {
            String userId = mUser.getUid();
            String emailid = mUser.getEmail();

            //Log.d("CurrentUserEmailId",mUser.getDisplayName());
        }

    ArrayList<ContactsModel> finallist=new ArrayList<ContactsModel>();

        if(contactList!=null){
            for(int i=0;i<contactList.size();i++){
                Log.d("insideFilllist",contactList.get(i).getContactName()+"");
                finallist.add(contactList.get(i));

            }

        }
        Log.d("finallist",finallist.toString());
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getBaseContext());
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        listContact.setLayoutManager(mLayoutManager);
        adapter = new ContactsListAdapter(this,finallist);
        listContact.setAdapter(adapter);
    }

}
