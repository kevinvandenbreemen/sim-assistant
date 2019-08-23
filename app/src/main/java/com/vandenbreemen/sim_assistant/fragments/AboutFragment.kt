package com.vandenbreemen.sim_assistant.fragments

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.vandenbreemen.sim_assistant.R

interface AboutListener {
    fun onReturnToMain()
}

class AboutFragment : Fragment() {

    lateinit var aboutListener: AboutListener

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.layout_flutter_container, container, false)

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