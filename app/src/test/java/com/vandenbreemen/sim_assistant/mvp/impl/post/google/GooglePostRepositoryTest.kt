package com.vandenbreemen.sim_assistant.mvp.impl.post.google

import com.vandenbreemen.sim_assistant.api.google.GoogleGroupsApi
import com.vandenbreemen.sim_assistant.api.sim.Sim
import com.vandenbreemen.sim_assistant.app.SimAssistantApp
import com.vandenbreemen.sim_assistant.mvp.impl.google.groups.GoogleGroupsCachedPostRepositoryImpl
import com.vandenbreemen.sim_assistant.mvp.impl.google.groups.GoogleGroupsPost
import com.vandenbreemen.sim_assistant.mvp.impl.google.groups.GoogleGroupsRSSFeed
import com.vandenbreemen.sim_assistant.mvp.impl.post.SimRepositoryImpl
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers.mainThread
import io.reactivex.plugins.RxJavaPlugins
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment

@RunWith(RobolectricTestRunner::class)
class GooglePostRepositoryTest {

    lateinit var googlePostRepository: GooglePostRepository

    lateinit var googleGroupsApi: GoogleGroupsApi

    var googlePosts :MutableList<GoogleGroupsPost> = mutableListOf()

    val contentLoader = object:GooglePostContentLoader(){
        override fun getPostBody(postUrl: String): String {
            return "Test Body of Post"
        }
    }

    @Before
    fun setup() {
        RxJavaPlugins.setIoSchedulerHandler { mainThread() }

        googlePosts = mutableListOf()
        googleGroupsApi = object: GoogleGroupsApi{
            override fun getRssFeed(groupName: String, postCount: Int): Single<GoogleGroupsRSSFeed> {

                var ret = GoogleGroupsRSSFeed()
                ret.channelTitle = "Test Channel"
                ret.articleList = googlePosts

                return Single.just(ret)
            }

        }

        //  Set up basic test article
        var firstPost = GoogleGroupsPost()
        firstPost.author = "Kevin"
        firstPost.link = "http://www.example.com"
        firstPost.pubDate = "Sun, 24 Jun 2018 14:40:31 UTC"
        firstPost.title = "This is a Test"
        googlePosts.add(firstPost)
    }

    @Test
    fun shouldRetrieveListOfPosts() {
        googlePostRepository = GooglePostRepository("sb118-constitution", googleGroupsApi, contentLoader,
                GoogleGroupsCachedPostRepositoryImpl(RuntimeEnvironment.application as SimAssistantApp),
                SimRepositoryImpl(RuntimeEnvironment.application as SimAssistantApp)
                )

        googlePostRepository.getPosts(1).firstElement().subscribe({ postedSim->
            assertNotNull("Post content", postedSim.content)
        })

    }

    @Test
    fun shouldNotIncludeNewLinesInSimAuthorOrTitle(){
        googlePostRepository = GooglePostRepository("sb118-constitution", googleGroupsApi, contentLoader,
                GoogleGroupsCachedPostRepositoryImpl(RuntimeEnvironment.application as SimAssistantApp),
                SimRepositoryImpl(RuntimeEnvironment.application as SimAssistantApp)
        )
        val postedSim = googlePostRepository.getPosts(1).blockingFirst()
        assertFalse("Sim Author newline", postedSim.author.contains("\n"))
        assertFalse("Sim Title newline", postedSim.title.contains("\n"))
    }

    @Test
    fun shouldCacheSimContents(){

        var crashOnCall = false
        val contentLoader = object:GooglePostContentLoader(){
            override fun getPostBody(postUrl: String): String {
                if(crashOnCall){
                    throw RuntimeException("Fail:  Second load when content should be cached!")
                }
                return "Test Content"
            }
        }

        googlePostRepository = GooglePostRepository("sb118-constitution", googleGroupsApi, contentLoader,
                GoogleGroupsCachedPostRepositoryImpl(RuntimeEnvironment.application as SimAssistantApp),
                SimRepositoryImpl(RuntimeEnvironment.application as SimAssistantApp)
                )

        var postedSim = googlePostRepository.getPosts(1).blockingFirst()

        crashOnCall = true

        postedSim = googlePostRepository.getPosts(1).blockingFirst()
        assertNotNull("Post content", postedSim.content)

        println(postedSim)

    }

    @Test
    fun shouldStoreSimsInDatabase(){
        //  Arrange
        var simRepository = SimRepositoryImpl(RuntimeEnvironment.application as SimAssistantApp)
        googlePostRepository = GooglePostRepository("sb118-constitution", googleGroupsApi, contentLoader,
                GoogleGroupsCachedPostRepositoryImpl(RuntimeEnvironment.application as SimAssistantApp),
                simRepository
        )

        //  Act
        googlePostRepository.getPosts(1).blockingFirst()

        //  Assert
        val sim = simRepository.load(1L).blockingGet()
        assertNotNull("Stored in DB", sim.content)
        assertNotNull("Stored in DB", sim.author)
        assertNotNull("Stored in DB", sim.title)

    }

    @Test
    fun shouldReturnStoredSimWhenCreating(){
        //  Arrange
        var simRepository = SimRepositoryImpl(RuntimeEnvironment.application as SimAssistantApp)
        googlePostRepository = GooglePostRepository("sb118-constitution", googleGroupsApi, contentLoader,
                GoogleGroupsCachedPostRepositoryImpl(RuntimeEnvironment.application as SimAssistantApp),
                simRepository
        )

        //  Act
        val sim = googlePostRepository.getPosts(1).blockingFirst()

        //  Assert
        assertEquals("Persisted sim", 1L, sim.id)
    }

    @Test
    fun shouldNotStoreSimAgainIfCached(){
        //  Arrange
        var simRepository = SimRepositoryImpl(RuntimeEnvironment.application as SimAssistantApp)
        googlePostRepository = GooglePostRepository("sb118-constitution", googleGroupsApi, contentLoader,
                GoogleGroupsCachedPostRepositoryImpl(RuntimeEnvironment.application as SimAssistantApp),
                simRepository
        )

        //  Act
        googlePostRepository.getPosts(1).blockingFirst()
        googlePostRepository.getPosts(1).blockingFirst()

        //  Assert
        val app = RuntimeEnvironment.application as SimAssistantApp
        assertEquals("Double Store", 1, app.boxStore.boxFor(Sim::class.java).all.size)
    }

    @Test
    fun shouldReturnStoredSimWhenCaching(){
        //  Arrange
        var simRepository = SimRepositoryImpl(RuntimeEnvironment.application as SimAssistantApp)
        googlePostRepository = GooglePostRepository("sb118-constitution", googleGroupsApi, contentLoader,
                GoogleGroupsCachedPostRepositoryImpl(RuntimeEnvironment.application as SimAssistantApp),
                simRepository
        )

        //  Act
        googlePostRepository.getPosts(1).blockingFirst()
        val cachedSim = googlePostRepository.getPosts(1).blockingFirst()

        //  Assert
        assertEquals("ID", 1L, cachedSim.id)
    }

}