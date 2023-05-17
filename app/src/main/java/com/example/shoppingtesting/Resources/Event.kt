package com.example.shoppingtesting.Resources

// This makes our livedata emits one time events.
// This happens in occasions such that making network request.
// Assume that we make a data request and the response fails, the snackbar
// will show fail.
// When we rotate the device, the live data emits again and the snackbar will
// show a fail again. To avoid this, we use this resource.
open class Event<out T>(private val content: T) {

    var hasBeenHandled = false
        private set // Allow external read but not write

    /**
     * Returns the content and prevents its use again.
     */
    fun getContentIfNotHandled(): T? {
        return if (hasBeenHandled) {
            null
        } else {
            hasBeenHandled = true
            content
        }
    }

    /**
     * Returns the content, even if it's already been handled.
     */
    fun peekContent(): T = content
}