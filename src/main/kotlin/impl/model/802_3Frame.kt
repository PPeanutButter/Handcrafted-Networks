package impl.model

import domain.model.Frame
import java.io.ByteArrayOutputStream
import java.util.zip.CRC32

internal class Frame_802_3(
    private val source: ByteArray,
    private val target: ByteArray,
    private val payloads: ByteArray,
) : Frame() {
    private val outputStream = ByteArrayOutputStream(1518)

    private fun calculateCRC(data: ByteArray): Long {
        val crc = CRC32()
        crc.update(data)
        return crc.value
    }

    override fun getData(): ByteArray {
        outputStream.write(ByteArray(7) { preamble.toByte() }) //前导码
        outputStream.write(sdf) //启动定界符
        outputStream.write(target) //目的MAC地址
        outputStream.write(source) //源MAC地址
        outputStream.write(payloads.size.shr(8)) //数据长度高字节
        outputStream.write(payloads.size) //数据长度低字节
        outputStream.write(payloads)// 数据负载
        outputStream.write(calculateCRC(outputStream.toByteArray()).toByteArray())//原始协议只计算MAC到负载的CRC
        return outputStream.toByteArray()
    }

    private fun Long.toByteArray(): ByteArray {
        return byteArrayOf(
            (this shr 24).toByte(),
            (this shr 16).toByte(),
            (this shr 8).toByte(),
            this.toByte()
        )
    }

    companion object {
        const val preamble = 0b1010_1010
        const val sdf = 0b1010_1011
    }


}