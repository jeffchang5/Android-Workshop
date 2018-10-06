package io.jeffchang.budget;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;

import io.jeffchang.budget.budgetlist.BudgetItem;
import io.jeffchang.budget.budgetlist.BudgetListRecyclerViewAdapter;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView budgetRecyclerView = findViewById(R.id.activity_main_budget_recycler_view);


        // Here we add mock data to test our UI
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


        BudgetListRecyclerViewAdapter adapter = new BudgetListRecyclerViewAdapter(
                budgetItemArrayList
        );
        budgetRecyclerView.setLayoutManager(
                new LinearLayoutManager(this)
        );
        budgetRecyclerView.setAdapter(adapter);
    }
    
}
