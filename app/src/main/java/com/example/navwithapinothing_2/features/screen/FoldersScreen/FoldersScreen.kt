package com.example.navwithapinothing_2.features.screen.FoldersScreen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.navwithapinothing_2.R
import com.example.navwithapinothing_2.data.ResultDb
import com.example.navwithapinothing_2.database.models.FolderWithMovies
import com.example.navwithapinothing_2.features.screen.MovieViewModel
import com.example.navwithapinothing_2.features.theme.poppinsFort

/**
 * @Author: Vadim
 * @Date: 16.05.2025
 */


@Composable
@Preview(showBackground = true)
fun UserCollectionsScreen(
    modifier: Modifier = Modifier,
    movieViewModel: MovieViewModel = hiltViewModel(),
    onSelectFolder: (id: Long) -> Unit
) {

    val state = movieViewModel.getAllFoldersState.collectAsState()

    var countFolders by remember {
        mutableIntStateOf(0)
    }

    val isShowCreateSheet = remember {
        mutableStateOf(false)
    }

    LaunchedEffect(Unit) {
        movieViewModel.getFolders()
    }
    

    LaunchedEffect(state.value) {
        if (state.value is ResultDb.Success<List<FolderWithMovies>>) {
            countFolders = (state.value as ResultDb.Success<List<FolderWithMovies>>).data.size
        }
    }

    Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.BottomEnd) {


        Column(modifier = Modifier.fillMaxSize()) {
            Row(
                modifier = Modifier.padding(start = 16.dp, top = 12.dp, bottom = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Image(imageVector = Icons.Default.ArrowBackIosNew, contentDescription = "Назад")

                Text(
                    "Мои коллекции",
                    modifier = Modifier.padding(start = 16.dp),
                    fontWeight = FontWeight.Bold,
                    fontFamily = poppinsFort,
                    fontSize = 24.sp,
                )

                Text(
                    countFolders.toString(),
                    modifier = Modifier.padding(start = 8.dp),
                    fontWeight = FontWeight.SemiBold,
                    fontFamily = poppinsFort,
                    fontSize = 24.sp,
                )
            }

            ShowCollectionList(state = state, onSelectFolder = onSelectFolder)
        }

        FloatingActionButton(
            modifier = Modifier.padding(24.dp),
            onClick = {
                isShowCreateSheet.value = true
            },
        ) {
            Icon(Icons.Filled.Add, "Add collection")
        }
    }
    if (isShowCreateSheet.value) {
        ShowCreateFolderBottomSheet(isVisible = isShowCreateSheet)
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShowCreateFolderBottomSheet(modifier: Modifier = Modifier, isVisible: MutableState<Boolean>) {
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true,
    )

    ModalBottomSheet(
        sheetState = sheetState,
        onDismissRequest = {
            isVisible.value = false
        }) {
        CreateSheetData()

    }
}

@Composable
fun CreateSheetData(modifier: Modifier = Modifier) {

    var nameFolder by remember {
        mutableStateOf("")
    }

    val listOfColors = listOf(
        Color(0xFF9FA8DA),
        Color(0xFFEF9A9A),
        Color(0xFFF48FB1),
        Color(0xFFE6EE9C),
        Color(0xFF80DEEA),

        Color(0xFFA1887F),
        Color(0xFF9575CD),
        Color(0xFFEA80FC),
        Color(0xFFA5D6A7),
        Color(0xFF80CBC4),
        Color(0xFFEF9A9A),
    )

    val listOfImage = listOf(
        R.drawable.rotate,
        R.drawable.add,
        R.drawable.sun,
        R.drawable.snowflake,
    )

    var selectColorIndex by remember {
        mutableIntStateOf(0)
    }

    var selectImageIndex by remember {
        mutableIntStateOf(0)
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        TextField(
            modifier = Modifier.fillMaxWidth(),
            value = nameFolder,
            onValueChange = { nameFolder = it },
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
            itemsIndexed(listOfColors) { index, item ->
                Card(
                    modifier = Modifier.size(48.dp),
                    border = BorderStroke(
                        if (selectColorIndex == index) 3.dp else 0.dp,
                        MaterialTheme.colorScheme.outlineVariant
                    ), onClick = {
                        selectColorIndex = index
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

            itemsIndexed(listOfImage) { index, item ->
                Card(
                    modifier = Modifier.size(128.dp),
                    onClick = { selectImageIndex = index },
                    border = BorderStroke(
                        if (selectImageIndex == index) 3.dp else 0.dp,
                        MaterialTheme.colorScheme.outlineVariant
                    )
                ) {
                    Image(
                        painter = painterResource(item),
                        contentDescription = "",
                        modifier = Modifier
                            .fillMaxSize()
                            .rotate(40f)
                    )
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
                        .background(listOfColors[selectColorIndex].copy(alpha = 0.8f)),
                    contentAlignment = Alignment.BottomEnd
                ) {


                    Row(

                    ) {
                        Image(
                            painter = painterResource(
                                listOfImage[selectImageIndex]
                            ),
                            contentDescription = "",
                            modifier = Modifier
                                .fillMaxHeight()
                                .width(150.dp)
                                .offset(y = 30.dp, x = (-10).dp)
                                .rotate(40f)
                        )
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                                .padding(start = 16.dp)
                                .padding(top = 16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                nameFolder,
                                fontWeight = FontWeight.SemiBold,
                                fontFamily = poppinsFort,
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

        Button(onClick = {}) {
            Text("Добавить коллекцию")
        }

    }
}

@Composable
fun ShowCollectionList(
    modifier: Modifier = Modifier,
    state: State<ResultDb<List<FolderWithMovies>>>,
    onSelectFolder: (Long) -> Unit,
    movieId: Long = -1,
    movieViewModel: MovieViewModel = hiltViewModel()
) {


    when (val data = state.value) {
        ResultDb.Error -> {

        }

        ResultDb.Loading -> {

        }

        is ResultDb.Success<*> -> {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                itemsIndexed(
                    (data.data as List<FolderWithMovies>),
                    key = { _, folder -> folder.folder.folderId }) { index, folder ->
                    InitFolderItem(
                        folder = folder,
                        index = index,
                        onSelectFolder = onSelectFolder,
                        onShowAddImage = folder.movies.any { it.movieId == movieId })
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
    onSelectFolder: (Long) -> Unit,
    onShowAddImage: Boolean = false
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
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
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