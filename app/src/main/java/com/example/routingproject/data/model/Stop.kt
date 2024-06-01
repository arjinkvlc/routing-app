package com.example.routingproject.data.model

import android.os.Parcel
import android.os.Parcelable
import java.io.Serializable

data class Stop(
    val location: Any?, // Daha iyi bir türle değiştirilmesi tavsiye edilir
    val locationId: Int,
    val priority: Int,
    val stopArriveTime: String,
    val stopDescription: String,
    val stopId: Int,
    val stopName: String,
    val trip: Any?, // Daha iyi bir türle değiştirilmesi tavsiye edilir
    val tripId: Int,
    val spendingTime: Int
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readSerializable(), // location
        parcel.readInt(),          // locationId
        parcel.readInt(),          // priority
        parcel.readString() ?: "", // stopArriveTime
        parcel.readString() ?: "", // stopDescription
        parcel.readInt(),          // stopId
        parcel.readString() ?: "", // stopName
        parcel.readSerializable(), // trip
        parcel.readInt(),          // tripId
        parcel.readInt()           // spendingTime
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeSerializable(location as Serializable?)
        parcel.writeInt(locationId)
        parcel.writeInt(priority)
        parcel.writeString(stopArriveTime)
        parcel.writeString(stopDescription)
        parcel.writeInt(stopId)
        parcel.writeString(stopName)
        parcel.writeSerializable(trip as Serializable?)
        parcel.writeInt(tripId)
        parcel.writeInt(spendingTime)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Stop> {
        override fun createFromParcel(parcel: Parcel): Stop {
            return Stop(parcel)
        }

        override fun newArray(size: Int): Array<Stop?> {
            return arrayOfNulls(size)
        }
    }
}
