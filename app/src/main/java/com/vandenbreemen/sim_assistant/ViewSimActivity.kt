package com.vandenbreemen.sim_assistant

import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
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

    var speakSimEnabled = true

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_sim)
        setSupportActionBar(toolbar)
    }

    override fun onPrepareOptionsMenu(menu: Menu): Boolean {
        menu.findItem(R.id.speakSim).setEnabled(speakSimEnabled)
        return true
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.menu_view_sim, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(R.id.speakSim == item.itemId){
            presenter.speakSims()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onResume() {
        super.onResume()
        presenter.start()

        findViewById<FloatingActionButton>(R.id.pause).setOnClickListener(View.OnClickListener { v ->
            presenter.pause()
        })
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

    override fun setPauseDictationEnabled(enabled: Boolean) {
        if (enabled) {
            findViewById<FloatingActionButton>(R.id.pause).visibility = VISIBLE
        } else {
            findViewById<FloatingActionButton>(R.id.pause).visibility = GONE
        }
    }

    override fun setSpeakSimsEnabled(enabled: Boolean) {
        speakSimEnabled = enabled
        invalidateOptionsMenu()
    }
}
