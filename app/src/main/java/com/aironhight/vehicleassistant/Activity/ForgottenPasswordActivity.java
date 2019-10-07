package com.aironhight.vehicleassistant.Activity;

import android.app.ProgressDialog;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.aironhight.vehicleassistant.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgottenPasswordActivity extends AppCompatActivity implements View.OnClickListener{

    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;
    private Button resetPasswordButton;
    private EditText emailEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgotten_password);
        firebaseAuth = FirebaseAuth.getInstance();
        resetPasswordButton = findViewById(R.id.resetPasswordButton);
        resetPasswordButton.setOnClickListener(this);
        emailEditText = findViewById(R.id.editTextEmail);
        progressDialog = new ProgressDialog(this);
    }

    private void resetPasswordRequest() {
        progressDialog.setMessage("Please wait...");
        progressDialog.show();
        firebaseAuth.sendPasswordResetEmail(emailEditText.getText().toString().trim()).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    progressDialog.hide();
                    Toast.makeText(ForgottenPasswordActivity.this, "A password reset email was sent to your email address.", Toast.LENGTH_LONG).show();
                } else {
                    progressDialog.hide();
                    Toast.makeText(ForgottenPasswordActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        if(view == resetPasswordButton) {
            resetPasswordRequest();
        }
    }
}
