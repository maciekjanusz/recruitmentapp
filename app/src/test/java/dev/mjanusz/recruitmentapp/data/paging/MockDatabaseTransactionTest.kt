package dev.mjanusz.recruitmentapp.data.paging

import androidx.room.withTransaction
import dev.mjanusz.recruitmentapp.data.local.AppDatabase
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.slot
import org.junit.Before

open class MockDatabaseTransactionTest {

    val mockDatabase = mockk<AppDatabase> { } // MockK!

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        mockkStatic(
            "androidx.room.RoomDatabaseKt"
        )
        val transactionLambda = slot<suspend () -> Any>()
        coEvery { mockDatabase.withTransaction(capture(transactionLambda)) } coAnswers {
            transactionLambda.captured.invoke()
        }
    }
}