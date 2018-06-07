package com.vandenbreemen.sim_assistant.api.google

import junit.framework.TestCase.assertEquals
import org.awaitility.Awaitility.await
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.simplexml.SimpleXmlConverterFactory
import java.util.concurrent.TimeUnit

private const val GOOGLE_GROUPS_BASE_URL = "https://groups.google.com/"

class GoogleGroupsApiLearningTest {

    @Test
    fun shouldRequestGoogleGroupsPosts() {
        val retroFit = Retrofit.Builder().baseUrl(GOOGLE_GROUPS_BASE_URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(SimpleXmlConverterFactory.create()).build()

        val googleGroupsApi = retroFit.create(GoogleGroupsApi::class.java)

        var complete = false
        googleGroupsApi.getRssFeed("sb118-constitution", 15).subscribe { rssFeed ->
            println("Huzzah ${rssFeed.channelTitle}:\n${rssFeed.articleList}")
            complete = true
        }

        await().atMost(5, TimeUnit.SECONDS).until { complete }

    }

    @Test
    fun shouldParametrizeNumberOfPosts(){
        val retroFit = Retrofit.Builder().baseUrl(GOOGLE_GROUPS_BASE_URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(SimpleXmlConverterFactory.create()).build()

        val googleGroupsApi = retroFit.create(GoogleGroupsApi::class.java)

        var complete = false
        googleGroupsApi.getRssFeed("sb118-constitution", 1).subscribe { rssFeed ->
            assertEquals("Single Article", 1, rssFeed.articleList!!.size)
            complete = true
        }

        await().atMost(5, TimeUnit.SECONDS).until { complete }
    }

}