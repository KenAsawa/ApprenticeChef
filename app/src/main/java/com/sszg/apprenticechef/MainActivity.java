package com.sszg.apprenticechef;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();

    }

    public void signIn(String email, String password) {
        if (email != null && password != null && !email.isEmpty() && !password.isEmpty()) {
            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        updateUI(user);
                    } else {
                        Toast.makeText(getApplicationContext(), "" + task.getException(), Toast.LENGTH_SHORT).show();
                        updateUI(null);
                    }
                }
            });
        } else {
            Toast.makeText(getApplicationContext(), "Please enter non empty email and password", Toast.LENGTH_SHORT).show();
        }
    }

    public void createAccount(final String email, String password) {
        if (email != null && password != null && !email.isEmpty() && !password.isEmpty()) {
            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        System.out.println("CREATED NEW USER " + email);
                        FirebaseUser user = mAuth.getCurrentUser();
                        updateUI(user);

                    } else {
                        Toast.makeText(getApplicationContext(), "" + task.getException(), Toast.LENGTH_SHORT).show();
                        updateUI(null);
                    }
                }
            });
        } else {
            Toast.makeText(getApplicationContext(), "Please enter non empty email and password", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            System.out.println("USER LOGGED IN");
            updateUI(currentUser);
        }
    }

    public void updateUI(FirebaseUser user) {
        if (user != null) {
            //FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            String email = user.getEmail();
            String name = user.getDisplayName();
            Uri photoUrl = user.getPhotoUrl();
            boolean emailVerified = user.isEmailVerified();
            String uid = user.getUid();
            System.out.println("USER FIREBASE: " + user);
        } else {

        }
    }
}
