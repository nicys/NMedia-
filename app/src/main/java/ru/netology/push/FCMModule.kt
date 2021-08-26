package ru.netology.push

import com.google.android.gms.common.GoogleApiAvailability
import com.google.firebase.installations.FirebaseInstallations
import com.google.firebase.messaging.FirebaseMessaging
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@InstallIn(SingletonComponent::class)
@Module
object FCMModule {
    @Singleton
    @Provides
    fun firebaseMessaging(): FirebaseMessaging = FirebaseMessaging.getInstance()

    @Singleton
    @Provides
    fun googleApiAvailability(): GoogleApiAvailability = GoogleApiAvailability.getInstance()

    @Singleton
    @Provides
    fun firebaseInstallations(): FirebaseInstallations = FirebaseInstallations.getInstance()
}