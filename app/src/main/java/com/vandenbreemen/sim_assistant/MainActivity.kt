package com.vandenbreemen.sim_assistant

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.vandenbreemen.sim_assistant.mvp.mainscreen.MainScreenPresenter
import dagger.android.AndroidInjection
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var presenter:MainScreenPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}
