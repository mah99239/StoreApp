package com.mz.storeapp.di.utils

import javax.inject.Qualifier

@Retention(AnnotationRetention.RUNTIME)
@Qualifier
annotation class Dispatcher(val appDispatchers: AppDispatchers)

enum class AppDispatchers {
    IO,
    DEFAULT,
}