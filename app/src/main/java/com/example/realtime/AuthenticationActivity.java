package com.example.realtime;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.realtime.databinding.ActivityAuthenticationBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AuthenticationActivity extends AppCompatActivity {
     ActivityAuthenticationBinding binding;
     String name , email, password;
     DatabaseReference databaseReference;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        binding = ActivityAuthenticationBinding.inflate (getLayoutInflater ());
        setContentView (binding.getRoot ());
        databaseReference = FirebaseDatabase.getInstance ().getReference ("users");

        binding.login.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View v) {
                email = binding.email.getText ().toString ();
                password = binding.password.getText ().toString ();

                login ();


            }
        });

        binding.signUp.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View v) {
                email=binding.email.getText ().toString ();
                password=binding.password.getText ().toString ();
                name=binding.name.getText ().toString ();

                signUp ();
            }
        });
    }
    @Override
    protected void onStart(){
        super.onStart ();
        if(FirebaseAuth.getInstance ().getCurrentUser ()!=null){
            startActivity (new Intent (AuthenticationActivity.this,MainActivity.class));
            finish ();
        }
    }
    private void login () {
        FirebaseAuth
                .getInstance ()
                .signInWithEmailAndPassword (email.trim (),password)
                .addOnSuccessListener (new OnSuccessListener<AuthResult> () {
                    @Override
                    public void onSuccess (AuthResult authResult) {
                        startActivity (new Intent (AuthenticationActivity.this,MainActivity.class));
                        finish ();
                    }
                });



    }

    private void signUp(){
        FirebaseAuth
                .getInstance ()
                .createUserWithEmailAndPassword (email.trim (),password)
                .addOnSuccessListener (new OnSuccessListener<AuthResult> () {
                    @Override
                    public void onSuccess (AuthResult authResult) {
                        UserProfileChangeRequest userProfileChangeRequest =new UserProfileChangeRequest.Builder ().setDisplayName (name).build ();
                        FirebaseUser firebaseUser=FirebaseAuth.getInstance ().getCurrentUser ();
                        firebaseUser.updateProfile (userProfileChangeRequest);
                        UserModel userModel=new UserModel (FirebaseAuth.getInstance ().getUid (), name,email,password);
                        databaseReference.child (FirebaseAuth.getInstance ().getUid ()).setValue (userModel);
                        startActivity (new Intent (AuthenticationActivity.this,MainActivity.class));
                        finish ();
                    }
                });
    }
}