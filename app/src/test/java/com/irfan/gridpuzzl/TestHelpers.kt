package com.irfan.gridpuzzl

import android.app.Activity
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.action.CoordinatesProvider
import androidx.test.espresso.matcher.BoundedMatcher
import androidx.test.ext.junit.rules.ActivityScenarioRule
import org.hamcrest.Description
import org.hamcrest.Matcher
import java.util.concurrent.atomic.AtomicReference


// Test helpers
fun <T : Activity> ActivityScenarioRule<T>.getActivity(): T {
    val activityRef: AtomicReference<T> = AtomicReference()
    this.scenario.onActivity(activityRef::set)
    return activityRef.get()
}

fun <T : Activity> ActivityScenarioRule<T>.moveToState(stat: Lifecycle.State) {
    this.scenario.moveToState(stat)
}


fun allChildren(matcher: Matcher<View>): Matcher<View> {
    return object : BoundedMatcher<View, RecyclerView>(
        RecyclerView::class.java
    ) {
        override fun describeTo(description: Description) {
            matcher.describeTo(description)
        }

        override fun matchesSafely(recyclerView: RecyclerView): Boolean {
            var actualChildrenCount = 0
            val layoutManager = recyclerView.layoutManager ?: return false
            val childCount = layoutManager.childCount ?: return false

            for (i in 0 until childCount) {
                actualChildrenCount = i
                val child = layoutManager.getChildAt(i)
                val isMatch = child != null && matcher.matches(child)

                if (!isMatch) return false
            }
            return actualChildrenCount + 1 == childCount
        }
    }
}


fun isDragHappen(dragView: View, dragViewCurrentPosition: Int): Matcher<View> {
    return object : BoundedMatcher<View, RecyclerView>(
        RecyclerView::class.java
    ) {
        override fun describeTo(description: Description) {
            description.appendText("dragging")
        }

        override fun matchesSafely(recyclerView: RecyclerView): Boolean {

            val layoutManager = recyclerView.layoutManager ?: return false
            val expected = layoutManager.getChildAt(dragViewCurrentPosition)?:throw NullPointerException("No child at 0")

            return dragView.tag != expected.tag  // view is drag some elsewhere
        }
    }
}






class ChildCoordinateProvider(private val position:Int): CoordinatesProvider {
    override fun calculateCoordinates(view: View?): FloatArray {
        (view as RecyclerView)
        val layoutManager = view.layoutManager ?: return floatArrayOf(0f,0f)
        val isValidPosition = layoutManager.childCount > position

        if(!isValidPosition) return floatArrayOf(0f,0f)

        val childAT = layoutManager.getChildAt(position)?:return floatArrayOf(0f,0f)

        return getCoordinates(childAT)

    }
    private fun getCoordinates(
        view: View,
    ): FloatArray {
        val xy = IntArray(2)
        view.getLocationOnScreen(xy)
        return floatArrayOf(xy[0].toFloat(),xy[1].toFloat())
    }
}