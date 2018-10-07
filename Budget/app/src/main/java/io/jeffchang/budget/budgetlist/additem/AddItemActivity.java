package io.jeffchang.budget.budgetlist.additem;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import io.jeffchang.budget.R;
import io.jeffchang.budget.budgetlist.BudgetItem;

public class AddItemActivity extends AppCompatActivity {

    public static int ADD_ITEM_REQUEST_CODE = 1001;

    public static String BUDGET_ITEM_EXTRA = "BUDGET_ITEM_EXTRA";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        Button finishedButton = findViewById(R.id.activity_add_item_finished_button);

        finishedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra(BUDGET_ITEM_EXTRA, getBudgetItem());
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }

    private BudgetItem getBudgetItem() {
        EditText categoryEditText = findViewById(R.id.activity_add_item_category_text_view);
        EditText titleEditText = findViewById(R.id.activity_add_item_title_text_view);
        EditText amountEditText = findViewById(R.id.activity_add_item_amount_text_view);

        float amount = Float.parseFloat(amountEditText.getText().toString());
        return new BudgetItem(
                "id",
                categoryEditText.getText().toString(),
                titleEditText.getText().toString(),
                amount
        );

    }

}