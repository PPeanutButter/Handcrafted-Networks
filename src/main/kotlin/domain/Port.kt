package domain

import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.flow.Flow

/** 设备端口；接收流由单个接收者消费。 */
interface Port {
    /** 顺序发送字节，不保留本次调用的消息边界。 */
    suspend fun tx(data: ByteArray)
    fun rx(): Flow<Byte>

    fun onConnect(
        outgoing: SendChannel<Byte>,
        incoming: ReceiveChannel<Byte>,
    )
    fun onDisconnect()
}