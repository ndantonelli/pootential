package com.pootentially.pootential.utility.dagger.modules

import com.pootentially.pootential.utility.firebase.FirebaseRepository
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * Created by nick on 2/6/18.
 */
@Module
class FirebaseRepositoryModule{
    @Provides
    @Singleton
    fun provideRepository(): FirebaseRepository{
        return FirebaseRepository()
    }
}