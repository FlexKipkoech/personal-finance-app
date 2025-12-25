package com.finance.app.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.finance.app.data.models.Budget
import com.finance.app.data.models.Category
import com.finance.app.data.models.Transaction
import com.finance.app.data.models.TransactionType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [Transaction::class, Budget::class, Category::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class FinanceDatabase : RoomDatabase() {
    abstract fun transactionDao(): TransactionDao
    abstract fun budgetDao(): BudgetDao
    abstract fun categoryDao(): CategoryDao

    companion object {
        @Volatile
        private var INSTANCE: FinanceDatabase? = null

        fun getDatabase(context: Context): FinanceDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    FinanceDatabase::class.java,
                    "finance_database"
                )
                    .addCallback(object : RoomDatabase.Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)
                            INSTANCE?.let { database ->
                                CoroutineScope(Dispatchers.IO).launch {
                                    populateDatabase(database.categoryDao())
                                }
                            }
                        }
                    })
                    .build()
                INSTANCE = instance
                instance
            }
        }

        private suspend fun populateDatabase(categoryDao: CategoryDao) {
            val defaultCategories = listOf(
                // Income Categories
                Category(name = "Salary", icon = "briefcase", color = "#4CAF50", type = TransactionType.INCOME),
                Category(name = "Freelance", icon = "laptop", color = "#8BC34A", type = TransactionType.INCOME),
                Category(name = "Investment", icon = "chart", color = "#CDDC39", type = TransactionType.INCOME),
                Category(name = "Gift", icon = "gift", color = "#FFC107", type = TransactionType.INCOME),
                Category(name = "Other Income", icon = "money", color = "#FF9800", type = TransactionType.INCOME),

                // Expense Categories
                Category(name = "Food & Dining", icon = "burger", color = "#F44336", type = TransactionType.EXPENSE),
                Category(name = "Transportation", icon = "car", color = "#E91E63", type = TransactionType.EXPENSE),
                Category(name = "Shopping", icon = "bag", color = "#9C27B0", type = TransactionType.EXPENSE),
                Category(name = "Entertainment", icon = "clapper", color = "#673AB7", type = TransactionType.EXPENSE),
                Category(name = "Bills & Utilities", icon = "bulb", color = "#3F51B5", type = TransactionType.EXPENSE),
                Category(name = "Healthcare", icon = "med", color = "#2196F3", type = TransactionType.EXPENSE),
                Category(name = "Education", icon = "books", color = "#03A9F4", type = TransactionType.EXPENSE),
                Category(name = "Travel", icon = "plane", color = "#00BCD4", type = TransactionType.EXPENSE),
                Category(name = "Personal Care", icon = "care", color = "#009688", type = TransactionType.EXPENSE),
                Category(name = "Other Expense", icon = "cash", color = "#607D8B", type = TransactionType.EXPENSE)
            )
            categoryDao.insertCategories(defaultCategories)
        }
    }
}
