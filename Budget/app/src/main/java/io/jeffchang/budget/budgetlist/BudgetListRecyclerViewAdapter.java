package io.jeffchang.budget.budgetlist;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import io.jeffchang.budget.R;

public class BudgetListRecyclerViewAdapter extends RecyclerView.Adapter<
        BudgetListRecyclerViewAdapter.BudgetListViewHolder> {

    private ArrayList<BudgetItem> budgetItems;

    float sum = 0;

    public BudgetListRecyclerViewAdapter(ArrayList<BudgetItem> budgetItems) {
        this.budgetItems = budgetItems;

        for (int i = 0; i < budgetItems.size(); i++) {
            sum += budgetItems.get(i).getAmount();
        }
    }

    @NonNull
    @Override
    public BudgetListViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Context context = viewGroup.getContext();
        View budgetItem = LayoutInflater
                .from(context)
                .inflate(R.layout.item_budget, viewGroup, false);

        BudgetListViewHolder viewHolder = new BudgetListViewHolder(budgetItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull BudgetListViewHolder viewHolder, int i) {
        viewHolder.bind(budgetItems.get(i));
    }

    @Override
    public int getItemCount() {
        return budgetItems.size();
    }

    public float getSum() {
        return sum;
    }

    // Returns sum of all balances.
    public float addBudgetItem(BudgetItem budgetItem) {
        budgetItems.add(budgetItem);
        sum += budgetItem.getAmount();

        notifyDataSetChanged();
        return sum;
    }

    class BudgetListViewHolder extends RecyclerView.ViewHolder {

        private final TextView titleTextView;

        private final TextView categoryTextView;

        private final TextView amountTextView;

        BudgetListViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.item_budget_title_text_view);

            categoryTextView = itemView.findViewById(R.id.item_budget_category_text_view);

            amountTextView = itemView.findViewById(R.id.item_budget_amount_text_view);
        }

        public void bind(BudgetItem budgetItem) {
            titleTextView.setText(budgetItem.getName());
            categoryTextView.setText(budgetItem.getCategory());

            String amount = Float.toString(
                    budgetItem.getAmount()
            );
            amountTextView.setText(amount);
        }
    }
}
