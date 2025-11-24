package com.dv.apps.komic.reader.ext

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update

inline infix fun <A, B> ((A) -> Unit).dispatchFor(
    crossinline other: (B) -> A
): (B) -> Unit = {
    invoke(other(it))
}

inline fun <T, R> Flow<List<T>>.mapItems(
    crossinline block: suspend (T) -> R?
) = map {
    it.mapNotNull { block(it) }
}

suspend inline fun <T, R> Flow<T>.collectInto(
    state: MutableStateFlow<R>,
    crossinline prop: (R, T) -> R
) {
    collect { data ->
        state.update {
            prop(it, data)
        }
    }
}