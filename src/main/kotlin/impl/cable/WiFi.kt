package impl.cable

import kotlinx.coroutines.flow.Flow
import domain.Cable

//impl for IEEE 802.11ac
internal class WiFi : Cable {
    override suspend fun write(data: Byte) {
        TODO("Not yet implemented")
    }

    override suspend fun read(): Flow<Byte> {
        TODO("Not yet implemented")
    }
}