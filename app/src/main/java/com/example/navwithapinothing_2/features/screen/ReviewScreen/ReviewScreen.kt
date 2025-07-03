package com.example.navwithapinothing_2.features.screen.ReviewScreen

import android.graphics.Typeface
import android.text.Spanned
import android.text.style.CharacterStyle
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.text.style.URLSpan
import android.text.style.UnderlineSpan
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.ThumbDown
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.text.HtmlCompat
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.navwithapinothing_2.R
import com.example.navwithapinothing_2.data.Result
import com.example.navwithapinothing_2.features.screen.FoldersScreen.FoldersIntent
import com.example.navwithapinothing_2.models.UserReview

import com.example.navwithapinothing_2.features.screen.MovieViewModel
import com.example.navwithapinothing_2.features.screen.shimmerEffect
import com.example.navwithapinothing_2.features.screen.toAnnotatedString
import com.example.navwithapinothing_2.features.theme.poppinsFort
import kotlinx.coroutines.flow.collectLatest
import java.time.LocalDate
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

/**
 * @Author: Vadim
 * @Date: 11.06.2025
 */

@Preview(showBackground = true)
@Composable
fun ReviewScreen(
    modifier: Modifier = Modifier,
    id: Long,
    reviewViewModel: ReviewViewModel = hiltViewModel(),
    onBack:() -> Unit
) {

    val stateReview = reviewViewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        reviewViewModel.effect.collectLatest { effect ->
            when(effect){
                ReviewEffect.OnBack -> {
                    onBack()
                }
            }
        }
    }

    var isLoad by remember {
        mutableStateOf(true)
    }



    LaunchedEffect(Unit) {
        reviewViewModel.onIntent(ReviewIntent.LoadReviews(id))
    }

    Column() {
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
                        reviewViewModel.onIntent(ReviewIntent.OnBack)
                    }
                    .padding(8.dp))

            Text(
                "Рецензии",
                modifier = Modifier.padding(start = 16.dp),
                fontWeight = FontWeight.Bold,
                fontFamily = poppinsFort,
                fontSize = 24.sp,
            )

            Text(
                stateReview.value.countReviews.toString(),
                modifier = Modifier.padding(start = 8.dp),
                fontWeight = FontWeight.SemiBold,
                fontFamily = poppinsFort,
                fontSize = 24.sp,
            )
        }

        AnimatedContent(targetState = stateReview.value.reviews, transitionSpec = {
            fadeIn() togetherWith fadeOut()
        }, label = "") { state ->

            when (val data = state) {

                is ReviewResult.Error -> {
                    EmptyList()
                }

                ReviewResult.Loading -> {
                    InitShimmer()
                }

                is ReviewResult.Success -> {
                    isLoad = false
                    if ((data.list as List<UserReview>).isNotEmpty()) {
                        InitList(list = data.list, modifier = modifier)
                    } else {
                        EmptyList()
                    }
                }
            }

        }


    }

}

@Composable
fun EmptyList(modifier: Modifier = Modifier) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(modifier = Modifier.padding(bottom = 48.dp)) {
            Image(
                modifier = Modifier
                    .size(140.dp)
                    .align(Alignment.CenterHorizontally),
                painter = painterResource(R.drawable.empty_result),
                contentDescription = "Empty result"
            )

            Text(
                modifier = Modifier.padding(top = 16.dp),
                text = "Не нашли ни одной рецензии",
                fontWeight = FontWeight.SemiBold,
                fontFamily = poppinsFort,
                fontSize = 18.sp,
            )
        }
    }
}

@Composable
fun InitShimmer(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
    ) {
        repeat(4) {
            InitShimmerBox(
                modifier = Modifier.then(
                    if (it == 0) {
                        Modifier
                            .padding(top = 16.dp)
                            .padding(bottom = 16.dp)
                    } else {
                        Modifier
                            .padding(top = 2.dp)
                            .padding(bottom = 16.dp)
                    }
                )
            )

        }
    }
}

@Composable
fun InitShimmerBox(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(280.dp)
            .padding(horizontal = 16.dp)
            .clip(RoundedCornerShape(24.dp))
            .shimmerEffect()
    )
}

@Composable
fun InitList(modifier: Modifier = Modifier, list: List<UserReview>) {

    val expandedState = remember {
        mutableStateMapOf<Long, Boolean>()
    }

    val listState = rememberLazyListState()


    AnimatedVisibility(true, enter = fadeIn()) {

        LazyColumn(
            modifier = modifier
                .fillMaxSize(),
            state = listState,

            ) {
            itemsIndexed(list, key = { _, item -> item.id }) { index, item ->

                val isExpanded = expandedState[item.id] ?: false

                InitReview(
                    modifier = Modifier
                        .animateItem()
                        .then(
                            if (index == 0) {
                                Modifier
                                    .padding(top = 16.dp)
                                    .padding(bottom = 16.dp)
                            } else {
                                Modifier
                                    .padding(top = 2.dp)
                                    .padding(bottom = 16.dp)
                            }
                        ), item = item, isExpanded, onToggleExpanded = {
                        expandedState[item.id] = !isExpanded
                    }
                )
            }

        }
    }
}


