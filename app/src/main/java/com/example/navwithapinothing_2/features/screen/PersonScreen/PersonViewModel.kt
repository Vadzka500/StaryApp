package com.example.navwithapinothing_2.features.screen.PersonScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moviesapi.models.Response
import com.example.moviesapi.models.movie.MovieDTO
import com.example.moviesapi.models.movie.Person
import com.example.navwithapinothing_2.data.MovieRepository
import com.example.navwithapinothing_2.data.Result
import com.example.navwithapinothing_2.models.common.ListMoviesResult
import com.example.navwithapinothing_2.usecase.GetMoviesByPersonIdUseCase
import com.example.navwithapinothing_2.usecase.GetPersonByIdUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * @Author: Vadim
 * @Date: 09.07.2025
 */
@HiltViewModel
class PersonViewModel @Inject constructor(
    private val movieRepository: MovieRepository,
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
        }
    }


    fun initData(id: Long) {
        viewModelScope.launch {
            combine(
                getPersonByIdUseCase(id),
                getMoviesByPersonIdUseCase(id)
            ) { person, movies -> Pair(person, movies) }.collect { (person, movies) ->
                if (person is Result.Success<*>) {
                    _state.update { it.copy(person = person.data as Person?) }
                }

                if (movies is Result.Success<*>) {
                    val list = ((movies.data as Response<*>).docs as List<MovieDTO>)
                    _state.update {
                        it.copy(listOfMovie = ListMoviesResult.Success(list.filter { !it.isSeries!! }
                            .sortedByDescending { movie -> movie.votes?.kp }))
                    }
                    _state.update {
                        it.copy(listOfSerials = ListMoviesResult.Success(list.filter { it.isSeries!! }
                            .sortedByDescending { movie -> movie.votes?.kp }))
                    }
                }
            }
        }
    }

    fun onSelectMovie(id: Long){
        viewModelScope.launch {
            _effect.emit(PersonEffect.OnSelectMovie(id))
        }
    }
}