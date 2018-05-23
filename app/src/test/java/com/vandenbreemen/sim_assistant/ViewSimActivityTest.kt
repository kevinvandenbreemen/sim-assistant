package com.vandenbreemen.sim_assistant

import android.content.Intent
import android.view.ViewGroup
import android.widget.TextView
import com.vandenbreemen.sim_assistant.ViewSimActivity.Companion.PARM_SIMS
import com.vandenbreemen.sim_assistant.api.sim.Sim
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric.buildActivity
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.util.*

/**
 * <h2>Intro</h2>
 *
 *
 * <h2>Other Details</h2>
 *
 * @author kevin
 */
@RunWith(RobolectricTestRunner::class)
class ViewSimActivityTest{

    val sim = Sim(
            "Test Sim Title",
            "Kevin Vandenbreemen",
            LocalDateTime.of(2018, 10, 10, 12,12,12).toEpochSecond(ZoneOffset.UTC)*1000,
            "This is a test of sim content"
    )

    @Test
    fun shouldDisplaySimInOwnContainer(){

        val intent = Intent(RuntimeEnvironment.application, ViewSimActivity::class.java)
        intent.putExtra(PARM_SIMS, arrayOf(sim))
        val activity = buildActivity(ViewSimActivity::class.java, intent)
                .create()
                .resume()
                .get()

        val simListContainer = activity.findViewById<ViewGroup>(R.id.simContainer)
        val simContainer = simListContainer.findViewWithTag<ViewGroup>("Test Sim Title_${sim.postedDate}")
        assertNotNull(simContainer)

    }

    @Test
    fun shouldDisplaySimTitle(){
        val intent = Intent(RuntimeEnvironment.application, ViewSimActivity::class.java)
        intent.putExtra(PARM_SIMS, arrayOf(sim))
        val activity = buildActivity(ViewSimActivity::class.java, intent)
                .create()
                .resume()
                .get()

        val simListContainer = activity.findViewById<ViewGroup>(R.id.simContainer)
        val simContainer = simListContainer.findViewWithTag<ViewGroup>("Test Sim Title_${sim.postedDate}")
        assertEquals("Title",
                sim.title,
                simContainer.findViewById<TextView>(R.id.simTitle).text.toString())
    }

    @Test
    fun shouldDisplaySimAuthor(){
        val intent = Intent(RuntimeEnvironment.application, ViewSimActivity::class.java)
        intent.putExtra(PARM_SIMS, arrayOf(sim))
        val activity = buildActivity(ViewSimActivity::class.java, intent)
                .create()
                .resume()
                .get()

        val simListContainer = activity.findViewById<ViewGroup>(R.id.simContainer)
        val simContainer = simListContainer.findViewWithTag<ViewGroup>("Test Sim Title_${sim.postedDate}")
        assertEquals("Author",
                sim.author,
                simContainer.findViewById<TextView>(R.id.simAuthor).text.toString())
    }

    @Test
    fun shouldDisplaySimContent(){
        val intent = Intent(RuntimeEnvironment.application, ViewSimActivity::class.java)
        intent.putExtra(PARM_SIMS, arrayOf(sim))
        val activity = buildActivity(ViewSimActivity::class.java, intent)
                .create()
                .resume()
                .get()

        val simListContainer = activity.findViewById<ViewGroup>(R.id.simContainer)
        val simContainer = simListContainer.findViewWithTag<ViewGroup>("Test Sim Title_${sim.postedDate}")
        assertEquals("Content",
                sim.content,
                simContainer.findViewById<TextView>(R.id.simContent).text.toString())
    }

}