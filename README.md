# Handcrafted-Networks
手搓计算机网络

## 物理层
考虑到没办法使用真实的物理链接将多个设备互联起来，这里采用操作系统提供的功能来模拟物理传输行为。如点对点传播的管道(Pipe)，多进程(或设备)可共享的文件(File)等。物理层需要具备的功能如下：
- 多个设备间通信

显然，使用基于UDP的套接字(Sockets)完美符合物理层的功能(有点讽刺，我们需要在计算机网络上实现计算机网络)。使用套接字有一个好处，那就是不需要考虑电压、频率、相位等物理规律，将关注点保持在编程实现上。
我们暂时也不用考虑**香农定理**，**调制-解调**相关的知识点。目前跳过的知识点有：

- 香农定理
- 调制-解调
- 编码规则（NRE、NRZI、曼彻斯特、4B/5B等）

以后有需要的时候可以再研究。
### 线缆
定义如下(应该采用高低电平Boolean以模仿线缆特性，为了简化采用Byte。注意:以太网的帧远超过一个Byte，所以仍然需要实现组帧以上的功能)：
```kotlin
interface Cable {
    suspend fun write(data: Byte)
    suspend fun read(): Flow<Byte>
}
```
### 组帧
> [!NOTE]
> 这一节要解决的问题是：如何知道传输的比特流(字节流)结束了?

我们采用IEEE 802.3标准的组帧协议([参考链接](https://www.tup.tsinghua.edu.cn/upload/books/yz/048036-01.pdf))。至于现在的1000BaseT以太网用的协议，可以最后捎带提一嘴([参考链接](https://note.t4x.org/basic/network-ethernet-protocol-ii/))。由于现代计算机网络架构采用了交换机，暂时也不考虑CSMA/CD算法，因为所有通信都是点对点的（除了WiFi，之后再考虑）。

| 7 bytes  | 1 byte | 6 bytes             | 6 bytes        | 2 bytes | 46-1500 bytes         | 4 bytes |
|----------|--------|---------------------|----------------|---------|-----------------------|---------|
| Preamble | SDF    | Destination Address | Source Address | Length  | 802.2 Header and Data | CRC     |

实际的代码实现如下:
```kotlin
internal class Frame_802_3(
    private val source: ByteArray,
    private val target: ByteArray,
    private val payloads: ByteArray,
) : Frame() {
    private val outputStream = ByteArrayOutputStream(1526)

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
}
```
