package com.chocolatecake.marvel.util
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer

open class Event<out T>(private val content: T) {

    var hasBeenHandled = false
        private set

    fun getContentIfNotHandled(): T? {
        return if (hasBeenHandled) {
            null
        } else {
            hasBeenHandled = true
            content
        }
    }

    fun peekContent(): T = content
}

class EventObserve<T>(private val onEventUnhandledContent:(T) ->Unit)
    : Observer<Event<T>> {
    override fun onChanged(value: Event<T>) {
        value?.getContentIfNotHandled()?.let {
            onEventUnhandledContent(it)
        }
    }
}


fun <T> LiveData<T>.observeNonNull(owner: LifecycleOwner, observer: (t: T) -> Unit) {
    this.observe(
        owner
    ) {
        it?.let(observer)
    }
}
