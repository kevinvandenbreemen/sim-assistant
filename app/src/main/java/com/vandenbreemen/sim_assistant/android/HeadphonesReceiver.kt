package com.vandenbreemen.sim_assistant.android

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.AudioManager

/**
 *
 * @author kevin
 */
abstract class HeadphonesReceiver :BroadcastReceiver(){
    override fun onReceive(context: Context?, intent: Intent?) {
        intent?.let {
            if(AudioManager.ACTION_HEADSET_PLUG == it.action){
                val state = it.getIntExtra("state", -1)
                when(state){
                    AudioManager.SCO_AUDIO_STATE_DISCONNECTED -> onHeadsetDisconnected()
                    AudioManager.SCO_AUDIO_STATE_CONNECTED -> onHeadssetConnected()
                }
            }
        }
    }

    abstract fun onHeadsetDisconnected()

    abstract fun onHeadssetConnected()
}