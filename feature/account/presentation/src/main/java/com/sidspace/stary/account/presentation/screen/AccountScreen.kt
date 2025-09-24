@file:Suppress("TooManyFunctions")

package com.sidspace.stary.account.presentation.screen

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.common.api.ApiException
import com.sidspace.stary.account.presentation.R
import com.sidspace.stary.ui.MovieCardHorizontal
import com.sidspace.stary.ui.ShimmerMovies
import com.sidspace.stary.ui.model.MovieLightUi
import com.sidspace.stary.ui.model.ResultData
import com.sidspace.stary.ui.uikit.poppinsFort
import kotlinx.coroutines.flow.collectLatest


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountScreen(
    modifier: Modifier = Modifier,
    accountViewModel: AccountViewModel = hiltViewModel(),
    onClickFolders: () -> Unit,
    onSelectMovie: (id: Long) -> Unit,
    toViewedScreen: () -> Unit,
    toBookmarkScreen: () -> Unit,
    toErrorScreen: () -> Unit
) {

    val state by accountViewModel.state.collectAsState()

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->

        val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)

        runCatching {
            val account = task.getResult(ApiException::class.java)
            accountViewModel.onIntent(AccountIntent.OnSignInResult(account))
        }.onFailure { error ->
            accountViewModel.onIntent(AccountIntent.OnSignInResult(null))
            error.printStackTrace()
        }
    }

    LaunchedEffect(Unit) {
        accountViewModel.effect.collectLatest { effect ->
            when (val data = effect) {
                AccountEffect.ToFoldersScreen -> onClickFolders()
                is AccountEffect.ToMovieScreen -> {
                    onSelectMovie(data.id)
                }

                AccountEffect.ToBookmarkScreen -> toBookmarkScreen()
                AccountEffect.ToViewedScreen -> toViewedScreen()
                AccountEffect.ToErrorScreen -> toErrorScreen()
                is AccountEffect.LaunchGoogle -> {
                    launcher.launch(data.intent)
                }
            }
        }
    }

    AccountContent(state = state, googleSignInClick = {
        accountViewModel.onIntent(AccountIntent.OnGoogleSignInClick)
    }, setGoogleSignInSheet = {
        accountViewModel.onIntent(AccountIntent.SetGoogleAccountSheetShown(it))
    }, toErrorScreen = {
        accountViewModel.onIntent(AccountIntent.ToErrorScreen)
    }, signOut = {
        accountViewModel.onIntent(AccountIntent.SignOut)
    }, modifier = modifier)


}

@Composable
fun AccountContent(
    state: AccountState,
    googleSignInClick: () -> Unit,
    signOut: () -> Unit,
    setGoogleSignInSheet: (Boolean) -> Unit,
    toErrorScreen: () -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .background(color = MaterialTheme.colorScheme.background)
            .animateContentSize()
    ) {
        item {
            if (state.account == null) {
                GoogleButton(
                    signIn = {
                        googleSignInClick()
                    }
                )
            } else {
                InitGoogleAccount(
                    setVisibleSheet = {
                        setGoogleSignInSheet(true)
                    },
                    account = state.account,
                )
            }

            Spacer(
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .height(1.dp)
                    .background(color = Color.Gray)
            )

            ListOfCollectionBlock(
                countBookmark = state.countBookmark,
                countViewed = state.countViewed,
                countFolder = state.countFolders,
            )

            if (state.isShowEmptyHint) {
                SadHint()
            }

            BookmarkContent(state = state, toErrorScreen = toErrorScreen)

            ViewedContent(state = state, toErrorScreen = toErrorScreen)

        }

    }

    if (state.isGoogleSheetShown) {
        InitGoogleAccountSheet(hideSheet = {
            setGoogleSignInSheet(false)
        }, signOut = {
            signOut()
        })
    }
}

@Composable
fun ViewedContent(state: AccountState, toErrorScreen:() -> Unit) {
    AnimatedContent(targetState = state.resultAccountViewed) { states ->

        when (val data = states) {


            is ResultData.Error -> {
                toErrorScreen()
            }

            ResultData.Loading -> {
                ShimmerMovies()
            }

            ResultData.None -> Unit

            is ResultData.Success -> {
                if (!(data.data as List<*>).isEmpty())
                    LatestViewedBlock(
                        list = data.data,
                        scrollIndex = state.scrollViewed.scrollIndex,
                        scrollOffSet = state.scrollViewed.scrollOffSet
                    )
            }
        }

    }
}

