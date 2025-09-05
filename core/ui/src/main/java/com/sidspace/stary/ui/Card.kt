package com.sidspace.stary.ui

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.fadeIn
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalResources
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.sidspace.stary.domain.model.Folder
import com.sidspace.stary.ui.enums.ViewMode
import com.sidspace.stary.ui.model.MoviePreviewUi
import com.sidspace.stary.ui.model.MovieUi
import com.sidspace.stary.ui.model.ResultData
import com.sidspace.stary.ui.uikit.Dimens
import com.sidspace.stary.ui.uikit.Purple40
import com.sidspace.stary.ui.uikit.poppinsFort
import com.sidspace.stary.ui.utils.InitRatingView
import com.sidspace.stary.ui.utils.ScoreManager


const val FOLDER_APPLY_PICTURE_ALPHA = 0.8F

const val MOVIE_COUNT_HORIZONTAL_LIST = 10

const val GENRES_COUNT_MAX = 3

@Composable
fun InitList(
    modifier: Modifier = Modifier,
    list: List<MovieUi>,
    onClick: (Long) -> Unit,
    viewType: ViewMode
) {

    AnimatedContent(
        targetState = viewType,
        transitionSpec = { fadeIn() togetherWith ExitTransition.None }) { viewType ->

        if (viewType == ViewMode.GRID) {
            GridView(
                modifier,
                list = list,
                onClick = onClick
            )
        } else {
            ListView(
                modifier,
                list = list,
                onClick = onClick
            )
        }
    }

}

@Composable
fun ListView(modifier: Modifier = Modifier, list: List<MovieUi>, onClick: (Long) -> Unit) {
    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        itemsIndexed(list) { index, item ->
            CardList(
                movie = item,
                onSelectMovie = { id -> onClick(id) },
                modifier = Modifier
            )
        }
    }
}

@Composable
fun GridView(modifier: Modifier = Modifier, list: List<MovieUi>, onClick: (Long) -> Unit) {

    LazyVerticalGrid(
        modifier = modifier
            .padding(horizontal = 16.dp),
        columns = GridCells.Fixed(2),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        itemsIndexed(list) { index, item ->
            MovieCardGrid(
                item = item,
                onSelectMovie = { id -> onClick(id) },
            )
        }
    }
}

@SuppressLint("ConfigurationScreenWidthHeight")
@Composable
fun MovieCardGrid(
    item: MovieUi,
    onSelectMovie: (Long) -> Unit
) {

    val configuration = LocalConfiguration.current
    val width = configuration.screenWidthDp / 2 - Dimens.HorizontalCardPadding
    val height = (configuration.screenWidthDp / 2 - Dimens.HorizontalCardPadding) * 1.5
    val boxHeight = height + 30


    var scale by remember { mutableStateOf(ContentScale.Crop) }
    var isShimmer by remember { mutableStateOf(true) }

    Box(
        modifier = Modifier
            .height(boxHeight.dp)
            .width(width.dp)
            .clickable { onSelectMovie(item.id) }) {
        Column(

            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .width(width.dp)
                    .height(height.dp), contentAlignment = Alignment.BottomEnd
            ) {
                AsyncImage(
                    modifier = Modifier
                        .width(width.dp)
                        .height(height.dp) // 255
                        .clip(RoundedCornerShape(16.dp))
                        .then(
                            if (isShimmer) Modifier.shimmerEffect()
                            else Modifier
                        ),
                    model = ImageRequest.Builder(LocalContext.current).data(item.previewUrl)
                        .listener(onStart = {
                            scale = ContentScale.Crop
                        }, onSuccess = { request, result ->
                            scale = ContentScale.FillBounds
                            isShimmer = false
                        })
                        .crossfade(true).build(),
                    contentDescription = null,
                    contentScale = scale, error = painterResource(R.drawable.ic_placeholder_4)
                )
            }
            Text(
                modifier = Modifier.padding(top = 5.dp),
                text = item.name ?: item.enName ?: "null",
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.SemiBold,
                lineHeight = 16.sp,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                fontSize = 14.sp
            )
        }

        InitRatingView(item.scoreKp)


    }
}

