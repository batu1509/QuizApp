package com.sample.quizapp.di

import com.batueksi.moviesapp.domain.repository.AuthRepository
import com.sample.quizapp.data.repository.FirebaseAuthRepositoryImpl
import com.sample.quizapp.data.repository.FirestoreUserRepositoryImpl
import com.sample.quizapp.domain.repository.UserRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent


@Module
@InstallIn(SingletonComponent::class)
abstract class AuthRepositoryModule {

    @Binds
    abstract fun bindAuthRepository(authRepository : FirebaseAuthRepositoryImpl): AuthRepository

    @Binds
    abstract fun bindUserRepository(userRepository: FirestoreUserRepositoryImpl): UserRepository
}