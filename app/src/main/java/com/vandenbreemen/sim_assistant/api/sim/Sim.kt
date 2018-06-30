package com.vandenbreemen.sim_assistant.api.sim

import android.os.Parcel
import android.os.Parcelable
import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id
import io.objectbox.annotation.Transient
import io.objectbox.annotation.Uid

@Uid(8075857466880120845L)
@Entity
data class Sim(
        @Id
        var id:Long,
        val title:String,
        val author:String,
        val postedDate: Long,
        val content: String,

        @Transient
        var selected: Boolean = false
) : Parcelable {

    //  No-arg constructor required to keep ObjectBox happy
    //  See also https://github.com/objectbox/objectbox-java/issues/320
    constructor() : this(
            0, "", "", 0, ""
    )

    constructor(parcel: Parcel) : this(
            parcel.readLong(),
            parcel.readString(),
            parcel.readString(),
            parcel.readLong(),
            parcel.readString(),
            parcel.readByte() != 0.toByte()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(id)
        parcel.writeString(title)
        parcel.writeString(author)
        parcel.writeLong(postedDate)
        parcel.writeString(content)
        parcel.writeByte(if (selected) 1 else 0)
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