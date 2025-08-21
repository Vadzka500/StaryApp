package com.sidspace.stary.person.presentation.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sidspace.stary.domain.model.Result
import com.example.domain.usecase.movie.GetMoviesByPersonIdUseCase
import com.example.domain.usecase.person.GetPersonByIdUseCase
import com.sidspace.stary.ui.mapper.toMovieData
import com.sidspace.stary.ui.mapper.toPersonUi
import com.example.ui.model.ResultData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class PersonViewModel @Inject constructor(
    private val getPersonByIdUseCase: GetPersonByIdUseCase,
    private val getMoviesByPersonIdUseCase: GetMoviesByPersonIdUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(PersonState())
    val state = _state.asStateFlow()

    private val _effect = MutableSharedFlow<PersonEffect>()
    val effect = _effect.asSharedFlow()

    fun onIntent(intent: PersonIntent) {

        when (intent) {
            is PersonIntent.LoadPersonData -> {
                initData(intent.id)
            }

            is PersonIntent.OnSelectMovie -> {
                onSelectMovie(intent.id)
            }

            PersonIntent.OnError -> {
                toErrorScreen()
            }
        }
    }


    fun initData(id: Long) {
        viewModelScope.launch {
            combine(
                getPersonByIdUseCase(id),
                getMoviesByPersonIdUseCase(id)
            ) { person, movies -> Pair(person, movies) }.collect { (person, movies) ->
                if (person is Result.Success) {
                    _state.update { it.copy(person = person.data.toPersonUi()) }
                }

                if (movies is Result.Success) {
                    val list = movies.data.map{ it.toMovieData()}
                    _state.update {
                        it.copy(listOfMovie = ResultData.Success(list.filter { !it.isSeries }
                            .sortedByDescending { movie -> movie.scoreKp }))
                    }
                    _state.update {
                        it.copy(listOfSerials = ResultData.Success(list.filter { it.isSeries }
                            .sortedByDescending { movie -> movie.scoreKp }))
                    }
                }else if (movies is Result.Error){
                    _state.update {
                        it.copy(listOfMovie = ResultData.Error)
                    }
                    _state.update {
                        it.copy(listOfSerials = ResultData.Error)
                    }
                }
            }
        }
    }

    private fun toErrorScreen(){
        viewModelScope.launch {
            _effect.emit(PersonEffect.ToErrorScreen)
        }
    }

    fun onSelectMovie(id: Long){
        viewModelScope.launch {
            _effect.emit(PersonEffect.OnSelectMovie(id))
        }
    }
}