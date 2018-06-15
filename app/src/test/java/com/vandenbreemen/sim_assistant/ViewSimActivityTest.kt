package com.vandenbreemen.sim_assistant

import android.content.Intent
import android.support.design.widget.FloatingActionButton
import android.view.MenuItem
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import com.vandenbreemen.sim_assistant.ViewSimActivity.Companion.PARM_SIMS
import com.vandenbreemen.sim_assistant.api.sim.Sim
import com.vandenbreemen.sim_assistant.mvp.headphones.HeadphonesReactionInteractor
import com.vandenbreemen.sim_assistant.shadows.ShadowTTSInteractor
import com.vandenbreemen.sim_assistant.shadows.spokenSim
import junit.framework.TestCase.assertTrue
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import org.robolectric.Robolectric.buildActivity
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config
import java.time.LocalDateTime
import java.time.ZoneOffset

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

    @Test
    @Config(shadows = [ShadowTTSInteractor::class])
    fun shouldSpeakSim(){
        val intent = Intent(RuntimeEnvironment.application, ViewSimActivity::class.java)
        intent.putExtra(PARM_SIMS, arrayOf(sim))
        val activity = buildActivity(ViewSimActivity::class.java, intent)
                .create()
                .resume()
                .get()

        val menuItem = mock(MenuItem::class.java)
        `when`(menuItem.itemId).thenReturn(R.id.speakSim)
        activity.onOptionsItemSelected(menuItem)

        assertEquals("Spoken sim", sim, spokenSim[0])
    }

    @Test
    fun shouldShowPauseButtonOnSpeakSims() {
        val intent = Intent(RuntimeEnvironment.application, ViewSimActivity::class.java)
        intent.putExtra(PARM_SIMS, arrayOf(sim))
        val activity = buildActivity(ViewSimActivity::class.java, intent)
                .create()
                .resume()
                .get()

        val menuItem = mock(MenuItem::class.java)
        `when`(menuItem.itemId).thenReturn(R.id.speakSim)
        activity.onOptionsItemSelected(menuItem)

        assertEquals("Pause Visible", VISIBLE, activity.findViewById<FloatingActionButton>(R.id.pause).visibility)

    }

    @Test
    fun shouldMakeDictationVisibleWhenUserClicksDictate(){
        val intent = Intent(RuntimeEnvironment.application, ViewSimActivity::class.java)
        intent.putExtra(PARM_SIMS, arrayOf(sim))
        val activity = buildActivity(ViewSimActivity::class.java, intent)
                .create()
                .resume()
                .get()

        val menuItem = mock(MenuItem::class.java)
        `when`(menuItem.itemId).thenReturn(R.id.speakSim)
        activity.onOptionsItemSelected(menuItem)

        assertEquals("Progress Visible", VISIBLE, activity.findViewById<ProgressBar>(R.id.dictationProgress).visibility)
    }

    @Test
    fun shouldSetAndUpdateSimsProgressBar() {
        val intent = Intent(RuntimeEnvironment.application, ViewSimActivity::class.java)
        intent.putExtra(PARM_SIMS, arrayOf(sim))
        val activity = buildActivity(ViewSimActivity::class.java, intent)
                .create()
                .resume()
                .get()

        activity.setTotalUtterancesToBeSpoken(10)
        activity.updateProgress(5)

        val progressBar = activity.findViewById<ProgressBar>(R.id.dictationProgress)
        assertEquals("Total Utterances", 9, progressBar.max)
        assertEquals("Current progress", 5, progressBar.progress)
    }

    @Test
    fun dictationProgressShouldBeInvisibleByDefault(){
        val intent = Intent(RuntimeEnvironment.application, ViewSimActivity::class.java)
        intent.putExtra(PARM_SIMS, arrayOf(sim))
        val activity = buildActivity(ViewSimActivity::class.java, intent)
                .create()
                .resume()
                .get()

        assertEquals("Dictation Progress Invisible", GONE,
                activity.findViewById<ProgressBar>(R.id.dictationProgress).visibility)
    }

    @Test
    fun shouldDetectHeadphonesDisconnected(){

        //  Arrange
        var headphonesDisconnected = false
        val headPhonesInteractor = object:HeadphonesReactionInteractor{
            override fun onHeadphonesDisconnected() {
                headphonesDisconnected = true
            }

            override fun onHeadphonesConnected() {
                TODO("onHeadphonesConnected should not be called")
            }

        }

        val intent = Intent(RuntimeEnvironment.application, ViewSimActivity::class.java)
        intent.putExtra(PARM_SIMS, arrayOf(sim))
        val activity = buildActivity(ViewSimActivity::class.java, intent)
                .create()
                .resume()
                .get()
        activity.setHeadphonesInteractor(headPhonesInteractor)

        val headphonesBroadcast = Intent()
        headphonesBroadcast.putExtra("state", 0)
        headphonesBroadcast.action = Intent.ACTION_HEADSET_PLUG
        RuntimeEnvironment.systemContext.sendBroadcast(headphonesBroadcast)

        assertTrue("Headphones Disconnected", headphonesDisconnected)

    }

    @Test
    fun shouldDetectHeadphonesConnected(){

        //  Arrange
        var headphonesConnected = false
        val headPhonesInteractor = object:HeadphonesReactionInteractor{
            override fun onHeadphonesDisconnected() {
                TODO("onHeadphonesDisconnected should not be called")
            }

            override fun onHeadphonesConnected() {
                headphonesConnected = true
            }

        }

        val intent = Intent(RuntimeEnvironment.application, ViewSimActivity::class.java)
        intent.putExtra(PARM_SIMS, arrayOf(sim))
        val activity = buildActivity(ViewSimActivity::class.java, intent)
                .create()
                .resume()
                .get()
        activity.setHeadphonesInteractor(headPhonesInteractor)

        val headphonesBroadcast = Intent()
        headphonesBroadcast.putExtra("state", 1)
        headphonesBroadcast.action = Intent.ACTION_HEADSET_PLUG
        RuntimeEnvironment.systemContext.sendBroadcast(headphonesBroadcast)

        assertTrue("Headphones Connected", headphonesConnected)

    }

}