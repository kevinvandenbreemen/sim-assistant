package com.vandenbreemen.sim_assistant.api.google

import com.vandenbreemen.sim_assistant.mvp.impl.google.groups.GoogleGroupsRSSFeed
import org.awaitility.Awaitility.await
import org.junit.Test
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.simplexml.SimpleXmlConverterFactory
import java.util.concurrent.TimeUnit

private const val GOOGLE_GROUPS_BASE_URL = "https://groups.google.com/"

class GoogleGroupsApiLearningTest {

    @Test
    fun shouldRequestGoogleGroupsPosts() {
        val retroFit = Retrofit.Builder().baseUrl(GOOGLE_GROUPS_BASE_URL)
                .addConverterFactory(SimpleXmlConverterFactory.create()).build()

        val googleGroupsApi = retroFit.create(GoogleGroupsApi::class.java)

        var complete = false
        googleGroupsApi.getRssFeed("uss-odyssey-oe").enqueue(
                object : Callback<GoogleGroupsRSSFeed> {
                    override fun onFailure(call: Call<GoogleGroupsRSSFeed>?, t: Throwable?) {
                        t?.printStackTrace()
                        throw RuntimeException("Request failed!")
                    }

                    override fun onResponse(call: Call<GoogleGroupsRSSFeed>?, response: Response<GoogleGroupsRSSFeed>?) {
                        println("Huzzah:  ${response!!.body().channelTitle}")
                        complete = true
                    }

                }
        )

        await().atMost(5, TimeUnit.SECONDS).until { complete }

    }

}