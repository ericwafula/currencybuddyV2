package tech.ericwathome.core.presentation.ui

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map

/**
 * Emits only the selected field from the original flow, and only when that field changes.
 *
 * @param selector A function that maps the emitted value to the field of interest.
 * @return A [Flow] emitting only the selected field, ignoring duplicate consecutive values.
 */
fun <T, R> Flow<T>.extract(selector: (T) -> R): Flow<R> = map(selector).distinctUntilChanged()

/**
 * Emits a [Pair] of selected fields from the original flow,
 * and only when either of them changes.
 *
 * @param selector A function that maps the emitted value to a [Pair] of fields.
 * @return A [Flow] emitting the selected pair of fields, ignoring duplicates.
 */
fun <T, A, B> Flow<T>.extractPair(selector: (T) -> Pair<A, B>): Flow<Pair<A, B>> = map(selector).distinctUntilChanged()

/**
 * Emits a [Triple] of selected fields from the original flow,
 * and only when any of them change.
 *
 * @param selector A function that maps the emitted value to a [Triple] of fields.
 * @return A [Flow] emitting the selected triple of fields, ignoring duplicates.
 */
fun <T, A, B, C> Flow<T>.extractTriple(selector: (T) -> Triple<A, B, C>): Flow<Triple<A, B, C>> = map(selector).distinctUntilChanged()
