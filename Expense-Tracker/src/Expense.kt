data class Expense (val date: String ,val desc: String, val category: String, val amount: Double )

enum class Menu {
    AddExpense,ViewExpenses,SearchExpense,DeleteExpense,MonthlySummary,Exit
}