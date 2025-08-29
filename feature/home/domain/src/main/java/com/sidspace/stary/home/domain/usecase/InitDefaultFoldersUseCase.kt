package com.sidspace.stary.home.domain.usecase


import com.sidspace.stary.home.domain.repository.HomeRepository
import javax.inject.Inject


class InitDefaultFoldersUseCase @Inject constructor(private val repository: HomeRepository) {
    suspend operator fun invoke() {
        repository.initDefaultFolders()
    }
}
