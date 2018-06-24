package com.vandenbreemen.sim_assistant.api.sim

import android.os.Parcel
import android.os.Parcelable
import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id
import io.objectbox.annotation.Uid

@Uid(8075857466880120845L)
@Entity
data class Sim(
        @Id
        var id:Long,
        val title:String,
        val author:String,
        val postedDate: Long,
        val content: String

) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readLong(),
            parcel.readString(),
            parcel.readString(),
            parcel.readLong(),
            parcel.readString()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(id)
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