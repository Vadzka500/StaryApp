package com.example.navwithapinothing_2.features.screen.AccountScreen


import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.moviesapi.models.movie.MovieDTO
import com.example.navwithapinothing_2.R
import com.example.navwithapinothing_2.data.Result
import com.example.navwithapinothing_2.data.ResultDb
import com.example.navwithapinothing_2.features.screen.MoviesListScreen.InitRow
import com.example.navwithapinothing_2.features.screen.MoviesListScreen.MovieCard
import com.example.navwithapinothing_2.features.screen.PersonScreen.ShimmerMovies
import com.example.navwithapinothing_2.features.theme.Purple40
import com.example.navwithapinothing_2.features.theme.poppinsFort
import kotlinx.coroutines.flow.collectLatest

/**
 * @Author: Vadim
 * @Date: 24.04.2025
 */

@Composable
fun AccountScreen(
    modifier: Modifier = Modifier,
    onClickFolders: () -> Unit,
    onSelectMovie: (id: Long) -> Unit,
    accountViewModel: AccountViewModel = hiltViewModel(),
    toViewedScreen: () -> Unit,
    toBookmarkScreen: () -> Unit
) {

    val state = accountViewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        accountViewModel.effect.collectLatest { effect ->
            when (val data = effect) {
                AccountEffect.ToFoldersScreen -> onClickFolders()
                is AccountEffect.ToMovieScreen -> {
                    onSelectMovie(data.id)
                }

                AccountEffect.ToBookmarkScreen -> toBookmarkScreen()
                AccountEffect.ToViewedScreen -> toViewedScreen()
            }
        }
    }

    LazyColumn(modifier = modifier) {
        item {
            GoogleButton()

            Spacer(
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .height(1.dp)
                    .background(color = Color.Gray)
            )

            ListOfCollectionBlock(
                state = state
            )

            if (state.value.isShowEmptyHint) {
                SadHint()
            }



            AnimatedContent(targetState = state.value.resultAccountBookmark) { states ->

                when (val data = states) {


                    is ResultAccountData.Error -> {

                    }

                    ResultAccountData.Loading -> {
                        ShimmerMovies()
                    }

                    ResultAccountData.None -> {

                    }

                    is ResultAccountData.Success -> {
                        if (!(data.data as List<*>).isEmpty())
                            LatestBookmarkBlock(list = data.data, state = state)
                    }
                }

            }

            AnimatedContent(targetState = state.value.resultAccountViewed) { states ->

                when (val data = states) {


                    is ResultAccountData.Error -> {

                    }

                    ResultAccountData.Loading -> {
                        ShimmerMovies()
                    }

                    ResultAccountData.None -> {

                    }

                    is ResultAccountData.Success -> {
                        if (!(data.data as List<*>).isEmpty())
                            LatestViewedBlock(list = data.data, state = state)
                    }
                }

            }


        }

    }
}

@Composable
fun SadHint(modifier: Modifier = Modifier) {


    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 128.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            modifier = Modifier.size(48.dp),
            painter = painterResource(R.drawable.empty_result),
            contentDescription = "sad"
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            "Вы еще ничего не добавили",
            fontWeight = FontWeight.SemiBold,
            fontFamily = poppinsFort,
            fontSize = 16.sp,
        )
    }

}

