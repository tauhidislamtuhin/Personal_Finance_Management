package com.tittech.personalfinancemanagement;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.tittech.personalfinancemanagement.authentication.LogIn;
import com.tittech.personalfinancemanagement.db.DatabaseHelper;


public class MainActivity extends AppCompatActivity {
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    TextView tvFinalBalance, tvTotalExpense, tvAddExpense, tvShowAllDataExpense, tvTotalIncome, tvAddIncome, tvShowAllDataIncome;
    DatabaseHelper dbHelper;
    String tableName;
    ImageView menu;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvFinalBalance = findViewById(R.id.tvFinalBalance);
        tvTotalExpense = findViewById(R.id.tvTotalExpense);
        tvAddExpense = findViewById(R.id.tvAddExpense);
        tvShowAllDataExpense = findViewById(R.id.tvShowAllDataExpense);
        tvTotalIncome = findViewById(R.id.tvTotalIncome);
        tvAddIncome = findViewById(R.id.tvAddIncome);
        tvShowAllDataIncome = findViewById(R.id.tvShowAllDataIncome);
        menu = findViewById(R.id.menu);

        sharedPreferences = getSharedPreferences("" + getString(R.string.app_name), MODE_PRIVATE);
        editor = sharedPreferences.edit();
        dbHelper = new DatabaseHelper(this);

        tableName = sharedPreferences.getString("tableName", "");


        if (tableName.isEmpty()) {
            startActivity(new Intent(MainActivity.this, LogIn.class));
            finish();
        }


        tvAddExpense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddData.EXPENSE = true;
                startActivity(new Intent(MainActivity.this, AddData.class));

            }
        });

        tvAddIncome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddData.EXPENSE = false;
                startActivity(new Intent(MainActivity.this, AddData.class));

            }
        });

        tvShowAllDataExpense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowData.EXPENSE = true;
                startActivity(new Intent(MainActivity.this, ShowData.class));
            }
        });

        tvShowAllDataIncome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowData.EXPENSE = false;
                startActivity(new Intent(MainActivity.this, ShowData.class));
            }
        });
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 102);

        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(MainActivity.this, menu);
                popupMenu.getMenuInflater().inflate(R.menu.actions, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        if (menuItem.getItemId() == R.id.logOut) {
                            editor.putString("tableName", "");
                            editor.apply();
                            startActivity(new Intent(MainActivity.this, LogIn.class));
                            finish();
                        }
                        return true;
                    }
                });

                popupMenu.show();
            }
        });

    }
    

    //=================================================UpdateUI
    private void updateUI() {
        tvTotalExpense.setText("BDT " + dbHelper.calculateTotalExpense(tableName));
        tvTotalIncome.setText("BDT " + dbHelper.calculateTotalIncome(tableName));

        double balance = dbHelper.calculateTotalIncome(tableName) - dbHelper.calculateTotalExpense(tableName);
        tvFinalBalance.setText("BDT " + balance);

    }

    //=================================================UpdateUI
    @Override
    protected void onPostResume() {
        super.onPostResume();
        updateUI();
    }

    //=================================================BackButtonExit
    @SuppressLint("MissingSuperCall")
    @Override
    public void onBackPressed() {

        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(MainActivity.this, R.style.AlertDialogTheme);
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