package com.vandenbreemen.sim_assistant.fragments

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.vandenbreemen.sim_assistant.R
import com.vandenbreemen.sim_assistant.flutter.*
import io.flutter.facade.Flutter
import io.flutter.plugin.common.BasicMessageChannel
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.StringCodec

interface AboutListener {
    fun onReturnToMain()
}

class AboutFragment : Fragment() {

    lateinit var aboutListener: AboutListener

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.layout_flutter_container, container, false)
        val flutterView = Flutter.createView(
                activity as Activity,
                lifecycle,
                HELP_SCREEN)
        view.findViewById<ViewGroup>(R.id.flutterContainer).addView(flutterView)

        val messageChannel = BasicMessageChannel<String>(flutterView, CHANNEL_ACTIONS, StringCodec.INSTANCE)
        messageChannel.setMessageHandler({ incoming, reply ->
            println("INCOMING:  $incoming")
            if (RETURN_TO_MAIN.equals(incoming)) {
                println("FIRING EVENT to RETURN TO MAIN!")
                aboutListener.onReturnToMain()
            }
        })

        MethodChannel(
                flutterView,
                CHANNEL_HELP
        ).setMethodCallHandler(MethodChannel.MethodCallHandler({ call, result ->

            if (call.method.equals(GET_VERSION)) {
                result.success("1.0.2x")
            }

        }))

        return view
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context !is AboutListener) {
            throw RuntimeException("Expected:  ${AboutListener::class.java.simpleName}")
        }

        this.aboutListener = context
    }

}