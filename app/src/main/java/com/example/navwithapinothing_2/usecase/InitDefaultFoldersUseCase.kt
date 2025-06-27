package com.example.navwithapinothing_2.usecase

import com.example.navwithapinothing_2.data.MovieDatabaseRepository
import com.example.navwithapinothing_2.utils.FolderDefaults
import javax.inject.Inject

/**
 * @Author: Vadim
 * @Date: 23.06.2025
 */
class InitDefaultFoldersUseCase @Inject constructor(private val repository: MovieDatabaseRepository) {
    suspend operator fun invoke(){
        println("count = " + repository.getFoldersCount())
        if(repository.getFoldersCount() == 0L){

            repository.addFolders(FolderDefaults.defaultFolders.reversed())
        }
    }
}