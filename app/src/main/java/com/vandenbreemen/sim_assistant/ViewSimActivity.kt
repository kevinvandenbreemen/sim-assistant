package com.vandenbreemen.sim_assistant

import android.content.IntentFilter
import android.graphics.Color
import android.graphics.PorterDuff
import android.media.AudioManager
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.SeekBar
import android.widget.TextView
import com.vandenbreemen.sim_assistant.R.id.simContainer
import com.vandenbreemen.sim_assistant.android.HeadphonesReceiver
import com.vandenbreemen.sim_assistant.api.sim.Sim
import com.vandenbreemen.sim_assistant.mvp.headphones.HeadphonesReactionInteractor
import com.vandenbreemen.sim_assistant.mvp.viewsim.ViewSimPresenter
import com.vandenbreemen.sim_assistant.mvp.viewsim.ViewSimView
import com.vandenbreemen.sim_assistant.ui.SelectSimByTitle
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_view_sim.*
import java.util.function.Consumer
import javax.inject.Inject

class ViewSimActivity : AppCompatActivity(), ViewSimView {


    companion object {
        const val PARM_SIMS = "__SIMS"
    }

    @Inject
    lateinit var presenter: ViewSimPresenter

    private var headphonesInteractor: HeadphonesReactionInteractor? = null

    private var headphonesReceiver:HeadphonesReceiver? = null

    var speakSimEnabled = true

    fun getHeadphonesInteractor():HeadphonesReactionInteractor?{
        return this.headphonesInteractor
    }

    override fun toggleRepeatDictationOn() {
        findViewById<FloatingActionButton>(R.id.toggleRepeatDictation)
                .setImageDrawable(resources.getDrawable(R.drawable.repeat_active, theme))
    }

    override fun toggleRepeatDictationOff() {
        findViewById<FloatingActionButton>(R.id.toggleRepeatDictation)
                .setImageDrawable(resources.getDrawable(R.drawable.repeat_inactive, theme))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_sim)
        setSupportActionBar(toolbar)

        //  Put together the seekbar
        findViewById<SeekBar>(R.id.dictationProgress).setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(p0: SeekBar?, progress: Int, fromUser: Boolean) {
                if(fromUser){
                    presenter.seekTo(progress)
                }
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {

            }

            override fun onStopTrackingTouch(p0: SeekBar?) {

            }

        })

        presenter.start()

        findViewById<SelectSimByTitle>(R.id.selectSimByTitle).simSelectionListener = Consumer { index->presenter.seekTo(index) }

        findViewById<FloatingActionButton>(R.id.pause).setOnClickListener(View.OnClickListener { v ->
            presenter.pause()
        })

        findViewById<FloatingActionButton>(R.id.toggleRepeatDictation).setOnClickListener(View.OnClickListener { v ->
            presenter.setRepeat()
        })

        listenForHeadphoneConnectivity()
    }

    private fun listenForHeadphoneConnectivity(){
        val intentFilter = IntentFilter(AudioManager.ACTION_HEADSET_PLUG)
        headphonesReceiver = object : HeadphonesReceiver(){
            override fun onHeadsetDisconnected() {
                headphonesInteractor?.let {
                    it.onHeadphonesDisconnected()
                }
            }

            override fun onHeadssetConnected() {
                headphonesInteractor?.let {
                    it.onHeadphonesConnected()
                }
            }

        }
        registerReceiver(headphonesReceiver, intentFilter)
    }

    override fun onDestroy() {
        super.onDestroy()
        headphonesReceiver?.let {
            unregisterReceiver(it)
        }
    }

    override fun setSelections(simTitlesToDictationIndexes: List<Pair<String, Int>>) {
        findViewById<SelectSimByTitle>(R.id.selectSimByTitle).setSimSelections(simTitlesToDictationIndexes)
    }

    override fun setDictationProgressEnabled(enabled: Boolean) {
        findViewById<ProgressBar>(R.id.dictationProgress).isEnabled = enabled
    }

    override fun updateSelectedSim(currentSimTitle: String) {
        findViewById<SelectSimByTitle>(R.id.selectSimByTitle).setSelectedSim(currentSimTitle)
    }

    override fun setSimSelectorEnabled(enabled: Boolean) {
        val visibility = if(enabled) VISIBLE else GONE
        findViewById<SelectSimByTitle>(R.id.selectSimByTitle).visibility = visibility
    }

    override fun onPrepareOptionsMenu(menu: Menu): Boolean {
        menu.findItem(R.id.speakSim).setEnabled(speakSimEnabled)

        val icon = resources.getDrawable(R.drawable.speaker, theme)
        if (!speakSimEnabled) {
            icon.mutate().setColorFilter(Color.GRAY, PorterDuff.Mode.SRC_IN)
        }
        menu.findItem(R.id.speakSim).icon = icon

        return true
    }

    override fun onBackPressed() {
        super.onBackPressed()

        presenter.close()

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

    override fun updateProgress(index: Int) {
        findViewById<ProgressBar>(R.id.dictationProgress).progress = index
    }

    override fun setTotalUtterancesToBeSpoken(totalUtterances: Int) {
        findViewById<ProgressBar>(R.id.dictationProgress).max = totalUtterances-1
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

    override fun setDictationControlsEnabled(enabled: Boolean) {
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

    override fun setDictationProgressVisible(visible: Boolean) {
        val visibility = if(visible) VISIBLE else GONE
        findViewById<ProgressBar>(R.id.dictationProgress).visibility = visibility
    }

    override fun setHeadphonesInteractor(headphonesReactionInteractor: HeadphonesReactionInteractor) {
        this.headphonesInteractor = headphonesReactionInteractor
    }
}
