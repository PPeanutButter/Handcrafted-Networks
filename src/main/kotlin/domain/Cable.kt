package domain

import kotlinx.coroutines.flow.Flow

interface Cable {
    suspend fun write(data: Byte)
    suspend fun read(): Flow<Byte>
}