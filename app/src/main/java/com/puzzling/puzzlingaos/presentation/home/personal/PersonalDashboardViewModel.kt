package com.puzzling.puzzlingaos.presentation.home.personal

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.puzzling.puzzlingaos.R
import com.puzzling.puzzlingaos.domain.entity.ActionPlan
import com.puzzling.puzzlingaos.domain.entity.MyPuzzleBoard
import com.puzzling.puzzlingaos.domain.entity.PuzzleBoard
import com.puzzling.puzzlingaos.domain.repository.MyBoardRepository
import com.puzzling.puzzlingaos.domain.repository.WriteReviewRepository
import com.puzzling.puzzlingaos.util.UserInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PersonalDashboardViewModel @Inject constructor(
    private val repository: MyBoardRepository,
    private val writeReviewRepository: WriteReviewRepository,
) : ViewModel() {
    private val _myNickname = MutableLiveData<String>()
    val myNickname: LiveData<String> get() = _myNickname
    private val _myPuzzleCount = MutableLiveData<Int>()
    val myPuzzleCount: LiveData<Int> get() = _myPuzzleCount
    private val _isReviewDay = MutableLiveData<Boolean>()
    val isReviewDay: LiveData<Boolean> get() = _isReviewDay
    private val _hasTodayReview = MutableLiveData<Boolean>()
    val hasTodayReview: LiveData<Boolean> get() = _hasTodayReview
    private val _puzzleBoardCount = MutableLiveData<Int>()
    val puzzleBoardCount: LiveData<Int> get() = _puzzleBoardCount

    private var _myPuzzleBoardList: MutableLiveData<List<MyPuzzleBoard>> = MutableLiveData()
    val myPuzzleBoardList: LiveData<List<MyPuzzleBoard>>
        get() = _myPuzzleBoardList

    private val _myPuzzleImage = MutableLiveData<List<String>>()
    val myPuzzleImage: LiveData<List<String>> get() = _myPuzzleImage

    private val _myReviewDate = MutableLiveData<List<String>>()
    val myReviewDate: LiveData<List<String>> get() = _myReviewDate

    private val _myReviewId = MutableLiveData<List<Int>>()
    val myReviewId: LiveData<List<Int>> get() = _myReviewId

    private var _actionPlanList: MutableLiveData<List<ActionPlan>> = MutableLiveData(null)
    val actionPlanList: LiveData<List<ActionPlan>>
        get() = _actionPlanList

    private val _previousReviewType = MutableLiveData<Int>()
    val previousReviewType: LiveData<Int>
        get() = _previousReviewType

    private val _isSuccess = MutableLiveData<Boolean?>(null)
    val isSuccess: LiveData<Boolean?> get() = _isSuccess

    val firstProjectId = MutableLiveData<Int>()

    val puzzleBoardList = listOf(
        PuzzleBoard("2023.07.03. ~ 2023.07.18.", R.drawable.img_myboard1),
        PuzzleBoard("2023.06.14. ~ 2023.06.29.", R.drawable.img_myboard2),
        PuzzleBoard("2023.05.04. ~ 2023.06.12.", R.drawable.img_myboard3),
        PuzzleBoard("2023.05.04. ~ 2023.06.12.", R.drawable.img_myboard4),

    )

//    init {
//        getMyPuzzleData()
//        getMyPuzzleData()
//        getActionPlan()
//        getMyPuzzleBoard()
//        getPreviousTemplate()
//    }

    fun getMyPuzzleData(projectId: Int) = viewModelScope.launch {
        repository.getUserPuzzle(UserInfo.MEMBER_ID, projectId, UserInfo.TODAY)
            .onSuccess { response ->
                Log.d("personal", "getMyPuzzleData() success:: $response")
                _myNickname.value = response.data.myPuzzle.nickname
                _myPuzzleCount.value = response.data.myPuzzle.puzzleCount
                _isReviewDay.value = response.data.isReviewDay
                _hasTodayReview.value = response.data.hasTodayReview
                _puzzleBoardCount.value = response.data.puzzleBoardCount
                Log.d("personal", "myNickname success:: ${_myNickname.value}")
                Log.d("personal", "puzzleCount success:: ${_myPuzzleCount.value}")
                Log.d("personal", "hasTodayReview success:: ${_hasTodayReview.value}")
                Log.d("personal", "_isReviewDay success:: ${_isReviewDay.value}")
            }
            .onFailure {
                Log.d("personal", "getMyPuzzleData() Fail:: $it")
            }
    }

    fun getMyPuzzleBoard(projectId: Int) = viewModelScope.launch {
        repository.getUserPuzzleBoard(UserInfo.MEMBER_ID, projectId, UserInfo.TODAY)
            .onSuccess { response ->
                _myPuzzleBoardList.value = response
                Log.d("personal", "getMyPuzzleBoard() success:: $response")

                val myPuzzles: List<MyPuzzleBoard> =
                    _myPuzzleBoardList.value!!
                _myReviewDate.value = myPuzzles.map {
                    it.reviewDate?.substringOrNull(5)?.replace("-", "/") ?: ""
                }
                _myReviewId.value = myPuzzles.map { it.reviewId ?: -1 }
                _myPuzzleImage.value = myPuzzles.map { it.puzzleAssetName }
                Log.d("personal", "_myReviewDate:: ${_myReviewDate.value}")
                Log.d("personal", "_myReviewId:: ${_myReviewId.value}")
                Log.d("personal", "_myPuzzleImage:: ${_myPuzzleImage.value}")
            }
            .onFailure {
                Log.d("personal", "getMyPuzzleBoard() Fail:: $it")
            }
    }

    fun String.substringOrNull(startIndex: Int): String? {
        if (startIndex >= length) {
            return null
        }
        return substring(startIndex)
    }

    fun getActionPlan(projectId: Int) = viewModelScope.launch {
        repository.getActionPlan(UserInfo.MEMBER_ID, projectId)
            .onSuccess { response ->
                _isSuccess.value = true
                Log.d("personal", "getActionPlan() success:: $response")
                val truncatedList = truncateActionPlanList(response)
                _actionPlanList.value = truncatedList
                Log.d("actionPlan", "actionPlanList.value:: ${_actionPlanList.value}")
                _actionPlanList.value?.forEach { actionPlan ->
                    actionPlan.actionPlanDate = actionPlan.actionPlanDate?.let {
                        convertActionPlanDate(
                            it,
                        )
                    }
                }
            }.onFailure {
                Log.d("personal", "getActionPlan() Fail:: $it")
            }
    }

    fun getPreviousTemplate(projectId: Int) {
        viewModelScope.launch {
            writeReviewRepository.getPreviousTemplate(
                UserInfo.MEMBER_ID,
                projectId,
            )
                .onSuccess { response ->
                    _previousReviewType.value = response.data.previousTemplateId
                    Log.d("write", "getPreviousTemplate() success:: ${_previousReviewType.value}")
                }.onFailure {
                    Log.d("write", "getPreviousTemplate() Fail:: $it")
                }
        }
    }

    private fun truncateActionPlanList(actionPlanList: List<ActionPlan>): List<ActionPlan> {
        val maxLength = 50
        val truncatedList = mutableListOf<ActionPlan>()

        for (actionPlan in actionPlanList) {
            val truncatedText = if (actionPlan.actionPlanContent!!.length <= maxLength) {
                actionPlan.actionPlanContent
            } else {
                actionPlan.actionPlanContent.substring(0, maxLength - 2) + ".."
            }
            val truncatedActionPlan = ActionPlan(
                truncatedText,
                actionPlan.actionPlanDate,
            )
            truncatedList.add(truncatedActionPlan)
        }

        return truncatedList
    }

    private fun convertActionPlanDate(actionPlanDate: String): String {
        val monthMap = mapOf(
            "01" to "1월",
            "02" to "2월",
            "03" to "3월",
            "04" to "4월",
            "05" to "5월",
            "06" to "6월",
            "07" to "7월",
            "08" to "8월",
            "09" to "9월",
            "10" to "10월",
            "11" to "11월",
            "12" to "12월",
        )

        val parts = actionPlanDate.split("-")
        val month = monthMap[parts[1]] ?: parts[1]
        val day = parts[2]

        return "$month ${day}일"
    }
}
