package domain

import domain.model.Port

interface Device {
    fun onConnect(vararg cable: Cable)
    fun onDisconnect(vararg cable: Cable)

    suspend fun tx(data: ByteArray, port: Port)
    suspend fun rx(data: ByteArray, port: Port)
}