package com.vandenbreemen.sim_assistant.adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import com.vandenbreemen.sim_assistant.R
import com.vandenbreemen.sim_assistant.api.sim.Tag

class ViewHolder(val container: ViewGroup) : RecyclerView.ViewHolder(container)

class TagAdapter() : RecyclerView.Adapter<ViewHolder>() {

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
    }

}