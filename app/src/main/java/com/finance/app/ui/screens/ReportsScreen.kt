package com.finance.app.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.finance.app.data.models.Transaction
import com.finance.app.data.models.TransactionType
import com.finance.app.ui.components.CategoryBreakdownCard
import com.finance.app.ui.components.MonthlyTrendCard
import com.finance.app.viewmodel.FinanceViewModel

@Composable
fun ReportsScreen(
    viewModel: FinanceViewModel
) {
    val transactions by viewModel.allTransactions.collectAsState()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text(
                text = "Financial Reports",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )
        }

        item {
            MonthlyTrendCard(viewModel = viewModel)
        }

        item {
            CategoryBreakdownCard(
                viewModel = viewModel,
                type = TransactionType.EXPENSE,
                title = "Expense Breakdown"
            )
        }

        item {
            CategoryBreakdownCard(
                viewModel = viewModel,
                type = TransactionType.INCOME,
                title = "Income Sources"
            )
        }

        item {
            TransactionSummaryCard(transactions = transactions)
        }
    }
}

@Composable
fun TransactionSummaryCard(transactions: List<Transaction>) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "Transaction Summary",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )

            Divider()

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Total Transactions:", style = MaterialTheme.typography.bodyLarge)
                Text(
                    text = transactions.size.toString(),
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Income Transactions:", style = MaterialTheme.typography.bodyMedium)
                Text(
                    text = transactions.count { it.type == TransactionType.INCOME }.toString(),
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Expense Transactions:", style = MaterialTheme.typography.bodyMedium)
                Text(
                    text = transactions.count { it.type == TransactionType.EXPENSE }.toString(),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}
