package com.example.journalapp.models

import android.os.Parcel
import android.os.Parcelable

class GoalModel (
    val id: Int,
    val name: String,
    val amount: Float,
    val endDate: String,
    val saved: Float,
    val isReached: Int
) : Parcelable {
    // Implement Parcelable methods
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString() ?: "",
        parcel.readFloat(),
        parcel.readString() ?: "",
        parcel.readFloat(),
        parcel.readInt()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(name)
        parcel.writeFloat(amount)
        parcel.writeString(endDate)
        parcel.writeFloat(saved)
        parcel.writeInt(isReached)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<GoalModel> {
        override fun createFromParcel(parcel: Parcel): GoalModel {
            return GoalModel(parcel)
        }

        override fun newArray(size: Int): Array<GoalModel?> {
            return arrayOfNulls(size)
        }
    }
}