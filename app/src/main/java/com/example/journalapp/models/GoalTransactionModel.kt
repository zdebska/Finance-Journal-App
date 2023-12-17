package com.example.journalapp.models

/**
 * @author Zdebska Kateryna (xzdebs00)
 * @brief Structure to store records from the "goal_transactions" table
 *
 * @param id The unique identifier of the goal transaction.
 * @param amount The amount associated with the goal transaction.
 * @param creationDate The creation date of the goal transaction.
 * @param goal The reference to the associated goal's unique identifier.
 */
class GoalTransactionModel(
    val id: Int,
    val amount: Float,
    val creationDate: String,
    val goal: Int
)
