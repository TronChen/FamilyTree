package com.tron.familytree.data

sealed class AppResult<out R> {

    data class Success<out T>(val data: T) : AppResult<T>()
    data class Fail(val error: String) : AppResult<Nothing>()
    data class Error(val exception: Exception) : AppResult<Nothing>()
    object Loading : AppResult<Nothing>()

    override fun toString(): String {
        return when (this) {
            is Success<*> -> "Success[result=$data]"
            is Fail -> "Fail[error=$error]"
            is Error -> "Error[exception=${exception.message}]"
            Loading -> "Loading"
        }
    }
}

/**
 * `true` if [Result] is of catalogType [Success] & holds non-null [Success.data].
 */
val AppResult<*>.succeeded
    get() = this is AppResult.Success && data != null