@Composable
fun BookmarkContent(state: AccountState, toErrorScreen:() -> Unit) {
    AnimatedContent(targetState = state.resultAccountBookmark) { states ->

        when (val data = states) {

            is ResultData.Error -> {
                toErrorScreen()
            }

            ResultData.Loading -> {
                ShimmerMovies()
            }

            ResultData.None -> Unit

            is ResultData.Success -> {
                if (!data.data.isEmpty())
                    LatestBookmarkBlock(
                        list = data.data,
                        scrollIndex = state.scrollBookmark.scrollIndex,
                        scrollOffSet = state.scrollBookmark.scrollOffSet
                    )
            }
        }

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InitGoogleAccountSheet(
    modifier: Modifier = Modifier,
    hideSheet: () -> Unit,
    signOut: () -> Unit,
) {
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true,

        )

    ModalBottomSheet(
        sheetState = sheetState,
        dragHandle = null,
        onDismissRequest = {
            hideSheet()
        }) {

        Column(modifier = modifier.padding(vertical = 16.dp)) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        signOut()
                        hideSheet()
                    }
                    .padding(horizontal = 16.dp)
                    .padding(vertical = 16.dp)) {
                Icon(
                    imageVector = Icons.AutoMirrored.Default.Logout,
                    contentDescription = stringResource(R.string.log_out)
                )
                Text(
                    modifier = Modifier.padding(start = 16.dp),
                    text = stringResource(R.string.log_out_rus), fontWeight = FontWeight.SemiBold,
                    fontFamily = poppinsFort,
                    fontSize = 16.sp,
                )
            }
        }

    }
}

@Composable
fun SadHint(modifier: Modifier = Modifier) {

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 128.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            modifier = Modifier.size(48.dp),
            painter = painterResource(com.sidspace.stary.ui.R.drawable.empty_result),
            contentDescription = null
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            stringResource(R.string.hint_empty_str),
            fontWeight = FontWeight.SemiBold,
            fontFamily = poppinsFort,
            fontSize = 16.sp,
        )
    }

}

@Composable
fun ListOfCollectionBlock(
    countBookmark: Int,
    countViewed: Int,
    countFolder: Long,
    accountViewModel: AccountViewModel = hiltViewModel(),
) {

    AccountRowContent(
        idImg = com.sidspace.stary.ui.R.drawable.ic_bookmark_fill,
        idStr = R.string.bookmark_str,
        count = countBookmark,
        onClick = {
            accountViewModel.onIntent(AccountIntent.ToBookmarkScreen)
        }
    )
    AccountRowContent(
        idImg = com.sidspace.stary.ui.R.drawable.ic_visibility_fill,
        idStr = R.string.viewed_str,
        count = countViewed,
        onClick = {
            accountViewModel.onIntent(AccountIntent.ToViewedScreen)
        }
    )
    AccountRowContent(
        idImg = com.sidspace.stary.ui.R.drawable.ic_folder,
        idStr = R.string.my_collections_str,
        count = countFolder.toInt(),
        onClick = {
            accountViewModel.onIntent(AccountIntent.ToFoldersScreen)
        }
    )

}

@Composable
fun AccountRowContent(@DrawableRes idImg: Int, @StringRes idStr: Int, count: Int, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onClick()
            }
            .padding(horizontal = 16.dp)
            .padding(vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Icon(
            painter = painterResource(
                idImg
            ),
            contentDescription = null,
            modifier = Modifier
                .size(25.dp),
            tint = MaterialTheme.colorScheme.onSecondaryContainer
        )

        Text(
            text = stringResource(idStr),
            modifier = Modifier.padding(start = 16.dp),
            fontWeight = FontWeight.SemiBold,
            fontFamily = poppinsFort,
            fontSize = 14.sp
        )

        Spacer(modifier = Modifier.weight(1f))

        Text(
            text = count.toString(),
            fontWeight = FontWeight.SemiBold,
            fontFamily = poppinsFort,
            fontSize = 14.sp
        )
    }

}


