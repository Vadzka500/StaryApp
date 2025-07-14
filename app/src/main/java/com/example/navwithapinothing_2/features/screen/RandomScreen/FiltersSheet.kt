package com.example.navwithapinothing_2.features.screen.RandomScreen

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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.navwithapinothing_2.models.collection.CollectionMovie
import com.example.navwithapinothing_2.features.screen.MoviesListScreen.ListCollectionResult
import com.example.navwithapinothing_2.features.theme.poppinsFort
import com.example.navwithapinothing_2.utils.RandomFiltersOption
import java.util.Calendar
import kotlin.math.roundToInt


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FiltersPopup(
    modifier: Modifier = Modifier,
    filters: RandomFiltersOption,
    listOfCollectionResult: ListCollectionResult,
    filerIntent: (intent: RandomIntent) -> Unit
) {
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = false,
    )


    ModalBottomSheet(
        modifier = Modifier.fillMaxHeight(), sheetState = sheetState, onDismissRequest = {
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
    listOfCollectionResult: ListCollectionResult,
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
                text = "Тип",
                fontWeight = FontWeight.SemiBold,
                fontFamily = poppinsFort,
                fontSize = 16.sp
            )

            FlowRow() {

                RandomFiltersOption.listOfTypes.toList().forEachIndexed { index, it ->

                    FilterChip(
                        modifier = Modifier.padding(5.dp),
                        shape = RoundedCornerShape(15.dp),
                        onClick = {
                            if (index != 0) {
                                listType[it.first] = !listType[it.first]!!

                                if (!listType[it.first]!!) {
                                    checkListOnEmpty(listType, RandomFiltersOption.listOfTypes.map { it.key })
                                    filerIntent(RandomIntent.FilterIntent.RemoveType(it.first))
                                } else {
                                    filerIntent(RandomIntent.FilterIntent.AddType(Pair(it.first, it.second)))
                                    listType[RandomFiltersOption.listOfTypes.map { it.key }[0]] = false
                                }

                            } else {
                                filerIntent(RandomIntent.FilterIntent.ClearTypes)
                                uncheckList(listType, RandomFiltersOption.listOfTypes.map { it.key })
                            }
                            checkListOnFull(listType, RandomFiltersOption.listOfTypes.map { it.key })
                        },
                        label = {
                            Text(it.first)
                        },
                        selected = listType[it.first]!!,

                        )
                }
            }

            Text(
                "Жанр", fontWeight = FontWeight.SemiBold, fontFamily = poppinsFort, fontSize = 16.sp
            )




            FlowRow() {
                RandomFiltersOption.listOfGenres.forEachIndexed { index, it ->

                    FilterChip(
                        modifier = Modifier.padding(5.dp),
                        onClick = {

                            if (index != 0) {
                                listGenres[it] = !listGenres[it]!!

                                if (!listGenres[it]!!) {
                                    checkListOnEmpty(listGenres, RandomFiltersOption.listOfGenres)
                                    filerIntent(RandomIntent.FilterIntent.RemoveGenre(it))
                                } else {
                                    listGenres[RandomFiltersOption.listOfGenres[0]] = false
                                    filerIntent(RandomIntent.FilterIntent.AddGenre(it))
                                }

                            } else {
                                uncheckList(listGenres, RandomFiltersOption.listOfGenres)
                                filerIntent(RandomIntent.FilterIntent.ClearGenres)
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
                "Оценка кп",
                fontWeight = FontWeight.SemiBold,
                fontFamily = poppinsFort,
                fontSize = 16.sp
            )
            var sliderPosition by remember { mutableStateOf(1f..10f) }
            Column(modifier = Modifier.padding(horizontal = 24.dp)) {
                RangeSlider(
                    value = sliderPosition,
                    steps = 8,
                    onValueChange = { range -> sliderPosition = range },
                    valueRange = 1f..10f,
                    onValueChangeFinished = {
                        filerIntent(
                            RandomIntent.FilterIntent.SetScore(
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
                "Дата релиза",
                fontWeight = FontWeight.SemiBold,
                fontFamily = poppinsFort,
                fontSize = 16.sp
            )

            val range = 1874f..Calendar.getInstance().get(Calendar.YEAR).toFloat()

            var sliderYears by remember { mutableStateOf(range) }
            Column(modifier = Modifier.padding(horizontal = 15.dp)) {
                RangeSlider(
                    value = sliderYears,
                    steps = (range.endInclusive - range.start).toInt() - 2,
                    onValueChange = { range -> sliderYears = range },
                    valueRange = range,
                    onValueChangeFinished = {
                        filerIntent(
                            RandomIntent.FilterIntent.SetYears(
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

                ListCollectionResult.Error -> {

                }

                ListCollectionResult.Loading -> {

                }

                is ListCollectionResult.Success -> {

                    Text(
                        text = "Популярные категории",
                        fontWeight = FontWeight.SemiBold,
                        fontFamily = poppinsFort,
                        fontSize = 16.sp
                    )


                    val listOfCollection = remember {
                        mutableStateMapOf<CollectionMovie, Boolean>().apply {
                            data.data.onEach { movie ->
                                put(movie, false)
                            }
                        }
                    }


                    FlowRow() {
                        listOfCollection.map { it.key }.sortedBy { it.name.length }
                            .forEachIndexed { index, it ->

                                FilterChip(
                                    modifier = Modifier.padding(5.dp),
                                    onClick = {

                                        listOfCollection[it] = !listOfCollection[it]!!


                                    },
                                    label = {
                                        Text(it.name)
                                    },
                                    selected = listOfCollection[it] == true,

                                    )
                            }
                    }
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