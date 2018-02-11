package com.pootentially.pootential

import android.app.Application
import com.pootentially.pootential.utility.dagger.ApplicationComponent

/**
 * Created by nick on 2/6/18.
 */
class Application: Application() {
    companion object {
        @JvmStatic lateinit var appComponent: ApplicationComponent
    }

    override fun onCreate() {
        super.onCreate()
        appComponent = ApplicationComponent.create(this)
    }
}