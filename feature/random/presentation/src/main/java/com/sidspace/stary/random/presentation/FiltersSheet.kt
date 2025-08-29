package com.sidspace.stary.random.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.RangeSlider
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.compose.runtime.toMutableStateMap
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sidspace.stary.domain.RandomFiltersOption
import com.sidspace.stary.random.presentation.model.CollectionRandomUi
import com.sidspace.stary.random.presentation.screen.RandomIntent
import com.sidspace.stary.random.presentation.screen.RandomIntent.FilterIntent.AddGenre
import com.sidspace.stary.random.presentation.screen.RandomIntent.FilterIntent.AddType
import com.sidspace.stary.random.presentation.screen.RandomIntent.FilterIntent.ClearGenres
import com.sidspace.stary.random.presentation.screen.RandomIntent.FilterIntent.ClearTypes
import com.sidspace.stary.random.presentation.screen.RandomIntent.FilterIntent.RemoveGenre
import com.sidspace.stary.random.presentation.screen.RandomIntent.FilterIntent.RemoveType
import com.sidspace.stary.random.presentation.screen.RandomIntent.FilterIntent.SetScore
import com.sidspace.stary.random.presentation.screen.RandomIntent.FilterIntent.SetYears
import com.sidspace.stary.random.presentation.screen.RandomIntent.FilterIntent.ToggleCollection
import com.sidspace.stary.random.presentation.screen.RandomState
import com.sidspace.stary.ui.model.ResultData
import com.sidspace.stary.ui.uikit.poppinsFort
import java.util.Calendar
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FiltersPopup(
    filters: RandomFiltersOption,
    listOfCollectionResult: ResultData<List<CollectionRandomUi>>,
    modifier: Modifier = Modifier,
    filerIntent: (intent: RandomIntent) -> Unit
) {
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = false,
    )

    ModalBottomSheet(
        modifier = modifier.fillMaxHeight(),
        sheetState = sheetState,
        onDismissRequest = {
            filerIntent(RandomIntent.SetShowFilters(false))
        }
    ) {
        ListFilters(
            filters = filters,
            listOfCollectionResult = listOfCollectionResult,
            filerIntent = filerIntent
        )
    }
}

@Composable
fun getTypesMap(filters: RandomFiltersOption): SnapshotStateMap<String, Boolean> =
    remember {
        RandomFiltersOption.listOfTypes
            .map { it.key }
            .mapIndexed { index, item ->
                item to when {
                    index == 0 && filters.listOfType == null -> true
                    filters.listOfType?.keys?.contains(item) == true -> true
                    else -> false
                }
            }
            .toMutableStateMap()
    }

@Composable
fun TypeFilters(filters: RandomFiltersOption, filerIntent: (RandomIntent.FilterIntent) -> Unit) {

    val listType = getTypesMap(filters)

    Text(
        modifier = Modifier.padding(horizontal = 5.dp),
        text = stringResource(R.string.type),
        fontWeight = FontWeight.SemiBold,
        fontFamily = poppinsFort,
        fontSize = 16.sp
    )

    FlowRow {
        RandomFiltersOption.listOfTypes.toList().forEachIndexed { index, item ->

            FilterChip(
                modifier = Modifier.padding(horizontal = 5.dp),
                shape = RoundedCornerShape(15.dp),
                onClick = {
                    if (index != 0) {
                        listType[item.first] = !listType[item.first]!!

                        if (!listType[item.first]!!) {
                            checkListOnEmpty(
                                listType,
                                RandomFiltersOption.listOfTypes.map { it.key }
                            )
                            filerIntent(RemoveType(item.first))
                        } else {
                            filerIntent(
                                AddType(
                                    Pair(
                                        item.first,
                                        item.second
                                    )
                                )
                            )
                            listType[RandomFiltersOption.listOfTypes.map { it.key }[0]] =
                                false
                        }
                    } else {
                        filerIntent(ClearTypes)
                        uncheckList(
                            listType,
                            RandomFiltersOption.listOfTypes.map { it.key }
                        )
                    }
                    checkListOnFull(
                        listType,
                        RandomFiltersOption.listOfTypes.map { it.key }
                    )
                },
                label = {
                    Text(item.first)
                },
                selected = listType[item.first]!!
            )
        }
    }
}

