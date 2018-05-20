package com.vandenbreemen.sim_assistant.mvp.impl.post.google

import io.reactivex.android.schedulers.AndroidSchedulers.mainThread
import io.reactivex.plugins.RxJavaPlugins
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class GooglePostRepositoryTest {

    lateinit var googlePostRepository: GooglePostRepository

    @Before
    fun setup() {
        RxJavaPlugins.setIoSchedulerHandler { mainThread() }

    }

    @Test
    fun shouldRetrieveListOfPosts() {
        googlePostRepository = GooglePostRepository("uss-odyssey-oe", GooglePostContentLoader())
        val postedSim = googlePostRepository.getPosts().blockingFirst()
        assertNotNull("Post content", postedSim.content)
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

        googlePostRepository = GooglePostRepository("uss-odyssey-oe", contentLoader)

        var postedSim = googlePostRepository.getPosts().blockingFirst()

        crashOnCall = true

        postedSim = googlePostRepository.getPosts().blockingFirst()
        assertNotNull("Post content", postedSim.content)

    }


}