package testUtils

import org.junit.jupiter.api.Assumptions
import org.junit.jupiter.api.extension.AfterEachCallback
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.api.extension.TestExecutionExceptionHandler
import kotlin.jvm.optionals.getOrNull

class TodoTestExecutionExceptionHandler : TestExecutionExceptionHandler, AfterEachCallback {
    override fun handleTestExecutionException(context: ExtensionContext, throwable: Throwable) {
        val element = context.element.getOrNull() ?: return
        val isTodo = element.isAnnotationPresent(Todo::class.java)
        if (isTodo) {
            Assumptions.abort<Nothing>("Test was marked as 'todo'")
        } else {
            throw throwable
        }
    }

    override fun afterEach(context: ExtensionContext) {
        val element = context.element.getOrNull() ?: return
        if(element.isAnnotationPresent(Todo::class.java) && context.executionException.isEmpty) {
            error("Test passed; 'Todo' annotation still present: Please remove the @Todo")
        }
    }
}