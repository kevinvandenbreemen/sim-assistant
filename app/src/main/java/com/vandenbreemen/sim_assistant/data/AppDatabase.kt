package com.vandenbreemen.sim_assistant.data

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase

@Database(entities = [UserSettings::class, GoogleGroup::class], version = 2)
abstract class AppDatabase : RoomDatabase(){
    abstract fun dao(): DAO


}
