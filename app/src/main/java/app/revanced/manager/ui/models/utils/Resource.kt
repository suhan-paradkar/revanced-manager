package app.revanced.manager.ui.models.utils

sealed class Resource<out T> {
    object Loading : Resource<Nothing>()
    data class Success<out T>(val data: T) : Resource<T>()
    @Suppress("ArrayInDataClass")
    /**
     * Represents a failure.
     * @param what What went wrong.
     * This is represented as: "An error occurred while [what]."
     */
    data class Failure(val what: String) : Resource<Nothing>()
}