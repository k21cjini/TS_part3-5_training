package com.example.model

/**
 * Question presentation mode: How the Korean prompt is presented to the user.
 */
enum class QuestionMode(val label: String, val description: String) {
    VOICE_AUTO("자동 음성+텍스트", "한글 질문을 자동으로 음성(TTS)으로 읽어주고 텍스트를 보여줍니다."),
    VOICE_ONLY("음성 듣기 집중", "텍스트를 가리고 음성(TTS)으로만 먼저 듣고 생각합니다."),
    TEXT_ONLY("텍스트 전용", "음성 없이 텍스트 카드 형태로만 조용히 학습합니다.")
}

/**
 * Answer input mode: How the user provides their English response.
 */
enum class AnswerMode(val label: String, val description: String) {
    VOICE("음성으로 말하기 (STT)", "마이크를 누르고 영어로 말하여 발음과 문장을 체크합니다."),
    TEXT("텍스트/채팅 입력", "키보드로 영작하여 타이핑하고 체크합니다."),
    HYBRID("음성 + 텍스트 자유 모드", "상황에 따라 마이크 또는 키보드를 자유롭게 사용합니다.")
}

/**
 * Combined session mode configuration.
 */
data class SessionModeConfig(
    val questionMode: QuestionMode = QuestionMode.VOICE_AUTO,
    val answerMode: AnswerMode = AnswerMode.HYBRID,
    val autoPlayEnglishOnResult: Boolean = true,
    val speechSpeed: Float = 1.0f, // 0.8f, 1.0f, 1.2f
    val showWordHint: Boolean = true
)
