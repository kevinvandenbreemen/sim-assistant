package com.vandenbreemen.sim_assistant.mvp.headphones

/**
 * Contract for handling events involving the headphones
 * @author kevin
 */
interface HeadphonesReactionInteractor {

    fun onHeadphonesDisconnected()

    fun onHeadphonesConnected()

}