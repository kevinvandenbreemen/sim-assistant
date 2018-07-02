package com.vandenbreemen.sim_assistant.util

import android.support.test.espresso.UiController
import android.support.test.espresso.ViewAction
import android.view.View
import android.view.ViewGroup
import org.hamcrest.Matcher

fun checkItemExists(matcher: Matcher<in View>):ViewAction{
    return object:ViewAction{
        override fun getDescription(): String {
            return "Verify match on specific item in RecyclerView"
        }

        override fun getConstraints(): Matcher<View>? {
            return matcher as Matcher<View>
        }

        override fun perform(uiController: UiController?, view: View) {
            if(findMatch(view, matcher) == null){
                throw NoSuchElementException("Element not found in ${view} matching ${matcher}")
            }
        }

    }
}

private fun findMatch(view:View, matcher: Matcher<in View>):View?{
    if(matcher.matches(view)){
        return view
    }
    if(view is ViewGroup){
        for(i in 0 until view.childCount){
            findMatch(view.getChildAt(i), matcher)?.let {
                return it
            }
        }
    }
    return null
}
