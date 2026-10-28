package domain

import kotlinx.coroutines.channels.Channel

abstract class Cable(
    val endA: Port,
    val endB: Port,
) {
    private val aToB = Channel<Byte>(Channel.BUFFERED)
    private val bToA = Channel<Byte>(Channel.BUFFERED)

    init {
        endA.onConnect(outgoing = aToB, incoming = bToA)
        endB.onConnect(outgoing = bToA, incoming = aToB)
    }

    fun disconnect(){
        aToB.cancel()
        bToA.cancel()
        endA.onDisconnect()
        endB.onDisconnect()
    }
}