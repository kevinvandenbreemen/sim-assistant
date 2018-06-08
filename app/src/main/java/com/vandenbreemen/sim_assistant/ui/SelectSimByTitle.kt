package com.vandenbreemen.sim_assistant.ui

import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import com.vandenbreemen.sim_assistant.R
import com.vandenbreemen.sim_assistant.api.message.ApplicationError
import java.util.function.Consumer


class SelectSimByTitle(val view: ViewGroup) {


    init {
        if (view.findViewById<Spinner>(R.id.simSelector) == null) {
            throw ApplicationError("Fatal:  Cannot create this widget since it is not a sim selector spinner")
        }
    }

    var simSelectionListener: Consumer<Int>? = null

    fun setSimSelections(simTitlesToDictationIndexes: List<Pair<String, Int>>) {

        val adapter = ArrayAdapter<String>(view.context, android.R.layout.simple_spinner_item,
                simTitlesToDictationIndexes.map { it.first })
        val spinner = view.findViewById<Spinner>(R.id.simSelector)
        spinner.adapter = adapter

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                simSelectionListener?.let {
                    it.accept(simTitlesToDictationIndexes[position].second)
                }
            }

        }
    }


}