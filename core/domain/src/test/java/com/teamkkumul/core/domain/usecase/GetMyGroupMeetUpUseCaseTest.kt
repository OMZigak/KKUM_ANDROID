package com.teamkkumul.core.domain.usecase

import com.teamkkumul.core.data.repository.MyGroupRepository
import com.teamkkumul.model.MyGroupMeetUpModel
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.anyBoolean
import org.mockito.Mockito.anyInt
import org.mockito.Mockito.mock
import org.mockito.kotlin.anyOrNull
import org.mockito.kotlin.whenever

class GetMyGroupMeetUpUseCaseTest {
    // 공통 테스트 픽스처
    private lateinit var myGroupRepository: MyGroupRepository
    private lateinit var getMyGroupMeetUpUseCase: GetMyGroupMeetUpUseCase

    // 클래스 내 각 테스트 이전에 호출
    @Before
    fun setUp() {
        myGroupRepository = mock(MyGroupRepository::class.java)
        getMyGroupMeetUpUseCase = GetMyGroupMeetUpUseCase(myGroupRepository)
    }

    @Test
    fun `d-day가_모두_양수_일때는_오름차순으로_정렬된다`() = runTest {
        // Given: d-day가 모두 양수인 약속이 있을 때
        val mockPromises = listOf(
            MyGroupMeetUpModel.Promise(dDay = 3, "테스트 1", "", "", 1),
            MyGroupMeetUpModel.Promise(dDay = 2, "테스트 2", "", "", 2),
            MyGroupMeetUpModel.Promise(dDay = 1, "테스트 3", "", "", 3),
        )

        // Given: myGroupRepository에서 약속들을 성공적으로 반환하도록 설정
        setupMockRepositorySuccess(mockPromises)

        // When: 내 모임의 약속 목록을 조회하면
        val result = getMyGroupMeetUpUseCase.invoke(123, true)

        // Then: 내 모임의 약속 목록 조회가 성공이고, 정렬이 올바르게 되었는지 확인.
        assertTrue(result.isSuccess)
        val sortedPromises = result.getOrNull()

        // Then: 약속들이 d-day에 따라 1,2,3 오름 차순으로 정렬 되었는지 확인.
        assertEquals(3, sortedPromises?.size)
        assertEquals(1, sortedPromises?.get(0)?.dDay) // dDay == 1이 먼저 와야 함
        assertEquals(2, sortedPromises?.get(1)?.dDay) // dDay == 1가 그 다음
        assertEquals(3, sortedPromises?.get(2)?.dDay) // dDay == 2가 마지막
    }

    @Test
    fun `d-day가_모두_음수_일때는_내림차순으로_정렬된다`() = runTest {
        // Given: d-day가 모두 음수인 약속이 있을 때
        val mockPromises = listOf(
            MyGroupMeetUpModel.Promise(dDay = -2, "테스트 1", "", "", 1),
            MyGroupMeetUpModel.Promise(dDay = -3, "테스트 2", "", "", 2),
            MyGroupMeetUpModel.Promise(dDay = -1, "테스트 3", "", "", 3),
        )

        // Given: myGroupRepository에서 약속들을 성공적으로 반환하도록 설정
        setupMockRepositorySuccess(mockPromises)

        // When: 내 모임의 약속 목록을 조회하면
        val result = getMyGroupMeetUpUseCase.invoke(123, true)

        // Then: 내 모임의 약속 목록 조회가 성공이고, 정렬이 올바르게 되었는지 확인.
        assertTrue(result.isSuccess)
        val sortedPromises = result.getOrNull()

        // Then: 약속들이 d-day에 따라 -1,-2,-3 내림 차순으로 정렬 되었는지 확인.
        assertEquals(3, sortedPromises?.size)
        assertEquals(-1, sortedPromises?.get(0)?.dDay) // dDay == -1이 먼저 와야 함
        assertEquals(-2, sortedPromises?.get(1)?.dDay) // dDay == -2가 그 다음
        assertEquals(-3, sortedPromises?.get(2)?.dDay) // dDay == -3가 마지막
    }

