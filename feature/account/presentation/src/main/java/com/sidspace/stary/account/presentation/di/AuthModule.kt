package com.sidspace.stary.account.presentation.di

import android.content.Context
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AuthModule {

    @Provides
    @Singleton
    fun getGoogleSignInClient(
        @ApplicationContext
        context: Context
    ): GoogleSignInClient{
        val signInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()

        return GoogleSignIn.getClient(context, signInOptions)
    }

    @Provides
    @Singleton
    fun getGoogleAccount(@ApplicationContext context: Context): GoogleSignInAccount?{
        return GoogleSignIn.getLastSignedInAccount(context)
    }
}