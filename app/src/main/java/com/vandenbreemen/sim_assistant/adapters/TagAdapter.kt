package com.vandenbreemen.sim_assistant.adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import com.vandenbreemen.sim_assistant.R
import com.vandenbreemen.sim_assistant.R.id.tagIconButton
import com.vandenbreemen.sim_assistant.api.sim.Sim
import com.vandenbreemen.sim_assistant.api.sim.Tag
import com.vandenbreemen.sim_assistant.mvp.tag.SimTagManagerPresenter

class ViewHolder(val container: ViewGroup) : RecyclerView.ViewHolder(container)

class TagAdapter(val simTagManagerPresenter: SimTagManagerPresenter, val sim: Sim) : RecyclerView.Adapter<ViewHolder>() {

    var tagList: List<Tag> = listOf()
        set(value) {
            field = value
            this.notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.layout_tag_item, parent, false) as ViewGroup)
    }

    override fun getItemCount(): Int {
        return tagList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.container.findViewById<TextView>(R.id.tagName).text =
                tagList[position].name

        val tag = tagList[position]
        holder.container.findViewById<ImageButton>(tagIconButton).setOnClickListener { v ->
            simTagManagerPresenter.addTag(sim, tag)
        }

        if (tag.selected) {
            holder.container.findViewById<ImageButton>(tagIconButton).setImageResource(R.drawable.tag_blue)
        }
    }

}