package com.example.audio

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.Locale

sealed class SpeechState {
    data object Idle : SpeechState()
    data object Listening : SpeechState()
    data object Processing : SpeechState()
    data class PartialResult(val text: String) : SpeechState()
    data class Success(val text: String) : SpeechState()
    data class Error(val message: String) : SpeechState()
}

class SpeechRecognitionManager(private val context: Context) {
    private var speechRecognizer: SpeechRecognizer? = null

    private val _state = MutableStateFlow<SpeechState>(SpeechState.Idle)
    val state: StateFlow<SpeechState> = _state.asStateFlow()

    private val _rmsLevel = MutableStateFlow(0f)
    val rmsLevel: StateFlow<Float> = _rmsLevel.asStateFlow()

    private val _isAvailable = MutableStateFlow(false)
    val isAvailable: StateFlow<Boolean> = _isAvailable.asStateFlow()

    init {
        _isAvailable.value = SpeechRecognizer.isRecognitionAvailable(context)
    }

    private fun ensureRecognizer(): Boolean {
        if (!SpeechRecognizer.isRecognitionAvailable(context)) {
            _state.value = SpeechState.Error("이 기기에서 음성 인식을 지원하지 않습니다.")
            return false
        }
        if (speechRecognizer == null) {
            speechRecognizer = SpeechRecognizer.createSpeechRecognizer(context).apply {
                setRecognitionListener(createListener())
            }
        }
        return true
    }

    private fun createListener() = object : RecognitionListener {
        override fun onReadyForSpeech(params: Bundle?) {
            _state.value = SpeechState.Listening
        }

        override fun onBeginningOfSpeech() {
            _state.value = SpeechState.Listening
        }

        override fun onRmsChanged(rmsdB: Float) {
            // Normalize -2dB..10dB to 0.0..1.0
            val normalized = ((rmsdB + 2f) / 12f).coerceIn(0f, 1f)
            _rmsLevel.value = normalized
        }

        override fun onBufferReceived(buffer: ByteArray?) {}

        override fun onEndOfSpeech() {
            _state.value = SpeechState.Processing
            _rmsLevel.value = 0f
        }

        override fun onError(error: Int) {
            _rmsLevel.value = 0f
            val message = when (error) {
                SpeechRecognizer.ERROR_AUDIO -> "오디오 녹음 오류가 발생했습니다."
                SpeechRecognizer.ERROR_CLIENT -> "음성 인식 클라이언트 오류입니다."
                SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS -> "마이크 권한이 필요합니다."
                SpeechRecognizer.ERROR_NETWORK -> "네트워크 연결을 확인해주세요."
                SpeechRecognizer.ERROR_NETWORK_TIMEOUT -> "네트워크 응답 시간이 초과되었습니다."
                SpeechRecognizer.ERROR_NO_MATCH -> "음성을 인식하지 못했습니다. 다시 말씀해주세요."
                SpeechRecognizer.ERROR_RECOGNIZER_BUSY -> "음성 인식 서비스가 실행 중입니다."
                SpeechRecognizer.ERROR_SERVER -> "음성 인식 서버 오류가 발생했습니다."
                SpeechRecognizer.ERROR_SPEECH_TIMEOUT -> "말씀이 감지되지 않았습니다."
                else -> "음성 인식 중 오류가 발생했습니다 ($error)."
            }
            _state.value = SpeechState.Error(message)
        }

        override fun onResults(results: Bundle?) {
            _rmsLevel.value = 0f
            val matches = results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
            val recognizedText = matches?.firstOrNull()?.trim().orEmpty()
            if (recognizedText.isNotBlank()) {
                _state.value = SpeechState.Success(recognizedText)
            } else {
                _state.value = SpeechState.Error("인식된 내용이 없습니다.")
            }
        }

        override fun onPartialResults(partialResults: Bundle?) {
            val matches = partialResults?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
            val partialText = matches?.firstOrNull()?.trim().orEmpty()
            if (partialText.isNotBlank()) {
                _state.value = SpeechState.PartialResult(partialText)
            }
        }

        override fun onEvent(eventType: Int, params: Bundle?) {}
    }

    fun startListening(languageLocale: Locale = Locale.US) {
        if (!ensureRecognizer()) return

        stopListening()

        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, languageLocale.toLanguageTag())
            putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE, languageLocale.toLanguageTag())
            putExtra(RecognizerIntent.EXTRA_ONLY_RETURN_LANGUAGE_PREFERENCE, languageLocale.toLanguageTag())
            putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true)
            putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 3)
            putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, context.packageName)
        }

        try {
            speechRecognizer?.startListening(intent)
            _state.value = SpeechState.Listening
        } catch (e: Exception) {
            _state.value = SpeechState.Error("음성 인식을 시작할 수 없습니다: ${e.localizedMessage}")
        }
    }

    fun stopListening() {
        try {
            speechRecognizer?.stopListening()
        } catch (e: Exception) {
            // Ignore
        }
        _rmsLevel.value = 0f
    }

    fun reset() {
        stopListening()
        _state.value = SpeechState.Idle
    }

    fun destroy() {
        try {
            speechRecognizer?.destroy()
        } catch (e: Exception) {
            // Ignore
        }
        speechRecognizer = null
        _state.value = SpeechState.Idle
    }
}
