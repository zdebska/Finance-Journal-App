/*
* @author Alakaev Kambulat (xalaka00)
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
        const val DATABASE_VERSION = 2
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
                "$KEY_ICON TEXT NOT NULL" +
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
                "$KEY_NOTE TEXT NOT NULL," +
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

    fun addGoalTransaction(transaction: GoalTransactionModel): Long {
        // set database op mode to write
        val db = this.writableDatabase

        //create container and fill it with passed data from a user
        val contentValues = ContentValues()
        contentValues.put(KEY_AMOUNT, transaction.amount)
        contentValues.put(KEY_NOTE, transaction.note)
        contentValues.put(KEY_DATE, transaction.creationDate)
        contentValues.put(KEY_GOAL, transaction.goal)

        // insert obtained data to the table "transactions"
        val success = db.insert(TABLE_GOALS_TRANSACTIONS, null, contentValues)
        // close db connection
        db.close()
        // return the result of insertion
        return success
    }

    // add category to the "categories" table
    fun addCategory(category: CategoryModel): Long {
        val db = this.writableDatabase

        // create container and fill it with passed data from a user
        val contentValues = ContentValues()
        contentValues.put(KEY_NAME, category.name)
        contentValues.put(KEY_ICON, category.iconPath)

        // insert obtained data to the table "categories"
        val success = db.insert(TABLE_CATEGORIES, null, contentValues)

        // close db connection
        db.close()
        // return the result of insertion
        return success
    }

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

    @SuppressLint("Range")
    fun viewGoals(): ArrayList<GoalModel> {
        // create an array whose elements are records from the goals table
        val goalList: ArrayList<GoalModel> = ArrayList()
        val selectQuery = "SELECT * FROM $TABLE_GOALS"

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
        var amount: Float
        var endDate: String

        if (cursor.moveToFirst()) {
            do {
                id = cursor.getInt(cursor.getColumnIndex(KEY_ID))
                name = cursor.getString(cursor.getColumnIndex(KEY_NAME))
                amount = cursor.getFloat(cursor.getColumnIndex(KEY_AMOUNT))
                endDate = cursor.getString(cursor.getColumnIndex(KEY_END_DATE))

                val goal = GoalModel(
                    id = id,
                    name = name,
                    amount = amount,
                    endDate = endDate
                )
                goalList.add(goal)
            } while (cursor.moveToNext())
            cursor.close()
        }
        db.close()
        return goalList
    }

    @SuppressLint("Range")
    fun viewGoalsTransactions(condition: String): ArrayList<GoalTransactionModel> {
        // create an array whose elements are records from the table
        val transList: ArrayList<GoalTransactionModel> = ArrayList()
        val selectQuery: String

        // set query depending on a presence of condition
        if (condition.isNotEmpty()) {
            selectQuery = "SELECT * FROM $TABLE_GOALS_TRANSACTIONS $condition"
        } else {
            selectQuery = "SELECT * FROM $TABLE_GOALS_TRANSACTIONS"
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
        var goal: Int

        if (cursor.moveToFirst()) {
            do {
                id = cursor.getInt(cursor.getColumnIndex(KEY_ID))
                amount = cursor.getFloat(cursor.getColumnIndex(KEY_AMOUNT))
                note = cursor.getString(cursor.getColumnIndex(KEY_NOTE))
                creationDate = cursor.getString(cursor.getColumnIndex(KEY_DATE))
                goal = cursor.getInt(cursor.getColumnIndex(KEY_GOAL))

                val transaction = GoalTransactionModel(
                    id = id,
                    amount = amount,
                    note = note,
                    creationDate = creationDate,
                    goal = goal
                )
                transList.add(transaction)
            } while (cursor.moveToNext())
            cursor.close()
        }
        db.close()
        return transList
    }


    //  select transactions from the "categories" table (TODO: add condition processing)
    @SuppressLint("Range")
    fun viewCategories(): ArrayList<CategoryModel> {
        val catList: ArrayList<CategoryModel> = ArrayList()
        val selectQuery = "SELECT * FROM $TABLE_TRANSACTIONS"

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
        if (cursor.moveToFirst()) {
            do {
                id = cursor.getInt(cursor.getColumnIndex(KEY_ID))
                name = cursor.getString(cursor.getColumnIndex(KEY_NAME))
                iconPath = cursor.getString(cursor.getColumnIndex(KEY_ICON))

                val category = CategoryModel(id = id, name = name, iconPath = iconPath)
                catList.add(category)
            } while (cursor.moveToNext())
            cursor.close()
        }
        db.close()
        return catList
    }


    // update existing transaction
    fun updateTransaction(transaction: TransactionModel): Int {
        val db = this.writableDatabase
        val contentValues = ContentValues()

        contentValues.put(KEY_AMOUNT, transaction.amount)
        contentValues.put(KEY_NOTE, transaction.note)
        contentValues.put(KEY_DATE, transaction.creationDate)
        contentValues.put(KEY_CAT, transaction.category)
        contentValues.put(KEY_TRANS_TYPE, transaction.transType)

        val success = db.update(
            TABLE_TRANSACTIONS, contentValues,
            KEY_ID + '=' + transaction.id, null
        )
        db.close()
        return success
    }


    // delete existing transaction
    fun deleteTransaction(transaction: TransactionModel): Int {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(KEY_ID, transaction.id)
        val success = db.delete(TABLE_TRANSACTIONS, KEY_ID + '=' + transaction.id, null)

        db.close()
        return success
    }


    // get the id of category by its name
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
            return cursor.getInt(cursor.getColumnIndex(KEY_NAME))
        }
        cursor.close()
        db.close()

        return 0
    }
}