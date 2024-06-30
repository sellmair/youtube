import testUtils.Todo
import kotlin.test.Test
import kotlin.test.assertEquals


class FancyTest {

    @Test
    fun `simple passing test`() {
        assertEquals(2, 1 + 1)
    }

    @Todo
    @Test
    fun `complicated business logic`() {
        assertEquals("bar", myBusinessLogic().id)
    }
}