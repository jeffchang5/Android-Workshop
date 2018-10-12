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


import java.util.ArrayList;
import java.util.List;

import io.jeffchang.budget.model.BudgetItem;
import io.jeffchang.budget.budgetlist.BudgetListRecyclerViewAdapter;
import io.jeffchang.budget.budgetlist.additem.AddItemActivity;
import io.jeffchang.budget.network.BudgetService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.moshi.MoshiConverterFactory;

public class MainActivity extends AppCompatActivity {

    public String TAG = MainActivity.class.getSimpleName();

    private BudgetListRecyclerViewAdapter adapter;
    private TextView budgetTrackerTextView;

    float budgetLimit = 0;
    private BudgetService budgetService;
    private ArrayList<BudgetItem> budgetItemArrayList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        budgetTrackerTextView = findViewById(R.id.activity_main_budget_tracker_view);

        setUpRetrofit();

        setupRecyclerView();

        // This has to be called after Retrofit and RecyclerView is set up.
        getBudgetFromNetwork();

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
        budgetItemArrayList = new ArrayList<>();

        RecyclerView budgetRecyclerView = findViewById(R.id.activity_main_budget_recycler_view);

        adapter = new BudgetListRecyclerViewAdapter();

        budgetRecyclerView.setLayoutManager(
                new LinearLayoutManager(this)
        );
        budgetRecyclerView.setAdapter(adapter);
    }

    public void setUpRetrofit() {
        Retrofit retrofit =
                new Retrofit.Builder()
                        .baseUrl("https://rocky-fortress-15911.herokuapp.com")
                        .addConverterFactory(MoshiConverterFactory.create())
                        .build();

        budgetService = retrofit.create(BudgetService.class);
    }

    // Only call when budgetService is not null.
    public void getBudgetFromNetwork() {
        budgetService.getBudget().enqueue(new Callback<List<BudgetItem>>() {
            @Override
            public void onResponse(
                    Call<List<BudgetItem>> call,
                    Response<List<BudgetItem>> response) {

                float sum = adapter.addBudgetItemList(response.body());
                setBudgetTrackerTextView(sum);

            }

            @Override
            public void onFailure(Call<List<BudgetItem>> call, Throwable t) {
                Log.e("MainActivity", "Couldn't get budgets");
            }
        });
    }


    // This method will update the database on our back-end and give back our budget that we update
    // our RecyclerViewAdapter.
    public void postBudget(final BudgetItem budgetItem) {
        budgetService.postBudget(budgetItem).enqueue(new Callback<BudgetItem>() {
            @Override
            public void onResponse(Call<BudgetItem> call, Response<BudgetItem> response) {
                budgetItemArrayList.add(budgetItem);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<BudgetItem> call, Throwable t) {
                Log.e("MainActivity", "Couldn't save budgets");
            }
        });

    }

    public void setBudgetTrackerTextView(float budget) {
        int positiveGreenColor = ContextCompat.getColor(this, R.color.green);
        int negativeRedColor = ContextCompat.getColor(this, R.color.red);

        /*
          Equivalent to

          int backgroundColor;
          if (budget >= budgetLimit) {
              backgroundColor = positiveGreenColor;
          } else {
              backgroundColor = negativeRedColor;
          }
         */
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

                postBudget(budgetItem);

                float sum = adapter.addBudgetItem(budgetItem);
                setBudgetTrackerTextView(sum);
            }

        }
    }
}
