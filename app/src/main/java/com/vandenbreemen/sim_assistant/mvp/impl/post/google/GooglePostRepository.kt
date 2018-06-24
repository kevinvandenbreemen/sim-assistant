package com.vandenbreemen.sim_assistant.mvp.impl.post.google

import com.vandenbreemen.sim_assistant.api.google.GoogleGroupsApi
import com.vandenbreemen.sim_assistant.api.sim.Sim
import com.vandenbreemen.sim_assistant.mvp.google.groups.GoogleGroupsCachedPostRepository
import com.vandenbreemen.sim_assistant.mvp.impl.google.groups.GoogleGroupsPost
import com.vandenbreemen.sim_assistant.mvp.post.PostRepository
import com.vandenbreemen.sim_assistant.mvp.post.SimRepository
import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe
import io.reactivex.schedulers.Schedulers.io
import java.text.SimpleDateFormat

const val GOOGLE_GROUPS_BASE_URL = "https://groups.google.com/"

class GooglePostRepository(val groupName: String,
                           private val googleGroupsApi: GoogleGroupsApi,
                           private val contentLoader: GooglePostContentLoader,
                           private val googleGroupsCachedPostRepository: GoogleGroupsCachedPostRepository,
                           private val simRepository: SimRepository
                           ) : PostRepository {

    override fun getPosts(numPosts: Int): Observable<Sim> {

        return googleGroupsApi.getRssFeed(groupName, numPosts).subscribeOn(io()).flatMapObservable { rssFeed ->
            Observable.create(ObservableOnSubscribe<Sim> { observableEmitter ->
                rssFeed.articleList!!.forEach { googleGroupPost ->

                    val urlKey = googleGroupPost.link!!

                    val cachedSim = googleGroupsCachedPostRepository.retrieve(urlKey)

                    var refToActualSim = if(cachedSim != null)
                        googleGroupsCachedPostRepository.findCorrespondingSim(cachedSim)
                    else
                        null

                    val postedSim = if(refToActualSim != null) refToActualSim else Sim(
                            0L,
                            googleGroupPost.title!!.trim(),
                            googleGroupPost.author!!.trim(),
                            SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z").parse(googleGroupPost.pubDate!!).time,
                            if (cachedSim == null) getPostBody(googleGroupPost) else cachedSim!!.content
                    )

                    if (cachedSim == null) {
                        var cachedSim = googleGroupsCachedPostRepository.cacheSim(urlKey, postedSim.content)
                        simRepository.store(postedSim).subscribe {
                            googleGroupsCachedPostRepository.referenceCachedPostToSim(cachedSim, postedSim)
                        }
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