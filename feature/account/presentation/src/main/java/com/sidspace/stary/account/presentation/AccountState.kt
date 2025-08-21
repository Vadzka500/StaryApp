package com.sidspace.stary.account.presentation


import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.sidspace.stary.ui.model.MovieUiLight
import com.sidspace.stary.ui.model.ResultData


data class AccountState (
    val resultAccountViewed : ResultData<List<MovieUiLight>> = ResultData.None,
    val resultAccountBookmark : ResultData<List<MovieUiLight>> = ResultData.None,
    val countViewed: Int = 0,
    val countBookmark: Int = 0,
    val countFolders: Long = 0,
    val isShowEmptyHint: Boolean = false,
    val bookmarkScrollOffSet:Int = 0,
    val scrollViewed: ScrollState = ScrollState(),
    val scrollBookmark: ScrollState = ScrollState(),
    val account: GoogleSignInAccount? = null,
    val isGoogleSheetShown: Boolean = false
)

data class ScrollState(
    val scrollIndex: Int = 0,
    val scrollOffSet: Int = 0
)

