package com.finance.app.data.repository

import com.finance.app.data.database.BudgetDao
import com.finance.app.data.database.CategoryDao
import com.finance.app.data.database.CategoryTotal
import com.finance.app.data.database.TransactionDao
import com.finance.app.data.models.Budget
import com.finance.app.data.models.Category
import com.finance.app.data.models.Transaction
import com.finance.app.data.models.TransactionType
import kotlinx.coroutines.flow.Flow
import java.util.Date

class FinanceRepository(
    private val transactionDao: TransactionDao,
    private val budgetDao: BudgetDao,
    private val categoryDao: CategoryDao
) {
    // Transaction Operations
    fun getAllTransactions(): Flow<List<Transaction>> = transactionDao.getAllTransactions()

    fun getTransactionsByType(type: TransactionType): Flow<List<Transaction>> =
        transactionDao.getTransactionsByType(type)

    fun getTransactionsByDateRange(startDate: Long, endDate: Long): Flow<List<Transaction>> =
        transactionDao.getTransactionsByDateRange(startDate, endDate)

    suspend fun insertTransaction(transaction: Transaction): Long =
        transactionDao.insertTransaction(transaction)

    suspend fun updateTransaction(transaction: Transaction) =
        transactionDao.updateTransaction(transaction)

    suspend fun deleteTransaction(transaction: Transaction) =
        transactionDao.deleteTransaction(transaction)

    suspend fun getTotalByTypeAndDateRange(
        type: TransactionType,
        startDate: Long,
        endDate: Long
    ): Double = transactionDao.getTotalByTypeAndDateRange(type, startDate, endDate) ?: 0.0

    suspend fun getTotalByTypeCategoryAndDateRange(
        type: TransactionType,
        category: String,
        startDate: Long,
        endDate: Long
    ): Double = transactionDao.getTotalByTypeCategoryAndDateRange(type, category, startDate, endDate) ?: 0.0

    suspend fun getTransactionByFirebaseId(firebaseId: String): Transaction? =
        transactionDao.getTransactionByFirebaseId(firebaseId)

    suspend fun getCategoryTotals(
        type: TransactionType,
        startDate: Long,
        endDate: Long
    ): List<CategoryTotal> = transactionDao.getCategoryTotals(type, startDate, endDate)

    // Budget Operations
    fun getAllBudgets(): Flow<List<Budget>> = budgetDao.getAllBudgets()

    fun getActiveBudgets(): Flow<List<Budget>> = budgetDao.getActiveBudgets(Date().time)

    fun getBudgetByCategory(category: String): Flow<Budget?> =
        budgetDao.getBudgetByCategory(category)

    suspend fun insertBudget(budget: Budget): Long = budgetDao.insertBudget(budget)

    suspend fun updateBudget(budget: Budget) = budgetDao.updateBudget(budget)

    suspend fun deleteBudget(budget: Budget) = budgetDao.deleteBudget(budget)

    suspend fun updateBudgetSpent(budgetId: Long, spent: Double) =
        budgetDao.updateBudgetSpent(budgetId, spent)

    // Category Operations
    fun getAllCategories(): Flow<List<Category>> = categoryDao.getAllCategories()

    fun getCategoriesByType(type: TransactionType): Flow<List<Category>> =
        categoryDao.getCategoriesByType(type)

    suspend fun insertCategory(category: Category) = categoryDao.insertCategory(category)
}
