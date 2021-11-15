package com.irfan.gridpuzzl.ui

import android.app.Activity
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.*
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.BoundedMatcher
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth
import com.irfan.gridpuzzl.GridViewHolder
import com.irfan.gridpuzzl.MainActivity
import com.irfan.gridpuzzl.R
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.junit.FixMethodOrder
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.MethodSorters
import java.util.concurrent.atomic.AtomicReference


/**
======TodoList=====
1- grid is proper initialised
2- All elements on the the grid visible
3- tile can move
4- game completion message showed
5- tile can not mov message showed
6- state is retained on activity rotation
7- Acceptance test
 */


@RunWith(AndroidJUnit4::class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
class PuzzleUITest {
    @get:Rule
    val activityRule = ActivityScenarioRule(MainActivity::class.java)


    @Test
    fun `1- when activity is created then grid is properly created`() {
        activityRule.moveToState(Lifecycle.State.CREATED)
        val activity: MainActivity = activityRule.getActivity()
        val recyclerView = activity.findViewById<RecyclerView>(R.id.gridRecyclerView)
        Truth.assertThat(recyclerView).isNotNull()
        Truth.assertThat(recyclerView.adapter).isNotNull()
        Truth.assertThat(recyclerView.layoutManager).isInstanceOf(GridLayoutManager::class.java)
        Truth.assertThat((recyclerView.layoutManager as GridLayoutManager).spanCount).isEqualTo(3)
        Truth.assertThat(recyclerView.adapter!!.itemCount).isEqualTo(9)
    }

    @Test
    fun `2- When Activity launches all grid cell should be visible`() {
        activityRule.moveToState(Lifecycle.State.RESUMED)
        Espresso.onView(withId(R.id.gridRecyclerView)).check(ViewAssertions.matches(
            allChildren(isDisplayed()))
        )

    }

//    @Test
//    fun `3- When tile is moved it then there position should be swapped`() {
//        onView(withId(R.id.gridRecyclerView))
//            .perform(
//                RecyclerViewActions.actionOnItemAtPosition<GridViewHolder>(5,
//                    GeneralSwipeAction(
//                        Swipe.FAST,
//                        GeneralLocation.CENTER,
//                        CoordinatesProvider { floatArrayOf(0F, 0F) },
//                        Press.FINGER
//                    )
//                ))
//    }
//
//    @Test
//    fun `4- When game completed then a message should be displayed`() {
//
//    }
//
//    @Test
//    fun `5- When a tile is at correct position then message should shown`() {
//
//    }
}


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
                val isMatch = child != null && matcher.matches(child) && matcher.matches((child as ViewGroup).getChildAt(0))

                if (!isMatch) return false
            }
            return actualChildrenCount + 1 == childCount
        }
    }
}