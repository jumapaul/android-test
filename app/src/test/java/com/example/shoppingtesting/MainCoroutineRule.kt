package com.example.shoppingtesting

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.TestCoroutineScope
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.rules.TestWatcher
import org.junit.runner.Description

@OptIn(ExperimentalCoroutinesApi::class)
class MainCoroutineRule(
    private val dispatcher: CoroutineDispatcher = TestCoroutineDispatcher()
): TestWatcher(), TestCoroutineScope by TestCoroutineScope(dispatcher) {
    // We use a special dispatcher which is not the main dispatcher and we can use this rule in all of our test classes.
    // This class inherits from test watcher, test coroutine scope

    override fun starting(description: Description) {
        super.starting(description)

        Dispatchers.setMain(dispatcher) // We can set many dispatchers for our coroutine
    }

    override fun finished(description: Description) {
        super.finished(description)
        cleanupTestCoroutines()
        Dispatchers.resetMain() // Reset our dispatcher to the initial dispatcher
    }
}