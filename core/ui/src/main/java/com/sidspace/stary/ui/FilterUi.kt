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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sidspace.stary.ui.enums.SortDirection
import com.sidspace.stary.ui.enums.SortType
import com.sidspace.stary.ui.enums.ViewMode
import com.sidspace.stary.ui.uikit.Purple40
import com.sidspace.stary.ui.uikit.poppinsFort


@Suppress("LongMethod")
@Composable
fun FilterSection(
    isVisibleFilter: Boolean,
    viewType: ViewMode,
    isShowSort: Boolean,
    modifier: Modifier = Modifier,
    sortType: SortType = SortType.NONE,
    sortDirection: SortDirection = SortDirection.DESCENDING,
    filterStateCallback: FilterStateCallback
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
                filterStateCallback.onHideFilter()
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
                    stringResource(R.string.view_str), fontWeight = FontWeight.Medium,
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
                            filterStateCallback.setGridView()

                        },
                        label = {
                            Text(
                                stringResource(R.string.view_mode_grid_str),
                                fontWeight = FontWeight.Medium,
                                fontFamily = poppinsFort,
                                fontSize = 14.sp
                            )
                        },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.GridView,
                                contentDescription = null,
                                tint = Purple40
                            )
                        },
                        selected = viewType == ViewMode.GRID,

                        )

                    FilterChip(
                        modifier = Modifier,
                        shape = RoundedCornerShape(15.dp),
                        onClick = {
                            filterStateCallback.setListView()

                        },
                        label = {
                            Text(stringResource(R.string.view_mode_list_str))
                        },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.ViewList,
                                contentDescription = null,
                                tint = Purple40
                            )
                        },
                        selected = viewType == ViewMode.LIST,

                        )
                }
                if (isShowSort) {
                    FilterSort(
                        sortType = sortType,
                        sortDirection = sortDirection,
                        setSortType = filterStateCallback.setSortType,
                        toggleSortDirection = filterStateCallback.toggleSortDirection,
                        sortList = filterStateCallback.sortList
                    )
                }
            }
        }
    }
}

@Composable
fun FilterSort(
    modifier: Modifier = Modifier,
    sortType: SortType,
    sortDirection: SortDirection,
    toggleSortDirection: () -> Unit,
    setSortType: (SortType) -> Unit,
    sortList: () -> Unit
) {

    Text(
        stringResource(R.string.sort_str),
        modifier = modifier.padding(top = 12.dp),
        fontWeight = FontWeight.Medium,
        fontFamily = poppinsFort,
        fontSize = 16.sp
    )

    FlowRow(
        modifier = Modifier.padding(top = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {

        FilterContent(
            label = stringResource(R.string.sort_name_sortbyname_str),
            sortType = sortType,
            targetSortType = SortType.NAME,
            sortDirection = sortDirection,
            toggleSortDirection = {
                toggleSortDirection()
            },
            setSortType = {
                setSortType(it)
            },
            sortList = sortList
        )

        FilterContent(
            label = stringResource(R.string.sort_name_sortbyscore_str),
            sortType = sortType,
            targetSortType = SortType.RATING,
            sortDirection = sortDirection,
            toggleSortDirection = {
                toggleSortDirection()
            },
            setSortType = {
                setSortType(it)
            },
            sortList = sortList
        )

        FilterContent(
            label = stringResource(R.string.sort_name_sortbydate_str),
            sortType = sortType,
            targetSortType = SortType.DATE,
            sortDirection = sortDirection,
            toggleSortDirection = {
                toggleSortDirection()
            },
            setSortType = {
                setSortType(it)
            },
            sortList = sortList
        )

        /*FilterChip(
            modifier = Modifier,
            shape = RoundedCornerShape(15.dp),
            onClick = {
                if (sortType == SortType.NAME) toggleSortDirection()
                setSortType(SortType.NAME)


                sortList()
            },
            label = {
                Text(stringResource(R.string.sort_name_sortbyname_str))
            },
            leadingIcon = {
                if (sortType == SortType.NAME) {
                    Icon(
                        imageVector = if (sortDirection == SortDirection.ASCENDING) {
                            Icons.Default.ArrowDownward
                        } else {
                            Icons.Default.ArrowUpward
                        },
                        contentDescription = null,
                        tint = Purple40
                    )
                }
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
                Text(stringResource(R.string.sort_name_sortbyscore_str))
            },
            leadingIcon = {
                if (sortType == SortType.RATING) {
                    Icon(
                        imageVector = if (sortDirection == SortDirection.ASCENDING) {
                            Icons.Default.ArrowDownward
                        } else {
                            Icons.Default.ArrowUpward
                        },
                        contentDescription = null,
                        tint = Purple40
                    )
                }
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
                Text(stringResource(R.string.sort_name_sortbydate_str))
            },
            leadingIcon = {
                if (sortType == SortType.DATE) {
                    Icon(
                        imageVector = if (sortDirection == SortDirection.ASCENDING) {
                            Icons.Default.ArrowDownward
                        } else {
                            Icons.Default.ArrowUpward
                        },
                        contentDescription = "",
                        tint = Purple40
                    )
                }
            },
            selected = sortType == SortType.DATE,

            )*/
    }
}

@Suppress("LongParameterList")
@Composable
fun FilterContent(
    label: String,
    sortType: SortType,
    targetSortType: SortType,
    sortDirection: SortDirection,
    modifier: Modifier = Modifier,
    toggleSortDirection: () -> Unit,
    setSortType: (SortType) -> Unit,
    sortList: () -> Unit
) {
    FilterChip(
        modifier = modifier,
        shape = RoundedCornerShape(15.dp),
        onClick = {
            if (sortType == targetSortType) toggleSortDirection()
            setSortType(targetSortType)
            sortList()
        },
        label = {
            Text(label)
        },
        leadingIcon = {
            if (sortType == targetSortType) {
                Icon(
                    imageVector = if (sortDirection == SortDirection.ASCENDING) {
                        Icons.Default.ArrowDownward
                    } else {
                        Icons.Default.ArrowUpward
                    },
                    contentDescription = "",
                    tint = Purple40
                )
            }
        },
        selected = sortType == targetSortType,

        )
}

data class FilterStateCallback(
    val toggleSortDirection: () -> Unit,
    val setSortType: (SortType) -> Unit,
    val sortList: () -> Unit,
    val onHideFilter: () -> Unit,
    val setGridView: () -> Unit,
    val setListView: () -> Unit
)
