package dev.mjanusz.recruitmentapp.ui.common

import kotlinx.coroutines.flow.Flow
import kotlin.reflect.KProperty

interface UIEventHandler<T : Any>  {
    suspend fun onEvent(event: T)
    operator fun getValue(thisRef: Any?, property: KProperty<*>): Flow<T>
}