package ng.mint.ocrscanner.model

sealed class CardResult {
    data class Success(val data: CardResponse) : CardResult()
    object Failure : CardResult()
}
