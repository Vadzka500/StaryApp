package com.example.data

import com.sidspace.stary.account.data.repository.AccountRepositoryImpl
import com.sidspace.stary.data.api.MovieApi
import com.sidspace.stary.data.database.MovieDao
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {

    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    @Test
    fun `dsas`() = runTest {
        val api = mockk<MovieApi>()
        val db = mockk<MovieDao>()
        val repo = AccountRepositoryImpl(api, db)
        repo.getFoldersCount()
    }
}
