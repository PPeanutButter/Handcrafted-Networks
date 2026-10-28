package impl.cable

import domain.Cable
import domain.model.Port
import kotlinx.coroutines.channels.Channel

class MemoryCable(
    override val endA: Port,
    override val endB: Port,
) : Cable(endA, endB) {
    private val aToB = Channel<Byte>(Channel.BUFFERED)
    private val bToA = Channel<Byte>(Channel.BUFFERED)

    init {
        endA.onConnect(outgoing = aToB, incoming = bToA)
        endB.onConnect(outgoing = bToA, incoming = aToB)
    }

    override fun disconnect() {
        aToB.cancel()
        bToA.cancel()
        endA.onDisconnect()
        endB.onDisconnect()
    }
}