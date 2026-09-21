package com.dishcovery.app

import android.app.Application
import com.dishcovery.app.di.AppContainer

class DishcoveryApplication : Application() {
    val container by lazy { AppContainer(this) }
}
