package io.jeffchang.budget.budgetlist;

public class BudgetItem {

    private String id;

    private String name;

    private String category;

    private float amount;

    public BudgetItem(
            String id,
            String name,
            String category,
            float amount
    ) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.amount = amount;

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }
}
