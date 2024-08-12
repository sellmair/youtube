@file:Suppress("UNREACHABLE_CODE", "unused", "ControlFlowWithEmptyBody")

class Foo

fun foo(): Foo {
    return never()
}

fun never(): Nothing {
    while (true) {}
}