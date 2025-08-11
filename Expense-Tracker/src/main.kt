import java.io.File
import java.time.LocalDate
import java.util.Scanner

/**
 * Project Description: Build a console-based expense tracker where a user can add expenses,
 * categorize them, see summaries, and search history.
 *
 * Date: August 10th, 2025
 *
 * Features:
 * 1. Add Expense
 *     - User inputs: date (default to today if left blank), description, category, amount.
 *     - Categories: "Food", "Transport", "Entertainment", "Bills", "Other".
 *     - Validate that amount is a positive decimal number.
 * 2. View All Expenses
 *     - Display in a table-like format with columns: Date | Description | Category | Amount
 *     - Sort by most recent first.
 * 3. Search Expenses
 *     - Search by category.
 *     - Show matching results only.
 * 4. Monthly Summary
 *     - Group expenses by category for the current month and show total per category.
 *     - Also show overall total spent.
 * 5. Delete Expense
 *     - Let the user pick an expense from the displayed list by index to remove.
 *
 * Project Status: In Progress
 */

const val fileName = "expense.txt"

fun displayMenu() {
    println("""
   
        === LedgerLite === 
        
        1. Add Expense
        2. View All Expenses
        3. Search Expense
        4. Delete Expense
        5. Monthly Summary
        6. Exit
        -------------
    """.trimIndent())
}

fun validDate(date: String): Boolean {
    val dateRegex = Regex("^\\d{4}-(0[1-9]|1[0-2])-(0[1-9]|[12]\\d|3[01])$")
    return dateRegex.matches(date)
}

fun validCategory(value: String): Boolean {
    val allowedValues = listOf("Food", "Transport", "Entertainment", "Bills", "Other")
    return value in allowedValues
}

fun saveExpense(expenses: List<Expense>){
    File(fileName).writeText(expenses.joinToString("\n")
    {"${it.date},${it.desc},${it.category},${it.amount}"})
}

fun loadExpenses(): MutableList<Expense>{
    val file = File(fileName)
    if(!file.exists()) return mutableListOf()
    return file.readLines().mapNotNull { line ->
        val parts = line.split(",")
        if (parts.size == 4) {
            val date = parts[0].trim()
            val desc = parts[1].trim()
            val category = parts[2].trim()
            val amt = parts[3].trim().toDoubleOrNull()?: return@mapNotNull null
            Expense(date,desc,category,amt)
        }
        else null
    }.toMutableList()
}

fun addExpense(){

    while(true) {
        print("Enter date (yyyy-mm-dd) or leave blank to use today's date: ")
        var date = readln()
        print("Enter description: ")
        val desc = readln()
        print("Enter category [Food/Transport/Entertainment/Bills/Other]: ")
        val cat = readln()
        print("Enter amount: ")
        val amt = readln()

        if(date.isBlank()){
            val currentDate = LocalDate.now()
            date = currentDate.toString()
        }

        if(desc.isBlank() || cat.isBlank() || amt.isBlank()){
            println("=====\n[!] One or more of the inputs are blank.\n===== ")
        }

        if(!(validDate(date) || validCategory(cat))){
           println("=====\n[!] One or more of the inputs are not valid.\n=====")
        }

        val expenses = loadExpenses()
        expenses.add(Expense(date,desc,cat,amt.toDouble()))
        saveExpense(expenses)
        println("[✓] Expense added successfully!")
        break
    }
}

fun viewExpenses(){

    val expenses = loadExpenses()
    if(expenses.isEmpty()){
        println("=======\nYou don't have any expenses yet. Create one to see it listed here.\n=======")
        return
    }
    println("\n--------------\n  Date  |  Description  |  Category  |  Amount")
    expenses.sortBy{it.date}
    expenses.forEach{ println("${it.date}     ${it.desc}     ${it.category}     $${it.amount}")
    }
}

fun deleteExpenses(){

}

fun summary(){

}

fun searchExpenses(){

    print("Enter category to search by: ")
    val category = readln().trim()

    if (category.isBlank()){
        println("\"=====\\n[!] Input was blank, exiting.\\n=====")
        return
    }

    val expenses = loadExpenses().filter {it.category.equals(category,true)}.sortedBy {it.date}
    println("Category: $category (sorted by date)")
    expenses.forEach { expense ->
        println("${expense.date}    |    ${expense.desc}    |    $${expense.amount}")
    }
}


fun main(){
    val scanner = Scanner(System.`in`)
    var running = true

    while (running) {

        //Display menu options
        displayMenu()

        //Read user input
        print("Enter your choice (1-${Menu.entries.size}): ")
        val choice = scanner.nextLine().toIntOrNull()
        val selectedOption = if (choice != null && choice in 1..Menu.entries.size) {
            Menu.entries[choice - 1]
        } else {
            println("[!] Invalid choice. Please try again.")
            continue
        }

        //Handle user choice
        when (selectedOption) {
            Menu.AddExpense -> addExpense()
            Menu.ViewExpenses -> viewExpenses()
            Menu.SearchExpense -> searchExpenses()
            Menu.DeleteExpense -> deleteExpenses()
            Menu.MonthlySummary -> summary()
            Menu.Exit -> {
                println("\nExiting program.")
                running = false
            }
        }
    }
}