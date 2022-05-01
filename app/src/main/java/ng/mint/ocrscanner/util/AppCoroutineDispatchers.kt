package ng.mint.ocrscanner.util

import kotlinx.coroutines.CoroutineDispatcher

interface AppCoroutineDispatchers {
    val main: CoroutineDispatcher
    val io: CoroutineDispatcher
    val default: CoroutineDispatcher
}