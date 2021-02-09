package ng.mint.ocrscanner.model

sealed class RecentCardsState {
    data class RecentCardList(var data: MutableList<RecentCard>) : RecentCardsState()
    object EmptyList : RecentCardsState()
}
