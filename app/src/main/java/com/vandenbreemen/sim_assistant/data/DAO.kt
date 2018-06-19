package com.vandenbreemen.sim_assistant.data

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import android.arch.persistence.room.Update
import com.vandenbreemen.sim_assistant.api.sim.CachedGoogleGroupsPost

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

    @Query("SELECT * from cached_sim WHERE key_url=:simUrl")
    fun getCachedSim(simUrl: String): CachedGoogleGroupsPost?

    @Insert
    fun storeCachedSim(cachedGoogleGroupsPost: CachedGoogleGroupsPost)


}
