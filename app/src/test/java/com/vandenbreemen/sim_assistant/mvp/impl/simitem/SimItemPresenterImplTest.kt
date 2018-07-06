package com.vandenbreemen.sim_assistant.mvp.impl.simitem

import com.vandenbreemen.sim_assistant.api.sim.Sim
import com.vandenbreemen.sim_assistant.mvp.simitem.SimItemView
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class SimItemPresenterImplTest {

    @Mock
    lateinit var view: SimItemView

    lateinit var presenter: SimItemPresenterImpl

    lateinit var sim: Sim

    @Before
    fun setup() {
        this.presenter = SimItemPresenterImpl(view)
        this.sim = Sim(
                0,
                "Test Sim",
                "Jim", 0, "Test Sim Content"
        )
    }

    @Test
    fun shouldOpenTags() {
        //  Act
        presenter.openTags(sim)

        //  Assert
        verify(view).showSimTagsDialog(sim)
    }

}