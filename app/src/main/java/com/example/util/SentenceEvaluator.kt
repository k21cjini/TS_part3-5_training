package com.example.util

import com.example.model.DiffStatus
import com.example.model.EvaluationResult
import com.example.model.MatchGrade
import com.example.model.WordTokenDiff
import java.util.Locale
import kotlin.math.max

object SentenceEvaluator {

    private val contractions = mapOf(
        "i'm" to "i am",
        "im" to "i am",
        "you're" to "you are",
        "youre" to "you are",
        "he's" to "he is",
        "hes" to "he is",
        "she's" to "she is",
        "shes" to "she is",
        "it's" to "it is",
        "its" to "it is",
        "we're" to "we are",
        "were" to "we are",
        "they're" to "they are",
        "theyre" to "they are",
        "i'll" to "i will",
        "ill" to "i will",
        "you'll" to "you will",
        "he'll" to "he will",
        "she'll" to "she will",
        "we'll" to "we will",
        "they'll" to "they will",
        "i've" to "i have",
        "ive" to "i have",
        "you've" to "you have",
        "we've" to "we have",
        "they've" to "they have",
        "i'd" to "i would",
        "you'd" to "you would",
        "he'd" to "he would",
        "she'd" to "she would",
        "we'd" to "we would",
        "they'd" to "they would",
        "can't" to "cannot",
        "cant" to "cannot",
        "don't" to "do not",
        "dont" to "do not",
        "doesn't" to "does not",
        "doesnt" to "does not",
        "didn't" to "did not",
        "didnt" to "did not",
        "won't" to "will not",
        "wont" to "will not",
        "wouldn't" to "would not",
        "couldn't" to "could not",
        "shouldn't" to "should not",
        "let's" to "let us",
        "lets" to "let us",
        "what's" to "what is",
        "whats" to "what is",
        "where's" to "where is",
        "wheres" to "where is",
        "there's" to "there is",
        "theres" to "there is",
        "how's" to "how is",
        "hows" to "how is"
    )

    fun normalize(text: String): String {
        var clean = text.lowercase(Locale.ROOT).trim()
        // Replace special punctuation with spaces
        clean = clean.replace(Regex("[.,!?;:\"()—\\[\\]{}]"), " ")
        // Expand common contractions
        val tokens = clean.split(Regex("\\s+")).filter { it.isNotBlank() }
        val expanded = tokens.map { word ->
            contractions[word] ?: word
        }
        return expanded.joinToString(" ")
    }

    private fun tokenize(text: String): List<String> {
        return normalize(text).split(Regex("\\s+")).filter { it.isNotEmpty() }
    }

    fun evaluate(userAnswer: String, primaryTarget: String, alternatives: List<String> = emptyList()): EvaluationResult {
        val allTargets = (listOf(primaryTarget) + alternatives).distinct()
        
        if (userAnswer.isBlank()) {
            val primaryTokens = primaryTarget.split(Regex("\\s+")).filter { it.isNotBlank() }
            return EvaluationResult(
                accuracy = 0,
                grade = MatchGrade.TRY_AGAIN,
                userAnswer = userAnswer,
                expectedAnswer = primaryTarget,
                tokenDiffs = primaryTokens.map { WordTokenDiff(it, DiffStatus.MISSING) },
                feedbackMessage = "답변이 비어있습니다. 음성으로 말하거나 텍스트로 입력해보세요!",
                matchedVariation = primaryTarget
            )
        }

        // Find the best matching target variation
        var bestResult: EvaluationResult? = null
        var bestScore = -1

        for (target in allTargets) {
            val result = evaluateAgainstTarget(userAnswer, target)
            if (result.accuracy > bestScore) {
                bestScore = result.accuracy
                bestResult = result
            }
        }

        return bestResult ?: evaluateAgainstTarget(userAnswer, primaryTarget)
    }

