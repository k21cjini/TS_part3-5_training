package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.EngSpeakApplication
import com.example.audio.SpeechRecognitionManager
import com.example.audio.SpeechState
import com.example.audio.TtsManager
import com.example.model.AnswerMode
import com.example.model.EvaluationResult
import com.example.model.HistoryRecord
import com.example.model.QuestionMode
import com.example.model.Sentence
import com.example.model.SessionModeConfig
import com.example.util.SentenceEvaluator
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.Locale

data class PracticeUiState(
    val currentSentence: Sentence? = null,
    val currentIndex: Int = 0,
    val totalCount: Int = 0,
    val textInput: String = "",
    val evaluationResult: EvaluationResult? = null,
    val isEvaluated: Boolean = false,
    val thinkingSeconds: Int = 0,
    val isTimerRunning: Boolean = false,
    val isShadowingMode: Boolean = false, // Repeating correct answer
    val shadowingEvaluation: EvaluationResult? = null,
    val showHintModal: Boolean = false,
    val hintLevel: Int = 0 // 0: None, 1: Structure/Grammar, 2: First Letter, 3: Word Chips
)

data class StatisticsUiState(
    val totalPracticed: Int = 0,
    val averageAccuracy: Int = 0,
    val streakDays: Int = 1,
    val masteredCount: Int = 0,
    val recentHistory: List<HistoryRecord> = emptyList()
)

class EngSpeakViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = (application as EngSpeakApplication).repository
    val ttsManager = TtsManager(application)
    val speechManager = SpeechRecognitionManager(application)

    // Mode Configuration
    private val _sessionConfig = MutableStateFlow(SessionModeConfig())
    val sessionConfig: StateFlow<SessionModeConfig> = _sessionConfig.asStateFlow()

    // Active Category Filter
    private val _selectedCategory = MutableStateFlow("전체")
    val selectedCategory: StateFlow<String> = _selectedCategory.asStateFlow()

    // Search Query
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    // Sentences Stream
    val allSentences = repository.allSentences.stateIn(
        viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList()
    )

    val bookmarkedSentences = repository.bookmarkedSentences.stateIn(
        viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList()
    )

    val weakSentences = repository.weakSentences.stateIn(
        viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList()
    )

    val recentHistory = repository.recentHistory.stateIn(
        viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList()
    )

    // Filtered Sentences for Training
    val currentDeck: StateFlow<List<Sentence>> = combine(
        allSentences,
        selectedCategory,
        searchQuery
    ) { sentences, category, query ->
        var list = when (category) {
            "전체" -> sentences
            "즐겨찾기" -> sentences.filter { it.isBookmarked }
            "복습필요" -> sentences.filter { it.practiceCount > 0 && it.accuracyPercent < 70 }
            "내 문장장" -> sentences.filter { it.isCustom }
            else -> sentences.filter { it.category == category }
        }
        if (query.isNotBlank()) {
            list = list.filter {
                it.korean.contains(query, ignoreCase = true) ||
                it.english.contains(query, ignoreCase = true) ||
                it.category.contains(query, ignoreCase = true)
            }
        }
        list
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Practice UI State
    private val _practiceState = MutableStateFlow(PracticeUiState())
    val practiceState: StateFlow<PracticeUiState> = _practiceState.asStateFlow()

    // Timer Job
    private var timerJob: Job? = null

    init {
        // Ensure default and YouTube preset sentences are synchronized
        viewModelScope.launch {
            repository.ensureDefaultSentencesLoaded()
        }

        // Observe speech recognition results
        viewModelScope.launch {
            speechManager.state.collect { state ->
                when (state) {
                    is SpeechState.PartialResult -> {
                        if (!_practiceState.value.isEvaluated && !_practiceState.value.isShadowingMode) {
                            _practiceState.value = _practiceState.value.copy(textInput = state.text)
                        }
                    }
                    is SpeechState.Success -> {
                        if (_practiceState.value.isShadowingMode) {
                            evaluateShadowing(state.text)
                        } else {
                            _practiceState.value = _practiceState.value.copy(textInput = state.text)
                            submitAnswer(state.text, isVoice = true)
                        }
                    }
                    else -> {}
                }
            }
        }

        // Initialize first sentence when deck updates
        viewModelScope.launch {
            currentDeck.collect { deck ->
                if (deck.isNotEmpty() && _practiceState.value.currentSentence == null) {
                    loadSentenceAtIndex(0, deck)
                }
            }
        }
    }

    // Category Selection
    fun selectCategory(category: String) {
        _selectedCategory.value = category
        viewModelScope.launch {
            delay(50)
            val deck = currentDeck.value
            if (deck.isNotEmpty()) {
                loadSentenceAtIndex(0, deck)
            } else {
                _practiceState.value = PracticeUiState()
            }
        }
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    // Mode Configuration
    fun updateSessionConfig(
        questionMode: QuestionMode = _sessionConfig.value.questionMode,
        answerMode: AnswerMode = _sessionConfig.value.answerMode,
        speechSpeed: Float = _sessionConfig.value.speechSpeed,
        autoPlayEnglish: Boolean = _sessionConfig.value.autoPlayEnglishOnResult
    ) {
        _sessionConfig.value = _sessionConfig.value.copy(
            questionMode = questionMode,
            answerMode = answerMode,
            speechSpeed = speechSpeed,
            autoPlayEnglishOnResult = autoPlayEnglish
        )
    }

    fun toggleAnswerMode() {
        val nextMode = when (_sessionConfig.value.answerMode) {
            AnswerMode.VOICE -> AnswerMode.TEXT
            AnswerMode.TEXT -> AnswerMode.HYBRID
            AnswerMode.HYBRID -> AnswerMode.VOICE
        }
        _sessionConfig.value = _sessionConfig.value.copy(answerMode = nextMode)
    }

    fun toggleQuestionMode() {
        val nextMode = when (_sessionConfig.value.questionMode) {
            QuestionMode.VOICE_AUTO -> QuestionMode.TEXT_ONLY
            QuestionMode.TEXT_ONLY -> QuestionMode.VOICE_ONLY
            QuestionMode.VOICE_ONLY -> QuestionMode.VOICE_AUTO
        }
        _sessionConfig.value = _sessionConfig.value.copy(questionMode = nextMode)
    }

    // Sentence Navigation
    private fun loadSentenceAtIndex(index: Int, deck: List<Sentence> = currentDeck.value) {
        if (deck.isEmpty()) {
            _practiceState.value = PracticeUiState()
            return
        }
        val safeIndex = index.coerceIn(0, deck.size - 1)
        val sentence = deck[safeIndex]
        
        speechManager.reset()
        ttsManager.stop()

        _practiceState.value = PracticeUiState(
            currentSentence = sentence,
            currentIndex = safeIndex,
            totalCount = deck.size,
            textInput = "",
            evaluationResult = null,
            isEvaluated = false,
            thinkingSeconds = 0,
            isTimerRunning = true,
            isShadowingMode = false,
            shadowingEvaluation = null,
            hintLevel = 0
        )

        startThinkingTimer()

        // Auto speak Korean prompt if QuestionMode is VOICE_AUTO or VOICE_ONLY
        if (_sessionConfig.value.questionMode != QuestionMode.TEXT_ONLY) {
            speakKoreanPrompt(sentence.korean)
        }
    }

    fun nextSentence() {
        val deck = currentDeck.value
        if (deck.isNotEmpty()) {
            val nextIdx = (_practiceState.value.currentIndex + 1) % deck.size
            loadSentenceAtIndex(nextIdx, deck)
        }
    }

    fun previousSentence() {
        val deck = currentDeck.value
        if (deck.isNotEmpty()) {
            val prevIdx = if (_practiceState.value.currentIndex - 1 < 0) deck.size - 1 else _practiceState.value.currentIndex - 1
            loadSentenceAtIndex(prevIdx, deck)
        }
    }

    fun shuffleSentences() {
        val deck = currentDeck.value
        if (deck.isNotEmpty()) {
            val randomIdx = deck.indices.random()
            loadSentenceAtIndex(randomIdx, deck)
        }
    }

    fun retryCurrentSentence() {
        _practiceState.value.currentSentence?.let { sentence ->
            loadSentenceAtIndex(_practiceState.value.currentIndex)
        }
    }

    // Timer
    private fun startThinkingTimer() {
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            while (_practiceState.value.isTimerRunning) {
                delay(1000)
                _practiceState.value = _practiceState.value.copy(
                    thinkingSeconds = _practiceState.value.thinkingSeconds + 1
                )
            }
        }
    }

    private fun stopThinkingTimer() {
        timerJob?.cancel()
        _practiceState.value = _practiceState.value.copy(isTimerRunning = false)
    }

    // User Text Input
    fun onTextInputChange(text: String) {
        _practiceState.value = _practiceState.value.copy(textInput = text)
    }

    // Audio & STT Actions
    fun startVoiceRecording() {
        ttsManager.stop()
        speechManager.startListening(Locale.US)
    }

    fun stopVoiceRecording() {
        speechManager.stopListening()
    }

    fun speakKoreanPrompt(text: String? = null) {
        val prompt = text ?: _practiceState.value.currentSentence?.korean ?: return
        ttsManager.speakKorean(prompt, _sessionConfig.value.speechSpeed)
    }

    fun speakEnglishAnswer(text: String? = null, speed: Float = _sessionConfig.value.speechSpeed) {
        val answer = text ?: _practiceState.value.currentSentence?.english ?: return
        ttsManager.speakEnglish(answer, speed)
    }

    // Hint
    fun cycleHint() {
        val currentLevel = _practiceState.value.hintLevel
        val nextLevel = if (currentLevel >= 3) 0 else currentLevel + 1
        _practiceState.value = _practiceState.value.copy(hintLevel = nextLevel)
    }

    // Answer Submission & Evaluation
    fun submitAnswer(answer: String = _practiceState.value.textInput, isVoice: Boolean = false) {
        val current = _practiceState.value.currentSentence ?: return
        if (answer.isBlank()) return

        stopThinkingTimer()
        speechManager.reset()

        val evaluation = SentenceEvaluator.evaluate(
            userAnswer = answer,
            primaryTarget = current.english,
            alternatives = current.getAlternatives()
        )

        _practiceState.value = _practiceState.value.copy(
            textInput = answer,
            evaluationResult = evaluation,
            isEvaluated = true,
            isTimerRunning = false
        )

        // Save history in background
        viewModelScope.launch {
            repository.recordAttempt(
                sentenceId = current.id,
                accuracy = evaluation.accuracy,
                korean = current.korean,
                expected = current.english,
                userAnswer = answer,
                modeUsed = if (isVoice) "VOICE" else "TEXT",
                durationSec = _practiceState.value.thinkingSeconds
            )
        }

        // Auto play English model pronunciation if enabled
        if (_sessionConfig.value.autoPlayEnglishOnResult) {
            viewModelScope.launch {
                delay(300)
                speakEnglishAnswer(current.english)
            }
        }
    }

    // Shadowing Practice
    fun startShadowingMode() {
        _practiceState.value = _practiceState.value.copy(
            isShadowingMode = true,
            shadowingEvaluation = null
        )
    }

    private fun evaluateShadowing(spokenText: String) {
        val current = _practiceState.value.currentSentence ?: return
        val evaluation = SentenceEvaluator.evaluate(
            userAnswer = spokenText,
            primaryTarget = current.english,
            alternatives = current.getAlternatives()
        )
        _practiceState.value = _practiceState.value.copy(
            shadowingEvaluation = evaluation
        )
    }

    // Bookmark Toggle
    fun toggleBookmark(sentence: Sentence) {
        viewModelScope.launch {
            val updated = !sentence.isBookmarked
            repository.toggleBookmark(sentence.id, updated)
            if (_practiceState.value.currentSentence?.id == sentence.id) {
                _practiceState.value = _practiceState.value.copy(
                    currentSentence = sentence.copy(isBookmarked = updated)
                )
            }
        }
    }

    fun syncDefaultSentences() {
        viewModelScope.launch {
            repository.resetOrSyncDefaultSentences()
        }
    }

    // Custom Sentences Management
    fun addCustomSentence(korean: String, english: String, patternTip: String = "", alternatives: String = "") {
        if (korean.isBlank() || english.isBlank()) return
        viewModelScope.launch {
            val newSentence = Sentence(
                category = "내 문장장",
                korean = korean.trim(),
                english = english.trim(),
                acceptableAnswers = alternatives.trim(),
                patternTip = patternTip.trim(),
                difficulty = "맞춤",
                isCustom = true
            )
            repository.insertSentence(newSentence)
        }
    }

    fun updateSentence(sentence: Sentence) {
        viewModelScope.launch {
            repository.updateSentence(sentence)
        }
    }

    fun deleteSentence(sentence: Sentence) {
        viewModelScope.launch {
            repository.deleteSentence(sentence)
            if (_practiceState.value.currentSentence?.id == sentence.id) {
                nextSentence()
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        timerJob?.cancel()
        ttsManager.shutdown()
        speechManager.destroy()
    }
}