    @Test
    fun `d-day에_0이_존재하는_경우_0이_가장_먼저_정렬된다`() = runTest {
        // Given: d-day가 모두 음수인 약속이 있을 때
        val mockPromises = listOf(
            MyGroupMeetUpModel.Promise(dDay = -2, "테스트 1", "", "", 1),
            MyGroupMeetUpModel.Promise(dDay = -1, "테스트 2", "", "", 2),
            MyGroupMeetUpModel.Promise(dDay = 0, "테스트 3", "", "", 3),
        )

        // Given: myGroupRepository에서 약속들을 성공적으로 반환하도록 설정
        setupMockRepositorySuccess(mockPromises)

        // When: 내 모임의 약속 목록을 조회하면
        val result = getMyGroupMeetUpUseCase.invoke(123, true)

        // Then: 내 모임의 약속 목록 조회가 성공이고, 정렬이 올바르게 되었는지 확인.
        assertTrue(result.isSuccess)
        val sortedPromises = result.getOrNull()

        // Then: 약속들이 d-day에 따라 0이 가장 먼저 반환되는지 확인.
        assertEquals(3, sortedPromises?.size)
        assertEquals(0, sortedPromises?.get(0)?.dDay) // dDay == 0이 먼저 와야 함
        assertEquals(-1, sortedPromises?.get(1)?.dDay) // dDay == -1가 그 다음
        assertEquals(-2, sortedPromises?.get(2)?.dDay) // dDay == -2가 마지막
    }

    @Test
    fun `d-day에_0_음수_양수가_존재하는_경우_0_음수_양수_순으로_정렬된다`() = runTest {
        // Given: d-day가 모두 음수인 약속이 있을 때
        val mockPromises = listOf(
            MyGroupMeetUpModel.Promise(dDay = -1, "테스트 1", "", "", 1),
            MyGroupMeetUpModel.Promise(dDay = 3, "테스트 2", "", "", 2),
            MyGroupMeetUpModel.Promise(dDay = 0, "테스트 3", "", "", 3),
        )

        // Given: myGroupRepository에서 약속들을 성공적으로 반환하도록 설정
        setupMockRepositorySuccess(mockPromises)

        // When: 내 모임의 약속 목록을 조회하면
        val result = getMyGroupMeetUpUseCase.invoke(123, true)

        // Then: 내 모임의 약속 목록 조회가 성공이고, 정렬이 올바르게 되었는지 확인.
        assertTrue(result.isSuccess)
        val sortedPromises = result.getOrNull()

        // Then: 약속들이 d-day에 따라 0,음수,양수 순으로 정렬 되는지 확인.
        assertEquals(3, sortedPromises?.size)
        assertEquals(0, sortedPromises?.get(0)?.dDay) // dDay == 0이 먼저 와야 함
        assertEquals(-1, sortedPromises?.get(1)?.dDay) // dDay == -1가 그 다음
        assertEquals(3, sortedPromises?.get(2)?.dDay) // dDay == -2가 마지막
    }

    @Test
    fun `리스트가_비었을때는_빈_리스트가_반환된다`() = runTest {
        // Given: 리스트가 비었을 때
        setupMockRepositorySuccess(emptyList())

        // When: 내 모임의 약속 목록을 조회하면
        val result = getMyGroupMeetUpUseCase.invoke(123, true)

        // Then: 내 모임의 약속 목록 조회가 성공이고, 리스트가 빈 값인지 확인.
        assertTrue(result.isSuccess)
        assertTrue(result.getOrNull()?.isEmpty() == true)
    }

    @Test
    fun `repository의_Result가_fail이면_에러메세지가_반환된다`() = runTest {
        // Given: 예외가 발생 했을 때
        val errorMessage = "Repository failed"
        setupMockRepositoryFailure(errorMessage)

        // When: 내 모임의 약속 목록을 조회하면.
        val result = getMyGroupMeetUpUseCase.invoke(123, true)

        // Then: 결과가 실패인지 확인하고, 에러 메시지가 일치하는지 확인.
        assertTrue(result.isFailure)
        assertEquals(errorMessage, result.exceptionOrNull()?.message)
    }

    // 성공적인 반환값을 설정
    private suspend fun setupMockRepositorySuccess(mockPromises: List<MyGroupMeetUpModel.Promise>) {
        whenever(
            myGroupRepository.getMyGroupMeetUp(
                anyInt(),
                anyBoolean(),
                anyOrNull(),
            ),
        ).thenReturn(Result.success(mockPromises))
    }

    // 실패하는 반환값을 설정
    private suspend fun setupMockRepositoryFailure(errorMessage: String) {
        whenever(
            myGroupRepository.getMyGroupMeetUp(
                anyInt(),
                anyBoolean(),
                anyOrNull(),
            ),
        ).thenReturn(Result.failure(Exception(errorMessage)))
    }
}
