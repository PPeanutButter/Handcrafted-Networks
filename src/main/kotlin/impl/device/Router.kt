package impl.device

import domain.Cable
import domain.Device
import domain.model.Port

class Router: Device {
    val wan = Port()
    val lan1 = Port()
    val lan2 = Port()
    val lan3 = Port()

    override fun onConnect(vararg cable: Cable) {
        TODO("Not yet implemented")
    }

    override fun onDisconnect(vararg cable: Cable) {
        TODO("Not yet implemented")
    }

    override suspend fun tx(data: ByteArray, port: Port) {
        TODO("Not yet implemented")
    }

    override suspend fun rx(data: ByteArray, port: Port) {
        TODO("Not yet implemented")
    }
}