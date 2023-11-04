/*
* @author Alakaev Kambulat (xalaka00)
* @brief Structure to store records from the "transactions" table
* */
package com.example.journalapp.models

// class representing data of a record from the "transactions" table
class TransactionModel(
    val id: Int,
    val amount: Float,
    val note: String,
    val creationDate: String,
    val category: Int,
    val transType: String
)