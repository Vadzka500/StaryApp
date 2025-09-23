package com.sidspace.stary.folders.presentation.screen


import android.widget.Toast
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
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
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.sidspace.stary.folders.presentation.R
import com.sidspace.stary.folders.presentation.util.FiltersUtil
import com.sidspace.stary.ui.ShowCollectionList
import com.sidspace.stary.ui.model.ResultData
import com.sidspace.stary.ui.uikit.poppinsFort
import kotlinx.coroutines.flow.collectLatest


@Composable
fun UserCollectionsScreen(
    modifier: Modifier = Modifier,
    foldersViewModel: FoldersViewModel = hiltViewModel(),
    onSelectFolder: (id: Long) -> Unit,
    onBack: () -> Unit,
    toErrorScreen: () -> Unit,
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

                FoldersEffect.ToErrorScreen -> {
                    toErrorScreen()
                }
            }
        }
    }

    val count = when (val result = state.value.filters) {
        is ResultData.Success -> result.data.size
        else -> 0
    }


    Box(
        modifier = modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.BottomEnd
    ) {


        Column(modifier = Modifier.fillMaxSize()) {
            FolderToolbar(count = count, onBack = {
                foldersViewModel.onIntent(FoldersIntent.OnBack)
            })

            ShowCollectionList(list = state.value.filters, onSelectFolder = { id ->
                foldersViewModel.onIntent(FoldersIntent.ToFolderScreen(id))
            }, onError = {
                foldersViewModel.onIntent(FoldersIntent.OnError)
            })
        }

        FloatingActionButton(
            modifier = Modifier.padding(24.dp),
            onClick = {
                foldersViewModel.onIntent(FoldersIntent.ClickCreateFolder)
            },
        ) {
            Icon(Icons.Filled.Add, stringResource(R.string.add_collection))
        }
    }
    if (state.value.isShowBottomSheet) {
        ShowCreateFolderBottomSheet(state = state)
    }

}

@Composable
fun FolderToolbar(count: Int, onBack: () -> Unit) {
    Row(
        modifier = Modifier.padding(start = 16.dp, top = 12.dp, bottom = 12.dp),
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
                .padding(8.dp))


        Text(
            stringResource(R.string.my_collection),
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
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShowCreateFolderBottomSheet(
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
    state: State<FoldersState>,
    modifier: Modifier = Modifier,
    foldersViewModel: FoldersViewModel = hiltViewModel(),
) {

    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxWidth()
            .verticalScroll(scrollState)
            .padding(horizontal = 16.dp)
    ) {
        InitTextFiledFolder(
            text = state.value.textFieldFolderValue,
            isError = state.value.isErrorEmptyName,
            onChangeValue = { text ->
                foldersViewModel.onIntent(FoldersIntent.UpdateNameFolder(text))
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            stringResource(R.string.choose_color),
            fontWeight = FontWeight.SemiBold,
            fontFamily = poppinsFort,
            fontSize = 16.sp
        )

        Spacer(modifier = Modifier.height(8.dp))

        InitListColors(state.value.listOfColors, state.value.selectColor, { index ->
            foldersViewModel.onIntent(FoldersIntent.UpdateColor(index))
        })

        Spacer(modifier = Modifier.height(16.dp))
        Text(
            stringResource(R.string.choose_picture),
            fontWeight = FontWeight.SemiBold,
            fontFamily = poppinsFort,
            fontSize = 16.sp
        )
        Spacer(modifier = Modifier.height(8.dp))


        InitListImages(
            listImages = state.value.listOfImages,
            selectImageIndex = state.value.selectImage,
            { index, name ->
                foldersViewModel.onIntent(FoldersIntent.UpdateImage(index, name))
            })

        Spacer(modifier = Modifier.height(16.dp))
        Text(
            stringResource(R.string.preview),
            fontWeight = FontWeight.SemiBold,
            fontFamily = poppinsFort,
            fontSize = 16.sp
        )
        Spacer(modifier = Modifier.height(8.dp))

        InitPreviewFolder(
            selectColor = state.value.selectColor,
            selectImage = state.value.selectImage,
            folderNameText = state.value.textFieldFolderValue
        )

        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = { foldersViewModel.onIntent(FoldersIntent.AddFolder) }) {
            Text(stringResource(R.string.add_collection_rus))
        }

    }
}

@Composable
fun InitPreviewFolder(selectColor: Int, selectImage: Int, folderNameText: String) {
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
                    .background(FiltersUtil.listOfColors[selectColor].copy(alpha = 0.8f)),
                contentAlignment = Alignment.BottomEnd
            ) {


                Row(

                ) {
                    if (FiltersUtil.listOfImage[selectImage] != null) {
                        Image(
                            painter = painterResource(
                                FiltersUtil.listOfImage[selectImage]!!
                            ),
                            contentDescription = "",
                            modifier = Modifier
                                .fillMaxHeight()
                                .width(150.dp)
                                .offset(y = 30.dp, x = (-10).dp)
                                .rotate(FoldersState.FOLDER_PICTURE_ROTATE)
                        )
                    }
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp)
                            .padding(top = 16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            folderNameText,
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
}

@Composable
fun InitListImages(listImages: List<Int?>, selectImageIndex: Int, onSelectImage: (Int, String?) -> Unit) {

    val context = LocalContext.current

    LazyRow(
        modifier = Modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {

        itemsIndexed(listImages) { index, item ->
            Card(
                modifier = Modifier.size(96.dp),
                onClick = {

                    val name =
                        if (item != null) context.resources.getResourceEntryName(item) else null
                    onSelectImage(index, name)
                },
                border = BorderStroke(
                    if (selectImageIndex == index) 3.dp else 0.dp,
                    MaterialTheme.colorScheme.outlineVariant
                )
            ) {
                if (item != null) {
                    Image(
                        painter = painterResource(item),
                        contentDescription = "",
                        modifier = Modifier
                            .fillMaxSize()
                            .rotate(FoldersState.FOLDER_PICTURE_ROTATE)
                            .padding(4.dp)
                    )
                }
            }

        }


    }
}

@Composable
fun InitListColors(listColors: List<Color>, selectColorIndex: Int, selectColor: (Int) -> Unit) {
    LazyRow(
        modifier = Modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        itemsIndexed(items = listColors) { index, item ->
            Card(
                modifier = Modifier.size(48.dp),
                border = BorderStroke(
                    if (selectColorIndex == index) 3.dp else 0.dp,
                    MaterialTheme.colorScheme.outlineVariant
                ), onClick = {
                    selectColor(index)
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
}

@Composable
fun InitTextFiledFolder(text: String, isError: Boolean, onChangeValue: (String) -> Unit) {
    TextField(
        modifier = Modifier.fillMaxWidth(),
        value = text,
        isError = isError,
        colors = TextFieldDefaults.colors(
            unfocusedContainerColor = Color.Transparent,
            focusedContainerColor = Color.Transparent
        ),
        onValueChange = { onChangeValue(it) },
        label = {
            Text(
                stringResource(R.string.name_collection), fontWeight = FontWeight.Normal,
                fontFamily = poppinsFort,
                fontSize = 16.sp
            )
        })
}