@Composable
fun CardList(
    modifier: Modifier = Modifier,
    movie: MovieUi,
    onSelectMovie: (Long) -> Unit
) {
    var scale by remember { mutableStateOf(ContentScale.Crop) }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(120.dp)
            .clickable { onSelectMovie(movie.id) }
            .padding(horizontal = 16.dp)
    ) {
        Row() {
            AsyncImage(
                modifier = Modifier
                    .width(80.dp)
                    .height(120.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .shimmerEffect(),
                model = ImageRequest.Builder(LocalContext.current).data(movie.previewUrl)

                    .listener(onStart = {
                        scale = ContentScale.Crop
                    }, onSuccess = { _, _ ->
                        scale = ContentScale.FillBounds
                    })
                    .crossfade(true).build(),
                contentScale = scale,
                contentDescription = null,
                error = painterResource(R.drawable.ic_placeholder_4)
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(start = 16.dp, end = 16.dp)
                    .padding(vertical = 8.dp)
            ) {
                Text(
                    text = movie.name ?: "null",
                    fontWeight = FontWeight.SemiBold,
                    fontFamily = poppinsFort,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    fontSize = 16.sp
                )

                if (!movie.isSeries) {
                    Text(
                        text = movie.year.toString(),
                        fontWeight = FontWeight.Normal,
                        fontFamily = poppinsFort,
                        fontSize = 12.sp
                    )
                } else {

                    val seasonCount = movie.countOfSeasons



                    if (movie.releaseStart != null) {
                        val releaseStart = movie.releaseStart
                        val releaseEnd = movie.releaseEnd

                        var text = releaseStart.toString()

                        if (releaseEnd != null && releaseStart != releaseEnd) {
                            text = "$releaseStart - $releaseEnd"
                        } else if (releaseEnd == null) {
                            text =
                                if (seasonCount == 1 || movie.status?.equals("completed") == true) {
                                    releaseStart.toString()
                                } else {
                                    "$releaseStart - н.в."
                                }
                        }

                        Text(
                            text = text,
                            fontWeight = FontWeight.Normal,
                            fontFamily = poppinsFort,
                            fontSize = 12.sp
                        )
                    } else {
                        Text(
                            text = movie.year.toString(),
                            fontWeight = FontWeight.Normal,
                            fontFamily = poppinsFort,
                            fontSize = 12.sp
                        )
                    }
                }


                Row(
                    modifier = Modifier.padding(vertical = 6.dp),
                    horizontalArrangement = Arrangement.spacedBy(5.dp)
                ) {

                    movie.genres?.take(GENRES_COUNT_MAX)?.forEachIndexed { index, item ->

                        if (index == 1 || index == 2) {
                            Text(
                                text = "•",
                                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)
                            )
                        }

                        Text(
                            text = item.replaceFirstChar { it.uppercase() },
                            fontWeight = FontWeight.Light,
                            fontFamily = poppinsFort,
                            fontSize = 12.sp
                        )

                    }

                }

                //===
                if (movie.scoreKp != null && movie.scoreKp != 0.0) {

                    val rating = ScoreManager.ratingToFormat(movie.scoreKp)


                    val color = if (rating < 5) {
                        Color.Red
                    } else if (rating < 7) {
                        Color.Gray
                    } else Purple40


                    Text(
                        text = rating.toString(),
                        fontWeight = FontWeight.Medium,
                        fontFamily = poppinsFort,
                        fontSize = 14.sp,
                        color = color
                    )
                }

            }
        }
    }

}


@Composable
fun InitRow(modifier: Modifier = Modifier, label: String, onClick: () -> Unit) {
    Row(
        modifier = modifier
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
            text = label,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
        )

        Text(
            "Все",
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold,
            color = Purple40,
            modifier = Modifier.clickable {
                onClick()
            }
        )
        Icon(
            modifier = Modifier.size(14.dp),
            imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
            contentDescription = "Все",
            tint = Purple40
        )
    }
}

@Composable
fun HorizontalList(
    list: List<MoviePreviewUi>,
    label: String,
    onSelectMovie: (Long) -> Unit,
    modifier: Modifier = Modifier
) {

    Spacer(modifier = Modifier.height(16.dp))
    Text(
        text = label,
        modifier = Modifier.padding(start = 16.dp),
        fontWeight = FontWeight.SemiBold,
        fontFamily = poppinsFort,
        fontSize = 18.sp
    )
    Spacer(modifier = Modifier.height(8.dp))

    LazyRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(horizontal = 16.dp)
    ) {

        itemsIndexed(
            list,
            key = { _, item -> item.id }) { index, item ->

            MovieCardHorizontal(
                id = item.id,
                name = item.name,
                enName = item.enName,
                previewUrl = item.previewUrl,
                score = item.score,
                onSelectMovie = { onSelectMovie(item.id) }
            )

        }
    }
}

