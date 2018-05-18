package com.vandenbreemen.sim_assistant.data

import android.arch.persistence.db.SupportSQLiteDatabase
import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.migration.Migration

@Database(entities = [UserSettings::class, GoogleGroup::class], version = 2)
abstract class AppDatabase : RoomDatabase(){

    companion object {
        val MIGRATION_1_2 = object:Migration(1,2){
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("CREATE TABLE google_group(groupName TEXT NOT NULL, PRIMARY KEY(groupName))")
            }

        }
    }

    abstract fun dao(): DAO


}
