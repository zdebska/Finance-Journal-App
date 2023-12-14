/*
* @author Alakaev Kambulat (xalaka00)
* @brief Structure to store records from the "categories" table
* */
package com.example.journalapp.models

import android.os.Parcelable
import android.os.Parcel

// class representing data of a record from the "categories" table
class CategoryModel(val id: Int = 100, val name: String, val iconPath: String, val colorResId: Int):
    Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readInt()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(name)
        parcel.writeString(iconPath)
        parcel.writeInt(colorResId)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<CategoryModel> {
        override fun createFromParcel(parcel: Parcel): CategoryModel {
            return CategoryModel(parcel)
        }

        override fun newArray(size: Int): Array<CategoryModel?> {
            return arrayOfNulls(size)
        }
    }
}
