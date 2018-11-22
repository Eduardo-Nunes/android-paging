package com.nunes.eduardo.paging


import android.view.KeyEvent
import com.nunes.eduardo.paging.ui.RepoViewHolder
import com.nunes.eduardo.paging.ui.SearchRepositoriesActivity
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import android.text.TextUtils
import android.view.View
import androidx.core.util.Preconditions.checkArgument
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers.allOf
import org.hamcrest.TypeSafeMatcher

@RunWith(AndroidJUnit4::class)
@LargeTest
open class SearchRepositoryActivityTest {

    private lateinit var stringToBeSearched: String
    private lateinit var expectedStringResult: String

    @get:Rule
    var activityRule: ActivityTestRule<SearchRepositoriesActivity>
            = ActivityTestRule(SearchRepositoriesActivity::class.java)

    @Before
    fun initValidString() {
        // Specify a valid string.
        stringToBeSearched = "android-paging"
        expectedStringResult = "Eduardo-Nunes/$stringToBeSearched"
    }

    @Test
    @Throws(Exception::class)
    fun searchedString_hasCorrectResult() {
        // Type text inside search text view
        onView(withId(R.id.search_repo))
                .perform(clearText())
                .perform(typeText(stringToBeSearched), closeSoftKeyboard())
                .perform(pressKey(KeyEvent.KEYCODE_ENTER))

        onView(withId(R.id.list))
                .perform(RecyclerViewActions.scrollTo<RepoViewHolder>(
                        hasDescendant(withText(expectedStringResult))))

        onView(withItemText(expectedStringResult)).check(matches(isDisplayed()))
    }

    /**
     * A custom {@link Matcher} which matches an item in a {@link RecyclerView} by its text.
     *
     * <p>
     * View constraints:
     * <ul>
     * <li>View must be a child of a {@link RecyclerView}
     * <ul>
     *
     * @param itemText the text to match
     * @return Matcher that matches text in the given view
     */
    private fun withItemText(itemText: String): Matcher<View> {
        checkArgument(!TextUtils.isEmpty(itemText), "itemText cannot be null or empty")
        return object : TypeSafeMatcher<View>() {
            override fun matchesSafely(item: View): Boolean {
                return allOf(
                        isDescendantOfA(isAssignableFrom(RecyclerView::class.java)),
                        withText(itemText)).matches(item)
            }

            override fun describeTo(description: Description) {
                description.appendText("is isDescendantOfA RV with text $itemText")
            }
        }
    }

}
