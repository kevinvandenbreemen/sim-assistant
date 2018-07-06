package com.vandenbreemen.sim_assistant.fragments

import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.vandenbreemen.sim_assistant.R

class SimTagManagerFragment : DialogFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val tagManagerContainer: ViewGroup = inflater.inflate(R.layout.layout_tag_create_select, container) as ViewGroup
        return tagManagerContainer
    }

}