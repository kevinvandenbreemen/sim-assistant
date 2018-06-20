package com.vandenbreemen.sim_assistant.mvp.impl.post.google

import com.vandenbreemen.sim_assistant.app.SimAssistantApp
import com.vandenbreemen.sim_assistant.mvp.impl.google.groups.GoogleGroupsCachedPostRepositoryImpl
import io.reactivex.android.schedulers.AndroidSchedulers.mainThread
import io.reactivex.plugins.RxJavaPlugins
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

    @Before
    fun setup() {
        RxJavaPlugins.setIoSchedulerHandler { mainThread() }

    }

    @Test
    fun shouldRetrieveListOfPosts() {
        googlePostRepository = GooglePostRepository("sb118-constitution", GooglePostContentLoader(),
                GoogleGroupsCachedPostRepositoryImpl(RuntimeEnvironment.application as SimAssistantApp)
                )

        googlePostRepository.getPosts(1).firstElement().subscribe({ postedSim->
            assertNotNull("Post content", postedSim.content)
        })

    }

    @Test
    fun shouldNotIncludeNewLinesInSimAuthorOrTitle(){
        googlePostRepository = GooglePostRepository("sb118-constitution", GooglePostContentLoader(),
                GoogleGroupsCachedPostRepositoryImpl(RuntimeEnvironment.application as SimAssistantApp)
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
                return super.getPostBody(postUrl)
            }
        }

        googlePostRepository = GooglePostRepository("sb118-constitution", contentLoader,
                GoogleGroupsCachedPostRepositoryImpl(RuntimeEnvironment.application as SimAssistantApp)
                )

        var postedSim = googlePostRepository.getPosts(1).blockingFirst()

        crashOnCall = true

        postedSim = googlePostRepository.getPosts(1).blockingFirst()
        assertNotNull("Post content", postedSim.content)

        println(postedSim)

    }


}