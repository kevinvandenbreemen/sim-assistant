package com.com.vandenbreemen.sim_assistant.data

import android.arch.persistence.room.Room
import com.vandenbreemen.sim_assistant.data.AppDatabase
import com.vandenbreemen.sim_assistant.data.UserSettings
import junit.framework.TestCase.assertEquals
import org.awaitility.Awaitility.await
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import java.util.concurrent.TimeUnit

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

        await().atMost(5, TimeUnit.SECONDS).until {
            dao.getUserSettings() != null && dao.getUserSettings()!!.dataSource == 1L
        }

    }

}