@Composable
fun GenresFilters(filters: RandomFiltersOption, filerIntent: (RandomIntent.FilterIntent) -> Unit) {

    val listGenres = remember {
        RandomFiltersOption.listOfGenres.mapIndexed { index, item ->
            when {
                index == 0 && filters.listOfGenres == null -> item to true
                filters.listOfGenres?.contains(item) == true -> item to true
                else -> item to false
            }
        }.toMutableStateMap()
    }

    Text(
        modifier = Modifier
            .padding(horizontal = 5.dp)
            .padding(top = 8.dp),
        text = stringResource(R.string.genre),
        fontWeight = FontWeight.SemiBold,
        fontFamily = poppinsFort,
        fontSize = 16.sp
    )

    FlowRow {
        RandomFiltersOption.listOfGenres.forEachIndexed { index, item ->

            FilterChip(
                modifier = Modifier.padding(horizontal = 5.dp),
                onClick = {
                    if (index != 0) {
                        listGenres[item] = !listGenres[item]!!

                        if (!listGenres[item]!!) {
                            checkListOnEmpty(listGenres, RandomFiltersOption.listOfGenres)
                            filerIntent(RemoveGenre(item))
                        } else {
                            listGenres[RandomFiltersOption.listOfGenres[0]] = false
                            filerIntent(AddGenre(item))
                        }
                    } else {
                        uncheckList(listGenres, RandomFiltersOption.listOfGenres)
                        filerIntent(ClearGenres)
                    }
                    checkListOnFull(listGenres, RandomFiltersOption.listOfGenres)
                },
                label = {
                    Text(item)
                },
                selected = listGenres[item]!!
            )
        }
    }
}

