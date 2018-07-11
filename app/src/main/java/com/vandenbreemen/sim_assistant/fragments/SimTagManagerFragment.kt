package com.vandenbreemen.sim_assistant.fragments

import android.content.Context
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.widget.Toast
import com.vandenbreemen.sim_assistant.R
import com.vandenbreemen.sim_assistant.R.id.tagSelector
import com.vandenbreemen.sim_assistant.adapters.TagAdapter
import com.vandenbreemen.sim_assistant.api.message.ApplicationError
import com.vandenbreemen.sim_assistant.api.sim.Sim
import com.vandenbreemen.sim_assistant.api.sim.Tag
import com.vandenbreemen.sim_assistant.mvp.tag.SimTagManagerPresenter
import com.vandenbreemen.sim_assistant.mvp.tag.SimTagManagerView
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject

class SimTagManagerFragment() : DialogFragment(), SimTagManagerView {


    companion object {
        const val ARG_SIM = "SIM_"
    }

    lateinit var sim: Sim

    @Inject
    lateinit var tagManagemerPresenter: SimTagManagerPresenter

    lateinit var tagAdapter: TagAdapter

    override fun setArguments(args: Bundle?) {
        super.setArguments(args)

        args?.let {
            sim = args.getParcelable(ARG_SIM)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val tagManagerContainer: ViewGroup = inflater.inflate(R.layout.layout_tag_create_select, container) as ViewGroup

        tagAdapter = TagAdapter(tagManagemerPresenter, sim)
        tagManagerContainer.findViewById<RecyclerView>(tagSelector).apply {
            layoutManager = LinearLayoutManager(this@SimTagManagerFragment.context)
            adapter = tagAdapter
        }

        return tagManagerContainer
    }

    override fun onAttach(context: Context?) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onStart() {
        super.onStart()
        tagManagemerPresenter.start(sim)
        dialog.window.setLayout(MATCH_PARENT, MATCH_PARENT);

    }

    override fun listTags(tags: List<Tag>) {
        tagAdapter.tagList = tags
    }

    override fun showError(applicationError: ApplicationError) {
        Toast.makeText(context, applicationError.localizedMessage, Toast.LENGTH_SHORT).show()
    }

}