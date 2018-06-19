package com.vandenbreemen.sim_assistant.mvp.impl.post.google

import com.vandenbreemen.sim_assistant.api.google.GoogleGroupsApi
import com.vandenbreemen.sim_assistant.api.sim.Sim
import com.vandenbreemen.sim_assistant.mvp.impl.google.groups.GoogleGroupsPost
import com.vandenbreemen.sim_assistant.mvp.impl.post.google.GooglePostCacheInteractor.Companion.NO_CACHE_HIT
import com.vandenbreemen.sim_assistant.mvp.post.PostRepository
import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe
import io.reactivex.schedulers.Schedulers.io
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.simplexml.SimpleXmlConverterFactory
import java.text.SimpleDateFormat

const val GOOGLE_GROUPS_BASE_URL = "https://groups.google.com/"

class GooglePostRepository(val groupName: String, private val contentLoader: GooglePostContentLoader,
                           private val googlePostCacheInteractor: GooglePostCacheInteractor
                           ) : PostRepository {

    val googleGroupsApi = Retrofit.Builder().baseUrl(GOOGLE_GROUPS_BASE_URL)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(SimpleXmlConverterFactory.create()).build().create(GoogleGroupsApi::class.java)

    override fun getPosts(numPosts: Int): Observable<Sim> {

        return googleGroupsApi.getRssFeed(groupName, numPosts).subscribeOn(io()).flatMapObservable { rssFeed ->
            Observable.create(ObservableOnSubscribe<Sim> { observableEmitter ->
                rssFeed.articleList!!.forEach { googleGroupPost ->

                    val urlKey = googleGroupPost.link!!

                    val cachedSim = googlePostCacheInteractor.retrieve(urlKey)

                    val postedSim = Sim(
                            googleGroupPost.title!!.trim(),
                            googleGroupPost.author!!.trim(),
                            SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z").parse(googleGroupPost.pubDate!!).time,
                            if (cachedSim == NO_CACHE_HIT) getPostBody(googleGroupPost) else cachedSim.content
                    )

                    if (cachedSim == NO_CACHE_HIT) {
                        googlePostCacheInteractor.cacheSim(urlKey, postedSim.content)
                    }

                    observableEmitter.onNext(postedSim)

                }
                observableEmitter.onComplete()
            }).subscribeOn(io())
        }

    }

    private fun getPostBody(googleGroupsPost: GoogleGroupsPost): String {

        val urlToLoad = "(/d/)".toRegex().replace(googleGroupsPost.link!!, "/forum/print/")
        return contentLoader.getPostBody(urlToLoad)
    }
}