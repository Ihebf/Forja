package com.example.forjav2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;

import java.util.regex.Pattern;


public class Register extends AppCompatActivity {

    private MaterialButton btnLinkToLogin;
    private MaterialButton btnRegister;
    private TextInputEditText firstName;
    private TextInputEditText lastName;
    private TextInputEditText email;
    private TextInputEditText password;
    private ProgressBar pb;

    private FirebaseAuth mAuth;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        pb = findViewById(R.id.progressBar);
        btnLinkToLogin = (MaterialButton) findViewById(R.id.btnLinkToLoginScreen);
        btnLinkToLogin.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Intent i = new Intent(Register.this, Login.class);
                startActivity(i);
            }
        });
        mAuth = FirebaseAuth.getInstance();
        firstName = (TextInputEditText) findViewById(R.id.rFirstName);
        lastName = (TextInputEditText) findViewById(R.id.rLastName);
        email = (TextInputEditText) findViewById(R.id.rEditEmail);
        password =(TextInputEditText) findViewById(R.id.rEditPassword);

        btnRegister = (MaterialButton) findViewById(R.id.btnRegister);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });



    }

    private void registerUser() {
        if(checkfileds()==1) return;
        pb.setVisibility(View.VISIBLE);
            mAuth.createUserWithEmailAndPassword(email.getText().toString(),password.getText().toString()).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    pb.setVisibility(View.GONE);
                    if(task.isSuccessful()){
                        Toast.makeText(getApplicationContext(),"User registered successfull",Toast.LENGTH_SHORT).show();
                    }else {
                        if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                            Toast.makeText(getApplicationContext(), "You are already registered", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(),Home.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);

                        } else {
                            Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            });
    }

    private int checkfileds() {
        int t=0;
        if(firstName.getText().toString().trim().isEmpty()){
            firstName.setError("Please enter your first name");
            firstName.requestFocus();
            t=1;
        }

        if(lastName.getText().toString().trim().isEmpty()){
            lastName.setError("Please enter your last name");
            lastName.requestFocus();
            t=1;
        }

        if(email.getText().toString().trim().isEmpty()){
            email.setError("Email is required");
            email.requestFocus();
            t=1;
        }else if(!isValid(email.getText().toString())){
            email.setError("Please set a valid email");
            email.requestFocus();
            t=1;
        }

        if(password.getText().toString().trim().isEmpty()){
            password.setError("Password is required");
            password.requestFocus();
            t=1;
        }
        if (password.getText().toString().length()<6){
            password.setError("Minimum lenght of password should be 6");
            email.requestFocus();
            t=1;
        }
        return t;
    }

    public static boolean isValid(String email)
    {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\."+
                "[a-zA-Z0-9_+&*-]+)*@" +
                "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                "A-Z]{2,7}$";

        Pattern pat = Pattern.compile(emailRegex);
        if (email == null)
            return false;
        return pat.matcher(email).matches();
    }

}
