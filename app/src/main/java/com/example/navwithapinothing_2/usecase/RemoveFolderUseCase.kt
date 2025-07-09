package com.example.navwithapinothing_2.usecase

import com.example.navwithapinothing_2.data.MovieDatabaseRepository
import com.example.navwithapinothing_2.data.ResultDb
import com.example.navwithapinothing_2.database.models.Folder
import javax.inject.Inject

/**
 * @Author: Vadim
 * @Date: 09.07.2025
 */
class RemoveFolderUseCase @Inject constructor(private val repository: MovieDatabaseRepository) {

    suspend operator fun invoke(folder: Folder): ResultDb<Unit> = repository.removeFolder(folder)
}