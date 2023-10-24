package com.example.journalapp.models

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.SQLException
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class AppDB(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    companion object {
        const val DATABASE_NAME = "FinanceJournalDatabase.db"
        const val DATABASE_VERSION = 1
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
    }


    override fun onCreate(db: SQLiteDatabase?) {
        val createTransactionsTable = "CREATE TABLE IF NOT EXISTS $TABLE_TRANSACTIONS" +
                "($KEY_ID INTEGER PRIMARY KEY," +
                "$KEY_AMOUNT INTEGER," +
                "$KEY_NOTE TEXT," +
                "$KEY_DATE NUMERIC DEFAULT CURRENT_TIMESTAMP," +
                "$KEY_CAT INTEGER," +
                "$KEY_TRANS_TYPE TEXT," +
                "CHECK ($KEY_TRANS_TYPE IN ('Expence','Income'))," +
                "FOREIGN KEY ($KEY_CAT) REFERENCES $TABLE_CATEGORIES($KEY_ID)" +
                ")"


        val createCategoryTable = "CREATE TABLE IF NOT EXISTS $TABLE_CATEGORIES" +
                "( $KEY_ID INTEGER PRIMARY KEY," +
                "$KEY_NAME TEXT," +
                "$KEY_ICON TEXT" +
                ")"

        db?.execSQL(createTransactionsTable)
        db?.execSQL(createCategoryTable)
    }


    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS $TABLE_TRANSACTIONS")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_CATEGORIES")
        onCreate(db)
    }


    fun addTransaction(transaction: TransactionModel): Long {
        val db = this.writableDatabase

        //create container and fill it with passed data from a user
        val contentValues = ContentValues()
        contentValues.put(KEY_AMOUNT, transaction.amount)
        contentValues.put(KEY_NOTE, transaction.note)
        contentValues.put(KEY_DATE, transaction.creationDate)
        contentValues.put(KEY_CAT, transaction.category)

        // insert obtained data to the table "transactions"
        val success = db.insert(TABLE_TRANSACTIONS, null, contentValues)
        // close db connection
        db.close()
        // return the result of insertion
        return success
    }


    fun addCategory(category: CategoryModel): Long {
        val db = this.writableDatabase

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


    @SuppressLint("Range")
    fun viewTransactions(condition: String): ArrayList<TransactionModel> {
        val transList: ArrayList<TransactionModel> = ArrayList()
        val selectQuery: String

        if (condition.isNotEmpty()) {
            selectQuery = "SELECT * FROM $TABLE_TRANSACTIONS WHERE $condition"
        } else {
            selectQuery = "SELECT * FROM $TABLE_TRANSACTIONS"
        }

        val db = this.readableDatabase
        var cursor: Cursor? = null

        try {
            cursor = db.rawQuery(selectQuery, null)
        } catch (e: SQLException) {
            db.execSQL(selectQuery)
            return ArrayList()
        }
        var id: Int
        var amount: Int
        var note: String
        var creationDate: String
        var category: Int
        var transType: String

        if (cursor.moveToFirst()) {
            do {
                id = cursor.getInt(cursor.getColumnIndex(KEY_ID))
                amount = cursor.getInt(cursor.getColumnIndex(KEY_AMOUNT))
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
        return transList
    }


    @SuppressLint("Range")
    fun viewCategories(): ArrayList<CategoryModel> {
        val catList: ArrayList<CategoryModel> = ArrayList()
        val selectQuery = "SELECT * FROM $TABLE_TRANSACTIONS"

        val db = this.readableDatabase
        var cursor: Cursor? = null

        try {
            cursor = db.rawQuery(selectQuery, null)
        } catch (e: SQLException) {
            db.execSQL(selectQuery)
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
        return catList
    }


    fun updateTransaction(transaction: TransactionModel): Int {
        val db = this.writableDatabase
        val contentValues = ContentValues()

        contentValues.put(KEY_AMOUNT, transaction.amount)
        contentValues.put(KEY_NOTE, transaction.note)
        contentValues.put(KEY_DATE, transaction.creationDate)
        contentValues.put(KEY_CAT, transaction.category)

        val success = db.update(
            TABLE_TRANSACTIONS, contentValues,
            KEY_ID + '=' + transaction.id, null
        )
        db.close()
        return success
    }


    fun deleteTransaction(transaction: TransactionModel): Int {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(KEY_ID, transaction.id)
        val success = db.delete(TABLE_TRANSACTIONS, KEY_ID + '=' + transaction.id, null)

        db.close()
        return success
    }
}