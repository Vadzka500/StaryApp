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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sidspace.stary.domain.RandomFiltersOption
import com.sidspace.stary.random.presentation.model.CollectionRandomUi

import com.sidspace.stary.random.presentation.screen.RandomIntent
import com.sidspace.stary.random.presentation.screen.RandomIntent.FilterIntent.*
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
        modifier = modifier.fillMaxHeight(), sheetState = sheetState, onDismissRequest = {
            filerIntent(RandomIntent.SetShowFilters(false))
        }) {

        ListFilters(
            filters = filters,
            listOfCollectionResult = listOfCollectionResult,
            filerIntent = filerIntent
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

    val listType = remember {
        mutableStateMapOf(*RandomFiltersOption.listOfTypes.map { it.key }.mapIndexed { index, it ->
            if (index == 0) {
                if (filters.listOfType == null) it to true
                else it to false
            } else if (filters.listOfType?.keys?.contains(
                    it
                ) == true
            ) it to true else it to false
        }.toTypedArray())
    }

    val listGenres = remember {
        mutableStateMapOf(*RandomFiltersOption.listOfGenres.mapIndexed { index, it ->
            if (index == 0) {
                if (filters.listOfGenres == null) it to true
                else it to false
            } else if (filters.listOfGenres?.contains(it) == true) it to true else it to false
        }.toTypedArray())
    }


    LazyColumn(modifier = Modifier.padding(horizontal = 11.dp)) {
        item {
            Text(
                modifier = Modifier.padding(horizontal = 5.dp),
                text = stringResource(R.string.type),
                fontWeight = FontWeight.SemiBold,
                fontFamily = poppinsFort,
                fontSize = 16.sp
            )

            FlowRow() {

                RandomFiltersOption.listOfTypes.toList().forEachIndexed { index, it ->

                    FilterChip(
                        modifier = Modifier.padding(horizontal = 5.dp),
                        shape = RoundedCornerShape(15.dp),
                        onClick = {
                            if (index != 0) {
                                listType[it.first] = !listType[it.first]!!

                                if (!listType[it.first]!!) {
                                    checkListOnEmpty(
                                        listType,
                                        RandomFiltersOption.listOfTypes.map { it.key })
                                    filerIntent(RemoveType(it.first))
                                } else {
                                    filerIntent(
                                        AddType(
                                            Pair(
                                                it.first,
                                                it.second
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
                                    RandomFiltersOption.listOfTypes.map { it.key })
                            }
                            checkListOnFull(
                                listType,
                                RandomFiltersOption.listOfTypes.map { it.key })
                        },
                        label = {
                            Text(it.first)
                        },
                        selected = listType[it.first]!!,

                        )
                }
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




            FlowRow() {
                RandomFiltersOption.listOfGenres.forEachIndexed { index, it ->

                    FilterChip(
                        modifier = Modifier.padding(horizontal = 5.dp),
                        onClick = {

                            if (index != 0) {
                                listGenres[it] = !listGenres[it]!!

                                if (!listGenres[it]!!) {
                                    checkListOnEmpty(listGenres, RandomFiltersOption.listOfGenres)
                                    filerIntent(RemoveGenre(it))
                                } else {
                                    listGenres[RandomFiltersOption.listOfGenres[0]] = false
                                    filerIntent(AddGenre(it))
                                }

                            } else {
                                uncheckList(listGenres, RandomFiltersOption.listOfGenres)
                                filerIntent(ClearGenres)
                            }
                            checkListOnFull(listGenres, RandomFiltersOption.listOfGenres)
                        },
                        label = {
                            Text(it)
                        },
                        selected = listGenres[it]!!,

                        )
                }
            }

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
                if (filters.listOfScore != null)
                    mutableStateOf(filters.listOfScore!![0].toFloat()..filters.listOfScore!![1].toFloat())
                else
                    mutableStateOf(1f..10f)
            }

            var scoreRange = 1f..10f

            Column(modifier = Modifier
                .padding(horizontal = 5.dp)
                .padding(top = 8.dp)) {
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
                                } else
                                    listOf(
                                        sliderPosition.start.roundToInt().toString(),
                                        sliderPosition.endInclusive.roundToInt().toString()
                                    )
                            )
                        )
                    },
                )
                Text(
                    text = (sliderPosition.start.roundToInt()
                        .toString() + "-" + sliderPosition.endInclusive.roundToInt()),
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }

            Text(
                modifier = Modifier
                    .padding(horizontal = 5.dp)
                    .padding(top = 8.dp),
                text = stringResource(R.string.date),
                fontWeight = FontWeight.SemiBold,
                fontFamily = poppinsFort,
                fontSize = 16.sp
            )


            val value = if (filters.years != null)
                filters.years!![0].toFloat()..filters.years!![1].toFloat()
            else
                1874f..Calendar.getInstance().get(Calendar.YEAR).toFloat()

            val range = 1874f..Calendar.getInstance().get(Calendar.YEAR).toFloat()

            var sliderYears by remember {
                mutableStateOf(value)
            }

            Column(modifier = Modifier
                .padding(horizontal = 5.dp)
                .padding(top = 8.dp)) {
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
                                } else
                                    listOf(
                                        sliderYears.start.roundToInt().toString(),
                                        sliderYears.endInclusive.roundToInt().toString()
                                    )
                            )
                        )
                    },
                )
                Text(
                    text = (sliderYears.start.roundToInt()
                        .toString() + "-" + sliderYears.endInclusive.roundToInt()),
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }


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
                                    if (filters.listOfCollection!!.contains(movie.slug))
                                        put(movie, true)
                                    else
                                        put(movie, false)
                                } else
                                    put(movie, false)
                            }
                        }
                    }


                    FlowRow() {
                        listOfCollection.map { it.key }.sortedBy { it.name.length }
                            .forEachIndexed { index, it ->

                                FilterChip(
                                    modifier = Modifier.padding(horizontal = 5.dp),
                                    onClick = {

                                        //listOfCollection[it] = !listOfCollection[it]!!
                                        //filters.listOfCollection?.toMutableList().add("") ?: emptyList()
                                        filerIntent(ToggleCollection(it.slug))

                                    },
                                    label = {
                                        Text(it.name)
                                    },
                                    selected = filters.listOfCollection?.contains(it.slug) == true,

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