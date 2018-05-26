package com.vandenbreemen.sim_assistant.fragments

import android.app.Fragment
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView
import com.vandenbreemen.sim_assistant.R
import com.vandenbreemen.sim_assistant.R.id.viewSims
import com.vandenbreemen.sim_assistant.ViewSimActivity
import com.vandenbreemen.sim_assistant.api.sim.Sim
import com.vandenbreemen.sim_assistant.mvp.post.simlist.SimListPresenter
import com.vandenbreemen.sim_assistant.mvp.post.simlist.SimListView
import java.text.SimpleDateFormat
import java.util.*

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
        val currentList = mutableListOf<Sim>()
        val adapter = object:ArrayAdapter<Sim>(
                context,
                android.R.layout.simple_list_item_1,
                currentList
        ){
            override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
                val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm")
                val sim = currentList[position]
                val view = inflater.inflate(R.layout.layout_sim_list_item, parent, false)
                view.findViewById<TextView>(R.id.simTitle).setText(sim.title)
                view.findViewById<TextView>(R.id.simAuthor).setText(sim.author)
                view.findViewById<TextView>(R.id.simDate).setText(simpleDateFormat.format(Date(sim.postedDate)))

                view.setOnClickListener(View.OnClickListener { view ->
                    presenter.viewSim(sim)
                })

                view.setOnLongClickListener(View.OnLongClickListener { view ->
                    presenter.selectSim(sim)
                    true
                })

                return view
            }
        }
        listView.adapter = adapter

        val view = object:SimListView{
            override fun viewSim(sim: Sim) {
                val intent = Intent(activity, ViewSimActivity::class.java)
                intent.putExtra(ViewSimActivity.PARM_SIMS, arrayOf(sim))
                startActivity(intent)
            }

            override fun addSimItem(sim: Sim) {
                currentList.add(sim)
                adapter.notifyDataSetChanged()
            }

            override fun displayViewSelectedSimsOption() {
                view.findViewById<View>(viewSims).visibility = VISIBLE
            }
        }

        presenter.start(view)

        return layout
    }

}