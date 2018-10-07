package com.sszg.apprenticechef;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private EditText username, password;
    private Button login, signup, forgotPassword;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
        if (mAuth != null) {
            mAuth.signOut();
        }

        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        forgotPassword = findViewById(R.id.forgot_password);
        signup = findViewById(R.id.create_account);
        login = findViewById(R.id.sign_in);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn(username.getText().toString(), password.getText().toString());
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createAccount(username.getText().toString(), password.getText().toString());
            }
        });

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
                        System.out.println("ERROR: " + task.getException());
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
                        System.out.println("ERROR: " + task.getException());
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
            Intent intent = new Intent(this, RecipeListActivity.class);
            startActivity(intent);
        } else {
            username.setText("");
            password.setText("");

            Toast.makeText(getApplicationContext(), "Please retry your email and password", Toast.LENGTH_SHORT).show();
        }
    }
}
