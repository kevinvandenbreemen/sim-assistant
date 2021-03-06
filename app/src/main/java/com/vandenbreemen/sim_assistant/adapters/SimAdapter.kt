package com.vandenbreemen.sim_assistant.adapters

import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.Button
import android.widget.PopupMenu
import android.widget.TextView
import com.vandenbreemen.sim_assistant.R
import com.vandenbreemen.sim_assistant.R.id.tags
import com.vandenbreemen.sim_assistant.api.sim.Sim
import com.vandenbreemen.sim_assistant.mvp.simitem.SimItemPresenter
import java.text.SimpleDateFormat
import java.util.*

class SimViewHolder(val cardView: CardView) : RecyclerView.ViewHolder(cardView) {
}

interface ClickAndLongClickListener {

    fun onClick(sim: Sim)

    fun onLongClick(sim: Sim)

}

class SimAdapter(val simItemPresenter: SimItemPresenter) : RecyclerView.Adapter<SimViewHolder>() {

    private val simList: MutableList<Sim> = mutableListOf()

    var clickAndLongClickListener: ClickAndLongClickListener? = null

    /**
     * Add the given sim and updates the recyclerview this is used in
     */
    fun addSim(sim: Sim) {
        this.simList.add(sim)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SimViewHolder {
        val cardView = LayoutInflater.from(parent.context).inflate(R.layout.layout_sim_list_item, parent, false) as CardView
        return SimViewHolder(cardView)
    }

    override fun getItemCount(): Int = simList.size

    private fun setupMenu(sim: Sim, view: ViewGroup) {
        val menuButton = view.findViewById<Button>(R.id.simMenu)
        menuButton.setOnClickListener(View.OnClickListener {
            val popupMenu = PopupMenu(view.context, menuButton)
            popupMenu.menuInflater.inflate(R.menu.menu_sim_menu, popupMenu.menu)

            popupMenu.setOnMenuItemClickListener {
                if (it.itemId == tags) {
                    simItemPresenter.openTags(sim)
                }
                true
            }

            popupMenu.show()
        })
    }

    override fun onBindViewHolder(holder: SimViewHolder, position: Int) {
        val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm")
        val sim = simList[position]
        val cardView = holder.cardView
        cardView.findViewById<TextView>(R.id.simTitle).text = sim.title
        cardView.findViewById<TextView>(R.id.simAuthor).text = sim.author
        cardView.findViewById<TextView>(R.id.simDate).text = simpleDateFormat.format(Date(sim.postedDate))

        if(sim.selected){
            cardView.findViewById<Button>(R.id.simMenu).visibility = VISIBLE
        }

        setupMenu(sim, cardView)

        cardView.setOnClickListener(View.OnClickListener { view ->
            clickAndLongClickListener?.let {
                it.onClick(sim)
            }
        })

        cardView.setOnLongClickListener(View.OnLongClickListener { view ->


            clickAndLongClickListener?.let {
                it.onLongClick(sim)
            }
            true
        })

        if (sim.selected) {
            cardView.setCardBackgroundColor(cardView.context.resources.getColor(R.color.selectedSim, cardView.context.theme))
        } else {
            cardView.setCardBackgroundColor(cardView.context.resources.getColor(R.color.defaultSimColor, cardView.context.theme))
        }
    }

}