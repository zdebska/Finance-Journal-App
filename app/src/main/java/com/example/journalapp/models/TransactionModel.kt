package com.example.journalapp.models

// class representing data of a record from the "transactions" table
class TransactionModel(
    val id: Int,
    val amount: Int,
    val note: String,
    val creationDate: String,
    val category: Int
)