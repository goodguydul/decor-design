package com.KevAndz.decordesign;
import android.support.test.rule.ActivityTestRule;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.*;

public class RegisterActivityTest {

    @Rule
    public ActivityTestRule<RegisterActivity> registerActivityTestRule = new ActivityTestRule<RegisterActivity>(RegisterActivity.class);

    @Before
    public void setUp() throws Exception {
    }

    String name = "kevin";
    String username = "kevin2";
    String email = "kevandz@decordesign.com";
    String password = "kevin123";
    String confirmPassword = "kevin123";
    String confirmPassword2 = "kevin1234";
    String successString = "User created successfully!";
    String failString ="Password does not match";

    @Test
    public void testUserRegister(){
        onView(withId(R.id.editTextName)).perform(typeText(name), closeSoftKeyboard());
        onView(withId(R.id.editTextUsername)).perform(typeText(username), closeSoftKeyboard());
        onView(withId(R.id.editTextEmail)).perform(typeText(email), closeSoftKeyboard());
        onView(withId(R.id.editTextPassword)).perform(typeText(password), closeSoftKeyboard());
        onView(withId(R.id.editTextPasswordConfirm)).perform(typeText(confirmPassword), closeSoftKeyboard());
        onView(withId(R.id.registerButton)).perform(click());
        onView(withText(successString)).check(matches(isDisplayed()));
    }

    @Test
    public void testUserRegisterFailed(){
        onView(withId(R.id.textViewName)).perform(typeText(name), closeSoftKeyboard());
        onView(withId(R.id.editTextUsername)).perform(typeText(username), closeSoftKeyboard());
        onView(withId(R.id.editTextEmail)).perform(typeText(email), closeSoftKeyboard());
        onView(withId(R.id.editTextPassword)).perform(typeText(password), closeSoftKeyboard());
        onView(withId(R.id.editTextPasswordConfirm)).perform(typeText(confirmPassword2), closeSoftKeyboard());
        onView(withId(R.id.registerButton)).perform(click());
        onView(withText(failString)).check(matches(isDisplayed()));
    }

    @After
    public void tearDown() throws Exception {
    }
}

