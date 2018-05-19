package com.vandenbreemen.sim_assistant.mvp.impl.post.google

import com.vandenbreemen.sim_assistant.api.google.GoogleGroupsApi
import com.vandenbreemen.sim_assistant.api.sim.PostedSim
import com.vandenbreemen.sim_assistant.mvp.impl.google.groups.GoogleGroupsPost
import com.vandenbreemen.sim_assistant.mvp.post.PostRepository
import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe
import org.jsoup.Jsoup
import org.jsoup.safety.Whitelist
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.simplexml.SimpleXmlConverterFactory
import java.net.HttpURLConnection
import java.net.URL
import java.util.*

private const val GOOGLE_GROUPS_BASE_URL = "https://groups.google.com/"

class GooglePostRepository(val groupName: String) : PostRepository {

    val googleGroupsApi = Retrofit.Builder().baseUrl(GOOGLE_GROUPS_BASE_URL)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(SimpleXmlConverterFactory.create()).build().create(GoogleGroupsApi::class.java)

    override fun getPosts(): Observable<PostedSim> {

        return googleGroupsApi.getRssFeed(groupName).flatMapObservable { rssFeed ->
            Observable.create(ObservableOnSubscribe<PostedSim> { observableEmitter ->
                rssFeed.articleList!!.forEach { googleGroupPost ->
                    observableEmitter.onNext(PostedSim(getPostBody(googleGroupPost)))
                }
                observableEmitter.onComplete()
            })
        }

    }

    private fun getPostBody(googleGroupsPost: GoogleGroupsPost): String {

        val urlToLoad = "(/d/)".toRegex().replace(googleGroupsPost.link!!, "/forum/print/")
        println("URL=$urlToLoad")
        val url = URL(urlToLoad)
        val connection = url.openConnection() as HttpURLConnection
        var postBody = ""

        val bld = StringBuilder()
        connection.getInputStream().use({ inputStream ->
            val scanner = Scanner(inputStream)
            while (scanner.hasNextLine()) {
                bld.append(scanner.nextLine()).append("\n")
            }

            postBody = bld.toString()
        })

        //  Now we parse
        val document = Jsoup.parse(postBody)
        document.outputSettings().prettyPrint(false)
        document.select("div").append("\\n")
        document.select("br").append("\\n")

        val element = document.getElementsByClass("messageContent").get(0)

        var html = "\\\\n".toRegex().replace(element.html(), "\n")
        postBody = Jsoup.clean(html, "", Whitelist.none(), org.jsoup.nodes.Document.OutputSettings().prettyPrint(false))
        postBody = postBody.trim({ it <= ' ' })

        return postBody
    }
}