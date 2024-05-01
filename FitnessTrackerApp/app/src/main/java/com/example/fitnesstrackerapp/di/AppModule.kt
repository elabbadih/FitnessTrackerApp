package com.example.fitnesstrackerapp.di

import com.example.fitnesstrackerapp.repositories.FlashcardRepositoryImpl
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideFlashcardRepository(firebaseDatabase: FirebaseDatabase): FlashcardRepositoryImpl {
        return FlashcardRepositoryImpl(firebaseDatabase)
    }

    @Provides
    @Singleton
    fun provideFirebaseDatabase(): FirebaseDatabase {
        return Firebase.database
    }

    @Provides
    @Singleton
    fun provideFirebaseUser(): FirebaseUser? {
        return Firebase.auth.currentUser
    }
}