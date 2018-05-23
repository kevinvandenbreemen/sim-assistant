package com.vandenbreemen.sim_assistant.api.sim

import android.os.Parcel
import android.os.Parcelable

data class Sim(
        val title:String,
        val author:String,
        val postedDate: Long,
        val content: String

) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString(),
            parcel.readLong(),
            parcel.readString()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(title)
        parcel.writeString(author)
        parcel.writeLong(postedDate)
        parcel.writeString(content)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Sim> {
        override fun createFromParcel(parcel: Parcel): Sim {
            return Sim(parcel)
        }

        override fun newArray(size: Int): Array<Sim?> {
            return arrayOfNulls(size)
        }
    }
}