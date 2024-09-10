package com.tittech.personalfinancemanagement.authentication;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.tittech.personalfinancemanagement.MainActivity;
import com.tittech.personalfinancemanagement.R;
import com.tittech.personalfinancemanagement.db.DatabaseHelper;

public class LogIn extends AppCompatActivity {
    TextInputEditText edEmail, edPass;
    Button btnLogIn;
    TextView btnSingUp;
    DatabaseHelper databaseHelper;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    LinearLayout card;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        edEmail = findViewById(R.id.edEmail);
        edPass = findViewById(R.id.edPass);
        btnLogIn = findViewById(R.id.btnLogIn);
        btnSingUp = findViewById(R.id.btnSingUp);
        card = findViewById(R.id.card);

        sharedPreferences = getSharedPreferences("" + getString(R.string.app_name), MODE_PRIVATE);
        editor = sharedPreferences.edit();
        databaseHelper = new DatabaseHelper(this);

        if (edPass.getText().toString().isEmpty()) edPass.setHint("Password");


        btnLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = edEmail.getText().toString();
                String password = edPass.getText().toString();

                if (email.isEmpty() || password.isEmpty())
                    Toast.makeText(LogIn.this, "All fields are mandatory", Toast.LENGTH_SHORT).show();
                else {
                    if (Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                        Boolean checkCredentials = databaseHelper.checkEmailPassword(email, password);

                        if (checkCredentials) {
                            Toast.makeText(LogIn.this, "Login Successfully!", Toast.LENGTH_SHORT).show();
                            String name = email.trim();
                            String nameWithoutDomain = name.substring(0, name.lastIndexOf('@'));
                            String modifiedName = nameWithoutDomain.replace('.', '_');
                            databaseHelper.createUserTable(modifiedName + "_");
                            editor.putString("tableName", modifiedName);
                            editor.apply();
                            startActivity(new Intent(LogIn.this, MainActivity.class));
                            finish();

                        } else {
                            Toast.makeText(LogIn.this, "Invalid User", Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        edEmail.setError("Enter valid Email address !");

                    }

                }
            }
        });

        btnSingUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LogIn.this, SignUp.class));
/*                Animation animation = AnimationUtils.loadAnimation(getApplicationContext()
                        , R.anim.fade_in);
                edEmail.startAnimation(animation);
                edPass.startAnimation(animation);
                btnLogIn.startAnimation(animation);
                btnSingUp.startAnimation(animation);*/
                finish();
            }
        });

    }

    //=================================================BackButtonExit
    @SuppressLint("MissingSuperCall")
    @Override
    public void onBackPressed() {

        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(LogIn.this, R.style.AlertDialogTheme);
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