package com.irfan.gridpuzzl

import androidx.test.espresso.Espresso
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.irfan.gridpuzzl.MainActivity
import com.irfan.gridpuzzl.R

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import org.junit.Rule

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    val TXT = "Hello World!"
    @get:Rule
    val activityRule = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.irfan.gridpuzzl", appContext.packageName)
    }
    @Test
    fun ifCorrectText(){
        Espresso.onView(ViewMatchers.withId(R.id.lble))
            .check(ViewAssertions.matches(ViewMatchers.withText(TXT)))

    }
}