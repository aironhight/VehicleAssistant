package com.aironhight.vehicleassistant;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


public class LoginActivity extends AppCompatActivity implements View.OnClickListener{


    FirebaseAuth firebaseAuth;

    private Button buttonLogIn;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private TextView registerTextView;
    private TextView forgottenPasswordTextView;

    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        firebaseAuth = FirebaseAuth.getInstance();

        if(firebaseAuth.getCurrentUser() != null) {
            firebaseAuth.signOut();
        }

        buttonLogIn =  (Button) findViewById(R.id.buttonLogin);
        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        registerTextView = (TextView) findViewById(R.id.textViewRegister);
        forgottenPasswordTextView = (TextView) findViewById(R.id.textViewForgottenPassword);

        buttonLogIn.setOnClickListener(this);
        registerTextView.setOnClickListener(this);
        forgottenPasswordTextView.setOnClickListener(this);

        progressDialog = new ProgressDialog(this);
    }

    @Override
    public void onClick(View view) {
        if(view == buttonLogIn) {
            logIn();
        }

        if(view == registerTextView) {
            startActivity(new Intent(getApplicationContext(), RegisterActivity.class));
            finish();
        }

        if(view == forgottenPasswordTextView) {
            passwordReset();
        }
    }

    private void logIn() {
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        if(TextUtils.isEmpty(email)) {
            // email field is empty, login request is not sent.
            Toast.makeText(this, "Please enter email", Toast.LENGTH_SHORT).show();
            return;
        }

        if(TextUtils.isEmpty(password)) {
            // password field is empty, login request is not sent.
            Toast.makeText(this, "Please enter password", Toast.LENGTH_SHORT).show();
            return;
        }

        progressDialog.setMessage("Logging in...");
        progressDialog.show();
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()) {
                    progressDialog.hide();
                    //logging in is successful. Start the Program activity
                    finish();
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                } else {
                    progressDialog.hide();
                    Toast.makeText(LoginActivity.this, "Email/Password combination doesn't match.", Toast.LENGTH_SHORT);
                }
            }
        });
    }

    private void passwordReset() {
        if(editTextEmail.getText().toString().length()<3) {
            Toast.makeText(this, "Please enter your Email in the email field.", Toast.LENGTH_LONG).show();
        } else {
            progressDialog.setMessage("Please wait...");
            progressDialog.show();
            firebaseAuth.sendPasswordResetEmail(editTextEmail.getText().toString().trim()).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()) {
                        progressDialog.hide();
                        Toast.makeText(LoginActivity.this, "A password reset email was sent to your email address.", Toast.LENGTH_LONG).show();
                    } else {
                        progressDialog.hide();
                        Toast.makeText(LoginActivity.this, "Failed to send password reset email: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }
}
