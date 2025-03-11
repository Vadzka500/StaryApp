package com.example.navwithapinothing_2.ui.screen.slider

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.RangeSlider

import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.navwithapinothing_2.data.Result
import com.example.navwithapinothing_2.models.Filter
import com.example.navwithapinothing_2.models.collection.CollectionMovie
import com.example.navwithapinothing_2.ui.screen.MoviesListScreen.MovieViewModel
import java.util.Calendar
import kotlin.math.roundToInt

val listOfTypes = listOf("Все", "Фильмы", "Сериалы", "Мультфильмы", "Мультсериалы", "Аниме")
val listOfGenres = listOf(
    "Любой жанр",
    "Комедия",
    "Ужасы",
    "Триллер",
    "Детектив",
    "Боевик",
    "Вестерн",
    "Драма",
    "Мелодрама",
    "Приключения",
    "Фантастика",
    "Криминал",
    "Биография",
    "Военный",
    "Документальный"
)

val listOfYears = listOf("2020-"+Calendar.getInstance().get(Calendar.YEAR), "2010-2020","2000-2010","1990-2000", "1980-1990")
val listOfYearsOld = listOf("1970-1980", "1960-1970","1950-1960","1940-1950")
val listOfYearsUi = listOf("За все время") + listOfYears + "До 1980"


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FiltersPopup(
    modifier: Modifier = Modifier,
    hideFilters: (List<String>, List<String>, List<CollectionMovie>, String) -> Unit,
    movieViewModel: MovieViewModel = viewModel(),
    filter: Filter?
) {
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = false,
    )
    movieViewModel.getCollections()
    val state = movieViewModel.state_collection.collectAsState()

    /* val listOfCollection = remember {
         mutableStateOf<List<CollectionMovie>>(listOf())
     }*/

    val listOfCollection = remember {
        mutableStateMapOf<CollectionMovie, Boolean>()
    }

    val listType = remember {
        mutableStateMapOf(
            *listOfTypes.mapIndexed { index, it -> if (index == 0) it to true else it to false }
                .toTypedArray()
        )
    }

    val listGenres = remember {
        mutableStateMapOf(
            *listOfGenres.mapIndexed { index, it -> if (index == 0) it to true else it to false }
                .toTypedArray()
        )
    }

    /* val listCollectionsMap = remember {
         mutableStateMapOf(
             *listOfCollection.value.mapIndexed { index, it -> if (index == 0) it.id to true else it.id to false }
                 .toTypedArray()
         )
     }*/

    when (val data = state.value) {
        Result.Error -> {}
        Result.Loading -> {}
        is Result.Success<*> -> {
            println("data = " + (data.data as List<CollectionMovie>))
            // listOfCollection.value = (data.data as List<CollectionMovie>)
            listOfCollection.putAll(data.data.map { it to false }
                .toTypedArray())
        }
    }

    val testFieldStateBegin = remember { mutableStateOf(listOfYearsUi.first().toString()) }
    //val testFieldStateEnd = remember { mutableStateOf(listOfYears.last().toString()) }

    ModalBottomSheet(
        modifier = Modifier.fillMaxHeight(),
        sheetState = sheetState,
        onDismissRequest = {
            hideFilters(
                listType.filterValues { it }.map { it.key },
                listGenres.filterValues { it }.map { it.key },
                listOfCollection.filterValues { it }.map { item ->
                    item.key
                },testFieldStateBegin.value)
        }) {

        ListFilters(
            listType = listType,
            listGenres = listGenres,
            listCollections = listOfCollection,
            testFieldStateBegin = testFieldStateBegin

        )
    }
}


