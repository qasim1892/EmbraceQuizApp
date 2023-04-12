package com.embrace.quizapp.extensions

fun String?.safeUnwrap(def: String = ""): String {
    return this ?: def
}

fun Int?.safeUnwrap(def: Int = 0): Int {
    return this ?: def
}
