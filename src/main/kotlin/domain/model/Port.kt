package domain.model

import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flow

/** 设备端口；接收流由单个接收者消费。 */
class Port {
    private var outgoing: SendChannel<Byte>? = null
    private var incoming: ReceiveChannel<Byte>? = null
    /** 顺序发送字节，不保留本次调用的消息边界。 */
    suspend fun tx(data: ByteArray) {
        val channel = checkNotNull(outgoing) { "端口未连接，无法发送" }
        data.forEach { channel.send(it) }
    }
    fun rx(): Flow<Byte> = checkNotNull(incoming) { "端口未连接，无法接收" }.consumeAsFlow()

    fun onConnect(
        outgoing: SendChannel<Byte>,
        incoming: ReceiveChannel<Byte>,
    ){
        this.outgoing = outgoing
        this.incoming = incoming
    }

    fun onDisconnect(){
        this.outgoing = null
        this.incoming = null
    }
}