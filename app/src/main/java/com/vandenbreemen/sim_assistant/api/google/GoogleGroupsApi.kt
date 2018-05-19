package com.vandenbreemen.sim_assistant.api.google

import com.vandenbreemen.sim_assistant.mvp.impl.google.groups.GoogleGroupsRSSFeed
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path

interface GoogleGroupsApi {

    @GET("forum/feed/{groupName}/msgs/rss.xml?num=15")
    fun getRssFeed(
            @Path("groupName")
            groupName: String): Single<GoogleGroupsRSSFeed>

}