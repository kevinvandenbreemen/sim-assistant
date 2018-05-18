package com.vandenbreemen.sim_assistant.data

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import android.arch.persistence.room.Update

@Dao
interface DAO {

    @Insert
    fun storeUserSettings(userSettings: UserSettings)

    @Update
    fun updateUserSettings(userSettings: UserSettings)

    @Query("SELECT * from usersettings WHERE id=1")
    fun getUserSettings(): UserSettings?

    @Insert
    fun storeGoogleGroup(googleGroup: GoogleGroup)

    @Query("SELECT * from google_group")
    fun getGoogleGroups(): List<GoogleGroup>


}
