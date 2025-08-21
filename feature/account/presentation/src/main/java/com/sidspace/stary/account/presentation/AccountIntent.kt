package com.sidspace.stary.account.presentation

import com.google.android.gms.auth.api.signin.GoogleSignInAccount


sealed interface AccountIntent {
    object ToFoldersScreen: AccountIntent
    data class ToMovieScreen(val id: Long): AccountIntent
    object ToViewedScreen: AccountIntent
    object ToBookmarkScreen: AccountIntent
    object ToErrorScreen: AccountIntent
    data class SaveScrollViewed(val scrollIndex: Int, val scrollOffSet: Int): AccountIntent
    data class SaveScrollBookmark(val scrollIndex: Int, val scrollOffSet: Int): AccountIntent

    object OnGoogleSignInClick : AccountIntent
    data class OnSignInResult(val account: GoogleSignInAccount?) : AccountIntent
    data class SetGoogleAccountSheetShown(val shown: Boolean): AccountIntent
    object SignOut: AccountIntent
}