package com.finance.app.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.finance.app.data.models.Budget
import kotlinx.coroutines.flow.Flow

@Dao
interface BudgetDao {
    @Query("SELECT * FROM budgets ORDER BY startDate DESC")
    fun getAllBudgets(): Flow<List<Budget>>

    @Query("SELECT * FROM budgets WHERE category = :category")
    fun getBudgetByCategory(category: String): Flow<Budget?>

    @Query("SELECT * FROM budgets WHERE endDate >= :currentTime")
    fun getActiveBudgets(currentTime: Long): Flow<List<Budget>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBudget(budget: Budget): Long

    @Update
    suspend fun updateBudget(budget: Budget)

    @Delete
    suspend fun deleteBudget(budget: Budget)

    @Query("UPDATE budgets SET spent = :spent WHERE id = :budgetId")
    suspend fun updateBudgetSpent(budgetId: Long, spent: Double)
}
