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
//            return object:Matcher<View>{
//                override fun describeTo(description: Description?) {
//
//                }
//
//                override fun describeMismatch(item: Any?, mismatchDescription: Description?) {
//
//                }
//
//                override fun _dont_implement_Matcher___instead_extend_BaseMatcher_() {
//
//                }
//
//                override fun matches(item: Any?): Boolean {
//                    return true
//                }
//
//            }
            return matcher as Matcher<View>
        }

        override fun perform(uiController: UiController?, view: View) {
            if(!findMatch(view, matcher)){
                throw NoSuchElementException("Element not found in ${view} matching ${matcher}")
            }
        }

    }
}

private fun findMatch(view:View, matcher: Matcher<in View>):Boolean{
    if(matcher.matches(view)){
        return true
    }
    if(view is ViewGroup){
        for(i in 0 until view.childCount){
            if(findMatch(view.getChildAt(i), matcher)){
                return true
            }
        }
    }
    return false
}
