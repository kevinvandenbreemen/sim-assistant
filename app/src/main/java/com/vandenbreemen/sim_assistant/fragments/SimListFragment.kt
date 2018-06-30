package com.vandenbreemen.sim_assistant.fragments

import android.app.Fragment
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.CardView
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import com.vandenbreemen.sim_assistant.R
import com.vandenbreemen.sim_assistant.R.id.viewSims
import com.vandenbreemen.sim_assistant.ViewSimActivity
import com.vandenbreemen.sim_assistant.adapters.ClickAndLongClickListener
import com.vandenbreemen.sim_assistant.adapters.SimAdapter
import com.vandenbreemen.sim_assistant.api.sim.Sim
import com.vandenbreemen.sim_assistant.mvp.post.simlist.SimListPresenter
import com.vandenbreemen.sim_assistant.mvp.post.simlist.SimListView

/**
 * <h2>Intro</h2>
 *
 * <h2>Other Details</h2>
 * @author kevin
 */
class SimListFragment: Fragment(), SimListView {

    private lateinit var presenter:SimListPresenter

    lateinit var currentList:MutableList<Sim>

    lateinit var adapter: SimAdapter

    val simToUiComponent: MutableMap<Sim, CardView> = mutableMapOf()

    fun setPresenter(presenter: SimListPresenter){
        this.presenter = presenter
        this.currentList = mutableListOf<Sim>()
    }

    override fun viewSelectedSims(sims: List<Sim>) {
        val intent = Intent(activity, ViewSimActivity::class.java)

        val arrayOfSims = Array(sims.size, {index->sims[index]})
        intent.putExtra(ViewSimActivity.PARM_SIMS, arrayOfSims)
        activity.startActivity(intent)
    }

    override fun viewSim(sim: Sim) {
        val intent = Intent(activity, ViewSimActivity::class.java)
        intent.putExtra(ViewSimActivity.PARM_SIMS, arrayOf(sim))
        startActivity(intent)
    }

    override fun addSimItem(sim: Sim) {
        this.adapter.addSim(sim)
    }

    override fun displayViewSelectedSimsOption() {
        view.findViewById<View>(viewSims).visibility = VISIBLE
    }

    override fun selectSim(sim: Sim) {

    }

    override fun deselectSim(sim: Sim) {

    }

    override fun hideViewSelectedSimsOption() {
        view.findViewById<View>(viewSims).visibility = GONE
    }

    private fun createSimListView(inflater: LayoutInflater, layout:ViewGroup){

        val viewManager = LinearLayoutManager(context)

        this.adapter = SimAdapter().apply {
            clickAndLongClickListener = object : ClickAndLongClickListener {
                override fun onClick(sim: Sim) {
                    presenter.viewSim(sim)
                }

                override fun onLongClick(sim: Sim) {
                    presenter.selectSim(sim)
                }

            }
        }

        layout.findViewById<RecyclerView>(R.id.simList).apply {
            layoutManager = viewManager
            adapter = this@SimListFragment.adapter
        }

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val layout = inflater.inflate(R.layout.layout_sim_list, container, false) as ViewGroup
        createSimListView(inflater, layout)

        //  Set up the FAB
        layout.findViewById<View>(R.id.viewSims).setOnClickListener(View.OnClickListener { view ->
            presenter.viewSelectedSims()
        })

        presenter.start(this)

        return layout
    }

}