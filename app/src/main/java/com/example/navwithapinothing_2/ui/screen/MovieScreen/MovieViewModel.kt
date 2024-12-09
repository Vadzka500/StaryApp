package com.example.navwithapinothing_2.ui.screen.MovieScreen

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.moviesapi.models.movie.MovieDTO
import com.example.navwithapinothing_2.data.MovieRepository
import com.example.navwithapinothing_2.data.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieViewModel @Inject constructor(private val movieRepository: MovieRepository) :
    ViewModel() {

    private val _state = MutableStateFlow(Result.Loading)
    val state = _state.asStateFlow()

    private val _state_random = MutableStateFlow<Result>(Result.Loading)
    val state_random = _state_random.asStateFlow()

    init {
        getAll()
    }

    fun getRandom(){
        viewModelScope.launch {
            movieRepository.getRandom().collect{
               _state_random.value = when(it){

                   is Result.Loading ->{
                        it
                   }

                   is Result.Error -> {
                        it
                   }

                   is Result.Success<*> -> {
                       it
                   }

               }

            }
        }
    }

    fun getAll() {

        /*viewModelScope.launch {
            movieRepository.getAll().collect {
                _state.value = when (it) {
                    is Result.Success -> it
                    is Result.Error -> it
                    is Result.Loading -> it
                }
            }
        }*/
    }

    fun getAllPaging(): Flow<PagingData<MovieDTO>> = movieRepository.getAll().cachedIn(viewModelScope)
}