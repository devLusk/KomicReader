package com.dv.apps.komic.reader.ext

inline infix fun <A, B> ((A) -> Unit).dispatchFor(
    crossinline other: (B) -> A
): (B) -> Unit = {
    invoke(other(it))
}