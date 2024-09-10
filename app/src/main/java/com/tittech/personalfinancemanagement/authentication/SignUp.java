package com.tittech.personalfinancemanagement.authentication;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.tittech.personalfinancemanagement.MainActivity;
import com.tittech.personalfinancemanagement.R;
import com.tittech.personalfinancemanagement.db.DatabaseHelper;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignUp extends AppCompatActivity {
    TextInputEditText edEmail, edPass, edConPass;
    Button btnSignUp;
    TextView btnLogIn;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    DatabaseHelper databaseHelper;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        edEmail = findViewById(R.id.edEmail);
        edPass = findViewById(R.id.edPass);
        edConPass = findViewById(R.id.edConPass);
        btnSignUp = findViewById(R.id.btnSignUp);
        btnLogIn = findViewById(R.id.btnLogIn);

        if (edPass.getText().toString().isEmpty()) edPass.setHint("Password");
        if (edConPass.getText().toString().isEmpty()) edConPass.setHint("Confirm Password");

        sharedPreferences = getSharedPreferences(getString(R.string.app_name), MODE_PRIVATE);
        editor = sharedPreferences.edit();
        databaseHelper = new DatabaseHelper(this);

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = edEmail.getText().toString();
                String password = edPass.getText().toString();
                String confirmPassword = edConPass.getText().toString();
                if (email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty())
                    Toast.makeText(SignUp.this, "All fields are mandatory", Toast.LENGTH_SHORT).show();
                else {
                    if (Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                        if (password.equals(confirmPassword)) {
                            if (isValidPassword(password)) {
                                Boolean checkUserEmail = databaseHelper.checkEmail(email);
                                if (!checkUserEmail) {
                                    Boolean insert = databaseHelper.insertData(email, password);
                                    if (insert) {
                                        String name = email.trim();
                                        String nameWithoutDomain = name.substring(0, name.lastIndexOf('@'));
                                        String modifiedName = nameWithoutDomain.replace('.', '_');
                                        databaseHelper.createUserTable(modifiedName + "_");
                                        editor.putString("tableName", modifiedName);
                                        editor.apply();
                                        Toast.makeText(SignUp.this, "Signup Successfully!", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(SignUp.this, MainActivity.class);
                                        startActivity(intent);
                                        finish();
                                    } else {
                                        Toast.makeText(SignUp.this, "Signup Failed!", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    Toast.makeText(SignUp.this, "User already exists! Please login", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(SignUp.this, "Password is invalid", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(SignUp.this, "Password & Confirm Password not same", Toast.LENGTH_SHORT).show();

                        }
                    } else {
                        edEmail.setError("Enter valid Email address !");
                    }

                }
            }
        });

        btnLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignUp.this, LogIn.class));
                finish();
            }
        });


    }

    //PasswordValidation
    private boolean isValidPassword(String password) {
        String passwordPattern = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{8,}$";
        Pattern pattern = Pattern.compile(passwordPattern);
        Matcher matcher = pattern.matcher(password);
        return matcher.matches();
    }

    //=================================================BackButtonExit
    @SuppressLint("MissingSuperCall")
    @Override
    public void onBackPressed() {

        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(SignUp.this, R.style.AlertDialogTheme);
        builder.setIcon(R.drawable.exit_icon);
        builder.setTitle(R.string.app_name);
        builder.setMessage("   Do you want to Exit ?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                finish();
            }

        });
        builder.setNegativeButton("No", null);
        builder.show();
    }
}