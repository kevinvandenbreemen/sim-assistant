package com.vandenbreemen.sim_assistant.fragments

import android.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import com.vandenbreemen.sim_assistant.R
import com.vandenbreemen.sim_assistant.api.sim.PostedSim
import com.vandenbreemen.sim_assistant.mvp.post.simlist.SimListPresenter
import com.vandenbreemen.sim_assistant.mvp.post.simlist.SimListView

/**
 * <h2>Intro</h2>
 *
 * <h2>Other Details</h2>
 * @author kevin
 */
class SimListFragment: Fragment() {

    private lateinit var presenter:SimListPresenter

    fun setPresenter(presenter: SimListPresenter){
        this.presenter = presenter
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val layout = inflater.inflate(R.layout.layout_sim_list, container, false)

        val listView = layout.findViewById<ListView>(R.id.simList)
        val currentList = mutableListOf<PostedSim>()
        val adapter = ArrayAdapter<PostedSim>(
                context,
                android.R.layout.simple_list_item_1,
                currentList
        )
        listView.adapter = adapter

        val view = object:SimListView{
            override fun addSimItem(sim: PostedSim) {
                currentList.add(sim)
                adapter.notifyDataSetChanged()
            }

        }

        presenter.start(view)

        return layout
    }

}