package com.example.model

enum class MatchGrade(val label: String, val scoreRange: String) {
    PERFECT("완벽해요! (Perfect)", "95% - 100%"),
    GREAT("아주 좋아요! (Great)", "80% - 94%"),
    GOOD("거의 맞았어요! (Good)", "60% - 79%"),
    TRY_AGAIN("다시 도전해보세요! (Try Again)", "0% - 59%")
}

data class WordTokenDiff(
    val word: String,
    val status: DiffStatus // MATCH, WRONG, MISSING, EXTRA
)

enum class DiffStatus {
    MATCH,    // Correct word
    WRONG,    // Substituted word
    MISSING,  // Missing from target
    EXTRA     // Extra word added by user
}

data class EvaluationResult(
    val accuracy: Int, // 0 - 100
    val grade: MatchGrade,
    val userAnswer: String,
    val expectedAnswer: String,
    val tokenDiffs: List<WordTokenDiff>,
    val feedbackMessage: String,
    val matchedVariation: String = ""
)
