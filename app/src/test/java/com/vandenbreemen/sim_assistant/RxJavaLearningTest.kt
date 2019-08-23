package com.vandenbreemen.sim_assistant

import io.reactivex.Observable
import junit.framework.TestCase.assertTrue
import org.junit.Test

class RxJavaLearningTest {

    @Test
    fun howToGetOnCompleteCallbackWithMockedObservable() {
        val observable = Observable.just(1, 2, 3)

        var completed = false
        observable.subscribe({
            println(it)
        }, {}, {
            println("Complete")
            completed = true
        })

        assertTrue("Completed", completed)
    }

}