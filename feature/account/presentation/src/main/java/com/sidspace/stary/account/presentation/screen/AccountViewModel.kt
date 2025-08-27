package com.sidspace.stary.account.presentation.screen

import android.content.Intent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sidspace.stary.domain.model.Result
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient

import com.sidspace.stary.account.domain.usecase.GetViewedMoviesUseCase
import com.sidspace.stary.account.domain.usecase.GetFoldersCountUseCase
import com.sidspace.stary.account.domain.usecase.GetBookmarkMoviesUseCase


import com.sidspace.stary.ui.mapper.toMovieUiLight
import com.sidspace.stary.ui.model.ResultData


import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AccountViewModel @Inject constructor(
    private val getViewedMoviesUseCase: GetViewedMoviesUseCase,
    private val getBookmarkMoviesUseCase: GetBookmarkMoviesUseCase,
    private val getFoldersCountUseCase: GetFoldersCountUseCase,
    private val googleSignInClient: GoogleSignInClient,
    private val getGoogleAccount: GoogleSignInAccount?

) : ViewModel() {

    private val _state = MutableStateFlow(AccountState())
    val state: StateFlow<AccountState> = _state.asStateFlow()

    private val _effect = MutableSharedFlow<AccountEffect>()
    val effect: SharedFlow<AccountEffect> = _effect.asSharedFlow()

    init {
        getViewedAndBookmarkMovies()
        initAccount()
    }

    fun initAccount() {
        if (getGoogleAccount != null) {
            _state.update { it.copy(account = getGoogleAccount) }
        }
    }

    fun getViewedAndBookmarkMovies() {



        viewModelScope.launch {

            combine(
                getViewedMoviesUseCase(),
                getBookmarkMoviesUseCase(),
                getFoldersCountUseCase()
            ) { viewedResult, bookmarkResult, foldersResult ->
                Triple(
                    viewedResult,
                    bookmarkResult,
                    foldersResult
                )
            }.collect { (viewed, bookmark, folders) ->

                if (viewed is Result.Success) {
                    _state.update {
                        it.copy(
                            countViewed = viewed.data.size,
                            resultAccountViewed = ResultData.Success(
                                viewed.data.take(10).map {
                                    it.toMovieUiLight()
                                })
                        )
                    }



                    updateHintState()
                }

                if (bookmark is Result.Success) {
                    _state.update {
                        it.copy(
                            countBookmark = bookmark.data.size,
                            resultAccountBookmark = ResultData.Success(bookmark.data.take(10).map{
                                it.toMovieUiLight()
                            })
                        )
                    }


                    updateHintState()
                }

                if (folders is Result.Success) {
                    _state.update { it.copy(countFolders = folders.data) }
                }


            }

        }
    }

    fun updateHintState() {
        if (_state.value.countViewed == 0 && _state.value.countBookmark == 0) {

            _state.update { it.copy(isShowEmptyHint = true) }
        } else {
            _state.update { it.copy(isShowEmptyHint = false) }
        }
    }


    fun onIntent(intent: AccountIntent) {
        when (val data = intent) {
            AccountIntent.ToFoldersScreen -> toCollectionScreen()
            is AccountIntent.ToMovieScreen -> {
                toMovieScreen(data.id)
            }

            AccountIntent.ToBookmarkScreen -> toBookmarkScreen()
            AccountIntent.ToViewedScreen -> toViewedScreen()
            is AccountIntent.SaveScrollBookmark -> {
                _state.update {
                    it.copy(
                        scrollBookmark = ScrollState(
                            data.scrollIndex,
                            data.scrollOffSet
                        )
                    )
                }
            }

            is AccountIntent.SaveScrollViewed -> {
                _state.update {
                    it.copy(
                        scrollViewed = ScrollState(
                            data.scrollIndex,
                            data.scrollOffSet
                        )
                    )
                }
            }

            AccountIntent.ToErrorScreen -> {
                toErrorScreen()
            }

            AccountIntent.OnGoogleSignInClick -> {

                val intent = googleSignInClient.signInIntent
                googleSignIn(intent)

            }

            is AccountIntent.OnSignInResult -> {
                _state.update { it.copy(account = intent.account) }
            }

            is AccountIntent.SetGoogleAccountSheetShown -> {
                _state.update { it.copy(isGoogleSheetShown = intent.shown) }
            }

            AccountIntent.SignOut -> {
                googleSignInClient.signOut()
                _state.update { it.copy(account = null) }
            }
        }
    }

    private fun toCollectionScreen() {
        viewModelScope.launch {
            _effect.emit(AccountEffect.ToFoldersScreen)
        }
    }

    private fun toErrorScreen() {
        viewModelScope.launch {
            _effect.emit(AccountEffect.ToErrorScreen)
        }
    }

    private fun toMovieScreen(id: Long) {
        viewModelScope.launch {
            _effect.emit(AccountEffect.ToMovieScreen(id))
        }
    }

    private fun toViewedScreen() {
        viewModelScope.launch {
            _effect.emit(AccountEffect.ToViewedScreen)
        }
    }

    private fun googleSignIn(intent: Intent) {
        viewModelScope.launch {
            _effect.emit(AccountEffect.LaunchGoogle(intent))
        }
    }

    private fun toBookmarkScreen() {
        viewModelScope.launch {
            _effect.emit(AccountEffect.ToBookmarkScreen)
        }
    }

}