@Composable
fun ScoreFilters(filters: RandomFiltersOption, filerIntent: (RandomIntent.FilterIntent) -> Unit) {
    Text(
        modifier = Modifier
            .padding(horizontal = 5.dp)
            .padding(top = 8.dp),
        text = stringResource(R.string.score_kp),
        fontWeight = FontWeight.SemiBold,
        fontFamily = poppinsFort,
        fontSize = 16.sp
    )
    var sliderPosition by remember {
        if (filters.listOfScore != null) {
            mutableStateOf(filters.listOfScore!![0].toFloat()..filters.listOfScore!![1].toFloat())
        } else {
            mutableStateOf(RandomState.SCORE_RANGE)
        }
    }

    val scoreRange = RandomState.SCORE_RANGE

    Column(
        modifier = Modifier
            .padding(horizontal = 5.dp)
            .padding(top = 8.dp)
    ) {
        RangeSlider(
            value = sliderPosition,
            steps = 8,
            onValueChange = { range -> sliderPosition = range },
            valueRange = scoreRange,
            onValueChangeFinished = {
                filerIntent(
                    SetScore(
                        if (scoreRange == sliderPosition) {
                            null
                        } else {
                            listOf(
                                sliderPosition.start.roundToInt().toString(),
                                sliderPosition.endInclusive.roundToInt().toString()
                            )
                        }
                    )
                )
            },
        )
        Text(
            text = (
                    sliderPosition.start.roundToInt()
                        .toString() + "-" + sliderPosition.endInclusive.roundToInt()
                    ),
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
    }
}

@Composable
fun DateFilters(filters: RandomFiltersOption, filerIntent: (RandomIntent.FilterIntent) -> Unit) {
    Text(
        modifier = Modifier
            .padding(horizontal = 5.dp)
            .padding(top = 8.dp),
        text = stringResource(R.string.date),
        fontWeight = FontWeight.SemiBold,
        fontFamily = poppinsFort,
        fontSize = 16.sp
    )

    val value = if (filters.years != null) {
        filters.years!![0].toFloat()..filters.years!![1].toFloat()
    } else {
        RandomState.OLDEST_YEAR_OF_MOVIE..Calendar.getInstance().get(Calendar.YEAR).toFloat()
    }

    val range = RandomState.OLDEST_YEAR_OF_MOVIE..Calendar.getInstance().get(Calendar.YEAR).toFloat()

    var sliderYears by remember {
        mutableStateOf(value)
    }

    Column(
        modifier = Modifier
            .padding(horizontal = 5.dp)
            .padding(top = 8.dp)
    ) {
        RangeSlider(
            value = sliderYears,
            steps = (range.endInclusive - range.start).toInt() - 2,
            onValueChange = { range -> sliderYears = range },
            valueRange = range,
            onValueChangeFinished = {
                filerIntent(
                    SetYears(
                        if (range == sliderYears) {
                            null
                        } else {
                            listOf(
                                sliderYears.start.roundToInt().toString(),
                                sliderYears.endInclusive.roundToInt().toString()
                            )
                        }
                    )
                )
            },
        )
        Text(
            text = (
                    sliderYears.start.roundToInt()
                        .toString() + "-" + sliderYears.endInclusive.roundToInt()
                    ),
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
    }
}

@OptIn(ExperimentalLayoutApi::class, ExperimentalMaterial3Api::class)
@Composable
fun ListFilters(
    filters: RandomFiltersOption,
    listOfCollectionResult: ResultData<List<CollectionRandomUi>>,
    filerIntent: (RandomIntent.FilterIntent) -> Unit
) {


    LazyColumn(modifier = Modifier.padding(horizontal = 11.dp)) {
        item {

            TypeFilters(filters, filerIntent)

            GenresFilters(filters, filerIntent)

            ScoreFilters(filters, filerIntent)

            DateFilters(filters, filerIntent)

            when (val data = listOfCollectionResult) {
                ResultData.Error -> {
                }

                ResultData.Loading -> {
                }

                is ResultData.Success -> {
                    Text(
                        modifier = Modifier
                            .padding(horizontal = 5.dp)
                            .padding(top = 8.dp),
                        text = stringResource(R.string.popular_categories),
                        fontWeight = FontWeight.SemiBold,
                        fontFamily = poppinsFort,
                        fontSize = 16.sp
                    )

                    val listOfCollection = remember {
                        mutableStateMapOf<CollectionRandomUi, Boolean>().apply {
                            data.data.onEach { movie ->
                                if (filters.listOfCollection != null) {
                                    if (filters.listOfCollection!!.contains(movie.slug)) {
                                        put(movie, true)
                                    } else {
                                        put(movie, false)
                                    }
                                } else {
                                    put(movie, false)
                                }
                            }
                        }
                    }

                    FlowRow {
                        listOfCollection.map { it.key }.sortedBy { it.name.length }
                            .forEachIndexed { index, item ->

                                FilterChip(
                                    modifier = Modifier.padding(horizontal = 5.dp),
                                    onClick = {
                                        filerIntent(ToggleCollection(item.slug))
                                    },
                                    label = {
                                        Text(item.name)
                                    },
                                    selected = filters.listOfCollection?.contains(item.slug) == true,

                                    )
                            }
                    }
                }

                ResultData.None -> {
                }
            }
        }
    }
}

private fun checkListOnEmpty(list: MutableMap<String, Boolean>, listOFMainData: List<String>) {
    if (!list.any { it.value }) {
        uncheckList(list, listOFMainData)
    }
}

private fun checkListOnFull(list: MutableMap<String, Boolean>, listOFMainData: List<String>) {
    if (list.filterValues { it }.size == list.size - 1) {
        uncheckList(list, listOFMainData)
    }
}

private fun uncheckList(list: MutableMap<String, Boolean>, listOFMainData: List<String>) {
    listOFMainData.forEachIndexed { index, _ ->
        list[listOFMainData[index]] = index == 0
    }
}
