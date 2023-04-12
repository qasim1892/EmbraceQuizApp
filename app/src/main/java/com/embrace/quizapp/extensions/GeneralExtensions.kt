package com.embrace.quizapp.extensions

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Html
import android.view.View
import android.widget.TextView
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

fun <T> T?.notNull(f: (it: T) -> Unit) {
    if (this != null) f(this)
}

inline fun <reified T> Any.cast(crossinline block: (T) -> Unit) {
    (this as? T).notNull {
        block(it)
    }
}

fun TextView.setTextAsHtml(data: String) {
    text = Html.fromHtml(
        data, Html.FROM_HTML_MODE_LEGACY
    )
}

fun LifecycleOwner.delayOnLifecycle(
    delayInMs: Long, block: () -> Unit
): Job = lifecycleScope.launch {
    delay(delayInMs)
    block()
}

fun <T> Context.openActivity(it: Class<T>, extras: Bundle.() -> Unit = {}) {
    val intent = Intent(this, it)
    intent.putExtras(Bundle().apply(extras))
    startActivity(intent)
}

fun View.visible() {
    this.visibility = View.VISIBLE
}

fun View.gone() {
    this.visibility = View.GONE
}