@Composable
fun HorizontalHomeList(
    list: List<MoviePreviewUi>,
    label: String,
    onClickHeader: () -> Unit,
    onSelectMovie: (Long) -> Unit
) {
    InitRow(label = label, onClick = onClickHeader)
    LazyRow(
        modifier = Modifier.padding(top = 10.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(horizontal = 16.dp)
    ) {

        itemsIndexed(
            list.take(MOVIE_COUNT_HORIZONTAL_LIST),
            key = { _, item -> item.id }) { index, item ->

            MovieCardHorizontal(
                id = item.id,
                name = item.name,
                enName = item.enName,
                previewUrl = item.previewUrl,
                score = item.score,
                onSelectMovie = { onSelectMovie(item.id) }
            )

        }
    }
}

@Composable
fun MovieCardHorizontal(
    modifier: Modifier = Modifier,
    id: Long,
    name: String?,
    enName: String?,
    previewUrl: String?,
    score: Double?,
    onSelectMovie: (Long) -> Unit
) {

    var scale by remember { mutableStateOf(ContentScale.Crop) }
    var isShimmer by remember { mutableStateOf(true) }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(255.dp)
            .width(150.dp)
            .clickable {
                onSelectMovie(id)
            }) {
        Column(

            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AsyncImage(
                modifier = Modifier
                    .width(150.dp)
                    .height(215.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .then(
                        if (isShimmer) Modifier.shimmerEffect()
                        else Modifier
                    ),
                model = ImageRequest.Builder(LocalContext.current).data(previewUrl)
                    .crossfade(true)

                    .listener(onStart = {
                        scale = ContentScale.Crop
                    }, onSuccess = { _, _ ->
                        isShimmer = false
                        scale = ContentScale.FillBounds
                    })
                    .crossfade(true).build(),
                contentScale = scale,
                contentDescription = null, error = painterResource(R.drawable.ic_placeholder_4)
            )
            Text(
                modifier = Modifier.padding(top = 5.dp),
                text = name ?: enName ?: "null",
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.SemiBold,
                lineHeight = 16.sp,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,

                fontSize = 14.sp
            )
        }

        InitRatingView(score)

    }
}


@Composable
fun ShowCollectionList(
    modifier: Modifier = Modifier,
    list: ResultData<List<Folder>>,
    movieId: Long = -1,
    onSelectFolder: (Long) -> Unit,
    onError: () -> Unit
) {
    val lazyListState = rememberLazyListState()

    var count by remember {
        mutableIntStateOf(0)
    }

    when (val data = list) {
        is ResultData.Error -> {
            onError()
        }

        ResultData.Loading -> {

        }

        is ResultData.Success -> {
            LazyColumn(
                modifier = modifier
                    .fillMaxSize()
                    .animateContentSize(),
                state = lazyListState
            ) {
                if (count == 0)
                    count = data.data.size
                itemsIndexed(
                    data.data,
                    key = { _, folder -> folder.id }) { index, folder ->

                    InitFolderItem(
                        folder = folder,
                        index = index,
                        onShowAddImage = folder.listOfMovies?.any { it.id == movieId } == true,
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

        ResultData.None -> {

        }
    }
}


@Composable
fun InitFolderItem(
    modifier: Modifier = Modifier,
    folder: Folder,
    index: Int,
    onShowAddImage: Boolean = false,
    onSelectFolder: (Long) -> Unit
) {
    ElevatedCard(
        modifier = modifier
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
            onSelectFolder(folder.id)
        }
    ) {
        FolderCardContent(folder = folder, onShowAddImage = onShowAddImage)
    }
}

@Composable
fun FolderCardContent(modifier: Modifier = Modifier, folder: Folder, onShowAddImage: Boolean) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(folder.color).copy(alpha = 0.8f)),
        contentAlignment = Alignment.BottomEnd
    ) {


        Row {
            if (folder.imageResName != null) {
                Image(
                    painter = painterResource(
                        LocalResources.current.getIdentifier(
                            folder.imageResName,
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
                    .padding(start = 0.dp)
                    .padding(top = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    folder.name,
                    fontWeight = FontWeight.SemiBold,
                    fontFamily = poppinsFort,
                    color = MaterialTheme.colorScheme.onSecondary,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    fontSize = 22.sp,
                    textAlign = TextAlign.Center
                )
                Text(
                    folder.listOfMovies?.size.toString(),
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
                    .alpha(FOLDER_APPLY_PICTURE_ALPHA)
            )
        }
    }
}
