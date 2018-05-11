package com.com.vandenbreemen.sim_assistant.data

import android.arch.persistence.room.Room
import com.vandenbreemen.sim_assistant.data.AppDatabase
import com.vandenbreemen.sim_assistant.data.UserSettings
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment

@RunWith(RobolectricTestRunner::class)
class UserSettingsTest {

    @Test
    fun shouldStoreUserSettings(){
        val database = Room.databaseBuilder(RuntimeEnvironment.application.applicationContext,
                AppDatabase::class.java, "test-database").build()

        val dao = database.dao()

        object: Thread(){
            override fun run() {
                if(Thread.currentThread().equals(this)) {
                    dao.storeUserSettings(UserSettings(1, 1))
                }
            }
        }.start()


    }

}