@Composable
fun InitReview(
    modifier: Modifier = Modifier,
    item: UserReview,
    isExpanded: Boolean,
    onToggleExpanded: () -> Unit
) {

    var lineCount by remember { mutableIntStateOf(0) }

    val color = if (item.type == "Позитивный") {
        Color.Green.copy(alpha = 0.4f)
    } else if (item.type == "Негативный") {
        Color.Red.copy(alpha = 0.5f)
    } else {
        Color.Gray.copy(alpha = 0.5f)
    }

    val isoFormat = DateTimeFormatter.ISO_DATE_TIME

    val dateReview = ZonedDateTime.parse(item.date, isoFormat).toLocalDate()
    val currentDate = LocalDate.now()
    var textDate = ""
    if (dateReview.year != currentDate.year) {
        val firstApiFormat =
            DateTimeFormatter.ofPattern("d MMMM yyyy", Locale.getDefault())
        textDate = dateReview.format(firstApiFormat)
    } else {
        val firstApiFormat =
            DateTimeFormatter.ofPattern("d MMMM", Locale.getDefault())
        textDate = dateReview.format(firstApiFormat)
    }




    ElevatedCard(
        modifier = modifier
            .padding(horizontal = 16.dp),
        elevation = CardDefaults.cardElevation(7.dp),
        shape = RoundedCornerShape(24.dp),

        ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .animateContentSize()
        ) {

            Row(
                modifier = Modifier
                    .height(50.dp)
                    .fillMaxWidth()
            ) {
                Image(
                    painter = painterResource(R.drawable.astronaut_img),
                    contentDescription = "Аватар",
                    colorFilter = ColorFilter.tint(color = Color.Black.copy(alpha = 0.6f)),
                    modifier = Modifier
                        .size(50.dp)
                        .clip(
                            CircleShape
                        )
                        .background(color = color)
                        .padding(3.dp)
                        .offset(y = 5.dp)
                )

                Column(
                    modifier = Modifier
                        .align(alignment = Alignment.CenterVertically)
                        .padding(start = 16.dp), verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {

                    Text(
                        item.author,
                        fontWeight = FontWeight.Medium,
                        fontFamily = poppinsFort,
                        fontSize = 12.sp,
                        lineHeight = 12.sp
                    )


                    Text(
                        textDate,
                        fontWeight = FontWeight.Light,
                        fontFamily = poppinsFort,
                        fontSize = 12.sp,
                        lineHeight = 12.sp
                    )
                }
                Spacer(modifier = Modifier.weight(1f))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Image(
                        modifier = Modifier.size(14.dp),
                        imageVector = Icons.Default.ThumbUp,
                        colorFilter = ColorFilter.tint(color = Color.Gray.copy(alpha = 0.5f)),
                        contentDescription = "Like count"
                    )
                    Text(
                        modifier = Modifier.padding(start = 6.dp),
                        text = item.reviewLikes.toString(), fontWeight = FontWeight.SemiBold,
                        fontFamily = poppinsFort,
                        fontSize = 14.sp
                    )
                    Image(
                        modifier = Modifier
                            .padding(start = 16.dp)
                            .size(14.dp),
                        imageVector = Icons.Default.ThumbDown,
                        colorFilter = ColorFilter.tint(color = Color.Gray.copy(alpha = 0.5f)),
                        contentDescription = "Dislike count"
                    )
                    Text(
                        modifier = Modifier.padding(start = 6.dp),
                        text = item.reviewDislikes.toString(), fontWeight = FontWeight.SemiBold,
                        fontFamily = poppinsFort,
                        fontSize = 14.sp
                    )
                }


            }

            Column(modifier = Modifier.fillMaxWidth()) {

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    item.title,
                    fontWeight = FontWeight.SemiBold,
                    fontFamily = poppinsFort,
                    fontSize = 16.sp
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = HtmlCompat.fromHtml(item.review, HtmlCompat.FROM_HTML_MODE_LEGACY)
                        .toAnnotatedString(),
                    fontWeight = FontWeight.Normal,
                    fontFamily = poppinsFort,
                    fontSize = 14.sp,
                    maxLines = if (isExpanded) Int.MAX_VALUE else 4,
                    overflow = TextOverflow.Ellipsis,
                    onTextLayout = {
                        lineCount = it.lineCount
                    }
                )

                if (lineCount >= 4) {
                    Text(
                        text = if (!isExpanded) "Развернуть" else "Свернуть",
                        fontWeight = FontWeight.SemiBold,
                        fontFamily = poppinsFort,
                        fontSize = 14.sp,

                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f),
                        modifier = Modifier
                            .padding(top = 4.dp)
                            .clickable(indication = null, interactionSource = null) {
                                onToggleExpanded()
                            })
                }

            }
        }
    }
}