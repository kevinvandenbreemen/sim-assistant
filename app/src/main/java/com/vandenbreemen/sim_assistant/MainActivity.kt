package com.vandenbreemen.sim_assistant

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.vandenbreemen.sim_assistant.mvp.mainscreen.MainScreenPresenter
import com.vandenbreemen.sim_assistant.mvp.mainscreen.MainScreenView
import com.vandenbreemen.sim_assistant.mvp.mainscreen.SimSource
import dagger.android.AndroidInjection
import javax.inject.Inject

class MainActivity : AppCompatActivity(), MainScreenView {

    @Inject
    lateinit var presenter:MainScreenPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onResume() {
        super.onResume()
        presenter.start()
    }

    override fun showSimSourceSelector(simSources: List<SimSource>) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
