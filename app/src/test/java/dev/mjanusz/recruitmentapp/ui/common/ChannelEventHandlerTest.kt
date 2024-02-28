package dev.mjanusz.recruitmentapp.ui.common

import app.cash.turbine.test
import dev.mjanusz.recruitmentapp.test.OverrideMainDispatcherRule
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class ChannelEventHandlerTest {

    @get:Rule
    val dispatcherRule = OverrideMainDispatcherRule(StandardTestDispatcher())

    private val handler = ChannelEventHandler<Any>()

    @Test
    fun `test event propagation`() = runTest {
        // given
        val event = "string event"
        // when
        handler.onEvent(event)
        // then
        val flow by handler
        flow.test {
            assertEquals(event, awaitItem())
        }
    }
}