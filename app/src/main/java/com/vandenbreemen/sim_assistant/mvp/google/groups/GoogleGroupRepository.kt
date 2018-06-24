package com.vandenbreemen.sim_assistant.mvp.google.groups

import com.vandenbreemen.sim_assistant.data.GoogleGroup

interface GoogleGroupRepository {

    fun addGoogleGroup(googleGroup: GoogleGroup)

    fun getGoogleGroups(): List<GoogleGroup>

}