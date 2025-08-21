package com.sidspace.stary.ui

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


@Composable
fun FilterSection(
    modifier: Modifier = Modifier,
    isVisibleFilter: Boolean,
    viewType: com.sidspace.stary.ui.enum.ViewMode,
    isShowSort: Boolean,
    onHideFilter: () -> Unit,
    setGridView: () -> Unit,
    setListView: () -> Unit,
    sortType: com.sidspace.stary.ui.enum.SortType = _root_ide_package_.com.sidspace.stary.ui.enum.SortType.NONE,
    sortDirection: com.sidspace.stary.ui.enum.SortDirection = _root_ide_package_.com.sidspace.stary.ui.enum.SortDirection.DESCENDING,
    toggleSortDirection:() -> Unit,
    setSortType:(com.sidspace.stary.ui.enum.SortType) -> Unit,
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
                    fontFamily = _root_ide_package_.com.sidspace.stary.ui.uikit.poppinsFort,
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
                                fontFamily = _root_ide_package_.com.sidspace.stary.ui.uikit.poppinsFort,
                                fontSize = 14.sp
                            )
                        },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.GridView,
                                contentDescription = "",
                                tint = _root_ide_package_.com.sidspace.stary.ui.uikit.Purple40
                            )
                        },
                        selected = viewType == _root_ide_package_.com.sidspace.stary.ui.enum.ViewMode.GRID,

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
                                tint = _root_ide_package_.com.sidspace.stary.ui.uikit.Purple40
                            )
                        },
                        selected = viewType == _root_ide_package_.com.sidspace.stary.ui.enum.ViewMode.LIST,

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
    sortType: com.sidspace.stary.ui.enum.SortType,
    sortDirection: com.sidspace.stary.ui.enum.SortDirection,
    toggleSortDirection:() -> Unit,
    setSortType:(com.sidspace.stary.ui.enum.SortType) -> Unit,
    sortList:() -> Unit
) {

    Text(
        "Сортировка:",
        modifier = Modifier.padding(top = 12.dp),
        fontWeight = FontWeight.Medium,
        fontFamily = _root_ide_package_.com.sidspace.stary.ui.uikit.poppinsFort,
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
                if (sortType == _root_ide_package_.com.sidspace.stary.ui.enum.SortType.NAME) toggleSortDirection()
                setSortType(_root_ide_package_.com.sidspace.stary.ui.enum.SortType.NAME)


                sortList()
            },
            label = {
                Text("Название")
            },
            leadingIcon = {
                if (sortType == _root_ide_package_.com.sidspace.stary.ui.enum.SortType.NAME)
                    Icon(
                        imageVector = if (sortDirection == _root_ide_package_.com.sidspace.stary.ui.enum.SortDirection.ASCENDING) Icons.Default.ArrowDownward else Icons.Default.ArrowUpward,
                        contentDescription = "",
                        tint = _root_ide_package_.com.sidspace.stary.ui.uikit.Purple40
                    )
            },
            selected = sortType == _root_ide_package_.com.sidspace.stary.ui.enum.SortType.NAME,

            )

        FilterChip(
            modifier = Modifier,
            shape = RoundedCornerShape(15.dp),
            onClick = {
                if (sortType == _root_ide_package_.com.sidspace.stary.ui.enum.SortType.RATING) toggleSortDirection()

                setSortType(_root_ide_package_.com.sidspace.stary.ui.enum.SortType.RATING)

                sortList()
            },
            label = {
                Text("Оценка")
            },
            leadingIcon = {
                if (sortType == _root_ide_package_.com.sidspace.stary.ui.enum.SortType.RATING)
                    Icon(
                        imageVector = if (sortDirection == _root_ide_package_.com.sidspace.stary.ui.enum.SortDirection.ASCENDING) Icons.Default.ArrowDownward else Icons.Default.ArrowUpward,
                        contentDescription = "",
                        tint = _root_ide_package_.com.sidspace.stary.ui.uikit.Purple40
                    )
            },
            selected = sortType == _root_ide_package_.com.sidspace.stary.ui.enum.SortType.RATING,

            )

        FilterChip(
            modifier = Modifier,
            shape = RoundedCornerShape(15.dp),
            onClick = {
                if (sortType == _root_ide_package_.com.sidspace.stary.ui.enum.SortType.DATE) toggleSortDirection()
                setSortType(_root_ide_package_.com.sidspace.stary.ui.enum.SortType.DATE)
                sortList()
            },
            label = {
                Text("Дата выхода")
            },
            leadingIcon = {
                if (sortType == _root_ide_package_.com.sidspace.stary.ui.enum.SortType.DATE)
                    Icon(
                        imageVector = if (sortDirection == _root_ide_package_.com.sidspace.stary.ui.enum.SortDirection.ASCENDING) Icons.Default.ArrowDownward else Icons.Default.ArrowUpward,
                        contentDescription = "",
                        tint = _root_ide_package_.com.sidspace.stary.ui.uikit.Purple40
                    )
            },
            selected = sortType == _root_ide_package_.com.sidspace.stary.ui.enum.SortType.DATE,

            )
    }
}