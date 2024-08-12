@file:Suppress("UNREACHABLE_CODE", "unused", "ControlFlowWithEmptyBody")

sealed class OurResult<out T> {
    data class Success<T>(val value: T) : OurResult<T>()
    data class Error(val error: Throwable?) : OurResult<Nothing>()
}

fun <T> OurResult<T>.valueOr(alternative: (OurResult.Error) -> T) :T {
    return when(this) {
        is OurResult.Error -> alternative(this)
        is OurResult.Success -> value
    }
}

fun foo(): String {
    return when(val result = httpRequest()){
        is OurResult.Error -> "Error happened: ${result.error?.message}"
        is OurResult.Success -> result.value
    }
}

fun httpRequest(): OurResult<String> {
    return OurResult.Success("")
}