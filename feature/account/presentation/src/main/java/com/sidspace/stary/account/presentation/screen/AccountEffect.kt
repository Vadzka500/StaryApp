package com.sidspace.stary.account.presentation.screen

import android.content.Intent


sealed class AccountEffect {
    object ToFoldersScreen: AccountEffect()
    data class ToMovieScreen(val id: Long) : AccountEffect()
    object ToBookmarkScreen: AccountEffect()
    object ToViewedScreen: AccountEffect()
    object ToErrorScreen: AccountEffect()
    data class LaunchGoogle(val intent: Intent): AccountEffect()
}