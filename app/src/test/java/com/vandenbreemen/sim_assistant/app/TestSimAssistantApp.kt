package com.vandenbreemen.sim_assistant.app

import org.robolectric.TestLifecycleApplication
import java.lang.reflect.Method

class TestSimAssistantApp : SimAssistantApp(), TestLifecycleApplication {

    override fun isInUnitTest(): Boolean {
        return true
    }

    override fun beforeTest(method: Method?) {

    }

    override fun prepareTest(test: Any?) {

    }

    override fun afterTest(method: Method?) {

    }

}