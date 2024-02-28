package dev.mjanusz.recruitmentapp.ui.common

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import javax.inject.Inject
import kotlin.reflect.KProperty

class ChannelEventHandler<T : Any> @Inject constructor(): UIEventHandler<T> {

    private val _events = Channel<T>(Channel.UNLIMITED)
    private val events = _events.receiveAsFlow()

    override fun getValue(thisRef: Any?, property: KProperty<*>): Flow<T> = events

    override suspend fun onEvent(event: T) {
        _events.send(event)
    }
}