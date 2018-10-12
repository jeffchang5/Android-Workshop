package io.jeffchang.budget.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.squareup.moshi.Json;

public class BudgetItem implements Parcelable {

    @Json(name = "id")
    private String id;

    @Json(name = "name")
    private String name;

    @Json(name = "category")
    private String category;

    @Json(name = "amount")
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.name);
        dest.writeString(this.category);
        dest.writeFloat(this.amount);
    }

    @Override
    public String toString() {
        return "Budget name: " + this.name;
    }

    private BudgetItem(Parcel in) {
        this.id = in.readString();
        this.name = in.readString();
        this.category = in.readString();
        this.amount = in.readFloat();
    }

    public static final Parcelable.Creator<BudgetItem> CREATOR = new Parcelable.Creator<BudgetItem>() {
        @Override
        public BudgetItem createFromParcel(Parcel source) {
            return new BudgetItem(source);
        }

        @Override
        public BudgetItem[] newArray(int size) {
            return new BudgetItem[size];
        }
    };
}
