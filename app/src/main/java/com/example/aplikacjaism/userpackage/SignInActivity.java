package com.example.aplikacjaism.userpackage;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.aplikacjaism.R;
import com.example.aplikacjaism.pizzapackage.PizzaListActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignInActivity extends AppCompatActivity {
    private FirebaseDatabase mDatabase;
    private DatabaseReference mUsers;

    private EditText mEmail;
    private EditText mPassword;

    private Button mSignInButton;
    private Button mRegisterButton;

    private ProgressBar mProgressBar;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_in_activity);

        mAuth = FirebaseAuth.getInstance();

        mDatabase = FirebaseDatabase.getInstance();
        mUsers = mDatabase.getReference("users");

        mEmail = (EditText) findViewById(R.id.email);
        mPassword = (EditText) findViewById(R.id.password);

        mSignInButton = (Button) findViewById(R.id.signInButton);
        mRegisterButton = (Button) findViewById(R.id.registerButton);

        mProgressBar = (ProgressBar) findViewById(R.id.loadingProgressBar);

        mSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isEmpty()) return;
                inProgress(true);
                mAuth.signInWithEmailAndPassword(mEmail.getText().toString(), mPassword.getText().toString())
                        .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                            @Override
                            public void onSuccess(AuthResult authResult) {

                                Toast.makeText(SignInActivity.this, "Zalogowano!", Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(SignInActivity.this, PizzaListActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                                finish();
                                return;
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        inProgress(false);
                        Toast.makeText(SignInActivity.this, "Logowanie się nie powiodło!" + e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            }
        });

        mRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isEmpty()) return;
                inProgress(true);
                mAuth.createUserWithEmailAndPassword(mEmail.getText().toString(), mPassword.getText().toString())
                        .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                            @Override
                            public void onSuccess(AuthResult authResult) {
                                User user = new User();

                                String uid = mAuth.getUid();
                                user.setAdmin(false);
                                user.setEmail(mEmail.getText().toString());
                                mUsers.child(uid).setValue(user);

                                Toast.makeText(SignInActivity.this, "Zarejestrowano!", Toast.LENGTH_LONG).show();
                                inProgress(false);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        inProgress(false);
                        Toast.makeText(SignInActivity.this, "Rejestracja się nie powiodła!" + e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            }
        });


    }

    private void inProgress(boolean x) {
        if (x) {
            mProgressBar.setVisibility(View.VISIBLE);
            mSignInButton.setEnabled(false);
            mRegisterButton.setEnabled(false);
        } else {
            mProgressBar.setVisibility(View.GONE);
            mSignInButton.setEnabled(true);
            mRegisterButton.setEnabled(true);
        }
    }

    private boolean isEmpty() {
        if (TextUtils.isEmpty(mEmail.getText().toString())) {
            mEmail.setError("Pole jest wymagane!");
            return true;
        }
        if (TextUtils.isEmpty(mPassword.getText().toString())) {
            mPassword.setError("Pole jest wymagane!");
            return true;
        }
        return false;
    }

}
