import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import io.sellmair.evas.Events
import io.sellmair.evas.States
import io.sellmair.evas.compose.installEvas
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import java.io.File
import java.lang.invoke.MethodHandle
import java.lang.invoke.MethodHandles
import java.lang.invoke.MethodType
import java.lang.reflect.Method
import java.net.URLClassLoader
import kotlin.jvm.Throws
import kotlin.time.Duration.Companion.seconds

fun main() {
    val appClasspath = System.getProperty("app.cp").split(File.pathSeparator)
        .map { File(it) }

    println("app.cp: $appClasspath")
    val parent = Thread.currentThread().contextClassLoader

    val events = Events()
    val states = States()

    val appComposableState = MutableStateFlow(Update(0, null, null))
    var methodHandle: MethodHandle? = null
    var reloadIteration: Int = 0

    MainScope().launch(Dispatchers.IO) {
        while (true) {
            reloadIteration++
            try {
                val appClassLoader = URLClassLoader(
                    appClasspath.map { it.toURI().toURL() }.toTypedArray(), parent
                )

                val mainKt = appClassLoader.loadClass("AppKt")
                val reflect = mainKt.getDeclaredMethod("App", Composer::class.java, Int::class.javaPrimitiveType)
                methodHandle = MethodHandles.lookup()
                    .findStatic(
                        mainKt, "App",
                        MethodType.methodType(Void.TYPE, Composer::class.java, Int::class.javaPrimitiveType)
                    )


                /*
                withContext(Dispatchers.Main) {
                    @Suppress("INVISIBLE_MEMBER", "INVISIBLE_REFERENCE")
                    val token = Recomposer.saveStateAndDisposeForHotReload()
                    appComposableState.value = Update(reloadIteration, methodHandle, reflect)
                    @Suppress("INVISIBLE_MEMBER", "INVISIBLE_REFERENCE")
                    Recomposer.loadStateAndComposeForHotReload(token)
                }

                 */
                appComposableState.value = Update(reloadIteration, methodHandle, reflect)


                println("Dispatch!")
            } catch (e: Throwable) {
                e.printStackTrace()
                break
            }
            delay(1.seconds)
        }
    }


    application {

        Window(
            onCloseRequest = ::exitApplication,
            state = rememberWindowState(width = 600.dp, height = 800.dp)
        ) {
            //App()
            val state = appComposableState.collectAsState().value

            installEvas(events, states) {
                key(state.id) {
                    println("Reload...: ${state.id}")
                    //state.reflect?.invoke(null, currentComposer, currentCompositeKeyHash)
                    state.method?.invoke(currentComposer, state.id)
                }
            }
        }
    }

}

private data class Update(
    val id: Int,
    val method: MethodHandle?,
    val reflect: Method?
)

