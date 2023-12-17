/*
* @author Alakaev Kambulat (xalaka00)
* @author Assatulla Dias (xassat00)
* @author Zdebska Kateryna (xzdebs00)
* @brief Database implementation
* */

package com.example.journalapp.models

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.SQLException
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
class AppDB(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    // basis constants with names of tables and fields
    companion object {
        const val DATABASE_NAME = "FinanceJournalDatabase.db"
        const val DATABASE_VERSION = 15
        const val TABLE_TRANSACTIONS = "transactions"
        const val TABLE_CATEGORIES = "categories"
        const val TABLE_GOALS = "goals"
        const val TABLE_GOALS_TRANSACTIONS = "goal_categories"

        const val KEY_ID = "id"
        const val KEY_AMOUNT = "amount"
        const val KEY_NOTE = "note"
        const val KEY_DATE = "creation_date"
        const val KEY_TRANS_TYPE = "trans_type"
        const val KEY_CAT = "category"
        const val KEY_NAME = "name"
        const val KEY_ICON = "icon_path"
        const val KEY_COLOR = "color_icon"
        const val KEY_GOAL = "goal"
        const val KEY_END_DATE = "end_date"
    }


    override fun onCreate(db: SQLiteDatabase?) {
        // create tables
        val createTransactionsTable = "CREATE TABLE IF NOT EXISTS $TABLE_TRANSACTIONS" +
                "($KEY_ID INTEGER PRIMARY KEY," +
                "$KEY_AMOUNT REAL NOT NULL," +
                "$KEY_NOTE TEXT NOT NULL," +
                "$KEY_DATE NUMERIC DEFAULT CURRENT_TIMESTAMP," +
                "$KEY_CAT INTEGER," +
                "$KEY_TRANS_TYPE TEXT," +
                "CHECK ($KEY_TRANS_TYPE IN ('Expense','Income'))," +
                "FOREIGN KEY ($KEY_CAT) REFERENCES $TABLE_CATEGORIES($KEY_ID)" +
                ");"


        val createCategoryTable = "CREATE TABLE IF NOT EXISTS $TABLE_CATEGORIES" +
                "( $KEY_ID INTEGER PRIMARY KEY," +
                "$KEY_NAME TEXT NOT NULL," +
                "$KEY_ICON TEXT NOT NULL," +
                "$KEY_COLOR INTEGER NOT NULL" +
                ");"


        val createGoalsTable = "CREATE TABLE IF NOT EXISTS $TABLE_GOALS" +
                "( $KEY_ID INTEGER PRIMARY KEY," +
                "$KEY_NAME TEXT NOT NULL," +
                "$KEY_AMOUNT REAL NOT NULL," +
                "$KEY_END_DATE NUMERIC DEFAULT CURRENT_TIMESTAMP" +
                ");"


        val createGoalsTransactionsTable = "CREATE TABLE IF NOT EXISTS $TABLE_GOALS_TRANSACTIONS" +
                "( $KEY_ID INTEGER PRIMARY KEY," +
                "$KEY_AMOUNT REAL NOT NULL," +
                "$KEY_DATE NUMERIC DEFAULT CURRENT_TIMESTAMP," +
                "$KEY_GOAL INTEGER" +
                ");"

        db?.execSQL(createTransactionsTable)
        db?.execSQL(createCategoryTable)
        db?.execSQL(createGoalsTable)
        db?.execSQL(createGoalsTransactionsTable)
    }


    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS $TABLE_TRANSACTIONS")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_CATEGORIES")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_GOALS")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_GOALS_TRANSACTIONS")
        onCreate(db)

    }

    // @author Assatulla Dias (xassat00)
    fun checkDefaultCategories(): Boolean {
        val db = this.readableDatabase
        val selectQuery = "SELECT COUNT(*) FROM $TABLE_CATEGORIES"

        var cursor: Cursor? = null
        var count = 0

        try {
            cursor = db.rawQuery(selectQuery, null)
            if (cursor.moveToFirst()) {
                count = cursor.getInt(0)
            }
        } catch (e: SQLException) {
            db.execSQL(selectQuery)
            return false
        } finally {
            cursor?.close()
            db.close()
        }

        // Check if the count is greater than 0, indicating that categories exist
        return count > 0
    }

    // @author Assatulla Dias (xassat00)
    fun insertDefaultCategories() {
        val defaultCategories = listOf(
            CategoryModel(1, "Food", "baseline_food_bank_24", android.R.color.black),
            CategoryModel(2, "Shopping", "baseline_shopping_cart_24", android.R.color.black),
            CategoryModel(3, "Taxes", "baseline_account_balance_24", android.R.color.black),
            CategoryModel(4, "Education", "baseline_school_24", android.R.color.black),
            CategoryModel(5, "Salary", "baseline_attach_money_24", android.R.color.black),
            CategoryModel(6, "Rent", "baseline_home_24", android.R.color.black),
            CategoryModel(7, "Relax", "baseline_sports_tennis_24", android.R.color.black),
            CategoryModel(8, "Other", "baseline_other_houses_24", android.R.color.black)
        )

        for (category in defaultCategories) {
            addCategory(category)
        }
    }

    // @author: Alakaev Kambulat (xalaka00)
    // add transaction to the "transactions" table
    fun addTransaction(transaction: TransactionModel): Long {
        // set database op mode to write
        val db = this.writableDatabase

        //create container and fill it with passed data from a user
        val contentValues = ContentValues()
        contentValues.put(KEY_AMOUNT, transaction.amount)
        contentValues.put(KEY_NOTE, transaction.note)
        contentValues.put(KEY_DATE, transaction.creationDate)
        contentValues.put(KEY_CAT, transaction.category)
        contentValues.put(KEY_TRANS_TYPE, transaction.transType)

        // insert obtained data to the table "transactions"
        val success = db.insert(TABLE_TRANSACTIONS, null, contentValues)
        // close db connection
        db.close()
        // return the result of insertion
        return success
    }

    // @author Zdebska Kateryna (xzdebs00)
    fun addGoal(goal: GoalModel): Long {
        // set database op mode to write
        val db = this.writableDatabase

        //create container and fill it with passed data from a user
        val contentValues = ContentValues()
        contentValues.put(KEY_NAME, goal.name)
        contentValues.put(KEY_AMOUNT, goal.amount)
        contentValues.put(KEY_END_DATE, goal.endDate)

        // insert obtained data to the table "transactions"
        val success = db.insert(TABLE_GOALS, null, contentValues)
        // close db connection
        db.close()
        // return the result of insertion
        return success
    }

    // @author Zdebska Kateryna (xzdebs00)
    fun addGoalTransaction(transaction: GoalTransactionModel): Long {
        // set database op mode to write
        val db = this.writableDatabase

        //create container and fill it with passed data from a user
        val contentValues = ContentValues()
        contentValues.put(KEY_AMOUNT, transaction.amount)
        contentValues.put(KEY_DATE, transaction.creationDate)
        contentValues.put(KEY_GOAL, transaction.goal)

        // insert obtained data to the table "transactions"
        val success = db.insert(TABLE_GOALS_TRANSACTIONS, null, contentValues)
        // close db connection
        db.close()
        // return the result of insertion
        return success
    }

    // @author: Alakaev Kambulat (xalaka00)
    // update existing transaction
    fun updateTransaction(transaction: TransactionModel, transID: Int): Int {
        val db = this.writableDatabase
        val contentValues = ContentValues()

        contentValues.put(KEY_AMOUNT, transaction.amount)
        contentValues.put(KEY_NOTE, transaction.note)
        contentValues.put(KEY_DATE, transaction.creationDate)
        contentValues.put(KEY_CAT, transaction.category)
        contentValues.put(KEY_TRANS_TYPE, transaction.transType)

        val success = db.update(
            TABLE_TRANSACTIONS, contentValues,
            "$KEY_ID=$transID", null
        )
        db.close()
        return success
    }

    // @author: Alakaev Kambulat (xalaka00)
    // delete existing transaction
    fun deleteTransaction(transID: Int): Int {
        val db = this.writableDatabase
        val success = db.delete(TABLE_TRANSACTIONS, "$KEY_ID=$transID", null)

        db.close()
        return success
    }

    // @author: Alakaev Kambulat (xalaka00)
    // select transactions from the "transactions" table depending on a condition(if there is one)
    @SuppressLint("Range")
    fun viewTransactions(condition: String): ArrayList<TransactionModel> {
        // create an array whose elements are records from the table
        val transList: ArrayList<TransactionModel> = ArrayList()
        val selectQuery: String

        // set query depending on a presence of condition
        if (condition.isNotEmpty()) {
            selectQuery = "SELECT * FROM $TABLE_TRANSACTIONS $condition"
        } else {
            selectQuery = "SELECT * FROM $TABLE_TRANSACTIONS"
        }

        val db = this.readableDatabase
        var cursor: Cursor? = null

        // create cursor in order to iterate through the data of the table
        try {
            cursor = db.rawQuery(selectQuery, null)
        } catch (e: SQLException) {
            db.execSQL(selectQuery)
            db.close()
            return ArrayList()
        }
        var id: Int
        var amount: Float
        var note: String
        var creationDate: String
        var category: Int
        var transType: String

        if (cursor.moveToFirst()) {
            do {
                id = cursor.getInt(cursor.getColumnIndex(KEY_ID))
                amount = cursor.getFloat(cursor.getColumnIndex(KEY_AMOUNT))
                note = cursor.getString(cursor.getColumnIndex(KEY_NOTE))
                creationDate = cursor.getString(cursor.getColumnIndex(KEY_DATE))
                category = cursor.getInt(cursor.getColumnIndex(KEY_CAT))
                transType = cursor.getString(cursor.getColumnIndex(KEY_TRANS_TYPE))

                val transaction = TransactionModel(
                    id = id,
                    amount = amount,
                    note = note,
                    creationDate = creationDate,
                    category = category,
                    transType = transType
                )
                transList.add(transaction)
            } while (cursor.moveToNext())
            cursor.close()
        }
        db.close()
        return transList
    }

    // @author Zdebska Kateryna (xzdebs00)
    @SuppressLint("Range")
    /**
     * Retrieves a list of goal transactions associated with a specific goal from the database.
     *
     * @param goalId The unique identifier of the goal to retrieve transactions for.
     * @return An ArrayList containing GoalTransactionModel objects representing goal transactions.
     */
    fun viewGoalsTransactions(goalId: Int): ArrayList<GoalTransactionModel> {
        // Create an array whose elements are records from the table
        val transList: ArrayList<GoalTransactionModel> = ArrayList()
        val selectQuery = "SELECT * FROM $TABLE_GOALS_TRANSACTIONS WHERE $KEY_GOAL = $goalId"

        val db = this.readableDatabase
        var cursor: Cursor? = null

        // Create a cursor to iterate through the data of the table
        try {
            cursor = db.rawQuery(selectQuery, null)
        } catch (e: SQLException) {
            db.execSQL(selectQuery)
            db.close()
            return ArrayList()
        }

        var id: Int
        var amount: Float
        var creationDate: String

        if (cursor.moveToFirst()) {
            do {
                id = cursor.getInt(cursor.getColumnIndex(KEY_ID))
                amount = cursor.getFloat(cursor.getColumnIndex(KEY_AMOUNT))
                creationDate = cursor.getString(cursor.getColumnIndex(KEY_DATE))

                val transaction = GoalTransactionModel(
                    id = id,
                    amount = amount,
                    creationDate = creationDate,
                    goal = goalId
                )
                transList.add(transaction)
            } while (cursor.moveToNext())
            cursor.close()
        }
        db.close()
        return transList
    }


    // @author Zdebska Kateryna (xzdebs00)
    @SuppressLint("Range")
            /**
             * Retrieves a list of goals from the database, including calculated saved amounts and reach status.
             *
             * @return An ArrayList containing GoalModel objects representing goals.
             */
    fun viewGoals(): ArrayList<GoalModel> {
        // Create an array whose elements are records from the goals table
        val goalList: ArrayList<GoalModel> = ArrayList()
        val selectQuery = "SELECT * FROM $TABLE_GOALS"

        val db = this.readableDatabase
        var cursor: Cursor? = null

        // Create cursor to iterate through the data of the table
        try {
            cursor = db.rawQuery(selectQuery, null)
        } catch (e: SQLException) {
            db.execSQL(selectQuery)
            db.close()
            return ArrayList()
        }

        var id: Int
        var name: String
        var amount: Float
        var endDate: String
        var saved: Float
        var isReached: Int

        if (cursor.moveToFirst()) {
            do {
                id = cursor.getInt(cursor.getColumnIndex(KEY_ID))
                name = cursor.getString(cursor.getColumnIndex(KEY_NAME))
                amount = cursor.getFloat(cursor.getColumnIndex(KEY_AMOUNT))
                endDate = cursor.getString(cursor.getColumnIndex(KEY_END_DATE))

                // Calculate the saved amount for the goal based on transactions
                val goalTransactions = viewGoalsTransactions(id)
                saved = goalTransactions.sumOf { it.amount.toDouble() }.toFloat()

                // Check if the goal is reached
                isReached = if (saved >= amount) {
                    1
                } else {
                    0
                }

                val goal = GoalModel(
                    id = id,
                    name = name,
                    amount = amount,
                    endDate = endDate,
                    saved = saved,
                    isReached = isReached
                )
                goalList.add(goal)
            } while (cursor.moveToNext())
            cursor.close()
        }
        db.close()
        return goalList
    }

    // @author Zdebska Kateryna (xzdebs00)
    /**
     * Calculates the saved amount for a specific goal based on associated transactions.
     *
     * @param goalID The ID of the goal for which the saved amount is calculated.
     * @return The saved amount for the specified goal.
     */
    fun calculateSavedAmountForGoal(goalID: Int): Float {
        val db = this.readableDatabase
        val selectQuery = "SELECT $KEY_AMOUNT FROM $TABLE_GOALS WHERE $KEY_ID = $goalID"
        var cursor: Cursor? = null
        var savedAmount: Float = 0f

        try {
            cursor = db.rawQuery(selectQuery, null)

            if (cursor.moveToFirst()) {
                // Calculate the saved amount for the goal based on transactions
                val goalTransactions = viewGoalsTransactions(goalID)
                savedAmount = goalTransactions.sumOf { it.amount.toDouble() }.toFloat()
            }
        } catch (e: SQLException) {
            db.execSQL(selectQuery)
        } finally {
            cursor?.close()
            db.close()
        }

        return savedAmount
    }

    // @author Zdebska Kateryna (xzdebs00)
    /**
     * Updates the details of a goal in the database.
     *
     * @param goal The updated GoalModel object containing new information.
     * @return The success status of the update operation.
     */
    fun updateGoal(goal: GoalModel): Int {
        val db = this.writableDatabase
        val contentValues = ContentValues()

        contentValues.put(KEY_NAME, goal.name)
        contentValues.put(KEY_AMOUNT, goal.amount)
        contentValues.put(KEY_END_DATE, goal.endDate)

        val success = db.update(
            TABLE_GOALS, contentValues,
            "$KEY_ID=${goal.id}", null
        )
        db.close()
        return success
    }

    // @author Zdebska Kateryna (xzdebs00)
    /**
     * Deletes a goal and its associated transactions from the database.
     *
     * @param goalID The ID of the goal to be deleted.
     * @return The success status of the delete operation.
     */
    fun deleteGoal(goalID: Int): Int {
        val db = this.writableDatabase
        var success = 0

        db.beginTransaction()
        try {
            // Delete transactions related to the goal
            db.delete(TABLE_GOALS_TRANSACTIONS, "$KEY_GOAL=?", arrayOf(goalID.toString()))

            // Now, delete the goal itself
            success = db.delete(TABLE_GOALS, "$KEY_ID=?", arrayOf(goalID.toString()))

            // Commit the transaction
            db.setTransactionSuccessful()
        } catch (e: Exception) {
            // Handle exceptions if needed
            e.printStackTrace()
        } finally {
            // End the transaction
            db.endTransaction()
            db.close()
        }

        return success
    }

    // @author Zdebska Kateryna (xzdebs00)
    /**
     * Deletes a goal transaction from the database.
     *
     * @param transId The ID of the goal transaction to be deleted.
     * @return The success status of the delete operation.
     */
    fun deleteGoalTrans(transId: Int): Int {
        val db = this.writableDatabase
        val success = db.delete(TABLE_GOALS_TRANSACTIONS, "$KEY_ID=$transId", null)
        db.close()
        return success
    }


    // add category to the "categories" table
    // @author Assatulla Dias (xassat00)
    fun addCategory(category: CategoryModel): Long {
        val db = this.writableDatabase

        // create container and fill it with passed data from a user
        val contentValues = ContentValues()
        contentValues.put(KEY_NAME, category.name)
        contentValues.put(KEY_ICON, category.iconPath)
        contentValues.put(KEY_COLOR, category.colorResId)

        // insert obtained data to the table "categories"
        val success = db.insert(TABLE_CATEGORIES, null, contentValues)

        // close db connection
        db.close()
        // return the result of insertion
        return success
    }

    // @author Assatulla Dias (xassat00)
    fun deleteCategory(categoryId: Int): Int {
        val db = this.writableDatabase
        val success = db.delete(TABLE_CATEGORIES, "$KEY_ID=$categoryId", null)
        db.close()
        return success
    }

    // @author Assatulla Dias (xassat00)
    fun updateCategory(category: CategoryModel): Int {
        val db = this.writableDatabase
        val contentValues = ContentValues()

        contentValues.put(KEY_NAME, category.name)
        contentValues.put(KEY_ICON, category.iconPath)
        contentValues.put(KEY_COLOR, category.colorResId)

        val success = db.update(
            TABLE_CATEGORIES, contentValues,
            "$KEY_ID=${category.id}", null
        )
        db.close()
        return success
    }

    //  select transactions from the "categories" table
    // @author Assatulla Dias (xassat00)
    @SuppressLint("Range")
    fun viewCategories(): ArrayList<CategoryModel> {
        if (!checkDefaultCategories()) {
            insertDefaultCategories()
        }

        val catList: ArrayList<CategoryModel> = ArrayList()
        val selectQuery = "SELECT * FROM $TABLE_CATEGORIES"

        val db = this.readableDatabase
        var cursor: Cursor? = null

        // create cursor in order to iterate through the data of the table
        try {
            cursor = db.rawQuery(selectQuery, null)
        } catch (e: SQLException) {
            db.execSQL(selectQuery)
            db.close()
            return ArrayList()
        }

        var id: Int
        var name: String
        var iconPath: String
        var colorResId: Int
        if (cursor.moveToFirst()) {
            do {
                id = cursor.getInt(cursor.getColumnIndex(KEY_ID))
                name = cursor.getString(cursor.getColumnIndex(KEY_NAME))
                iconPath = cursor.getString(cursor.getColumnIndex(KEY_ICON))
                colorResId = cursor.getInt(cursor.getColumnIndex(KEY_COLOR))

                val category = CategoryModel(id = id, name = name, iconPath = iconPath, colorResId = colorResId)
                catList.add(category)
            } while (cursor.moveToNext())
            cursor.close()
        }
        db.close()
        return catList
    }


    // get the id of category by its name
    // @author Assatulla Dias (xassat00)
    @SuppressLint("Range")
    fun getCategoryId(catName: String): Int {
        val selectQuery = "SELECT * FROM $TABLE_CATEGORIES WHERE name = '$catName'"
        val db = this.readableDatabase
        var cursor: Cursor? = null

        try {
            cursor = db.rawQuery(selectQuery, null)
        } catch (e: SQLException) {
            db.execSQL(selectQuery)
            return 0
        }

        if (cursor.moveToFirst()) {
            return cursor.getInt(cursor.getColumnIndex(KEY_ID))
        }
        cursor.close()
        db.close()

        return 0
    }
}