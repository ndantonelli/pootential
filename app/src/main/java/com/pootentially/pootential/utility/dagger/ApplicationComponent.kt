package com.pootentially.pootential.utility.dagger

import android.app.Application
import com.pootentially.pootential.utility.dagger.modules.ApplicationModule
import com.pootentially.pootential.utility.dagger.modules.FirebaseRepositoryModule
import com.pootentially.pootential.viewModels.RestroomViewModel
import dagger.Component
import javax.inject.Singleton

/**
 * Created by nick on 2/6/18.
 */
@Singleton
@Component(
        modules = [ApplicationModule::class, FirebaseRepositoryModule::class]
)
interface ApplicationComponent {
    fun inject(viewModel: RestroomViewModel)


    companion object Factory{
        fun create(app: Application): ApplicationComponent {
            return DaggerApplicationComponent.builder()
                    .applicationModule(ApplicationModule(app))
                    .firebaseRepositoryModule(FirebaseRepositoryModule())
                    .build()
        }
    }
}