# LedgerLite

<p align="center">
  <img src="Expense-Tracker/img/Ledger.png" alt="LedgerLite Logo" width="200">
</p>

**LedgerLite** is a Kotlin-based console application that enables efficient personal expense tracking. It allows users to monitor their spending, categorize expenses, and generate comprehensive monthly summaries.

---

## Features  

- **Add Expense**  
  - Enter date (defaults to today if blank)  
  - Description of the expense  
  - Category (Food, Transport, Entertainment, Bills, Other)  
  - Positive amount validation  

- **View All Expenses**  
  - Sorted by most recent date  
  - Clean, table-like display  

- **Search Expenses**  
  - Filter by category
  - Display the dates(starting with most recent), descriptions and amounts of that category  

- **Monthly Summary**  
  - Total spent per category for the current month  
  - Overall total spent  

- **Delete Expense**  
  - Remove an expense by selecting its index from the list  

- **Persistent Storage**  
  - Saves all expenses to a local file (`expenses.txt`)  
  - Loads data automatically on startup  

---
### Example Usage: Adding an Expense and Monthly Summary
```ps
=== LedgerLite ===
1. Add Expense
2. View All Expenses
3. Search Expenses
4. Delete Expense
5. Monthly Summary
6. Exit
Choose: 1

Enter date (yyyy-mm-dd) or leave blank for today: 
Enter description: Energy drink
Enter category [Food/Transport/Entertainment/Bills/Other]: Food
Enter amount: 4.75
[✓] Expense added successfully!

...
Choose: 5

Summary for June 2025
---------------------------------------
Other        : $150.00
Food         : $100.00
Bills        : $50.00
Entertainment: $35.00
---------------------------------------
Total: $335.00
```

## Stretch Goals
- **Updated Search Expenses Function**:
  - Enable users to filter expenses by category over a selectable range of months, and display the total amount spent within that category and timeframe.
- **Budget Alerts**:
  - Enable users to set monthly spending limits per category and display warnings in the console when limits are exceeded.