    private fun evaluateAgainstTarget(userAnswer: String, target: String): EvaluationResult {
        val userTokens = tokenize(userAnswer)
        val rawTargetTokens = target.split(Regex("\\s+")).filter { it.isNotBlank() }
        val targetTokensNormalized = tokenize(target)

        // Build token diffs
        val tokenDiffs = mutableListOf<WordTokenDiff>()
        var matchCount = 0
        val targetMatchedIndices = mutableSetOf<Int>()

        // 1. First pass: exact and close token matches
        for (i in rawTargetTokens.indices) {
            val rawTargetWord = rawTargetTokens[i]
            val normalizedTargetWord = if (i < targetTokensNormalized.size) targetTokensNormalized[i] else normalize(rawTargetWord)
            
            // Check if user answer contains this word
            val matchIndex = userTokens.indices.firstOrNull { uIdx ->
                !targetMatchedIndices.contains(uIdx) && (
                    userTokens[uIdx] == normalizedTargetWord ||
                    similarity(userTokens[uIdx], normalizedTargetWord) >= 0.82
                )
            }

            if (matchIndex != null) {
                targetMatchedIndices.add(matchIndex)
                matchCount++
                tokenDiffs.add(WordTokenDiff(rawTargetWord, DiffStatus.MATCH))
            } else {
                tokenDiffs.add(WordTokenDiff(rawTargetWord, DiffStatus.MISSING))
            }
        }

        // Calculate score
        val totalExpected = max(1, rawTargetTokens.size)
        val totalUser = max(1, userTokens.size)
        val baseScore = ((matchCount.toDouble() / totalExpected) * 100).toInt()
        
        // Penalize excess extra words slightly if user said something completely off
        val extraWords = max(0, totalUser - matchCount)
        val penalty = (extraWords * 5).coerceAtMost(30)
        val finalAccuracy = (baseScore - penalty).coerceIn(0, 100)

        val grade = when {
            finalAccuracy >= 95 -> MatchGrade.PERFECT
            finalAccuracy >= 80 -> MatchGrade.GREAT
            finalAccuracy >= 60 -> MatchGrade.GOOD
            else -> MatchGrade.TRY_AGAIN
        }

        val feedbackMessage = when (grade) {
            MatchGrade.PERFECT -> "🎉 완벽한 문장입니다! 자연스럽게 영어식으로 발화하셨습니다."
            MatchGrade.GREAT -> "👍 아주 훌륭합니다! 거의 원어민처럼 자연스럽게 구사하셨습니다."
            MatchGrade.GOOD -> "👏 좋습니다! 핵심 단어들을 잘 활용했습니다. 몇 가지 어순과 표현을 다듬어보세요."
            MatchGrade.TRY_AGAIN -> "💪 조금 아쉬워요! 아래 모범 문장과 발음을 듣고 다시 시도해보세요."
        }

        return EvaluationResult(
            accuracy = finalAccuracy,
            grade = grade,
            userAnswer = userAnswer,
            expectedAnswer = target,
            tokenDiffs = tokenDiffs,
            feedbackMessage = feedbackMessage,
            matchedVariation = target
        )
    }

    private fun similarity(s1: String, s2: String): Double {
        if (s1 == s2) return 1.0
        val maxLen = max(s1.length, s2.length)
        if (maxLen == 0) return 1.0
        val dist = levenshteinDistance(s1, s2)
        return (maxLen - dist).toDouble() / maxLen
    }

    private fun levenshteinDistance(s1: String, s2: String): Int {
        val dp = Array(s1.length + 1) { IntArray(s2.length + 1) }
        for (i in 0..s1.length) dp[i][0] = i
        for (j in 0..s2.length) dp[0][j] = j

        for (i in 1..s1.length) {
            for (j in 1..s2.length) {
                val cost = if (s1[i - 1] == s2[j - 1]) 0 else 1
                dp[i][j] = minOf(
                    dp[i - 1][j] + 1,
                    dp[i][j - 1] + 1,
                    dp[i - 1][j - 1] + cost
                )
            }
        }
        return dp[s1.length][s2.length]
    }
}
