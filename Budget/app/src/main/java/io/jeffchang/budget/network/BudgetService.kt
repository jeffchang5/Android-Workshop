package io.jeffchang.budget.network

import io.jeffchang.budget.model.BudgetItem
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

/**
 * This is what Retrofit uses as a template to want network calls you want from your back-end /
 * data source.
 */

interface BudgetService {

    @GET("/budget")
    fun getBudget(): Call<List<BudgetItem>>

    @POST("/budget")
    fun postBudget(@Body budgetItem: BudgetItem): Call<BudgetItem>

}