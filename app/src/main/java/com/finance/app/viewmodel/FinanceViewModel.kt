package com.finance.app.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.finance.app.data.database.FinanceDatabase
import com.finance.app.data.firebase.FirebaseManager
import com.finance.app.data.models.Budget
import com.finance.app.data.models.Category
import com.finance.app.data.models.Transaction
import com.finance.app.data.models.TransactionType
import com.finance.app.data.repository.FinanceRepository
import com.finance.app.utils.DateUtils
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class FinanceViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: FinanceRepository
    private val firebaseManager = FirebaseManager()
    
    val allTransactions: StateFlow<List<Transaction>>
    val allBudgets: StateFlow<List<Budget>>
    val allCategories: StateFlow<List<Category>>
    
    private val _monthlyIncome = MutableStateFlow(0.0)
    val monthlyIncome: StateFlow<Double> = _monthlyIncome.asStateFlow()
    
    private val _monthlyExpenses = MutableStateFlow(0.0)
    val monthlyExpenses: StateFlow<Double> = _monthlyExpenses.asStateFlow()
    
    private val _balance = MutableStateFlow(0.0)
    val balance: StateFlow<Double> = _balance.asStateFlow()

    init {
        val database = FinanceDatabase.getDatabase(application)
        repository = FinanceRepository(
            database.transactionDao(),
            database.budgetDao(),
            database.categoryDao()
        )
        
        allTransactions = repository.getAllTransactions()
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
        
        allBudgets = repository.getAllBudgets()
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
        
        allCategories = repository.getAllCategories()
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
        
        calculateMonthlyTotals()

        viewModelScope.launch {
            if (!firebaseManager.isUserSignedIn()) {
                firebaseManager.signInAnonymously()
            }
        }
    }

    private fun calculateMonthlyTotals() {
        viewModelScope.launch {
            val startOfMonth = DateUtils.getStartOfMonth()
            val endOfMonth = DateUtils.getEndOfMonth()
            
            val income = repository.getTotalByTypeAndDateRange(
                TransactionType.INCOME,
                startOfMonth,
                endOfMonth
            )
            
            val expenses = repository.getTotalByTypeAndDateRange(
                TransactionType.EXPENSE,
                startOfMonth,
                endOfMonth
            )
            
            _monthlyIncome.value = income
            _monthlyExpenses.value = expenses
            _balance.value = income - expenses
        }
    }

    // Transaction Operations
    fun addTransaction(transaction: Transaction) {
        viewModelScope.launch {
            repository.insertTransaction(transaction)
            calculateMonthlyTotals()
            updateBudgetSpending(transaction.category)
        }
    }

    fun updateTransaction(transaction: Transaction) {
        viewModelScope.launch {
            repository.updateTransaction(transaction)
            calculateMonthlyTotals()
        }
    }

    fun deleteTransaction(transaction: Transaction) {
        viewModelScope.launch {
            repository.deleteTransaction(transaction)
            calculateMonthlyTotals()
        }
    }

    fun syncTransactionToCloud(transaction: Transaction) {
        viewModelScope.launch {
            val result = firebaseManager.syncTransaction(transaction)
            if (result.isSuccess) {
                val firebaseId = result.getOrNull()
                if (firebaseId != null && transaction.firebaseId == null) {
                    repository.updateTransaction(
                        transaction.copy(firebaseId = firebaseId, synced = true)
                    )
                }
            }
        }
    }

    // Budget Operations
    fun addBudget(budget: Budget) {
        viewModelScope.launch {
            repository.insertBudget(budget)
        }
    }

    fun updateBudget(budget: Budget) {
        viewModelScope.launch {
            repository.updateBudget(budget)
        }
    }

    fun deleteBudget(budget: Budget) {
        viewModelScope.launch {
            repository.deleteBudget(budget)
        }
    }

    fun syncBudgetToCloud(budget: Budget) {
        viewModelScope.launch {
            val result = firebaseManager.syncBudget(budget)
            if (result.isSuccess) {
                val firebaseId = result.getOrNull()
                if (firebaseId != null && budget.firebaseId == null) {
                    repository.updateBudget(
                        budget.copy(firebaseId = firebaseId)
                    )
                }
            }
        }
    }

    fun syncAllToCloud() {
        viewModelScope.launch {
            allTransactions.value.forEach { transaction ->
                if (!transaction.synced) {
                    syncTransactionToCloud(transaction)
                }
            }

            allBudgets.value.forEach { budget ->
                if (budget.firebaseId == null) {
                    syncBudgetToCloud(budget)
                }
            }
        }
    }

    private fun updateBudgetSpending(category: String) {
        viewModelScope.launch {
            val startOfMonth = DateUtils.getStartOfMonth()
            val endOfMonth = DateUtils.getEndOfMonth()
            
            val spent = repository.getTotalByTypeAndDateRange(
                TransactionType.EXPENSE,
                startOfMonth,
                endOfMonth
            )
            
            // Update budget if exists
            allBudgets.value.find { it.category == category }?.let { budget ->
                repository.updateBudgetSpent(budget.id, spent)
            }
        }
    }

    // Category Operations
    fun getCategoriesByType(type: TransactionType) = repository.getCategoriesByType(type)

    fun addCategory(category: Category) {
        viewModelScope.launch {
            repository.insertCategory(category)
        }
    }

    // Analytics
    suspend fun getCategoryTotals(
        type: TransactionType,
        startDate: Long,
        endDate: Long
    ) = repository.getCategoryTotals(type, startDate, endDate)
}
