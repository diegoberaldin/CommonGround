package com.github.diegoberaldin.commonground.core.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModel
import org.koin.androidx.compose.koinViewModel
import org.koin.java.KoinJavaComponent

inline fun <reified T> getByInjection(): T = KoinJavaComponent.get(T::class.java)

@Composable
inline fun <reified T> rememberByInjection(key: Any = Unit): T = remember(key) {
    KoinJavaComponent.get(T::class.java)
}

@Composable
inline fun <reified T : ViewModel> injectViewModel(): T {
    return koinViewModel<T>()
}