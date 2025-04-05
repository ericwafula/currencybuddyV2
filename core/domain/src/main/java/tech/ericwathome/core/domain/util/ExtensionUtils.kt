package tech.ericwathome.core.domain.util

/**
 * Utility function to execute a block of code and return its result,
 * or null if an exception is thrown.
 *
 * @param T The type of the result.
 * @param block The block of code to execute.
 * @return The result of the block, or null if an exception is thrown.
 */
fun <T> tryOrNull(block: () -> T): T? =
    try {
        block()
    } catch (e: Exception) {
        null
    }

/**
 * Returns `false` if the Boolean is null, otherwise returns the Boolean's value.
 *
 * @receiver The nullable Boolean value.
 * @return `false` if the Boolean is null, otherwise the Boolean's value.
 */
fun Boolean?.orFalse() = this ?: false

/**
 * Returns `true` if the Boolean is null, otherwise returns the Boolean's value.
 *
 * @receiver The nullable Boolean value.
 * @return `true` if the Boolean is null, otherwise the Boolean's value.
 */
fun Boolean?.orTrue() = this ?: true