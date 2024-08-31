import androidx.compose.runtime.AbstractApplier
import androidx.compose.runtime.BroadcastFrameClock
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ComposeNode
import androidx.compose.runtime.Composition
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Recomposer
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshots.Snapshot
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.concurrent.atomic.AtomicBoolean
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext
import kotlin.time.Duration.Companion.seconds

class Node(
    val value: Any? = null,
    val children: MutableList<Node> = mutableListOf()
) {
    override fun toString(): String {
        return buildString {
            if (value != null) append(value)
            if (children.isNotEmpty()) {
                appendLine()
                append(children.joinToString(separator = "\n").prependIndent("--"))
            }
        }
    }
}

fun main() {
    val root = Node()
    val recomposer = Recomposer(EmptyCoroutineContext)
    val composition = Composition(Applier(root), recomposer)

    val clock = BroadcastFrameClock()
    val composeContext = Dispatchers.Default + clock

    val scope = CoroutineScope(composeContext)
    GlobalSnapshotManager().ensureStarted(scope)

    composition.setContent {
        var showNext by remember { mutableStateOf<Boolean>(false) }

        rememberCoroutineScope().launch {
            //showNext = true
        }

        LaunchedEffect(Unit) {
            delay(5.seconds)
            println("SHOW NEXT!")
            showNext = true
        }

        Person("Jakob") {
            Person("Sebastian") {
                if (showNext) {
                    Person("Emily")
                }
            }
            Person("Sophia")
            Person("Jonas")
        }
    }

    runBlocking(composeContext) {
        launch {
            while (true) {
                println(root.toString())
                delay(1.seconds)
            }
        }

        launch {
            while (true) {
                clock.sendFrame(0)
                delay(1.seconds)
            }
        }

        launch {
            recomposer.runRecomposeAndApplyChanges()
        }
    }

    println(root)
}

class Applier(node: Node) : AbstractApplier<Node>(node) {
    override fun insertBottomUp(index: Int, instance: Node) {
        current.children.add(index, instance)
    }

    override fun insertTopDown(index: Int, instance: Node) = Unit

    override fun move(from: Int, to: Int, count: Int) {
        current.children.move(from, to, count)
    }

    override fun remove(index: Int, count: Int) {
        current.children.remove(index, count)
    }

    override fun onClear() = Unit
}

@Composable
fun Person(name: String, children: @Composable () -> Unit = {}) {
    ComposeNode<Node, Applier>(
        factory = { Node(name, mutableListOf()) },
        update = {
            // Shrug
        },
        content = children
    )
}

class Name(val name: String)

internal class GlobalSnapshotManager {
    private val started = AtomicBoolean(false)
    private val sent = AtomicBoolean(false)

    fun ensureStarted(scope: CoroutineScope) {
        if (started.compareAndSet(false, true)) {
            val channel = Channel<Unit>(1)
            scope.launch {
                channel.consumeEach {
                    sent.set(false)
                    Snapshot.sendApplyNotifications()
                }
            }
            Snapshot.registerGlobalWriteObserver {
                if (sent.compareAndSet(false, true)) {
                    channel.trySend(Unit)
                }
            }
        }
    }
}
