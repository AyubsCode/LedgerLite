import java.io.File
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.Locale
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
    val allowedValues = listOf("food", "transport", "entertainment", "bills", "other")
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
        var date = readln().trim()
        print("Enter description: ")
        val desc = readln().trim()
        print("Enter category [Food/Transport/Entertainment/Bills/Other]: ")
        val cat = readln().trim()
        print("Enter amount: ")
        val amt = readln().trim()

        if(date.isBlank()){
            val currentDate = LocalDate.now()
            date = currentDate.toString()
        }

        if(desc.isBlank() || cat.isBlank() || amt.isBlank()){
            println("=====\n[!] One or more of the inputs are blank.\n===== ")

        }

        if(!validDate(date) || !validCategory(cat.lowercase())){
           println("=====\n[!] One or more of the inputs are not valid.\n=====")

        }
        val value = amt.toDouble()
        if (value <= 0.00){
            println("=====\n[!] Amount value can't be less than or equal to 0.\n=====")
        }
        val expenses = loadExpenses()
        expenses.add(Expense(date,desc,cat,value))
        saveExpense(expenses)
        println("[✓] Expense added successfully!")
        break
    }
}

fun viewExpenses() {

    val expenses = loadExpenses()
    if (expenses.isEmpty()) {
        println("=======\nYou don't have any expenses yet. Create one to see it listed here.\n=======")
        return
    }
    println("\n----------------------------------------------------------")
    println(String.format("%-12s |  %-15s | %-15s|%8s", "Date", "Description", "Category", "Amount"))
    println("----------------------------------------------------------")

    expenses.sortBy { it.date }
    expenses.forEach {
        println(
            String.format(
                "%-12s   %-15s    %-15s   $%5.2f",
                it.date,
                it.desc,
                it.category,
                it.amount
            )
        )
    }
}

fun deleteExpenses() {

    val expenses = loadExpenses()
    if(expenses.isEmpty()){
        println("=======\nYou don't have any expenses yet. Create one to see it listed here.\n=======")
        return
    }

    expenses.sortBy { it.date }
    expenses.mapIndexed { index, expense ->
        "${index+1}. ${expense.date} | ${expense.desc} | ${expense.category} | $${expense.amount}"
    }.forEach { println(it) }

    print("Enter the index of the expense you want to remove: ")
    val index = readln().toIntOrNull()
    if (index == null || index !in expenses.indices) {
        println("[!]Invalid index.")
        return
    }
    val chosenExpense = expenses.removeAt(index+1)
    println("Removed: ${chosenExpense.date} | ${chosenExpense.desc} | ${chosenExpense.category} | $${chosenExpense.amount}")

    saveExpense(expenses)
}

fun formatMonthYear(yyyyMm: String): String {
    val ym = YearMonth.parse(yyyyMm)
    val monthName = ym.month.getDisplayName(TextStyle.FULL, Locale.ENGLISH)
    return "$monthName ${ym.year}"
}

fun summary(){
    var targetMonth: String
    val expenses = loadExpenses()
    if(expenses.isEmpty()){
        println("=======\nYou don't have any expenses yet. Create one to see it listed here.\n=======")
        return
    }
    print("Enter month (yyyy-mm) or press Enter to use current month: ")
    val input = readln().trim()
    if(input.isBlank()) {
        val now = LocalDate.now()
        targetMonth = "${now.year}-${now.monthValue.toString().padStart(2, '0')}" }
    else{
        targetMonth = input
    }
    val displayDate = formatMonthYear(targetMonth)
    val filtered = expenses.filter { it.date.startsWith(targetMonth) }
    if (filtered.isEmpty()){
        println("No expenses found for $displayDate")
        return
    }

    val total = filtered.sumOf{it.amount}
    println("\nSummary for $displayDate")
    println("---------------------------------------")
    filtered.groupBy { it.category }
        .mapValues { (_,list) -> list.sumOf { it.amount } }
        .toList().sortedByDescending { (_,sum) ->sum}
        .forEach { (category, sum) -> println("%-13s: $%3.2f".format(category,sum))
        }
    println("---------------------------------------")
    println("Total: $%.2f".format(total))
}
fun searchExpenses(){

    print("Enter category to search by [Food/Transport/Entertainment/Bills/Other]: ")
    val category = readln().trim()

    if (category.isBlank()){
        println("=====\n[!] Input was blank, exiting.\n=====")
        return
    }

    if(!validCategory(category.lowercase())){
        println("=====\n[!] Invalid input.\n=====")
        return
    }

    val expenses = loadExpenses().filter {it.category.equals(category,true)}.sortedBy {it.date}
    println("Category: $category (sorted by date)")
    println(String.format("%-12s|  %-15s| %8s","Date","Description","Amount"))
    expenses.forEach { expense ->
        println(String.format("%-14s  %-15s   $%3s",expense.date,expense.desc,expense.amount))
    }
    print("---------------------------------------------")
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