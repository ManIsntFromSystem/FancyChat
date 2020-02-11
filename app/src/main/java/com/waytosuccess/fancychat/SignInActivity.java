package com.waytosuccess.fancychat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignInActivity extends AppCompatActivity {

    private static final String TAG = "MyTag";
    private FirebaseAuth mAuth;

    private EditText emailEditText;
    private EditText passwordEditText;
    private EditText passwordConfirmEditText;
    private EditText nameEditText;
    private Button loginSignUpBtn;
    private TextView toggleLoginSignUpTextView;

    private boolean loginModeActive;

    private FirebaseDatabase database;
    private DatabaseReference usersDatabaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        usersDatabaseReference = database.getReference().child("users");

        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        passwordConfirmEditText = findViewById(R.id.passwordConfirmEditText);
        nameEditText = findViewById(R.id.nameEditText);
        loginSignUpBtn = findViewById(R.id.loginSignUpBtn);
        toggleLoginSignUpTextView = findViewById(R.id.toggleLoginSignUpTextView);

        mAuth = FirebaseAuth.getInstance();

        loginSignUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginSignUpUser(emailEditText.getText().toString().trim(),
                        passwordEditText.getText().toString().trim());
            }
        });

        if (mAuth.getCurrentUser() != null) {
            startActivity(new Intent(SignInActivity.this,
                    MainActivity.class));
        }
    }

    private void loginSignUpUser(String email, String password) {
        if (loginModeActive) {
            if (passwordEditText.getText().toString().trim().length() < 7) {
                Toast.makeText(this, "Passwoard must be at least 7 characters", Toast.LENGTH_SHORT).show();
            } else if (emailEditText.getText().toString().trim().equals("")) {
                Toast.makeText(this, "Email mustn't be empty", Toast.LENGTH_SHORT).show();
            } else {
                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    Log.d(TAG, "signInWithEmail:success");
                                    FirebaseUser user = mAuth.getCurrentUser();

                                    Intent intent = new Intent(SignInActivity.this,
                                            MainActivity.class);
                                    intent.putExtra("user_name", nameEditText.getText()
                                            .toString().trim());
                                    startActivity(intent);
                                    //updateUI(user);
                                } else {
                                    Log.w(TAG, "signInWithEmail:failure", task.getException());
                                    Toast.makeText(SignInActivity.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
                                    //updateUI(null);
                                }
                            }
                        });
            }
        } else {
            if (!passwordEditText.getText().toString().trim().equals(
                    passwordConfirmEditText.getText().toString().trim())) {
                Toast.makeText(this, "Passwords aren't the same", Toast.LENGTH_LONG).show();
            } else if (passwordEditText.getText().toString().trim().length() < 7) {
                Toast.makeText(this, "Passwoard must be at least 7 characters", Toast.LENGTH_SHORT).show();
            } else if (emailEditText.getText().toString().trim().equals("")) {
                Toast.makeText(this, "Email mustn't be empty", Toast.LENGTH_SHORT).show();
            } else {
                Log.d(TAG, "Email: " + email + "/n" + "Password: " + password);
                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    Log.d(TAG, "createUserWithEmail:success");
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    createUser(user);

                                    Intent intent = new Intent(SignInActivity.this,
                                            MainActivity.class);
                                    intent.putExtra("user_name", nameEditText.getText()
                                            .toString().trim());
                                    startActivity(intent);
                                    //updateUI(user);
                                } else {
                                    Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                    Toast.makeText(SignInActivity.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
                                    //updateUI(null);
                                }
                            }
                        });
            }
        }
    }

    private void createUser(FirebaseUser firebaseUser) {
        User user = new User();
        user.setId(firebaseUser.getUid());
        user.setEmail(firebaseUser.getEmail());
        user.setName(nameEditText.getText().toString().trim());

        usersDatabaseReference.push().setValue(user);
    }

    public void toggleLoginMode(View view) {

        if (loginModeActive) {
            loginModeActive = false;
            passwordConfirmEditText.setVisibility(View.VISIBLE);
            loginSignUpBtn.setText("Sign Up");
            toggleLoginSignUpTextView.setText("Or, login in");
        } else {
            loginModeActive = true;
            passwordConfirmEditText.setVisibility(View.GONE);
            loginSignUpBtn.setText("Login In");
            toggleLoginSignUpTextView.setText("Or, sign up");
        }
    }
}
