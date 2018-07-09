package com.vandenbreemen.sim_assistant.util

import android.support.test.espresso.NoMatchingViewException
import android.support.test.espresso.ViewAssertion
import android.support.v7.widget.RecyclerView
import android.view.View
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat

private class RecyclerViewItemCountAssertion(val expectedCount:Int) :ViewAssertion{
    override fun check(view: View?, noViewFoundException: NoMatchingViewException?) {
        noViewFoundException?.let {
            throw it
        }

        val recyclerView = view!! as RecyclerView
        assertThat(recyclerView.adapter.itemCount, `is`(expectedCount))
    }
}

fun hasItemCount(itemCount:Int):ViewAssertion{
    return RecyclerViewItemCountAssertion(itemCount)
}