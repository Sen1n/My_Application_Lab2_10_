package com.example.my_application_lab2_10

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.launch

class DownloadViewModel : ViewModel() {

    private val repository = FileRepository()
    private var downloadJob: Job? = null

    private val _progress = MutableStateFlow(0)
    val progress: StateFlow<Int> = _progress.asStateFlow()

    private val _status = MutableStateFlow("Очікування...")
    val status: StateFlow<String> = _status.asStateFlow()

    fun startDownload() {
        if (downloadJob?.isActive == true) return

        val currentVal = if (_progress.value >= 100) 0 else _progress.value

        downloadJob = viewModelScope.launch {
            _status.value = "Завантаження почалося"
            if (currentVal == 0) {
                _progress.value = 0
            }

            repository.downloadFile(currentVal)
                .onCompletion { cause ->
                    if (cause == null) {
                        _status.value = "Файл успішно скачано!"
                    } else {
                        _status.value = "Завантаження скасовано"
                    }
                }
                .catch {
                    _status.value = "Помилка завантаження"
                }
                .collect { currentProgress ->
                    _progress.value = currentProgress
                    _status.value = "Завантаження: $currentProgress%"
                }
        }
    }

    fun cancelDownload() {
        downloadJob?.cancel()
    }
}