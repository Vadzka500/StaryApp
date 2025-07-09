package com.example.navwithapinothing_2.features.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.ViewList
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.navwithapinothing_2.features.screen.CollectionMoviesScreen.ListMoviesViewModel
import com.example.navwithapinothing_2.features.theme.Purple40
import com.example.navwithapinothing_2.features.theme.poppinsFort
import com.example.navwithapinothing_2.models.common.SortDirection
import com.example.navwithapinothing_2.models.common.SortType
import com.example.navwithapinothing_2.models.common.ViewMode

/**
 * @Author: Vadim
 * @Date: 09.07.2025
 */
@Composable
fun FilterSection(
    modifier: Modifier = Modifier,
    isVisibleFilter: Boolean,
    viewType: ViewMode,
    isShowSort: Boolean,
    onHideFilter: () -> Unit,
    setGridView: () -> Unit,
    setListView: () -> Unit,
    sortType: SortType = SortType.NONE,
    sortDirection: SortDirection = SortDirection.DESCENDING,
    toggleSortDirection:() -> Unit,
    setSortType:(SortType) -> Unit,
    sortList:() -> Unit
) {


    val alpha by animateFloatAsState(
        targetValue = if (isVisibleFilter) 0.4f else 0f,
        animationSpec = tween(durationMillis = 300)
    )

    AnimatedVisibility(
        visible = isVisibleFilter,
        enter = fadeIn(),
        exit = fadeOut(),
        modifier = modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = alpha))
            .clickable(indication = null, interactionSource = null) {
                onHideFilter()
            }
    ) {

    }


    AnimatedVisibility(
        visible = isVisibleFilter,
        enter = expandVertically(),
        exit = shrinkVertically(),
        modifier = modifier
            .fillMaxWidth()
    ) {
        ElevatedCard(
            elevation = CardDefaults.cardElevation(),
            shape = RoundedCornerShape(
                topStart = 0.dp,
                topEnd = 0.dp,
                bottomStart = 8.dp,
                bottomEnd = 8.dp
            ),
            modifier = Modifier
                .fillMaxWidth()
        ) {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.background)
                    .padding(horizontal = 16.dp)
                    .padding(bottom = 12.dp)
            ) {

                Text(
                    "Вид:", fontWeight = FontWeight.Medium,
                    fontFamily = poppinsFort,
                    fontSize = 16.sp,
                )

                FlowRow(
                    modifier = Modifier.padding(top = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    FilterChip(
                        modifier = Modifier,
                        shape = RoundedCornerShape(15.dp),
                        onClick = {
                            setGridView()

                        },
                        label = {
                            Text(
                                "Таблица", fontWeight = FontWeight.Medium,
                                fontFamily = poppinsFort,
                                fontSize = 14.sp
                            )
                        },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.GridView,
                                contentDescription = "",
                                tint = Purple40
                            )
                        },
                        selected = viewType == ViewMode.GRID,

                        )

                    FilterChip(
                        modifier = Modifier,
                        shape = RoundedCornerShape(15.dp),
                        onClick = {
                            setListView()

                        },
                        label = {
                            Text("Список")
                        },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.ViewList,
                                contentDescription = "",
                                tint = Purple40
                            )
                        },
                        selected = viewType == ViewMode.LIST,

                        )
                }
                if (isShowSort) {
                    FilterSort(sortType = sortType, sortDirection = sortDirection, setSortType = setSortType, toggleSortDirection = toggleSortDirection, sortList = sortList)
                }
            }
        }
    }
}

@Composable
fun FilterSort(
    modifier: Modifier = Modifier,
    listMoviesViewModel: ListMoviesViewModel = hiltViewModel(),
    sortType: SortType,
    sortDirection: SortDirection,
    toggleSortDirection:() -> Unit,
    setSortType:(SortType) -> Unit,
    sortList:() -> Unit
) {

    Text(
        "Сортировка:",
        modifier = Modifier.padding(top = 12.dp),
        fontWeight = FontWeight.Medium,
        fontFamily = poppinsFort,
        fontSize = 16.sp
    )

    FlowRow(
        modifier = Modifier.padding(top = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        FilterChip(
            modifier = Modifier,
            shape = RoundedCornerShape(15.dp),
            onClick = {
                if (sortType == SortType.NAME) toggleSortDirection()
                setSortType(SortType.NAME)


                sortList()
            },
            label = {
                Text("Название")
            },
            leadingIcon = {
                if (sortType == SortType.NAME)
                    Icon(
                        imageVector = if (sortDirection == SortDirection.ASCENDING) Icons.Default.ArrowDownward else Icons.Default.ArrowUpward,
                        contentDescription = "",
                        tint = Purple40
                    )
            },
            selected = sortType == SortType.NAME,

            )

        FilterChip(
            modifier = Modifier,
            shape = RoundedCornerShape(15.dp),
            onClick = {
                if (sortType == SortType.RATING) toggleSortDirection()

                setSortType(SortType.RATING)

                sortList()
            },
            label = {
                Text("Оценка")
            },
            leadingIcon = {
                if (sortType == SortType.RATING)
                    Icon(
                        imageVector = if (sortDirection == SortDirection.ASCENDING) Icons.Default.ArrowDownward else Icons.Default.ArrowUpward,
                        contentDescription = "",
                        tint = Purple40
                    )
            },
            selected = sortType == SortType.RATING,

            )

        FilterChip(
            modifier = Modifier,
            shape = RoundedCornerShape(15.dp),
            onClick = {
                if (sortType == SortType.DATE) toggleSortDirection()
                setSortType(SortType.DATE)
                sortList()
            },
            label = {
                Text("Дата выхода")
            },
            leadingIcon = {
                if (sortType == SortType.DATE)
                    Icon(
                        imageVector = if (sortDirection == SortDirection.ASCENDING) Icons.Default.ArrowDownward else Icons.Default.ArrowUpward,
                        contentDescription = "",
                        tint = Purple40
                    )
            },
            selected = sortType == SortType.DATE,

            )
    }
}

fun SortDirection.toggle(): SortDirection {
    return if (this == SortDirection.DESCENDING) SortDirection.ASCENDING else SortDirection.DESCENDING
}