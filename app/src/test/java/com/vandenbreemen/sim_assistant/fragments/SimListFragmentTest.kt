package com.vandenbreemen.sim_assistant.fragments

import android.view.View
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.ListView
import android.widget.TextView
import com.vandenbreemen.sim_assistant.R
import com.vandenbreemen.sim_assistant.ViewSimActivity
import com.vandenbreemen.sim_assistant.api.sim.Sim
import com.vandenbreemen.sim_assistant.mvp.impl.post.simlist.SimListModelImpl
import com.vandenbreemen.sim_assistant.mvp.impl.post.simlist.SimListPresenterImpl
import com.vandenbreemen.sim_assistant.mvp.post.PostRepository
import com.vandenbreemen.sim_assistant.mvp.post.simlist.SimListPresenter
import io.reactivex.Observable
import junit.framework.TestCase.assertNotNull
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnit
import org.robolectric.RobolectricTestRunner
import org.robolectric.Shadows.shadowOf
import org.robolectric.util.FragmentTestUtil.startFragment
import java.util.*

@RunWith(RobolectricTestRunner::class)
class SimListFragmentTest{

    @get:Rule
    val mockingRule = MockitoJUnit.rule()

    @Mock
    lateinit var postRepository: PostRepository

    lateinit var presenter:SimListPresenter

    @Before
    fun setup(){
        presenter = SimListPresenterImpl(SimListModelImpl(postRepository))
    }

    @Test
    fun shouldListSims(){
        //  Arrange
        val sim = Sim(0L,
                "test sim",
                "Kevin",
                System.currentTimeMillis(),
                "This is some test content"
        )
        `when`(postRepository.getPosts(15)).thenReturn(Observable.just(sim))
        val fragment = SimListFragment()
        fragment.setPresenter(presenter)

        //  Act
        startFragment(fragment)
        val listView = fragment.view!!.findViewById<ListView>(R.id.simList)
        val shadowListView = shadowOf(listView)
        shadowListView.populateItems()

        //  Assert
        assertEquals("Sim list", 1, listView.childCount)
    }

    @Test
    fun shouldShowSimTitle(){
        //  Arrange
        val sim = Sim(0L,
                "test sim",
                "Kevin",
                System.currentTimeMillis(),
                "This is some test content"
        )
        `when`(postRepository.getPosts(15)).thenReturn(Observable.just(sim))
        val fragment = SimListFragment()
        fragment.setPresenter(presenter)

        //  Act
        startFragment(fragment)
        val listView = fragment.view!!.findViewById<ListView>(R.id.simList)
        val shadowListView = shadowOf(listView)
        shadowListView.populateItems()
        val simItem = listView.getChildAt(0) as ViewGroup

        //  Assert
        assertEquals("Sim Title",
                sim.title,
                simItem.findViewById<TextView>(R.id.simTitle).text)
    }

    @Test
    fun shouldShowSimAuthor(){
        //  Arrange
        val sim = Sim(0L,
                "test sim",
                "Kevin",
                System.currentTimeMillis(),
                "This is some test content"
        )
        `when`(postRepository.getPosts(15)).thenReturn(Observable.just(sim))
        val fragment = SimListFragment()
        fragment.setPresenter(presenter)

        //  Act
        startFragment(fragment)
        val listView = fragment.view!!.findViewById<ListView>(R.id.simList)
        val shadowListView = shadowOf(listView)
        shadowListView.populateItems()
        val simItem = listView.getChildAt(0) as ViewGroup

        //  Assert
        assertEquals("Sim Author",
                sim.author,
                simItem.findViewById<TextView>(R.id.simAuthor).text)
    }

    @Test
    fun shouldShowSimPostedDate(){

        //  Arrange
        val expectedDateString = "2018-01-01 20:00"

        val cal = Calendar.Builder()
        cal.set(Calendar.YEAR, 2018).set(Calendar.MONTH, 0).set(Calendar.DAY_OF_MONTH, 1)
                .set(Calendar.HOUR_OF_DAY, 20).set(Calendar.MINUTE, 0)

        val sim = Sim(0L,
                "test sim",
                "Kevin",
                cal.build().timeInMillis,
                "This is some test content"
        )
        `when`(postRepository.getPosts(15)).thenReturn(Observable.just(sim))
        val fragment = SimListFragment()
        fragment.setPresenter(presenter)

        //  Act
        startFragment(fragment)
        val listView = fragment.view!!.findViewById<ListView>(R.id.simList)
        val shadowListView = shadowOf(listView)
        shadowListView.populateItems()
        val simItem = listView.getChildAt(0) as ViewGroup

        //  Assert
        assertEquals("Sim Date",
                expectedDateString,
                simItem.findViewById<TextView>(R.id.simDate).text)

    }

