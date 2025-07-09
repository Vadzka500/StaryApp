package com.example.navwithapinothing_2.features.screen.FoldersScreen

import android.widget.Toast
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.navwithapinothing_2.R
import com.example.navwithapinothing_2.data.ResultDb
import com.example.navwithapinothing_2.database.models.FolderWithMovies
import com.example.navwithapinothing_2.features.screen.AccountScreen.AccountEffect
import com.example.navwithapinothing_2.features.screen.MovieViewModel
import com.example.navwithapinothing_2.features.theme.poppinsFort
import com.example.navwithapinothing_2.utils.FiltersUtil
import kotlinx.coroutines.flow.collectLatest

/**
 * @Author: Vadim
 * @Date: 16.05.2025
 */


@Composable
fun UserCollectionsScreen(
    modifier: Modifier = Modifier,
    foldersViewModel: FoldersViewModel = hiltViewModel(),
    onSelectFolder: (id: Long) -> Unit,
    onBack: () -> Unit
) {

    val state = foldersViewModel.state.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        foldersViewModel.effect.collectLatest { effect ->
            when (val data = effect) {
                FoldersEffect.OnBack -> {
                    onBack()
                }

                is FoldersEffect.ToFolderScreen -> {
                    onSelectFolder(data.id)
                }

                is FoldersEffect.ErrorToast -> {
                    Toast.makeText(context, data.str, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    val count = when (val result = state.value.filters) {
        is ResultFilterData.Success -> result.data.size
        else -> 0
    }


    Box(
        modifier = modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.BottomEnd
    ) {


        Column(modifier = Modifier.fillMaxSize()) {
            Row(
                modifier = Modifier.padding(start = 16.dp, top = 12.dp, bottom = 12.dp),
                verticalAlignment = Alignment.CenterVertically
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
                            foldersViewModel.onIntent(FoldersIntent.OnBack)
                        }
                        .padding(8.dp))


                Text(
                    "Мои коллекции",
                    modifier = Modifier.padding(start = 16.dp),
                    fontWeight = FontWeight.Bold,
                    fontFamily = poppinsFort,
                    fontSize = 24.sp,
                )

                Text(
                    count.toString(),
                    modifier = Modifier.padding(start = 8.dp),
                    fontWeight = FontWeight.SemiBold,
                    fontFamily = poppinsFort,
                    fontSize = 24.sp,
                )
            }

            ShowCollectionList(list = state.value.filters, onSelectFolder = { id ->
                foldersViewModel.onIntent(FoldersIntent.ToFolderScreen(id))
            })
        }

        FloatingActionButton(
            modifier = Modifier.padding(24.dp),
            onClick = {
                foldersViewModel.onIntent(FoldersIntent.ClickCreateFolder)
            },
        ) {
            Icon(Icons.Filled.Add, "Add collection")
        }
    }
    if (state.value.isShowBottomSheet) {
        ShowCreateFolderBottomSheet(state = state)
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShowCreateFolderBottomSheet(
    modifier: Modifier = Modifier,
    foldersViewModel: FoldersViewModel = hiltViewModel(),
    state: State<FoldersState>
) {
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true,
    )

    ModalBottomSheet(
        sheetState = sheetState,
        onDismissRequest = {
            foldersViewModel.onIntent(FoldersIntent.HideBottomSheet)
        }) {
        CreateSheetData(state = state)

    }
}

@Composable
fun CreateSheetData(
    modifier: Modifier = Modifier,
    state: State<FoldersState>,
    foldersViewModel: FoldersViewModel = hiltViewModel(),
) {

    val context = LocalContext.current
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(scrollState)
            .padding(horizontal = 16.dp)
    ) {
        TextField(
            modifier = Modifier.fillMaxWidth(),
            value = state.value.textFieldFolderValue,
            isError = state.value.isErrorEmptyName,
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = Color.Transparent,
                focusedContainerColor = Color.Transparent
            ),
            onValueChange = { foldersViewModel.onIntent(FoldersIntent.UpdateNameFolder(it)) },
            label = {
                Text(
                    "Название коллекции", fontWeight = FontWeight.Normal,
                    fontFamily = poppinsFort,
                    fontSize = 16.sp
                )
            })

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            "Выберите цвет",
            fontWeight = FontWeight.SemiBold,
            fontFamily = poppinsFort,
            fontSize = 16.sp
        )

        Spacer(modifier = Modifier.height(8.dp))

        LazyRow(
            modifier = Modifier,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            itemsIndexed(items = state.value.listOfColors) { index, item ->
                Card(
                    modifier = Modifier.size(48.dp),
                    border = BorderStroke(
                        if (state.value.selectColor == index) 3.dp else 0.dp,
                        MaterialTheme.colorScheme.outlineVariant
                    ), onClick = {
                        foldersViewModel.onIntent(FoldersIntent.UpdateColor(index))
                    }
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(item)
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            "Выберите картинку",
            fontWeight = FontWeight.SemiBold,
            fontFamily = poppinsFort,
            fontSize = 16.sp
        )
        Spacer(modifier = Modifier.height(8.dp))

        LazyRow(
            modifier = Modifier,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {

            itemsIndexed(state.value.listOfImages) { index, item ->
                Card(
                    modifier = Modifier.size(96.dp),
                    onClick = {

                        val name = if( item != null )context.resources.getResourceEntryName(item) else null
                        foldersViewModel.onIntent(FoldersIntent.UpdateImage(index, name))
                    },
                    border = BorderStroke(
                        if (state.value.selectImage == index) 3.dp else 0.dp,
                        MaterialTheme.colorScheme.outlineVariant
                    )
                ) {
                    if(item != null) {
                        Image(
                            painter = painterResource(item),
                            contentDescription = "",
                            modifier = Modifier
                                .fillMaxSize()
                                .rotate(40f)
                                .padding(4.dp)
                        )
                    }
                }

            }


        }

        Spacer(modifier = Modifier.height(16.dp))
        Text(
            "Предпросмотр",
            fontWeight = FontWeight.SemiBold,
            fontFamily = poppinsFort,
            fontSize = 16.sp
        )
        Spacer(modifier = Modifier.height(8.dp))

        Box(modifier = Modifier.fillMaxWidth()) {

            ElevatedCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
                    .height(140.dp),
                elevation = CardDefaults.cardElevation(18.dp),
                shape = RoundedCornerShape(32.dp)
            ) {

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(FiltersUtil.listOfColors[state.value.selectColor].copy(alpha = 0.8f)),
                    contentAlignment = Alignment.BottomEnd
                ) {


                    Row(

                    ) {
                        if(FiltersUtil.listOfImage[state.value.selectImage] != null) {
                            Image(
                                painter = painterResource(
                                    FiltersUtil.listOfImage[state.value.selectImage]!!
                                ),
                                contentDescription = "",
                                modifier = Modifier
                                    .fillMaxHeight()
                                    .width(150.dp)
                                    .offset(y = 30.dp, x = (-10).dp)
                                    .rotate(40f)
                            )
                        }
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(16.dp)
                                .padding(start = 16.dp)
                                .padding(top = 16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                state.value.textFieldFolderValue,
                                fontWeight = FontWeight.SemiBold,
                                fontFamily = poppinsFort,
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis,
                                color = MaterialTheme.colorScheme.onSecondary,
                                fontSize = 22.sp,
                                textAlign = TextAlign.Center
                            )
                            Text(
                                "",
                                fontWeight = FontWeight.SemiBold,
                                fontFamily = poppinsFort,
                                color = MaterialTheme.colorScheme.onSecondary,
                                fontSize = 18.sp,
                            )
                        }
                    }
                }
            }
        }

        Button(modifier = Modifier.fillMaxWidth(), onClick = { foldersViewModel.onIntent(FoldersIntent.AddFolder) }) {
            Text("Добавить коллекцию")
        }

    }
}

