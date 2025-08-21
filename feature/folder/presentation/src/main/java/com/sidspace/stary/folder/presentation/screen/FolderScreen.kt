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

import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.ui.FilterSection
import com.example.ui.InitList
import com.example.ui.R
import com.example.ui.ShimmerGridList
import com.example.ui.model.ResultData
import com.example.ui.uikit.poppinsFort
import kotlinx.coroutines.flow.collectLatest



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

    Box(modifier = modifier.fillMaxSize().padding(top = 5.dp)) {

        if (state.value.folder?.imageResName != null) {
            Image(
                painter = painterResource(
                    LocalContext.current.resources.getIdentifier(
                        state.value.folder!!.imageResName,
                        "drawable",
                        LocalContext.current.packageName
                    )
                ),
                contentDescription = "",
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
                    ShimmerGridList(modifier = Modifier.padding(top = 60.dp))
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
                contentDescription = "",
                modifier = Modifier
                    .size(40.dp)
                    .clip(
                        CircleShape
                    )
                    .clickable {
                        folderViewModel.onIntent(FolderIntent.OnBack)
                    }
                    .padding(8.dp))

            Row(modifier = Modifier.weight(1f)  .padding(end = 64.dp)) {


                Text(
                    state.value.folder?.name ?: "",
                    modifier = Modifier
                        .padding(start = 16.dp),
                    fontWeight = FontWeight.Bold,
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
                painter = painterResource(R.drawable.settings),
                contentDescription = "",
                modifier = Modifier
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
                contentDescription = "Удалить коллекцию",
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
            true,
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
            title = { Text("Удалить коллекцию?") },
            text = { Text("Вы уверены, что хотите удалить коллекцию?") },
            confirmButton = {
                TextButton(onClick = { folderViewModel.onIntent(FolderIntent.RemoveFolder) }) {
                    Text("Удалить")
                }
            },
            dismissButton = {
                TextButton(onClick = { folderViewModel.onIntent(FolderIntent.HideDialog) }) {
                    Text("Отмена")
                }
            }
        )
    }


}