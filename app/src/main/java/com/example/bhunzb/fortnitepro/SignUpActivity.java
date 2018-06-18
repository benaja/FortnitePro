package com.example.bhunzb.fortnitepro;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;

import java.util.regex.Pattern;

public class SignUpActivity extends AppCompatActivity {

    EditText newUserName, newPassword;
    Button registration;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        findViewById(R.id.registButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (view.equals(registration)){
                    registrationComplete();
                }
            }
        });

        registration = findViewById(R.id.registButton);
        newUserName = findViewById(R.id.newUsername);
        newPassword = findViewById(R.id.newPassword);
    }

    private void registrationComplete(){
        String username = newUserName.getText().toString();
        String password = newPassword.getText().toString();

        if(username.isEmpty()){
        newUserName.setError("type in a username!");
        newUserName.requestFocus();
        return;
        }

        if(password.isEmpty()){
            newPassword.setError("type in ur password!");
            newPassword.requestFocus();
            return;
        }

        mAuth.createUserWithEmailAndPassword(username, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(getApplicationContext(),"Registration done", Toast.LENGTH_SHORT);
                } else {
                    Toast.makeText(getApplicationContext(),"Registration failed", Toast.LENGTH_SHORT);
                }
            }
        });
    }
}
