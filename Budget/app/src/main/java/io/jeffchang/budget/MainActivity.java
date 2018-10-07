package io.jeffchang.budget;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

import io.jeffchang.budget.budgetlist.BudgetItem;
import io.jeffchang.budget.budgetlist.BudgetListRecyclerViewAdapter;
import io.jeffchang.budget.budgetlist.additem.AddItemActivity;

public class MainActivity extends AppCompatActivity {

    public String TAG = MainActivity.class.getSimpleName();

    private BudgetListRecyclerViewAdapter adapter;
    private TextView budgetTrackerTextView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        budgetTrackerTextView = findViewById(R.id.activity_main_budget_tracker_view);

        setupRecyclerView();

        Button addButton = findViewById(R.id.activity_main_add_button);

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

        int backgroundColor = (budget >= 0) ? positiveGreenColor: negativeRedColor;
        budgetTrackerTextView.setBackgroundColor(backgroundColor);

        String budgetText = Float.toString(budget);
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