    @Test
    fun shouldStartViewSimOnClickSim(){
        //  Arrange
        val sim = Sim(0L,
                "test sim",
                "Kevin",
                System.currentTimeMillis(),
                "This is some test content"
        )
        `when`(postRepository.getPosts(15)).thenReturn(Observable.just(sim))
        val fragment = SimListFragment()
        fragment.setPresenter(presenter)

        //  Act
        startFragment(fragment)
        val listView = fragment.view!!.findViewById<ListView>(R.id.simList)
        val shadowListView = shadowOf(listView)
        shadowListView.populateItems()
        val simItem = listView.getChildAt(0) as ViewGroup

        simItem.performClick()

        //  Assert
        val shadowActivity = shadowOf(fragment.activity)
        val intent = shadowActivity.nextStartedActivity
        assertNotNull("Start an Activity", intent)

        val shadowIntent = shadowOf(intent)
        assertEquals("View Sim Activity", ViewSimActivity::class.java, shadowIntent.intentClass)
    }

    @Test
    fun shouldSupportSelectingMultipleSimsByLongClick() {
        //  Arrange
        val sim1 = Sim(0L,
                "test sim",
                "Kevin",
                System.currentTimeMillis(),
                "This is some test content"
        )

        val sim2 = Sim(0L,
                "test sim1",
                "Kevin",
                System.currentTimeMillis(),
                "This is some test content"
        )

        `when`(postRepository.getPosts(15)).thenReturn(Observable.just(sim1, sim2))
        val fragment = SimListFragment()
        fragment.setPresenter(presenter)

        //  Act
        startFragment(fragment)
        val listView = fragment.view!!.findViewById<ListView>(R.id.simList)
        val shadowListView = shadowOf(listView)
        shadowListView.populateItems()

        var simItem = listView.getChildAt(0) as ViewGroup
        simItem.performLongClick()

        simItem = listView.getChildAt(1) as ViewGroup
        simItem.performLongClick()

        //  Assert
        val viewTextButton = fragment.view!!.findViewById<View>(R.id.viewSims)
        assertEquals("View Sims Visible", VISIBLE, viewTextButton.visibility)

    }

    @Test
    fun shouldDisplaySimTextForMultipleSelectedSims() {
        //  Arrange
        val sim1 = Sim(0L,
                "test sim",
                "Kevin",
                System.currentTimeMillis(),
                "This is some test content"
        )

        val sim2 = Sim(0L,
                "test sim1",
                "Kevin",
                System.currentTimeMillis(),
                "This is some test content"
        )

        `when`(postRepository.getPosts(15)).thenReturn(Observable.just(sim1, sim2))
        val fragment = SimListFragment()
        fragment.setPresenter(presenter)

        //  Act
        startFragment(fragment)
        val listView = fragment.view!!.findViewById<ListView>(R.id.simList)
        val shadowListView = shadowOf(listView)
        shadowListView.populateItems()

        var simItem = listView.getChildAt(0) as ViewGroup
        simItem.performLongClick()

        simItem = listView.getChildAt(1) as ViewGroup
        simItem.performLongClick()

        fragment.view!!.findViewById<View>(R.id.viewSims).performClick()

        //  Assert
        val shadowActivity = shadowOf(fragment.activity)
        val intent = shadowActivity.nextStartedActivity
        assertNotNull("Start an Activity", intent)
        assertEquals("Selected sims", 2, intent.getParcelableArrayExtra(ViewSimActivity.PARM_SIMS)?.size)

        val shadowIntent = shadowOf(intent)
        assertEquals("View Sim Activity", ViewSimActivity::class.java, shadowIntent.intentClass)
    }



}