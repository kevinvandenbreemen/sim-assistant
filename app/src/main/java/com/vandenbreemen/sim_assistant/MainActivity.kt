package com.vandenbreemen.sim_assistant

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT
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
                .subscribe()
    }

    override fun promptForGoogleGroupDetails() {
        val popupContainer = findViewById<ViewGroup>(R.id.popupContainer)
        val googleGroupUi = layoutInflater.inflate(R.layout.layout_google_group_details, popupContainer, false)

        //  Set up OK button
        googleGroupUi.findViewById<Button>(R.id.ok).setOnClickListener(View.OnClickListener {
            view -> presenter.setGoogleGroupName(findViewById<EditText>(R.id.googleGroupName).text.toString()) })

        popupContainer.removeAllViews()
        popupContainer.addView(googleGroupUi)
    }

    override fun showError(errorMessage: String) {
        Toast.makeText(this, errorMessage, LENGTH_SHORT).show()
    }

    override fun showSimSourceSelector(simSources: List<SimSource>) {
        val popupContainer = findViewById<ViewGroup>(R.id.popupContainer)
        val simSourceSelectUi = layoutInflater.inflate(R.layout.layout_sim_source_selector, popupContainer, false)

        simSources.forEach { source ->
            val fieldName = "sim_src_${source.name}".toLowerCase()
            val idIdentifier = resources.getIdentifier(fieldName, "id", packageName)
            val view = simSourceSelectUi.findViewById<View>(idIdentifier)

            view.setOnClickListener(View.OnClickListener { view -> presenter.selectSimSource(source) })

            view.tag = source.name
        }

        popupContainer.addView(simSourceSelectUi)
    }

    override fun showSimList() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
