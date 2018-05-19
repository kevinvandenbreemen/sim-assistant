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
        googlePostRepository = GooglePostRepository("uss-odyssey-oe")
    }

    @Test
    fun shouldRetrieveListOfPosts() {
        val postedSim = googlePostRepository.getPosts().blockingFirst()
        assertNotNull("Post content", postedSim.content)
    }


}