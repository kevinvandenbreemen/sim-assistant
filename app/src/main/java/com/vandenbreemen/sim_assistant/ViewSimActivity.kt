package com.vandenbreemen.sim_assistant

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.view.ViewGroup
import android.widget.TextView
import com.vandenbreemen.sim_assistant.R.id.simContainer
import com.vandenbreemen.sim_assistant.api.sim.Sim
import com.vandenbreemen.sim_assistant.mvp.viewsim.ViewSimPresenter
import com.vandenbreemen.sim_assistant.mvp.viewsim.ViewSimView
import dagger.android.AndroidInjection

import kotlinx.android.synthetic.main.activity_view_sim.*
import javax.inject.Inject

class ViewSimActivity : AppCompatActivity(), ViewSimView {

    companion object {
        const val PARM_SIMS = "__SIMS"
    }

    @Inject
    lateinit var presenter: ViewSimPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_sim)
        setSupportActionBar(toolbar)
    }

    override fun onResume() {
        super.onResume()
        presenter.start()
    }

    override fun displaySim(sim: Sim) {
        val container = findViewById<ViewGroup>(simContainer)
        val simContent = layoutInflater.inflate(R.layout.layout_sim_display, container, false)

        simContent.tag = "${sim.title}_${sim.postedDate}"
        simContent.findViewById<TextView>(R.id.simTitle).text = sim.title
        simContent.findViewById<TextView>(R.id.simAuthor).text = sim.author
        simContent.findViewById<TextView>(R.id.simContent).text = sim.content

        container.addView(simContent)
    }
}
