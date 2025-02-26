
# Personal Finance Tracker

## Table of Contents
1. [Introduction](#introduction)
2. [Features](#features)
3. [Concepts Used](#concepts-used)
4. [Technologies Used](#technologies-used)
5. [Setup and Execution](#setup-and-execution)
6. [Usage Instructions](#usage-instructions)
7. [Project Structure](#project-structure)
8. [File Details](#file-details)
9. [Acknowledgment](#acknowledgment)


## Introduction
The **Personal Finance Tracker** is a Java-based application that helps users keep track of their income and expenses, calculate their savings, and store the financial data for future reference. This project allows users to input transactions, view categorized expenses, and manage their finances efficiently.

## Features
- **Track Transactions**: Add income or expense transactions with amount, category, and date.
- **Income and Expense Categories**: Predefined categories for income and expenses (e.g., Salary, Food, Rent).
- **View Total Savings**: Calculate and display the total savings by subtracting expenses from income.
- **Transaction History**: View a complete list of all recorded transactions.
- **Closing Balance**: After entering transactions, view the cumulative closing balance, updated with each transaction.
- **Save and Load Transactions**: Save the transactions to a text file and load them back when restarting the program.
- **Date Sorting**: Transactions are sorted by date and saved in chronological order.

## Concepts Used
- **Object-Oriented Programming (OOP)**: This project uses object-oriented principles like classes, inheritance, and polymorphism to manage transactions.
- **File Handling**: Uses `BufferedReader` and `BufferedWriter` to read and write transaction data to a file.
- **Collections Framework**: Uses lists, maps, and sorting to manage and organize transactions.
- **Exception Handling**: Ensures valid input for amount and date fields, and gracefully handles errors.
- **Date Formatting**: Utilizes `SimpleDateFormat` to handle date input and sorting.

## Technologies Used
- **Java**: The programming language used to build the application.
- **BufferedReader and BufferedWriter**: For file handling and transaction data storage.
- **SimpleDateFormat**: For validating and formatting date inputs.


## **Setup and Execution**
1. **Extract the ZIP file** provided by your faculty to a desired location.
2. Open the folder and navigate to the `src/` directory.
3. Compile the source files by running:

   ```bash
   javac PersonalFinanceTracker.java
   ```
4. Execute the program from the `bin/` directory using:

   ```bash
   java PersonalFinanceTracker
   ```


## Usage Instructions
1. Upon running the program, you'll be presented with a menu:
   - **Add Income**: Add an income transaction by entering the amount, selecting the category, and specifying the date.
   - **Add Expense**: Add an expense transaction by entering the amount, selecting the category, and specifying the date.
   - **View Savings**: View your total savings (Income - Expenses).
   - **View Transactions**: Display all recorded transactions.
   - **Exit**: Exit the program after saving the transactions to a file.

2. For each transaction, you'll be prompted to input the amount, select the appropriate category, and enter the transaction date in `dd-MM-yyyy` format.

## Project Structure
- **PersonalFinanceTracker.java**: Contains the main program and logic for tracking and managing transactions.
- **Transaction Class**: Represents a transaction with attributes like type (income/expense), amount, category, and date.
- **Income and Expense Subclasses**: Extend the `Transaction` class to differentiate between income and expense transactions.
- **File Handling**: Functions to save and load transaction data from a text file.

## File Details
- **transactions.txt**: This is the file where all transaction data is stored. It records each transaction by date, category, type, amount, and the closing balance for each day.
  - The file is saved in the same directory as the program.
  - Example content:
    ```
    Date: 08-12-2024
    Serial No | Category | Type | Amount
    --------------------------------------
    1 | Salary | Income | ₹10000
    2 | Rent | Expense | ₹2000
    Closing Balance: ₹8000
    ```


## Acknowledgment

This project is a part of my mini-project submission for **CSE2006 Programing in JAVA** and demonstrates my skills in Java programming, object-oriented design, file handling, data structures, date manipulation, error handling, and sorting/grouping of data.


---

This README file provides an overview of how the **Personal Finance Tracker** works, how to set it up, and how to use it for tracking personal finances.