@Composable
fun LatestViewedBlock(
    list: List<MovieLightUi>,
    scrollIndex: Int,
    scrollOffSet: Int,
    modifier: Modifier = Modifier,
    accountViewModel: AccountViewModel = hiltViewModel(),

    ) {
    val listState = rememberLazyListState()

    Column {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(end = 16.dp)
                .padding(start = 16.dp)
                .padding(top = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = Modifier
                    .padding(end = 16.dp)
                    .weight(1f),
                text = stringResource(R.string.last_viewed),
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
            )
        }

        LazyRow(
            state = listState,
            modifier = Modifier.padding(top = 10.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(horizontal = 16.dp)
        ) {

            itemsIndexed(list, key = { _, item -> item.id }) { index, item ->

                MovieCardHorizontal(
                    id = item.id,
                    name = item.name,
                    enName = item.enName,
                    previewUrl = item.previewUrl,
                    score = item.score,
                    onSelectMovie = { accountViewModel.onIntent(AccountIntent.ToMovieScreen(item.id)) }
                )

            }
        }
    }

    LaunchedEffect(Unit) {
        listState.scrollToItem(
            scrollIndex,
            scrollOffSet
        )
    }

    DisposableEffect(Unit) {
        onDispose {
            accountViewModel.onIntent(
                AccountIntent.SaveScrollViewed(
                    scrollIndex = listState.firstVisibleItemIndex,
                    scrollOffSet = listState.firstVisibleItemScrollOffset
                )
            )
        }
    }
}


@Composable
fun LatestBookmarkBlock(
    scrollIndex: Int,
    scrollOffSet: Int,
    list: List<MovieLightUi>,
    modifier: Modifier = Modifier,
    accountViewModel: AccountViewModel = hiltViewModel(),

    ) {
    val listState = rememberLazyListState()

    Column {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(end = 16.dp)
                .padding(start = 16.dp)
                .padding(top = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = Modifier
                    .padding(end = 16.dp)
                    .weight(1f),
                text = stringResource(R.string.last_bookmark),
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
            )
        }

        LazyRow(
            state = listState,
            modifier = Modifier.padding(top = 10.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(horizontal = 16.dp)
        ) {

            itemsIndexed(list, key = { _, item -> item.id }) { index, item ->

                MovieCardHorizontal(
                    id = item.id,
                    name = item.name,
                    enName = item.enName,
                    previewUrl = item.previewUrl,
                    score = item.score,
                    onSelectMovie = { accountViewModel.onIntent(AccountIntent.ToMovieScreen(item.id)) }
                )

            }
        }

        LaunchedEffect(Unit) {
            listState.scrollToItem(
                scrollIndex,
                scrollOffSet
            )
        }

        DisposableEffect(Unit) {
            onDispose {
                accountViewModel.onIntent(
                    AccountIntent.SaveScrollViewed(
                        scrollIndex = listState.firstVisibleItemIndex,
                        scrollOffSet = listState.firstVisibleItemScrollOffset
                    )
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun InitGoogleAccount(
    account: GoogleSignInAccount,
    modifier: Modifier = Modifier,
    setVisibleSheet: () -> Unit,


    ) {

    ElevatedCard(
        modifier = modifier
            .fillMaxWidth()

            .padding(horizontal = 16.dp)
            .padding(vertical = 24.dp),
        onClick = {
            setVisibleSheet()
        },
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .padding(vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = account.photoUrl,
                contentDescription = null,
                modifier = Modifier
                    .width(64.dp)
                    .height(64.dp)
                    .clip(CircleShape)
            )

            Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                Text(
                    text = stringResource(R.string.hi_str) + " ${account.displayName}",
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    fontFamily = poppinsFort,
                    fontSize = 16.sp,
                )

                Text(
                    text = account.email.toString(),
                    fontWeight = FontWeight.Medium,
                    fontFamily = poppinsFort,
                    fontSize = 12.sp,
                )
            }
        }
    }


}

@Composable
private fun GoogleButton(
    modifier: Modifier = Modifier,
    signIn: () -> Unit
) {
    ElevatedCard(
        modifier = modifier
            .fillMaxWidth()

            .padding(horizontal = 16.dp)
            .padding(vertical = 24.dp),
        onClick = {
            signIn()
        },
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .padding(vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(com.sidspace.stary.ui.R.drawable.google_ic),
                contentDescription = null,
                modifier = Modifier
                    .width(64.dp)
                    .height(64.dp)
            )

            Text(
                text = stringResource(R.string.sign_in_str),
                modifier = Modifier.padding(horizontal = 16.dp),
                fontWeight = FontWeight.SemiBold,
                fontFamily = poppinsFort,
                fontSize = 16.sp,
            )
        }
    }
}

