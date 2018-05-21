package com.vandenbreemen.sim_assistant.mvp.google.groups

import com.vandenbreemen.sim_assistant.data.GoogleGroup
import io.reactivex.Completable
import io.reactivex.Single

/**
 * <h2>Intro</h2>
 *
 * <h2>Other Details</h2>
 * @author kevin
 */
interface GoogleGroupsInteractor {

    fun addGoogleGroup(groupName:String):Completable
    fun getGoogleGroups(): Single<List<GoogleGroup>>
    fun googleGroupExists(groupName: String): Single<Boolean>

}