@Composable
fun ListOfCollectionBlock(
    modifier: Modifier = Modifier,
    accountViewModel: AccountViewModel = hiltViewModel(),
    state: State<AccountState>
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                accountViewModel.onIntent(AccountIntent.ToBookmarkScreen)
            }
            .padding(horizontal = 16.dp)
            .padding(vertical = 16.dp), verticalAlignment = Alignment.CenterVertically
    ) {

        Icon(
            painter = painterResource(
                R.drawable.ic_bookmark_fill
            ),
            contentDescription = null,
            modifier = Modifier
                .size(25.dp),
            tint = MaterialTheme.colorScheme.onSecondaryContainer
        )

        Text(
            text = "Закладки",
            modifier = Modifier.padding(start = 16.dp),
            fontWeight = FontWeight.SemiBold,
            fontFamily = poppinsFort,
            fontSize = 14.sp
        )

        Spacer(modifier = Modifier.weight(1f))

        Text(
            text = state.value.countBookmark.toString(), fontWeight = FontWeight.SemiBold,
            fontFamily = poppinsFort,
            fontSize = 14.sp
        )
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                accountViewModel.onIntent(AccountIntent.ToViewedScreen)
            }
            .padding(horizontal = 16.dp)
            .padding(vertical = 16.dp), verticalAlignment = Alignment.CenterVertically
    ) {

        Icon(
            painter = painterResource(
                R.drawable.ic_visibility_fill
            ),
            contentDescription = null,
            modifier = Modifier
                .size(25.dp),
            tint = MaterialTheme.colorScheme.onSecondaryContainer
        )

        Text(
            text = "Просмотрено",
            modifier = Modifier.padding(start = 16.dp),
            fontWeight = FontWeight.SemiBold,
            fontFamily = poppinsFort,
            fontSize = 14.sp
        )

        Spacer(modifier = Modifier.weight(1f))

        Text(
            text = state.value.countViewed.toString(), fontWeight = FontWeight.SemiBold,
            fontFamily = poppinsFort,
            fontSize = 14.sp
        )
    }


    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { accountViewModel.onIntent(AccountIntent.ToFoldersScreen) }
            .padding(horizontal = 16.dp)
            .padding(vertical = 16.dp), verticalAlignment = Alignment.CenterVertically
    ) {

        Icon(
            painter = painterResource(
                R.drawable.ic_folder
            ),
            contentDescription = null,
            modifier = Modifier
                .size(25.dp),
            tint = MaterialTheme.colorScheme.onSecondaryContainer
        )

        Text(
            text = "Мои коллекции",
            modifier = Modifier.padding(start = 16.dp),
            fontWeight = FontWeight.SemiBold,
            fontFamily = poppinsFort,
            fontSize = 14.sp
        )

        Spacer(modifier = Modifier.weight(1f))

        Text(
            text = state.value.countFolders.toString(), fontWeight = FontWeight.SemiBold,
            fontFamily = poppinsFort,
            fontSize = 14.sp
        )


    }
}

@Composable
fun LatestViewedBlock(
    modifier: Modifier = Modifier,
    list: List<MovieDTO>,
    accountViewModel: AccountViewModel = hiltViewModel(),
    state: State<AccountState>
) {
    val listState = rememberLazyListState()

    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(end = 16.dp)
                .padding(start = 16.dp)
                .padding(vertical = 10.dp)
                .padding(top = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = Modifier
                    .padding(end = 16.dp)
                    .weight(1f),
                text = "Последние просмотренные",
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

            itemsIndexed(list, key = { _, it -> it.id!! }) { index, item ->

                MovieCard(
                    item = item,
                    index = index,
                    onSelectMovie = { accountViewModel.onIntent(AccountIntent.ToMovieScreen(item.id!!)) }
                )

            }
        }
    }

    LaunchedEffect(Unit) {
        listState.scrollToItem(
            state.value.scrollViewed.scrollIndex,
            state.value.scrollViewed.scrollOffSet
        )
    }

    DisposableEffect(Unit) {
        onDispose {
            accountViewModel.onIntent(AccountIntent.SaveScrollViewed(scrollIndex =  listState.firstVisibleItemIndex, scrollOffSet = listState.firstVisibleItemScrollOffset))
        }
    }
}

@Composable
fun LatestBookmarkBlock(
    modifier: Modifier = Modifier,
    list: List<MovieDTO>,
    accountViewModel: AccountViewModel = hiltViewModel(),
    state: State<AccountState>
) {
    val listState = rememberLazyListState()

    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(end = 16.dp)
                .padding(start = 16.dp)
                .padding(vertical = 10.dp)
                .padding(top = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = Modifier
                    .padding(end = 16.dp)
                    .weight(1f),
                text = "Последние в закладках",
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

            itemsIndexed(list, key = { _, it -> it.id!! }) { index, item ->

                MovieCard(
                    item = item,
                    index = index,
                    onSelectMovie = { accountViewModel.onIntent(AccountIntent.ToMovieScreen(item.id!!)) }
                )

            }
        }

        LaunchedEffect(Unit) {
            listState.scrollToItem(
                state.value.scrollViewed.scrollIndex,
                state.value.scrollViewed.scrollOffSet
            )
        }

        DisposableEffect(Unit) {
            onDispose {
                accountViewModel.onIntent(AccountIntent.SaveScrollViewed(scrollIndex =  listState.firstVisibleItemIndex, scrollOffSet = listState.firstVisibleItemScrollOffset))
            }
        }
    }
}

@Composable
private fun GoogleButton(modifier: Modifier = Modifier) {
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()

            .padding(horizontal = 16.dp)
            .padding(vertical = 24.dp),
        onClick = {},
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .padding(vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(R.drawable.google_ic),
                contentDescription = null,
                modifier = Modifier
                    .width(48.dp)
                    .height(48.dp)
            )

            Text(
                text = "Войдите в аккаунт",
                modifier = Modifier.padding(start = 32.dp),
                fontWeight = FontWeight.SemiBold,
                fontFamily = poppinsFort,
                fontSize = 14.sp,
            )
        }
    }
}

