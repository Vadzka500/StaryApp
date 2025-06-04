package com.example.navwithapinothing_2.api

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.moviesapi.models.movie.MovieDTO
import com.example.navwithapinothing_2.data.Result

class MoviePagingSource(
    private val newsApiService: MovieApi,
): PagingSource<Int, MovieDTO>() {
    override fun getRefreshKey(state: PagingState<Int, MovieDTO>): Int? {
        println("refresh")
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, MovieDTO> {
        return try {
            val page = params.key ?: 1
            val response = newsApiService.getAllMovies(page = page)

            val prevKey = if (page == 1) null else page.minus(1)
            val nextKey = if (response.body()!!.docs.isEmpty()) null else page.plus(1)
            println("prev key = " + prevKey)
            println("next key = " + nextKey)
            println("size = " + response.body()!!.docs.size)
            LoadResult.Page(
                data = response.body()!!.docs,
                prevKey = prevKey,
                nextKey = nextKey,
            )
        } catch (e: Exception) {
            println("error = $e")
            LoadResult.Error(e)
        }
    }
}