import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.jetbrains.sample.platform
import org.jetbrains.sample.scanBluetooth
import kotlin.test.Test
import kotlin.test.assertTrue

class MyTests {
    @Test
    fun `test - platform`() {
        platform()
        error("sadf")
    }

    @Test
    fun `test - scanBluetooth`() = runTest {
        assertTrue(scanBluetooth().first().startsWith("Device:"))
    }
}
