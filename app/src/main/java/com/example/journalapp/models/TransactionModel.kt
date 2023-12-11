/*
* @author Alakaev Kambulat (xalaka00)
* @brief Structure to store records from the "transactions" table
* */
package com.example.journalapp.models

// class representing data of a record from the "transactions" table
import android.os.Parcel
import android.os.Parcelable

class TransactionModel(
    val id: Int,
    val amount: Float,
    val note: String,
    val creationDate: String,
    val category: Int,
    val transType: String
) : Parcelable {
    // Implement Parcelable methods
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readFloat(),
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readInt(),
        parcel.readString() ?: ""
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeFloat(amount)
        parcel.writeString(note)
        parcel.writeString(creationDate)
        parcel.writeInt(category)
        parcel.writeString(transType)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<TransactionModel> {
        override fun createFromParcel(parcel: Parcel): TransactionModel {
            return TransactionModel(parcel)
        }

        override fun newArray(size: Int): Array<TransactionModel?> {
            return arrayOfNulls(size)
        }
    }
}
