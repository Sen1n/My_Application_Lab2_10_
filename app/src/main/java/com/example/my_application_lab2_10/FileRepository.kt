package com.example.my_application_lab2_10

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class FileRepository {

    fun downloadFile(startProgress: Int = 0): Flow<Int> = flow {
        val start = if (startProgress in 0..99) startProgress else 0
        for (progress in start..100 step 5) {
            emit(progress)
            delay(150)
        }
    }.flowOn(Dispatchers.IO)
}