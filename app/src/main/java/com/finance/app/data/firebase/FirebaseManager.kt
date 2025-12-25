package com.finance.app.data.firebase

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.finance.app.data.models.Transaction
import com.finance.app.data.models.Budget
import kotlinx.coroutines.tasks.await

class FirebaseManager {
    private val auth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()

    // Authentication
    suspend fun signInAnonymously(): Result<String> {
        return try {
            val result = auth.signInAnonymously().await()
            Result.success(result.user?.uid ?: "")
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun getCurrentUserId(): String? = auth.currentUser?.uid

    fun isUserSignedIn(): Boolean = auth.currentUser != null

    // Sync Transactions
    suspend fun syncTransaction(transaction: Transaction): Result<String> {
        return try {
            val userId = getCurrentUserId() ?: return Result.failure(Exception("User not signed in"))

            val docRef = if (transaction.firebaseId != null) {
                firestore.collection("users")
                    .document(userId)
                    .collection("transactions")
                    .document(transaction.firebaseId)
            } else {
                firestore.collection("users")
                    .document(userId)
                    .collection("transactions")
                    .document()
            }

            val data = hashMapOf(
                "amount" to transaction.amount,
                "category" to transaction.category,
                "type" to transaction.type.name,
                "description" to transaction.description,
                "date" to transaction.date,
                "isRecurring" to transaction.isRecurring,
                "recurringFrequency" to transaction.recurringFrequency
            )

            docRef.set(data).await()
            Result.success(docRef.id)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun syncBudget(budget: Budget): Result<String> {
        return try {
            val userId = getCurrentUserId() ?: return Result.failure(Exception("User not signed in"))

            val docRef = if (budget.firebaseId != null) {
                firestore.collection("users")
                    .document(userId)
                    .collection("budgets")
                    .document(budget.firebaseId)
            } else {
                firestore.collection("users")
                    .document(userId)
                    .collection("budgets")
                    .document()
            }

            val data = hashMapOf(
                "category" to budget.category,
                "amount" to budget.amount,
                "period" to budget.period.name,
                "spent" to budget.spent,
                "startDate" to budget.startDate,
                "endDate" to budget.endDate,
                "alertThreshold" to budget.alertThreshold
            )

            docRef.set(data).await()
            Result.success(docRef.id)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Fetch from cloud
    suspend fun fetchTransactions(): Result<List<Map<String, Any>>> {
        return try {
            val userId = getCurrentUserId() ?: return Result.failure(Exception("User not signed in"))

            val snapshot = firestore.collection("users")
                .document(userId)
                .collection("transactions")
                .get()
                .await()

            val transactions = snapshot.documents.map { doc ->
                doc.data?.toMutableMap()?.apply {
                    put("firebaseId", doc.id)
                } ?: emptyMap()
            }

            Result.success(transactions)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
