package com.irfan.gridpuzzl.ui

import android.app.Activity
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.*
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.BoundedMatcher
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth
import com.irfan.gridpuzzl.*
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.junit.FixMethodOrder
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.MethodSorters
import java.util.concurrent.atomic.AtomicReference


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
        onView(withId(R.id.gridRecyclerView)).check(ViewAssertions.matches(
            allChildren(isDisplayed()))
        )
    }

    @Test
    fun `3-When item is dragged then its position should be changed`(){
        activityRule.moveToState(Lifecycle.State.RESUMED)
        val activity: MainActivity = activityRule.getActivity()
        val recyclerView = activity.findViewById<RecyclerView>(R.id.gridRecyclerView)
        val layoutManager = recyclerView.layoutManager?:throw NullPointerException("recyclerView has no layoutManager")
        val firstChildPosition = 3
        val lastChildPosition = 8

        val dragChildView = layoutManager.getChildAt(firstChildPosition)?:throw NullPointerException("No child at $firstChildPosition")

        onView(withId(R.id.gridRecyclerView))
            .perform(GeneralSwipeAction(Swipe.FAST,ChildCoordinateProvider(firstChildPosition),ChildCoordinateProvider(lastChildPosition),Press.FINGER))
            .check(ViewAssertions.matches(isDragHappen(dragChildView,firstChildPosition)))
    }

}






