package com.vandenbreemen.sim_assistant.data

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert

@Dao
interface DAO {

    @Insert
    fun storeUserSettings(userSettings: UserSettings)


}
