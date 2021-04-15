package ng.mint.ocrscanner.callbacks

import ng.mint.ocrscanner.model.RecentCard

interface DataHandler {
    fun emitData(data: RecentCard)
}