package com.finance.app.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import com.finance.app.data.models.TransactionType
import com.finance.app.utils.CurrencyUtils
import com.finance.app.utils.DateUtils
import com.finance.app.viewmodel.FinanceViewModel
import kotlinx.coroutines.launch

@Composable
fun CategoryBreakdownCard(
    viewModel: FinanceViewModel,
    type: TransactionType,
    title: String
) {
    var categoryTotals by remember { mutableStateOf<List<Pair<String, Double>>>(emptyList()) }
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        scope.launch {
            val startOfMonth = DateUtils.getStartOfMonth()
            val endOfMonth = DateUtils.getEndOfMonth()
            val totals = viewModel.getCategoryTotals(type, startOfMonth, endOfMonth)
            categoryTotals = totals.map { it.category to it.total }
        }
    }

    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge
            )

            if (categoryTotals.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No data available",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            } else {
                SimplePieChart(data = categoryTotals)

                Divider()

                categoryTotals.forEach { (category, amount) ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = category,
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Text(
                            text = CurrencyUtils.formatAmount(amount),
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun SimplePieChart(data: List<Pair<String, Double>>) {
    val total = data.sumOf { it.second }
    val colors = listOf(
        Color(0xFF4CAF50),
        Color(0xFFF44336),
        Color(0xFF2196F3),
        Color(0xFFFF9800),
        Color(0xFF9C27B0),
        Color(0xFF00BCD4),
        Color(0xFFFFEB3B),
        Color(0xFFE91E63)
    )

    Canvas(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .padding(16.dp)
    ) {
        val radius = size.minDimension / 2
        val centerX = size.width / 2
        val centerY = size.height / 2

        var startAngle = -90f

        data.forEachIndexed { index, (_, amount) ->
            val sweepAngle = ((amount / total) * 360).toFloat()

            drawArc(
                color = colors[index % colors.size],
                startAngle = startAngle,
                sweepAngle = sweepAngle,
                useCenter = true,
                size = androidx.compose.ui.geometry.Size(radius * 2, radius * 2),
                topLeft = androidx.compose.ui.geometry.Offset(
                    centerX - radius,
                    centerY - radius
                )
            )

            startAngle += sweepAngle
        }

        drawCircle(
            color = Color.White,
            radius = radius,
            center = androidx.compose.ui.geometry.Offset(centerX, centerY),
            style = Stroke(width = 2f)
        )
    }
}
