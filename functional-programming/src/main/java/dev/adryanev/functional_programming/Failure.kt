package dev.adryanev.functional_programming

sealed class Failure {

    /**
     * Extend this class in order to provide your own
     * custom failure.
     */
    open class CustomFailure() : Failure()

    data class UnexpectedFailure(
        val message: String?
    ) : Failure()

}