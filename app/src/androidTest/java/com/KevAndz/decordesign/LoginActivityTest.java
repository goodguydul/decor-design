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

public class LoginActivityTest {

    @Rule
    public ActivityTestRule<LoginActivity> loginActivityTestRule;


    private String email = "kevandz@decordesign.com", password = "kevin123", password2 = "kevin1234", successDialog = "Successfully Logged in!", failDialog = "Username / password is not found!";

    public LoginActivityTest() {
        loginActivityTestRule = new ActivityTestRule<LoginActivity>(LoginActivity.class);
    }


    @Before
    public void setUp() throws Exception {

    }

    @Test
    public void onCreate() {
        onView(withId(R.id.editTextEmail)).perform(typeText(email), closeSoftKeyboard());
        onView(withId(R.id.editTextPassword)).perform(typeText(password), closeSoftKeyboard());
        onView(withId(R.id.cirLoginButton)).perform(click());
        onView(withText(successDialog)).check(matches(isDisplayed()));
    }

    @Test
    public void testLoginFailed(){
        onView(withId(R.id.editTextEmail)).perform(typeText(email), closeSoftKeyboard());
        onView(withId(R.id.editTextPassword)).perform(typeText(password2), closeSoftKeyboard());
        onView(withId(R.id.cirLoginButton)).perform(click());
        onView(withText(failDialog)).check(matches(isDisplayed()));
    }


    @After
    public void tearDown() throws Exception {
    }

}