package com.vandenbreemen.sim_assistant

import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.SearchView
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.*
import android.widget.Toast.LENGTH_SHORT
import com.vandenbreemen.sim_assistant.api.presenter.SimListPresenterProvider
import com.vandenbreemen.sim_assistant.api.sim.Sim
import com.vandenbreemen.sim_assistant.api.sim.Tag
import com.vandenbreemen.sim_assistant.fragments.SimListFragment
import com.vandenbreemen.sim_assistant.mvp.mainscreen.MainScreenPresenter
import com.vandenbreemen.sim_assistant.mvp.mainscreen.MainScreenView
import com.vandenbreemen.sim_assistant.mvp.mainscreen.SimSource
import com.vandenbreemen.sim_assistant.mvp.tag.TagSimSearchPresenter
import com.vandenbreemen.sim_assistant.mvp.tag.TagSimSearchRouter
import com.vandenbreemen.sim_assistant.mvp.tag.TagSimSearchView
import dagger.android.AndroidInjection
import io.reactivex.android.schedulers.AndroidSchedulers.mainThread
import javax.inject.Inject

class MainActivity : AppCompatActivity(), MainScreenView, TagSimSearchView, TagSimSearchRouter {


    @Inject
    lateinit var presenter:MainScreenPresenter

    @Inject
    lateinit var presenterProvider:SimListPresenterProvider

    @Inject
    lateinit var tagSearchPresenter: TagSimSearchPresenter

    lateinit var searchAutoComplete: SearchView.SearchAutoComplete

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolBar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolBar)
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
            override fun onQueryTextSubmit(query: String): Boolean {
                tagSearchPresenter.searchTag(query!!)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                tagSearchPresenter.searchTag(newText!!)
                return true
            }
        })

        searchAutoComplete = autoComplete

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

    override fun displayTags(tags: List<Tag>) {
        val adapter = object : ArrayAdapter<Tag>(this, R.layout.layout_tag_item, tags) {
            override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
                val tag = tags[position]
                val view = layoutInflater.inflate(R.layout.layout_tag_item, parent, false)
                view.findViewById<TextView>(R.id.tagName).text = tag.name

                view.setOnClickListener { v ->
                    tagSearchPresenter.selectTag(tag)
                }

                return view
            }

        }

        searchAutoComplete.setAdapter(adapter)
        adapter.notifyDataSetChanged()
    }

    override fun gotoSimList(sims: List<Sim>) {
        val popupContainer = findViewById<ViewGroup>(R.id.popupContainer)
        val presenter = presenterProvider.getSimListPresenter(sims)
        val fragment = SimListFragment()
        fragment.setPresenter(presenter)
        popupContainer.removeAllViews()

        fragmentManager.beginTransaction().add(R.id.popupContainer, fragment).commit()
    }
}