@Composable
fun ShowCollectionList(
    modifier: Modifier = Modifier,
    list: ResultFilterData,
    movieId: Long = -1,
    onSelectFolder: (Long) -> Unit
) {
    val lazyListState = rememberLazyListState()

    var count by remember {
        mutableIntStateOf(0)
    }

    when (val data = list) {
        is ResultFilterData.Error -> {

        }

        ResultFilterData.Loading -> {

        }

        is ResultFilterData.Success -> {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .animateContentSize(),
                state = lazyListState
            ) {
                if (count == 0)
                    count = (data.data as List<FolderWithMovies>).size
                itemsIndexed(
                    (data.data as List<FolderWithMovies>),
                    key = { _, folder -> folder.folder.folderId }) { index, folder ->

                    InitFolderItem(
                        folder = folder,
                        index = index,
                        onShowAddImage = folder.movies.any { it.movieId == movieId },
                        onSelectFolder = { id ->
                            onSelectFolder(id)
                        })
                }
            }

            LaunchedEffect(data.data.size) {
                if (data.data.size > count) {
                    lazyListState.animateScrollToItem(0)
                    count = data.data.size
                }
            }
        }
    }


}

@Composable
fun InitFolderItem(
    modifier: Modifier = Modifier,
    folder: FolderWithMovies,
    index: Int,
    onShowAddImage: Boolean = false,
    foldersViewModel: FoldersViewModel = hiltViewModel(),
    onSelectFolder: (Long) -> Unit
) {
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()

            .then(
                if (index == 0) {
                    Modifier.padding(16.dp)
                } else {
                    Modifier
                        .padding(horizontal = 16.dp)
                        .padding(bottom = 16.dp)
                }
            )
            .height(140.dp),
        elevation = CardDefaults.cardElevation(18.dp),
        shape = RoundedCornerShape(32.dp),
        onClick = {
            onSelectFolder(folder.folder.folderId)
        }
    ) {

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(folder.folder.color).copy(alpha = 0.8f)),
            contentAlignment = Alignment.BottomEnd
        ) {


            Row(

            ) {
                if(folder.folder.imageResName != null) {
                    Image(
                        painter = painterResource(
                            LocalContext.current.resources.getIdentifier(
                                folder.folder.imageResName,
                                "drawable",
                                LocalContext.current.packageName
                            )
                        ),
                        contentDescription = "",
                        modifier = Modifier
                            .fillMaxHeight()
                            .width(150.dp)
                            .offset(y = 30.dp, x = (-10).dp)
                            .rotate(40f)
                    )
                }
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                        .padding(start = 16.dp)
                        .padding(top = 16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        folder.folder.folderName,
                        fontWeight = FontWeight.SemiBold,
                        fontFamily = poppinsFort,
                        color = MaterialTheme.colorScheme.onSecondary,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        fontSize = 22.sp,
                        textAlign = TextAlign.Center
                    )
                    Text(
                        folder.movies.size.toString(),
                        fontWeight = FontWeight.SemiBold,
                        fontFamily = poppinsFort,
                        color = MaterialTheme.colorScheme.onSecondary,
                        fontSize = 18.sp,
                    )
                }
            }

            if (onShowAddImage) {
                Image(
                    painter = painterResource(R.drawable.img_ok), contentDescription = "",
                    modifier = Modifier
                        .size(64.dp)
                        .offset(y = (5).dp, x = (5).dp)
                        .alpha(0.8f)
                )
            }
        }
    }
}