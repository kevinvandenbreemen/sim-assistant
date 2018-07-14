package com.vandenbreemen.sim_assistant.fragments

import android.app.Activity
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.vandenbreemen.sim_assistant.R
import io.flutter.facade.Flutter

class AboutFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.layout_flutter_container, container, false)
        val flutterView = Flutter.createView(
                activity as Activity,
                lifecycle,
                "route1")
        view.findViewById<ViewGroup>(R.id.flutterContainer).addView(flutterView)
        return view
    }

}