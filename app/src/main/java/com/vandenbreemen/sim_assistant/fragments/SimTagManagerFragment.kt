package com.vandenbreemen.sim_assistant.fragments

import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import com.vandenbreemen.sim_assistant.R
import com.vandenbreemen.sim_assistant.api.sim.Sim

class SimTagManagerFragment() : DialogFragment() {

    companion object {
        const val ARG_SIM = "SIM_"
    }

    lateinit var sim: Sim

    override fun setArguments(args: Bundle?) {
        super.setArguments(args)

        args?.let {
            sim = args.getParcelable(ARG_SIM)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val tagManagerContainer: ViewGroup = inflater.inflate(R.layout.layout_tag_create_select, container) as ViewGroup
        return tagManagerContainer
    }

    override fun onStart() {
        super.onStart()
        dialog.window.setLayout(MATCH_PARENT, MATCH_PARENT);

    }

}