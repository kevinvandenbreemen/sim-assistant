package com.vandenbreemen.sim_assistant.android

import android.content.Intent
import org.junit.Assert.assertTrue
import org.junit.Assert.fail
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment

/**
 * @author kevin
 */
@RunWith(RobolectricTestRunner::class)
class HeadphonesReceiverTest{



    @Test
    fun shouldCallDisconnectOnDisconnectEvent(){

        var disconnect = false

        var headphonesReceiver = object : HeadphonesReceiver(){
            override fun onHeadsetDisconnected() {
                disconnect = true
            }

            override fun onHeadssetConnected() {
                fail("Should not call onHeadsetConnected")
            }

        }

        val intent = Intent()
        intent.putExtra("state", 0)
        intent.action = Intent.ACTION_HEADSET_PLUG

        headphonesReceiver.onReceive(RuntimeEnvironment.systemContext, intent)

        assertTrue("Disconnect action", disconnect)
    }

    @Test
    fun shouldCallConnectOnConnectEvent(){
        var connect = false

        var headphonesReceiver = object : HeadphonesReceiver(){
            override fun onHeadsetDisconnected() {
                fail("Should not call onHeadsetDisconnected")
            }

            override fun onHeadssetConnected() {
                connect = true
            }

        }

        val intent = Intent()
        intent.putExtra("state", 1)
        intent.action = Intent.ACTION_HEADSET_PLUG

        headphonesReceiver.onReceive(RuntimeEnvironment.systemContext, intent)

        assertTrue("Connect action", connect)
    }

}