package com.vandenbreemen.sim_assistant.ui

import android.content.Context
import android.support.constraint.ConstraintLayout
import android.view.LayoutInflater
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import com.vandenbreemen.sim_assistant.R
import java.util.function.Consumer


class SelectSimByTitle(viewContext:Context): ConstraintLayout(viewContext) {


    init {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        inflater.inflate(R.layout.layout_sim_select_by_title_content, this, true)
    }

    var simSelectionListener: Consumer<Int>? = null

    lateinit var itemSelectedListener:AdapterView.OnItemSelectedListener

    fun setSimSelections(simTitlesToDictationIndexes: List<Pair<String, Int>>) {

        val adapter = ArrayAdapter<String>(context, android.R.layout.simple_spinner_item,
                simTitlesToDictationIndexes.map { it.first })
        val spinner = findViewById<Spinner>(R.id.simSelector)
        spinner.adapter = adapter

        this.itemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                simSelectionListener?.let {
                    it.accept(simTitlesToDictationIndexes[position].second)
                }
            }

        }

        spinner.onItemSelectedListener = this.itemSelectedListener
    }

    fun setSelectedSim(title: String) {
        val spinner = findViewById<Spinner>(R.id.simSelector)
        val adapter = spinner.adapter as ArrayAdapter<String>

        try {
            spinner.onItemSelectedListener = null
            spinner.setSelection(adapter.getPosition(title))
        }
        finally{
            spinner.onItemSelectedListener = this.itemSelectedListener
        }

    }


}