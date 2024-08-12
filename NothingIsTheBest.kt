@file:Suppress("UNREACHABLE_CODE", "unused", "ControlFlowWithEmptyBody")

sealed class OurResult<T> {
    data class Success<T>(val value: T) : OurResult<T>()
    data class Error<T>(val error: Throwable?) : OurResult<T>()
}

fun foo(): Foo {
    return never()
}

fun never(): Nothing {
    while (true) {}
}