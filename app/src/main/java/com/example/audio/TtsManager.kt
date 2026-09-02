package com.example.audio

import android.content.Context
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.Locale

enum class TtsState {
    IDLE,
    INITIALIZING,
    SPEAKING,
    ERROR
}

class TtsManager(context: Context) {
    private var tts: TextToSpeech? = null
    private val _state = MutableStateFlow(TtsState.INITIALIZING)
    val state: StateFlow<TtsState> = _state.asStateFlow()

    private val _isSpeaking = MutableStateFlow(false)
    val isSpeaking: StateFlow<Boolean> = _isSpeaking.asStateFlow()

    private var isInitialized = false

    init {
        tts = TextToSpeech(context.applicationContext) { status ->
            if (status == TextToSpeech.SUCCESS) {
                isInitialized = true
                _state.value = TtsState.IDLE
                setupProgressListener()
            } else {
                _state.value = TtsState.ERROR
            }
        }
    }

    private fun setupProgressListener() {
        tts?.setOnUtteranceProgressListener(object : UtteranceProgressListener() {
            override fun onStart(utteranceId: String?) {
                _isSpeaking.value = true
                _state.value = TtsState.SPEAKING
            }

            override fun onDone(utteranceId: String?) {
                _isSpeaking.value = false
                _state.value = TtsState.IDLE
            }

            @Deprecated("Deprecated in Java")
            override fun onError(utteranceId: String?) {
                _isSpeaking.value = false
                _state.value = TtsState.IDLE
            }

            override fun onError(utteranceId: String?, errorCode: Int) {
                _isSpeaking.value = false
                _state.value = TtsState.IDLE
            }
        })
    }

    fun speakKorean(text: String, speed: Float = 1.0f, onDone: (() -> Unit)? = null) {
        speak(text, Locale.KOREAN, speed, onDone)
    }

    fun speakEnglish(text: String, speed: Float = 1.0f, onDone: (() -> Unit)? = null) {
        speak(text, Locale.US, speed, onDone)
    }

    private fun speak(text: String, locale: Locale, speed: Float, onDone: (() -> Unit)? = null) {
        if (!isInitialized || tts == null) return

        tts?.stop()
        val result = tts?.setLanguage(locale)
        if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
            // fallback if US specific locale missing, try standard English
            if (locale == Locale.US) {
                tts?.setLanguage(Locale.ENGLISH)
            }
        }

        tts?.setSpeechRate(speed.coerceIn(0.5f, 2.0f))
        tts?.setPitch(1.0f)

        val utteranceId = "tts_${System.currentTimeMillis()}"
        tts?.speak(text, TextToSpeech.QUEUE_FLUSH, null, utteranceId)
    }

    fun stop() {
        tts?.stop()
        _isSpeaking.value = false
        _state.value = TtsState.IDLE
    }

    fun shutdown() {
        tts?.stop()
        tts?.shutdown()
        tts = null
    }
}
