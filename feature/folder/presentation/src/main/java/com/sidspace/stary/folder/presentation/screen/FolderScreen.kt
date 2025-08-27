package com.sidspace.stary.folder.presentation.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Notes
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow

import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.sidspace.stary.ui.FilterSection
import com.sidspace.stary.ui.InitList
import com.sidspace.stary.ui.R
import com.sidspace.stary.ui.ShimmerGridList
import com.sidspace.stary.ui.model.ResultData
import com.sidspace.stary.ui.uikit.poppinsFort

import kotlinx.coroutines.flow.collectLatest
import androidx.compose.ui.platform.LocalResources
import androidx.compose.ui.res.stringResource


@Composable
fun UserCollectionScreen(
    folderId: Long,
    onBack:() -> Unit,
    onSelectMovie:(Long) -> Unit,
    toErrorScreen:() -> Unit,
    modifier: Modifier = Modifier,
    folderViewModel: FolderViewModel = hiltViewModel()
) {
    val state = folderViewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        folderViewModel.onIntent(FolderIntent.LoadFolder(folderId))

        folderViewModel.effect.collectLatest { effect ->
            when(effect){
                FolderEffect.OnBack -> {
                    onBack()
                }
                is FolderEffect.OnSelectedMovie -> {
                    onSelectMovie(effect.id)
                }

                FolderEffect.ToErrorScreen -> {
                    toErrorScreen()
                }
            }
        }
    }

    Box(modifier = modifier
        .fillMaxSize()
        .padding(top = 5.dp)) {

        if (state.value.folder?.imageResName != null) {
            Image(
                painter = painterResource(
                    LocalResources.current.getIdentifier(
                        state.value.folder!!.imageResName,
                        "drawable",
                        LocalContext.current.packageName
                    )
                ),
                contentDescription = null,
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .size(380.dp)
                    .offset(y = 70.dp, x = (-40).dp)
                    .rotate(40f)
                    .alpha(0.3f)
            )


            when (val data = state.value.list) {

                is ResultData.Error -> {
                    folderViewModel.onIntent(FolderIntent.OnError)
                }

                ResultData.Loading -> {
                    ShimmerGridList(modifier = Modifier.padding(top = 60.dp), state.value.countMovies)
                }

                is ResultData.Success -> {
                    InitList(
                        modifier = Modifier.padding(top = 60.dp),
                        list = data.data,
                        onClick = { id ->
                            folderViewModel.onIntent(FolderIntent.OnSelectMovie(id))

                        },
                        viewType = state.value.viewMode
                    )
                }

                ResultData.None -> {

                }
            }
        }

        val topHeight = 50.dp

        Row(
            modifier = Modifier
                .height(topHeight)
                .padding(horizontal = 12.dp)
            , verticalAlignment = Alignment.CenterVertically

        ) {

            Icon(
                imageVector = Icons.Default.ArrowBackIosNew,
                contentDescription = null,
                modifier = Modifier
                    .size(40.dp)
                    .clip(
                        CircleShape
                    )
                    .clickable {
                        folderViewModel.onIntent(FolderIntent.OnBack)
                    }
                    .padding(8.dp))

            Row(modifier = Modifier
                .weight(1f)
                .padding(end = 42.dp), verticalAlignment = Alignment.CenterVertically) {


                Text(
                    state.value.folder?.name ?: "",
                    modifier = Modifier
                        .padding(start = 16.dp),
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    fontFamily = poppinsFort,
                    fontSize = 24.sp,
                )

                Text(
                    state.value.countMovies.toString(),
                    modifier = Modifier.padding(start = 8.dp),
                    fontWeight = FontWeight.SemiBold,
                    fontFamily = poppinsFort,
                    fontSize = 24.sp,
                )
            }

            Icon(
                imageVector = Icons.AutoMirrored.Default.Notes,
                contentDescription = null,
                modifier = Modifier
                    .padding(end = 8.dp)
                    .size(40.dp)
                    .clip(
                        CircleShape
                    )
                    .clickable {
                        folderViewModel.onIntent(FolderIntent.IsShowFilters(!state.value.isShowFilter))
                    }
                    .padding(8.dp))

            Icon(
                imageVector = Icons.Default.DeleteForever,
                contentDescription = stringResource(com.sidspace.stary.folder.presentation.R.string.remove_collection),
                tint = Color.Red,
                modifier = Modifier
                    .size(40.dp)
                    .clip(
                        CircleShape
                    )
                    .clickable {
                        folderViewModel.onIntent(FolderIntent.ShowDialog)
                    }
                    .padding(8.dp))

        }



        FilterSection(
            modifier = Modifier.padding(top = topHeight + 5.dp),
            isVisibleFilter = state.value.isShowFilter,
            viewType = state.value.viewMode,
            isShowSort = true,
            onHideFilter = {
                folderViewModel.onIntent(FolderIntent.IsShowFilters(false))
            },
            setGridView = {
                folderViewModel.onIntent(FolderIntent.SetGridView)
            },
            setListView = {
                folderViewModel.onIntent(FolderIntent.SetListView)
            }, sortType = state.value.sortType, sortDirection = state.value.sortDirection,
            toggleSortDirection = {
                folderViewModel.onIntent(FolderIntent.ToggleSortDirection)
            },
            sortList = {
                folderViewModel.onIntent(FolderIntent.SortMovies)
            }, setSortType = {
                folderViewModel.onIntent(FolderIntent.SetSortType(it))
            }
        )
    }

    if (state.value.isShowDialog) {
        AlertDialog(
            onDismissRequest = { folderViewModel.onIntent(FolderIntent.HideDialog) },
            title = { Text(stringResource(com.sidspace.stary.folder.presentation.R.string.remove_collection) + "?") },
            text = { Text(stringResource(com.sidspace.stary.folder.presentation.R.string.remove_collection_approve)) },
            confirmButton = {
                TextButton(onClick = { folderViewModel.onIntent(FolderIntent.RemoveFolder) }) {
                    Text(stringResource(com.sidspace.stary.folder.presentation.R.string.remove))
                }
            },
            dismissButton = {
                TextButton(onClick = { folderViewModel.onIntent(FolderIntent.HideDialog) }) {
                    Text(stringResource(com.sidspace.stary.folder.presentation.R.string.cancel))
                }
            }
        )
    }


}