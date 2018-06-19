package com.vandenbreemen.sim_assistant.data

import android.arch.persistence.db.SupportSQLiteDatabase
import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.migration.Migration
import com.vandenbreemen.sim_assistant.api.sim.CachedGoogleGroupsPost

val DATABASE_NAME = "sims-database"

@Database(entities = [UserSettings::class, GoogleGroup::class, CachedGoogleGroupsPost::class], version = 3)
abstract class AppDatabase : RoomDatabase(){

    companion object {
        val MIGRATION_1_2 = object:Migration(1,2){
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("CREATE TABLE google_group(groupName TEXT NOT NULL, PRIMARY KEY(groupName))")
            }

        }

        val MIGRATION_2_3 = object:Migration(2,3){
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("CREATE TABLE cached_sim(key_url TEXT NOT NULL, content TEXT NOT NULL, PRIMARY KEY(key_url))")
            }
        }

    }

    abstract fun dao(): DAO


}
