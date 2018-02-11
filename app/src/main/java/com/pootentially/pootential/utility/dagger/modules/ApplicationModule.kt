package com.pootentially.pootential.utility.dagger.modules

import android.app.Application
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * Created by nick on 2/6/18.
 */
@Module
class ApplicationModule(private val application: Application){

    @Provides
    @Singleton
    fun provideApplication() : Application {
        return application
    }
}