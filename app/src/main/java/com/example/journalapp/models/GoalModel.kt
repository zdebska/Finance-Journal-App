package com.example.journalapp.models

/**
 * @author Zdebska Kateryna (xzdebs00)
 * @brief Structure to store records from the "goals" table
 *
 * @param id The unique identifier of the goal.
 * @param name The name of the goal.
 * @param amount The target amount for the goal.
 * @param endDate The end date of the goal.
 * @param saved The amount saved towards the goal.
 * @param isReached Flag indicating whether the goal is reached (1) or not (0).
 */
import android.os.Parcel
import android.os.Parcelable

class GoalModel(
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

    /**
     * Write object values to a Parcel for serialization.
     *
     * @param parcel The Parcel in which the object should be written.
     * @param flags Additional flags about how the object should be written.
     */
    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(name)
        parcel.writeFloat(amount)
        parcel.writeString(endDate)
        parcel.writeFloat(saved)
        parcel.writeInt(isReached)
    }

    /**
     * Describe the kinds of special objects contained in this Parcelable instance.
     *
     * @return a bitmask indicating the set of special object types.
     */
    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<GoalModel> {
        /**
         * Create a new instance of the Parcelable class, instantiating it from the given Parcel
         * whose data had previously been written by [Parcelable.writeToParcel].
         *
         * @param parcel The Parcel to read the object's data from.
         * @return Returns a new instance of the Parcelable class.
         */
        override fun createFromParcel(parcel: Parcel): GoalModel {
            return GoalModel(parcel)
        }

        /**
         * Create a new array of the Parcelable class.
         *
         * @param size Size of the array.
         * @return Returns an array of the Parcelable class, with every entry initialized to null.
         */
        override fun newArray(size: Int): Array<GoalModel?> {
            return arrayOfNulls(size)
        }
    }
}
