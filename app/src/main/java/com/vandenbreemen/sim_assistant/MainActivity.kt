package com.vandenbreemen.sim_assistant

import android.graphics.Color
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.SearchView
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT
import com.vandenbreemen.sim_assistant.api.presenter.SimListPresenterProvider
import com.vandenbreemen.sim_assistant.fragments.AboutFragment
import com.vandenbreemen.sim_assistant.fragments.AboutListener
import com.vandenbreemen.sim_assistant.fragments.SimListFragment
import com.vandenbreemen.sim_assistant.mvp.mainscreen.MainScreenPresenter
import com.vandenbreemen.sim_assistant.mvp.mainscreen.MainScreenView
import com.vandenbreemen.sim_assistant.mvp.mainscreen.SimSource
import dagger.android.AndroidInjection
import io.reactivex.android.schedulers.AndroidSchedulers.mainThread
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

class MainActivity : AppCompatActivity(), MainScreenView, AboutListener {

    @Inject
    lateinit var presenter:MainScreenPresenter

    @Inject
    lateinit var presenterProvider:SimListPresenterProvider

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolBar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolBar)

        findViewById<FloatingActionButton>(R.id.help).setOnClickListener({ v ->
            val popupContainer = findViewById<ViewGroup>(R.id.popupContainer)
            val helpAndAbout = AboutFragment()
            supportFragmentManager.beginTransaction().add(R.id.popupContainer, helpAndAbout).commit()
        })
    }

    override fun onResume() {
        super.onResume()
        presenter.start()
                .subscribe()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        getMenuInflater().inflate(R.menu.menu_main_activity, menu)

        //  Set up search action
        //  See also https://www.dev2qa.com/android-actionbar-searchview-autocomplete-example/
        val searchView = menu!!.findItem(R.id.app_bar_search).actionView as SearchView
        val autoComplete = searchView.findViewById<SearchView.SearchAutoComplete>(
                R.id.search_src_text
        )
        autoComplete.setTextColor(Color.WHITE)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                Toast.makeText(this@MainActivity, "Submitted $query", LENGTH_SHORT).show()
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }
        })

        return super.onCreateOptionsMenu(menu)
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
        val popupContainer = findViewById<ViewGroup>(R.id.popupContainer)
        presenterProvider.getSimListPresenter().observeOn(mainThread()).subscribe { presenter->
            val fragment = SimListFragment()
            fragment.setPresenter(presenter)
            popupContainer.removeAllViews()

            fragmentManager.beginTransaction().add(R.id.popupContainer, fragment).commit()
        }
    }

    override fun onReturnToMain() {
        popupContainer.removeAllViews()
        presenter.start().subscribe()
    }
}
