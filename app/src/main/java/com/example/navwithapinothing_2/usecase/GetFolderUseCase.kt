package com.example.navwithapinothing_2.usecase

import com.example.navwithapinothing_2.data.MovieDatabaseRepository
import com.example.navwithapinothing_2.data.ResultDb
import com.example.navwithapinothing_2.database.models.Folder
import javax.inject.Inject

/**
 * @Author: Vadim
 * @Date: 24.06.2025
 */
class GetFolderUseCase @Inject constructor(private val repository: MovieDatabaseRepository) {
    suspend operator fun invoke(id: Long) : ResultDb<Folder> = repository.getFolder(id)
}