@OptIn(ExperimentalLayoutApi::class, ExperimentalMaterial3Api::class)
@Composable
fun ListFilters(
    modifier: Modifier = Modifier,
    listType: MutableMap<String, Boolean>,
    listGenres: MutableMap<String, Boolean>,
    listCollections: MutableMap<CollectionMovie, Boolean>,
    testFieldStateBegin: MutableState<String>
) {


    LazyColumn {
        item {
            Text("Тип")

            FlowRow() {

                listOfTypes.forEachIndexed { index, it ->

                    FilterChip(
                        modifier = Modifier.padding(5.dp),
                        shape = RoundedCornerShape(15.dp),
                        onClick = {
                            if (index != 0) {
                                listType[it] = !listType[it]!!

                                if (!listType[it]!!) {
                                    checkListOnEmpty(listType, listOfTypes)
                                } else listType[listOfTypes[0]] = false

                            } else {
                                uncheckList(listType, listOfTypes)
                            }
                            checkListOnFull(listType, listOfTypes)
                        },
                        label = {
                            Text(it)
                        },
                        selected = listType[it]!!,

                        )
                }
            }

            Text("Жанр")




            FlowRow() {
                listOfGenres.forEachIndexed { index, it ->

                    FilterChip(
                        modifier = Modifier.padding(5.dp),
                        onClick = {

                            if (index != 0) {
                                listGenres[it] = !listGenres[it]!!

                                if (!listGenres[it]!!) {
                                    checkListOnEmpty(listGenres, listOfGenres)
                                } else listGenres[listOfGenres[0]] = false

                            } else {
                                uncheckList(listGenres, listOfGenres)
                            }
                            checkListOnFull(listGenres, listOfGenres)
                        },
                        label = {
                            Text(it)
                        },
                        selected = listGenres[it]!!,

                        )
                }
            }

            Text("Оценка кп")
            var sliderPosition by remember { mutableStateOf(1f..10f) }
            Column(modifier = Modifier.padding(horizontal = 24.dp)) {
                RangeSlider(
                    value = sliderPosition,
                    steps = 8,
                    onValueChange = { range -> sliderPosition = range },
                    valueRange = 1f..10f,
                    onValueChangeFinished = {
                        // launch some business logic update with the state you hold
                        // viewModel.updateSelectedSliderValue(sliderPosition)
                    },
                )
                Text(text = (sliderPosition.start.roundToInt().toString() + "-" + sliderPosition.endInclusive.roundToInt()), modifier = Modifier.align(Alignment.CenterHorizontally))
            }

            Text("Дата релиза")

            val range = 1874f..Calendar.getInstance().get(Calendar.YEAR).toFloat()

            var sliderPosition_1 by remember { mutableStateOf(range) }
            Column(modifier = Modifier.padding(horizontal = 15.dp)) {
                RangeSlider(
                    value = sliderPosition_1,
                    steps = (range.endInclusive - range.start).toInt() - 2,
                    onValueChange = { range -> sliderPosition_1 = range },
                    valueRange = range,
                    onValueChangeFinished = {
                        // launch some business logic update with the state you hold
                        // viewModel.updateSelectedSliderValue(sliderPosition)
                    },
                )
                Text(text = (sliderPosition_1.start.roundToInt().toString() + "-" + sliderPosition_1.endInclusive.roundToInt()), modifier = Modifier.align(Alignment.CenterHorizontally))
            }

            Row() {


                var expanded by remember { mutableStateOf(false) }





                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = !expanded },
                   // modifier = Modifier.weight(1f)
                ) {
                    OutlinedTextField(

                        readOnly = true,
                        value = testFieldStateBegin.value,
                        onValueChange = {},
                        label = { Text(text = "От") },
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                        },
                        colors = OutlinedTextFieldDefaults.colors(),
                        modifier = Modifier
                            .menuAnchor()
                            .fillMaxWidth()
                    )

                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }) {
                        listOfYearsUi.forEach { option: String ->
                            DropdownMenuItem(
                                text = { Text(text = option) },
                                onClick = {
                                    expanded = false
                                    testFieldStateBegin.value = option
                                }
                            )
                        }
                    }
                }

            }

            Text(text = "Популярные категории")

            FlowRow() {
                listCollections.map { it.key }.forEachIndexed { index, it ->
                    //println("item = " + it.toString())
                    FilterChip(
                        modifier = Modifier.padding(5.dp),
                        onClick = {
                            println("item1 = " + it.id)
                            println("item2 = " + listCollections.size)
                            //if (index != 0) {
                            listCollections[it] = !listCollections[it]!!

                            /*  if (!listCollections[it]!!) {
                                  checkListOnEmpty(
                                      listCollections,
                                      listCollections.map{ it.key.name })
                              } else listCollections[listOfCollections.value[0].id] = false

                          } else {
                              uncheckList(listCollections, listOfCollections.value.map { it.id })
                          }
                          checkListOnFull(listCollections, listOfCollections.value.map { it.id })*/
                        },
                        label = {
                            Text(it.name)
                        },
                        selected = listCollections[it] ?: false,

                        )
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