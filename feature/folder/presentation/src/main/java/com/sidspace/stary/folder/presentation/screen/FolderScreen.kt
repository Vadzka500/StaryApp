package com.sidspace.stary.folder.presentation.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
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
import androidx.compose.ui.platform.LocalResources
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.sidspace.stary.ui.FilterSection
import com.sidspace.stary.ui.FilterStateCallback
import com.sidspace.stary.ui.InitList
import com.sidspace.stary.ui.ShimmerGridList
import com.sidspace.stary.ui.model.ResultData
import com.sidspace.stary.ui.uikit.poppinsFort
import kotlinx.coroutines.flow.collectLatest


@Composable
fun UserCollectionScreen(
    folderId: Long,
    onBack: () -> Unit,
    onSelectMovie: (Long) -> Unit,
    toErrorScreen: () -> Unit,
    modifier: Modifier = Modifier,
    folderViewModel: FolderViewModel = hiltViewModel()
) {
    val state = folderViewModel.state.collectAsState()

    val topHeight = 50.dp

    LaunchedEffect(Unit) {
        folderViewModel.onIntent(FolderIntent.LoadFolder(folderId))

        folderViewModel.effect.collectLatest { effect ->
            when (effect) {
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

    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(top = 5.dp)
    ) {

        if (state.value.folder?.imageResName != null) {

            FolderImage(state.value.folder!!.imageResName!!)


            when (val data = state.value.list) {

                is ResultData.Error -> {
                    folderViewModel.onIntent(FolderIntent.OnError)
                }

                ResultData.Loading -> {
                    ShimmerGridList(
                        modifier = Modifier.padding(top = 60.dp),
                        state.value.countMovies
                    )
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



        FolderToolbar(
            topHeight = topHeight,
            countMovies = state.value.countMovies,
            name = state.value.folder?.name,
            onBack = {
                folderViewModel.onIntent(FolderIntent.OnBack)
            },
            showFilter = {
                folderViewModel.onIntent(FolderIntent.IsShowFilters(!state.value.isShowFilter))
            },
            showDialog = {
                folderViewModel.onIntent(FolderIntent.ShowDialog)
            }
        )


        val filterStateCallback = FilterStateCallback(
            onHideFilter = {
                folderViewModel.onIntent(FolderIntent.IsShowFilters(false))
            },
            setGridView = {
                folderViewModel.onIntent(FolderIntent.SetGridView)
            },
            setListView = {
                folderViewModel.onIntent(FolderIntent.SetListView)
            },
            setSortType = {
                folderViewModel.onIntent(FolderIntent.SetSortType(it))
            },
            sortList = {
                folderViewModel.onIntent(FolderIntent.SortMovies)
            },
            toggleSortDirection = {
                folderViewModel.onIntent(FolderIntent.ToggleSortDirection)
            }
        )

        FilterSection(
            modifier = Modifier.padding(top = topHeight + 5.dp),
            isVisibleFilter = state.value.isShowFilter,
            viewType = state.value.viewMode,
            isShowSort = true,
            filterStateCallback = filterStateCallback
        )
    }

    RemoveFolderDialog(state.value.isShowDialog, hideDialog = {
        folderViewModel.onIntent(FolderIntent.HideDialog)
    }, removeFolder = {
        folderViewModel.onIntent(FolderIntent.RemoveFolder)
    })


}

@Composable
fun FolderToolbar(
    topHeight: Dp,
    name: String?,
    countMovies: Int,
    onBack: () -> Unit,
    showFilter: () -> Unit,
    showDialog: () -> Unit
) {
    Row(
        modifier = Modifier
            .height(topHeight)
            .padding(horizontal = 12.dp),
        verticalAlignment = Alignment.CenterVertically

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
                    onBack()
                }
                .padding(8.dp)
        )

        Row(
            modifier = Modifier
                .weight(1f)
                .padding(end = 42.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {


            Text(
                name ?: "",
                modifier = Modifier
                    .padding(start = 16.dp),
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                fontFamily = poppinsFort,
                fontSize = 24.sp,
            )

            Text(
                countMovies.toString(),
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
                    showFilter()
                }
                .padding(8.dp)
        )

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
                    showDialog()
                }
                .padding(8.dp)
        )

    }
}

@Composable
fun FolderImage(url: String) {

    Box {
        Image(
            painter = painterResource(
                LocalResources.current.getIdentifier(
                    url,
                    "drawable",
                    LocalContext.current.packageName
                )
            ),
            contentDescription = null,
            modifier = Modifier
                .align(Alignment.BottomStart)
                .size(380.dp)
                .offset(y = 70.dp, x = (-40).dp)
                .rotate(FolderState.FOLDER_PICTURE_ROTATE)
                .alpha(FolderState.FOLDER_PICTURE_ALPHA)
        )
    }

}

@Composable
fun RemoveFolderDialog(isShowDialog: Boolean, hideDialog: () -> Unit, removeFolder: () -> Unit) {
    if (isShowDialog) {
        AlertDialog(
            onDismissRequest = { hideDialog() },
            title = { Text(stringResource(com.sidspace.stary.folder.presentation.R.string.remove_collection) + "?") },
            text = { Text(stringResource(com.sidspace.stary.folder.presentation.R.string.remove_collection_approve)) },
            confirmButton = {
                TextButton(onClick = { removeFolder() }) {
                    Text(stringResource(com.sidspace.stary.folder.presentation.R.string.remove))
                }
            },
            dismissButton = {
                TextButton(onClick = { hideDialog() }) {
                    Text(stringResource(com.sidspace.stary.folder.presentation.R.string.cancel))
                }
            }
        )
    }
}
