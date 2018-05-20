package com.vandenbreemen.sim_assistant.mvp.impl

import android.arch.persistence.room.Room
import com.vandenbreemen.sim_assistant.app.SimAssistantApp
import com.vandenbreemen.sim_assistant.data.AppDatabase
import com.vandenbreemen.sim_assistant.data.DAO
import com.vandenbreemen.sim_assistant.data.DATABASE_NAME
import java.util.function.Consumer

/**
 * <h2>Intro</h2>
 *
 * <h2>Other Details</h2>
 * @author kevin
 */
open class DatabaseInteractor(private val application: SimAssistantApp) {

    protected fun doWithDatabase(function: Consumer<DAO>){

        val builder = Room.databaseBuilder(application.applicationContext,
                AppDatabase::class.java, DATABASE_NAME)
                .addMigrations(
                        AppDatabase.MIGRATION_1_2,
                        AppDatabase.MIGRATION_2_3
                )

        if (application.isInUnitTest()) {
            builder.allowMainThreadQueries()
        }

        val database = builder.build()

        val dao = database.dao()

        try{
            function.accept(dao)
        }
        finally{
            database.close()
        }
    }

}