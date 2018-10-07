package io.jeffchang.budget;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.ArrayList;

import io.jeffchang.budget.budgetlist.BudgetItem;
import io.jeffchang.budget.budgetlist.BudgetListRecyclerViewAdapter;
import io.jeffchang.budget.budgetlist.additem.AddItemActivity;

public class MainActivity extends AppCompatActivity {

    public String TAG = MainActivity.class.getSimpleName();

    private BudgetListRecyclerViewAdapter adapter;
    private TextView budgetTrackerTextView;

    float budgetLimit = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        budgetTrackerTextView = findViewById(R.id.activity_main_budget_tracker_view);

        setupRecyclerView();

        Button addButton = findViewById(R.id.activity_main_add_button);


        final EditText budgetLimitEditText = findViewById(R.id.activity_main_budget_edit_text);

        budgetLimitEditText.addTextChangedListener(new TextWatcher() {

            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String input = s.toString();
                if (!input.startsWith("$")) {
                    input = "$" + input;

                    // Prevents the user from deleting past the dollar sign symbol.
                    budgetLimitEditText.setText(input);
                    budgetLimitEditText.setSelection(1);
                }
                try {
                    // Parses float of all characters after the dollar sign.
                    input = input.substring(1);
                    budgetLimit = Float.parseFloat(input);
                } catch (NumberFormatException e) {
                    // If the string is empty or invalid, treat the limit as zero.
                    budgetLimit = 0;
                }
                setBudgetTrackerTextView(adapter.getSum());
            }
        });

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(
                        MainActivity.this,
                        AddItemActivity.class
                );
                startActivityForResult(intent, AddItemActivity.ADD_ITEM_REQUEST_CODE);
            }
        });
    }

    private void setupRecyclerView() {
        // Here we add mock data to test our UI.
        ArrayList<BudgetItem> budgetItemArrayList = new ArrayList<>();

        BudgetItem gasItem = new BudgetItem(
                "gas", "BP", "Utility", -43
        );
        budgetItemArrayList.add(gasItem);

        BudgetItem tacoBellItem = new BudgetItem(
                "taco_bell", "Taco Bell", "Food", -8
        );
        budgetItemArrayList.add(tacoBellItem);

        BudgetItem payCheck = new BudgetItem(
                "pay_check", "Pay Check", "Income", 3000
        );
        budgetItemArrayList.add(payCheck);

        RecyclerView budgetRecyclerView = findViewById(R.id.activity_main_budget_recycler_view);

        adapter = new BudgetListRecyclerViewAdapter(
                budgetItemArrayList
        );
        setBudgetTrackerTextView(adapter.getSum());

        budgetRecyclerView.setLayoutManager(
                new LinearLayoutManager(this)
        );
        budgetRecyclerView.setAdapter(adapter);
    }


    public void setBudgetTrackerTextView(float budget) {
        int positiveGreenColor = ContextCompat.getColor(this, R.color.green);
        int negativeRedColor = ContextCompat.getColor(this, R.color.red);

        int backgroundColor = (budget >= budgetLimit) ? positiveGreenColor: negativeRedColor;
        budgetTrackerTextView.setBackgroundColor(backgroundColor);

        String budgetText = Float.toString(budget - budgetLimit);
        budgetTrackerTextView.setText("$" + budgetText);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == AddItemActivity.ADD_ITEM_REQUEST_CODE) {
            Log.d(MainActivity.class.getSimpleName(), "Result from AddItemActivity");

            if (data != null) {
                Bundle args = data.getExtras();
                BudgetItem budgetItem = args.getParcelable(
                        AddItemActivity.BUDGET_ITEM_EXTRA
                );
                Log.d(TAG, budgetItem.toString());

                float sum = adapter.addBudgetItem(budgetItem);
                setBudgetTrackerTextView(sum);
            }

        }
    }
}
