package com.finance.app.utils

fun categoryIconToEmoji(rawIcon: String): String {
    val icon = rawIcon.trim().lowercase()

    return when (icon) {
        "briefcase" -> "💼"
        "laptop" -> "💻"
        "chart" -> "📈"
        "gift" -> "🎁"
        "money" -> "💰"

        "burger" -> "🍔"
        "car" -> "🚗"
        "bag" -> "🛍️"
        "clapper" -> "🎬"
        "bulb" -> "💡"
        "med" -> "⚕️"
        "books" -> "📚"
        "plane" -> "✈️"
        "care" -> "💅"
        "cash" -> "💸"

        else -> rawIcon
    }
}
