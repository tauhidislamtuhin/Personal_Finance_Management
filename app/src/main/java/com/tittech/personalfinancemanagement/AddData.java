package com.tittech.personalfinancemanagement;

import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.tittech.personalfinancemanagement.db.DatabaseHelper;

public class AddData extends AppCompatActivity {
    TextView tvTitle;
    EditText edAmount, edReason;
    Button button;
    DatabaseHelper dbHelper;
    public static boolean EXPENSE = true;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_data);
        tvTitle = findViewById(R.id.tvTitle);
        edAmount = findViewById(R.id.edAmount);
        edReason = findViewById(R.id.edReason);
        button = findViewById(R.id.button);
        dbHelper = new DatabaseHelper(this);

        sharedPreferences = getSharedPreferences("" + getString(R.string.app_name), MODE_PRIVATE);
        editor = sharedPreferences.edit();

        edAmount.getBackground().mutate().setColorFilter(ContextCompat.getColor(this, R.color.text_color), PorterDuff.Mode.SRC_ATOP);
        edReason.getBackground().mutate().setColorFilter(ContextCompat.getColor(this, R.color.text_color), PorterDuff.Mode.SRC_ATOP);

        String tableName = sharedPreferences.getString("tableName", "");
        tvTitle.setText(tableName);

        if (EXPENSE == true) tvTitle.setText("Add Expense");
        if (EXPENSE == false) tvTitle.setText("Add Income");


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String sAmount = edAmount.getText().toString();
                String reason = edReason.getText().toString();
                // double amount = Double.parseDouble(sAmount);

                if (sAmount.isEmpty()) {
                    edAmount.setError("Enter Your Amount");

                } else {
                    if (reason.isEmpty()) {
                        edReason.setError("Enter Your Reason");

                    } else {
                        double amount = Double.parseDouble(sAmount);
                        if (EXPENSE) {

                            double balance = dbHelper.calculateTotalIncome(tableName) - dbHelper.calculateTotalExpense(tableName);
                            if (balance - amount > 0) {
                                boolean expenseInserted = dbHelper.addExpense(amount, reason, tableName);
                                if (expenseInserted) {
                                    tvTitle.setText("Expense Added");
                                    edAmount.setText("");
                                    edReason.setText("");
                                    Toast.makeText(AddData.this, "Expense Added", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(AddData.this, "Balance Insufficient", Toast.LENGTH_SHORT).show();
                            }

                        } else {
                            boolean incomeInserted = dbHelper.addIncome(amount, reason, tableName);

                            if (incomeInserted) {
                                tvTitle.setText("Income Added");
                                edAmount.setText("");
                                edReason.setText("");
                                Toast.makeText(AddData.this, "Income Added", Toast.LENGTH_SHORT).show();
                            } else
                                Toast.makeText(AddData.this, "something wrong", Toast.LENGTH_SHORT).show();

                        }
                    }
                }
/*                if (EXPENSE) {
                    double balance = dbHelper.calculateTotalIncome(tableName) - dbHelper.calculateTotalExpense(tableName);
                    if (balance - amount > 0) {
                        boolean expenseInserted = dbHelper.addExpense(amount, reason, tableName);
                        if (expenseInserted) {
                            tvTitle.setText("Expense Added");
                            Toast.makeText(AddData.this, "Expense Added", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(AddData.this, "Balance Insufficient", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    boolean incomeInserted = dbHelper.addIncome(amount, reason, tableName);

                    if (incomeInserted) {
                        tvTitle.setText("Income Added");
                        Toast.makeText(AddData.this, "Income Added", Toast.LENGTH_SHORT).show();
                    } else
                        Toast.makeText(AddData.this, "something wrong", Toast.LENGTH_SHORT).show();

                }*/


            }
        });


    }
}