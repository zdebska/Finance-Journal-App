/*
* @author Alakaev Kambulat (xalaka00)
* @author Assatulla Dias (xassat00)
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

        const val KEY_ID = "id"
        const val KEY_AMOUNT = "amount"
        const val KEY_NOTE = "note"
        const val KEY_DATE = "creation_date"
        const val KEY_TRANS_TYPE = "trans_type"
        const val KEY_CAT = "category"
        const val KEY_NAME = "name"
        const val KEY_ICON = "icon_path"
        const val KEY_COLOR = "color_icon"
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
                ")"


        val createCategoryTable = "CREATE TABLE IF NOT EXISTS $TABLE_CATEGORIES" +
                "( $KEY_ID INTEGER PRIMARY KEY," +
                "$KEY_NAME TEXT NOT NULL," +
                "$KEY_ICON TEXT NOT NULL," +
                "$KEY_COLOR INTEGER NOT NULL" +
                ")"

        db?.execSQL(createTransactionsTable)
        db?.execSQL(createCategoryTable)
    }


    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS $TABLE_TRANSACTIONS")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_CATEGORIES")
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


    // delete existing transaction
    fun deleteTransaction(transID: Int): Int {
        val db = this.writableDatabase
        val success = db.delete(TABLE_TRANSACTIONS, "$KEY_ID=$transID", null)

        db.close()
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