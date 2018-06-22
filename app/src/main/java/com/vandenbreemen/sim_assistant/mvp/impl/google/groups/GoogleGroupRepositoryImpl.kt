package com.vandenbreemen.sim_assistant.mvp.impl.google.groups

import com.vandenbreemen.sim_assistant.app.SimAssistantApp
import com.vandenbreemen.sim_assistant.data.GoogleGroup
import com.vandenbreemen.sim_assistant.mvp.google.groups.GoogleGroupRepository

class GoogleGroupRepositoryImpl(private val simAssistantApp: SimAssistantApp) : GoogleGroupRepository {
    override fun addGoogleGroup(googleGroup: GoogleGroup) {
        simAssistantApp.boxStore.boxFor(GoogleGroup::class.java).put(googleGroup)
    }

    override fun getGoogleGroups(): List<GoogleGroup> {
        return simAssistantApp.boxStore.boxFor(GoogleGroup::class.java).all
    }
}