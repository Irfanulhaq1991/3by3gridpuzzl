package com.irfan.gridpuzzl.ui

import android.app.Activity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.google.common.truth.Truth
import com.irfan.gridpuzzl.MainActivity
import com.irfan.gridpuzzl.R
import org.junit.*
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
    fun `1- When activity is created then adaptor is attached to gridRecyclerView`() {
        activityRule.moveToState(Lifecycle.State.CREATED)
        val activity: MainActivity = activityRule.getActivity()
        val recyclerView = activity.findViewById<RecyclerView>(R.id.gridRecyclerView)
        Truth.assertThat(recyclerView).isNotNull()
        Truth.assertThat(recyclerView.adapter).isNotNull()
        Truth.assertThat(recyclerView.layoutManager).isInstanceOf(GridLayoutManager::class.java)
        Truth.assertThat((recyclerView.layoutManager as GridLayoutManager).spanCount).isEqualTo(3)
        Truth.assertThat(recyclerView.adapter!!.itemCount).isEqualTo(9)
    }

//    @Test
//    fun `2- When activity resumed all tiled should be visible`() {
//
//    }
//
//    @Test
//    fun `3- When tile is moved it should be visible at the new position`() {
//
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