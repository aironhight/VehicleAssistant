package com.aironhight.vehicleassistant;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener{

    FirebaseAuth firebaseAuth;

    private Button buttonRegister;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private EditText editTextPasswordRepeat;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        firebaseAuth = FirebaseAuth.getInstance();

        buttonRegister = (Button) findViewById(R.id.buttonRegister);
        editTextEmail = (EditText) findViewById(R.id.registerEditTextEmail);
        editTextPassword = (EditText) findViewById(R.id.registerEditTextPassword);
        editTextPasswordRepeat = (EditText) findViewById(R.id.registerEditTextPasswordRepeat);
        progressDialog = new ProgressDialog(this);

        buttonRegister.setOnClickListener(this);
    }

    private void register() {

    }

    @Override
    public void onClick(View view) {
        if(view == buttonRegister) {
            register();
        }
    }
}
