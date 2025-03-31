package tech.ericwathome.core.data.util

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

inline fun launchCoroutineScope(
    context: CoroutineContext = Dispatchers.IO,
    crossinline block: suspend CoroutineScope.() -> Unit,
) {
    CoroutineScope(context).launch {
        block()
    }
}