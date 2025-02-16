package tech.ericwathome.core.domain.util

/**
 * Utility function to execute a block of code and return its result,
 * or null if an exception is thrown.
 *
 * @param T The type of the result.
 * @param block The block of code to execute.
 * @return The result of the block, or null if an exception is thrown.
 */
fun <T> tryOrNull(block: () -> T): T? = try {
    block()
} catch (e: Exception) {
    null
}