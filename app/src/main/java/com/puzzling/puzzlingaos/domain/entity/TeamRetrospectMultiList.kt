package com.puzzling.puzzlingaos.domain.entity

data class TeamRetrospectMultiList(
    var type: Int,
    var memberNickname: String?,
    var memberRole: String?
) {

    companion object {
        const val DO_RETROSPECT_TEXT = 0
        const val DO_RETROSPECT_LIST = 1
        const val DONT_RETROSPECT_TEXT = 2
        const val DONT_RETROSPECT_LIST = 3
    }
}
